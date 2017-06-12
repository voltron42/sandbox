(ns domain.core
  (:require [clojure.xml :as xml]
            [clojure.string :as s]))

(defn- describe-cardinality [cardinality]
  (if (nil? cardinality)
    :required
    ({"?" :optional "*" :one-to-many "$" :ref} cardinality)))

(defn- type-constraints [type]
  (let [cardinality (ns type)
        type (keyword (name type))]
    {:type type
     :cardinality (describe-cardinality cardinality)}))

(defn- fieldify [[field-name constraints]]
  (let [constraints (if (vector? constraints) constraints (vector constraints))
        [type & constraints] constraints
        constraints (if (and (= 1 (count constraints)) (map? (first constraints)))
                      (first constraints)
                      (if (empty? constraints) {} {:enum constraints}))
        constraints (merge constraints (type-constraints type) {:field-name (keyword (name field-name))})]
    constraints))

(defn- classify [[class-name class]] {:class-name (keyword (name class-name)) :fields (map fieldify class)})

(defn objectify [domain] (map classify domain))

(defn invert-cardinality [model]
  (let [model-map (reduce #(assoc %1 (:class-name %2) %2) {} model)
        ]
    ))

(defn nest-cardinality [model]
  (let [model-map (reduce #(assoc %1 (:class-name %2) %2) {} model)]
    (reduce (fn [m-map {:keys [class-name fields]}]
              )
            model-map
            model)))

(defn create-id-field [class-name]
  {:name (keyword (str (s/lower-case class-name) "_id"))
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
             (str "CREATE TABLE " (name class-name) "(\n" field-list "\n);")
             )
           )
         model)
    )
  )

(defn schemafy [domain ^String domain-name]
  (let [model (objectify domain)]
    (with-out-str
      (xml/emit
        {:tag :xs:schema
         :attrs {:xmlns:xs "http://www.w3.org/2001/XMLSchema"
                 :targetNamespace "https://www.w3schools.com"
                 :xmlns "https://www.w3schools.com"
                 :elementFormDefault "qualified"}
         :content [{:tag :xs:element
                    :attrs {:name domain-name
                            :type domain-name}}
                   {:tag :xs:complexType
                    :attrs {:name domain-name}
                    :contents []}]}))
    )
  )