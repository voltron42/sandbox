(ns dao.factory
  (:require [schema.core :as s]
            [clojure.spec.alpha :as spec]
            [clojure.string :as str]
            [clojure.set :as set]
            [clojure.java.jdbc :as jdbc])
  (:import (clojure.lang PersistentVector Keyword)
           (dao.factory Inquisitor)))

(defn singleton? [col] (= 1 (count col)))

(spec/def ::where
  (spec/and list?
         (spec/cat :op symbol? :args (spec/& (spec/or
                                               :w ::where
                                               :s symbol?
                                               :k keyword?
                                               :n number?
                                               :s string?
                                               :b boolean?)))))

(spec/def ::tables
  (spec/and vector?
            (spec/cat :first symbol?
                      :additional (spec/*
                                    (spec/cat :t symbol?
                                              :j (spec/? #{:inner :left-outer :right-outer})
                                              :k (spec/map-of symbol? symbol?))))))

(spec/def ::order-by
  (spec/coll-of (spec/or :k symbol?
                         :d (spec/and
                              (spec/map-of symbol? #{:desc :asc})
                              singleton?))))

(spec/def ::group-by
  (spec/or
    :one symbol?
    :some (spec/and vector? (spec/coll-of symbol?))))

(spec/def ::columns
  (spec/and vector?
            (spec/cat :d (spec/? #{:distinct})
                      :c (spec/+ (spec/or :k symbol?
                                          :m (spec/and (spec/map-of symbol? symbol?) singleton?)
                                          :e (spec/and (spec/map-of symbol? ::where) singleton?))))))

(spec/def ::query
  (spec/keys :req [::tables]
          :opt [::select
                ::order-by
                ::where
                ::group-by]))

(defprotocol Inquisitor
  (submit-query [this values]))

(defprotocol IFP
  (build-inquisitor ^Inquisitor [this query opts]))

(defn- polyanary [operator]
  (fn [& args]
    (str/join (str " " operator " ") args)))

(defn- func [func-name]
  (fn [& args]
    (str func-name "(" (str/join "," args) ")")))

(def ^:private where-fn-map {})

(defn- build-where-clause [where]
  (let [fn-label (first where)
        func (get where-fn-map fn-label)
        _ (when (nil? func) (throw (IllegalArgumentException. (str "'" fn-label "' is not a valid operation."))))
        argv (mapv #(cond
                      (list? %) (let [[clause & vars] (build-where-clause %)]
                                  {:clause (str "(" clause ")")
                                   :vars vars})
                      (string? %) {:clause (str "'" % "'")}
                      (keyword? %) {:clause "?"
                                    :vars [%]}
                      :else {:clause (str %)}) (rest where))
        args (mapv :clause argv)
        vars (flatten (mapv :vars (filter :vars argv)))
        clause (apply func args)]
    (into [clause] vars)))

(defn- build-query [query]
  )

(defn- build-var-fn [vars]
  (fn [values]
    (let [missing-vars (set/difference (set vars) (keys values))]
      (when (< 0 (count missing-vars))
        (throw (IllegalArgumentException. (str "Following vars not set for the given query: " missing-vars))))
      (mapv #(get values %) vars))))

(deftype InquisitorFactory [db] IFP
  (build-inquisitor ^Inquisitor [this query opts]
    (let [[clause & vars] (build-query query)
          var-fn (build-var-fn vars)]
      (reify Inquisitor
        (submit-query [this values]
          (let [query-arg (into [clause] (var-fn values))]
            (jdbc/query db query-arg opts)))))))

(spec/def ::table symbol?)

(spec/def ::id-fields (spec/and (spec/coll-of symbol?) not-empty))

(spec/def ::sysdate-fields (spec/and (spec/coll-of symbol?) not-empty))

(spec/def ::sequence (spec/or :s symbol?
                              :e ::where))

(spec/def ::can-update boolean?)

(spec/def ::can-delete boolean?)

(spec/def ::cond-map (spec/map-of keyword? ::where))

(spec/def ::read-cond-map ::cond-map)

(spec/def ::delete-cond-map ::cond-map)

(spec/def ::udpate-cond-map ::cond-map)

(spec/def ::dao-spec
  (spec/keys :req [::table ::id-fields]
             :opt [::sequence ::sysdate-fields ::can-delete ::can-update ::read-cond-map ::delete-cond-map ::update-cond-map]))

(defprotocol DaoSpec
  (table-name ^Keyword [this])
  (id-fields ^PersistentVector [this])
  (sysdate-fields ^PersistentVector [this])
  (can-update ^Boolean [this])
  (can-delete ^Boolean [this])
  (get-query [this cond-name values])
  (get-update [this cond-name values])
  (get-delete [this cond-name values])
  )

(defprotocol Dao
  (create [this obj])
  (create-all [this objs])
  (get-all [this])
  (get-by-id [this id])
  (get-by [this cond-name values])
  (update-by-id [this values])
  (update-by [this cond-name values])
  (delete-by-id [this id])
  (delete-by [this cond-name values])
  )

(defprotocol DFP
  (create-dao ^Dao [this dao-spec]))

(defn- build-where-mapping [where]
  (let [[clause & vars] (build-where-clause where)
        var-fn (build-var-fn vars)]
    (fn [values]
        (into [clause] (var-fn values)))))

(defn- map-cond-fns [cond-map]
  (reduce-kv (fn [out k v]
               (assoc out k (build-where-mapping v)))
             {} cond-map))

(defn- create-dao-spec ^DaoSpec [dao-spec]
  (when-not (spec/valid? ::dao-spec dao-spec)
    (throw (IllegalArgumentException. ^String (with-out-str (spec/explain ::dao-spec dao-spec)))))
  (let [query-map (map-cond-fns (:query-cond-map dao-spec))
        delete-map (map-cond-fns (:delete-cond-map dao-spec))
        update-map (map-cond-fns (:update-cond-map dao-spec))]
    (reify DaoSpec
      (table-name ^Keyword [this]
        (:table dao-spec))
      (id-fields ^PersistentVector [this]
        (:id-fields dao-spec))
      (sysdate-fields ^PersistentVector [this]
        (get dao-spec :sysdate-fields []))
      (can-update ^Boolean [this]
        (get dao-spec :can-update true))
      (can-delete ^Boolean [this]
        (get dao-spec :can-delete true))
      (get-query ^PersistentVector [this cond-name values]
        ((get query-map cond-name) values))
      (get-update [this cond-name values]
        ((get delete-map cond-name) values))
      (get-delete [this cond-name values]
        ((get update-map cond-name) values)))))

(deftype DaoFactory [db] DFP
  (create-dao ^Dao [this dao-spec]
    (let [^DaoSpec spec (create-dao-spec dao-spec)]
      (reify Dao
        (create [this obj]
          )
        (create-all [this objs]
          )
        (get-all [this]
          )
        (get-by-id [this id]
          )
        (get-by [this cond-name values]
          )
        (update-by-id [this values]
          )
        (update-by [this cond-name values]
          )
        (delete-by-id [this id]
          )
        (delete-by [this cond-name values]
          )
        ))))

(spec/def ::queries (spec/map-of keyword? ::query))

(spec/def ::daos (spec/and vector? (spec/coll-of ::dao-spec)))

(spec/def ::data-access-service
  (spec/keys :opt [::queries ::daos]))

(defprotocol DataService
  (get-query ^Inquisitor [this query-name])
  (get-dao ^Dao [this table-name]))

(defprotocol DSFP
  (create-data-service ^DataService [this data-access-service]))

(deftype DataServiceFactory [db] DSFP
  (create-data-service ^DataService [this data-access-service query-opts]
    (let [query-factory (InquisitorFactory db)
          dao-factory (DaoFactory db)
          query-map (reduce-kv
                      (fn [out key query]
                        (let [opts (get query-opts key {})]
                          (assoc out key (.build_inquisitor query-factory query opts))))
                      {}
                      (:queries data-access-service))
          dao-map (reduce
                    (fn [out dao-spec]
                      (assoc out (:table dao-spec) (.create_dao dao-factory dao-spec)))
                    {}
                    (:daos data-access-service))]
      (reify DataService
        (get-query ^Inquisitor [this query-name]
          (get query-map query-name))
        (get-dao ^Dao [this table-name]
          (get dao-map table-name))))))