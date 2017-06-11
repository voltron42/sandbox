(ns query.core
  (:require [clojure.string :as s]
            [clojure.java.jdbc :as jdbc]))

(def ^:private clause-list '[Select From Where Order-By Limit])

(defn unary [term] #(str term "(" % ")"))

(defn binary [term] #(str %1 " " term " " %2))

(defn n-ary [term] #(str "(" (s/join (str " " term " ") (map str %&)) ")"))

(defn map-ops [fnc ops] (reduce #(assoc %1 %2 (fnc %2)) {} ops))

(def ops (merge (map-ops unary '[count])
                (map-ops binary '[= <= >])
                (map-ops n-ary '[and or])
                {'is-null #(str % " IS NULL")}))

(defn- resolve-field [field-symbol] (let [nm (name field-symbol)
                                          ns (namespace field-symbol)]
                                      (str nm (if (nil? ns) "" (str " " ns)))))

(defn- resolve-expression [expr all-params]
  (cond
    (list? expr) (let [op (first expr)
                       op-func (ops op)
                       {:keys [args param-list]} (reduce
                                                   (fn [{:keys [args param-list]} param]
                                                     (let [{:keys [query params]} (resolve-expression param param-list)]
                                                       {:args (concat args (vector query))
                                                        :param-list params}))
                                                   {:args [] :param-list all-params}
                                                   (rest expr))]
                   {:query (apply op-func args)
                    :params param-list})
    (symbol? expr) {:query (resolve-field expr) :params all-params}
    (keyword? expr) {:query "?" :params (concat all-params (vector expr))}
    :else {:query (str expr) :params all-params}))

(defn- resolve-column [params column]
  (cond
    (symbol? column)   {:query (resolve-field column)
                        :params params}
    (map? column) (let [[[k v]] column
                        {:keys [query params]} (resolve-column params V)]
                    {:query (str query " as " k)
                     :params params})
    (list? column) (resolve-expression column params)))

(defn- resolve-sorter [column params]
  (cond
    (symbol? column) {})
  )

(defn- resolve-table [table])

(defn- resolve-join [join])

(defn- resolve-where [clause params] (resolve-expression clause params))

(def ^:private clause-funcs {'Select #(str "SELECT " (s/join ", " (map (partial resolve-column %2) (if (vector? %1) %1 (vector %1)))))
                             'From #(let [tables (if (vector? %1) %1 (vector %1))
                                          {:keys [query params]} (reduce (fn [{:keys [query params]} table]
                                                                           {:query (resolve-join table)
                                                                            :params })
                                                                         {:query "" :params %2}
                                                                         (rest tables))]
                                      {:query (str "FROM " (resolve-table (first tables)) " " query)
                                       :params params}
                                      )
                             'Where #(let [{:keys [query params]} (resolve-where %1 %2)] {:query (str "WHERE " query) :params params})
                             'Order-By #(str "ORDER BY " (s/join ", " (map (partial resolve-sorter %2) (if (vector? %1) %1 (vector %1)))))})

(defn- resolve-query [query paramlist] (reduce (fn [{:keys [query params]} clause]
                                                 (let [[prev-query prev-params] [query params]
                                                       {:keys [query params]} ((clause-funcs %) (query %) params)]
                                                   {:query (str prev-query query " ")
                                                    :params (concat prev-params params)}))
                                               {:query "" :params paramlist}
                                               (filter #(contains? (set (keys query)) %) clause-list)))

(defn build-query [db q]
  (let [{:keys [query params]} (resolve-query q [])]
    (fn [argmap]
      (apply jdbc/query (into [db (s/trim query)] (map #()))))))