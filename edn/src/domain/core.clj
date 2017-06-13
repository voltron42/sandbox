(ns domain.core
  (:require [clojure.xml :as xml]
            [clojure.string :as s]
            [xml-short.core :refer [x-pand]]
            [clojure.set :as set]))

(defn- describe-cardinality [cardinality]
  (if (nil? cardinality)
    :required
    ({"?" :optional "*" :one-to-many "$" :ref} cardinality)))

(defn- type-constraints [type]
  (let [cardinality (namespace type)
        type-name (keyword (name type))]
    (println (str "type: " type))
    (println (str "cardinality: " cardinality))
    {:type type-name
     :cardinality (describe-cardinality cardinality)}))

(defn- fieldify [[field-name constraints]]
  (let [constraints (if (vector? constraints) constraints (vector constraints))
        [type & constraints] constraints
        constraints (if (and (= 1 (count constraints)) (map? (first constraints)))
                      (first constraints)
                      (if (empty? constraints) {} {:enum constraints}))
        constraints (merge constraints (type-constraints type) {:field-name (keyword (name field-name))})]
    constraints))

(defn- classify [[class-name class]] {:class-name (keyword (name class-name)) :fields (mapv fieldify class)})

(defn objectify [domain] (mapv classify domain))

(defn- create-id [class-name]
  (keyword (str (s/lower-case (name class-name)) "_id")))

(defn- swap-fields [my-map class-name field-name type]
  (println (str "class: " class-name ", " field-name ", " type))
  (let [f-key-name (create-id class-name)
        f-key {:field-name f-key-name
               :type class-name
               :cardinality :ref}
        [from to] (map my-map [class-name type])
        [from-fields to-fields] (map :fields [from to])
        from-fields (dissoc from-fields field-name)
        to-fields (assoc to-fields f-key-name f-key)
        from (assoc from :fields from-fields)
        to (assoc to :fields to-fields)]
    (println (str "new foreign key: " f-key))
    (assoc my-map class-name from type to)))

(defn invert-cardinality [model]
  (let [order (map :class-name model)
        model-map (reduce
                    (fn [out {:keys [class-name fields] :as class}]
                      (assoc out class-name
                        (assoc class :order (map :field-name fields) :fields
                          (reduce
                            (fn [field-map {:keys [field-name] :as field}]
                              (assoc field-map field-name field)) {} fields))))
                          {} model)
        model-map (reduce (fn [my-model-map {:keys [class-name fields] :as class}]
                            (reduce (fn [my-map {:keys [field-name type cardinality] :as field}]
                                      (if (and (contains? my-map type) (= cardinality :one-to-many))
                                        (swap-fields my-map class-name field-name type)
                                        my-map)) my-model-map fields)) model-map model)]
    (mapv #(let [{:keys [fields order] :as class} (model-map %)
                new-fields (set/difference (set (keys fields)) (set order))
                order (concat (filter (partial contains? fields) order) new-fields)]
            (assoc (dissoc class :order) :fields (mapv fields order))) order)))

(defn create-id-field [class-name]
  {:field-name (create-id class-name)
   :type :id})

(defn sqlize-cardinality [cardinality]
  ({:required " NOT NULL"
    :optional ""} cardinality))

(defn sqlize-type [type field]
  (({:text (fn [{:keys [max-size]}] (str "VARCHAR(" max-size ")"))
     :int (constantly "NUMBER")
     :duration (constantly "LONG")
     :id (constantly "LONG GENERATED ALWAYS AS IDENTITY PRIMARY KEY")} type) field))

(defn sqlize-fields [{:keys [field-name type cardinality] :as field}]
  (str " " field-name " " (sqlize-type type field) (sqlize-cardinality cardinality)))

(defn sqlize [domain]
  (let [model (objectify domain)
        model (invert-cardinality model)]
    (map (fn [{:keys [class-name fields]}]
           (let [field-list (map sqlize-fields
                                 (concat
                                   [(create-id-field class-name)]
                                   (filter #(not= :one-to-many (:cardinality %)) fields)))]
             (str "CREATE TABLE " (name class-name) "(\n" field-list "\n);")))
         model)))

(defn- schemafy-elem [elem]
  )

(defn- schemafy-attr [attr]
  )

(defn- schemafy-class [{:keys [class-name fields]}]
  (let [[elems attrs] (map
                        #(filter
                           (fn [{:keys [type]}]
                             (% (s/lower-case type) type))
                           fields)
                        [not= =])]
    (into [:xs:complexType
           {:name class-name}
           (into [:xs:sequence]
                 (mapv schemafy-elem elems))]
          (mapv schemafy-attr attrs))))

(defn schemafy [domain ^String domain-name ^:symbol root-type]
  (let [{:keys [type cardinality]} (describe-cardinality root-type)
        model (objectify domain)
        root-complex-type [:xs:elem (merge {:name type :type type}
                                           (if (= cardinality :one-to-many)
                                             {:maxOccurs "unbounded"}
                                             {}))]
        complex-types (map schemafy-class model)]
    (with-out-str
      (xml/emit
        (x-pand
          (into
            [:xs:schema
             {:xmlns:xs "http://www.w3.org/2001/XMLSchema"
              :targetNamespace "https://www.w3schools.com"
              :xmlns "https://www.w3schools.com"
              :elementFormDefault "qualified"}
             [:xs:element
              {:name domain-name
               :type domain-name}]
             [:xs:complexType
              {:name domain-name}
              [:xs:sequence root-complex-type]]]
            complex-types))))))