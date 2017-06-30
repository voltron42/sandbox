(ns grammar.core)

(defn- type-validator [type-name type?]
  (fn [script]
    (when-not
      (type script)
      (throw (Exception. (str "does not match type: " type-name ", " script))))))

(defn- build-validator [validator & validators]
  (fn [script]
    ))

(def ^:private validator-factories
  {"*" (fn [entity recursor])
   "int" (fn [entity recursor]
           (cond
             (nil? entity)
             ))
   "float" (fn [entity recursor]
             (cond
               (nil? entity)
               ))
   "fraction" (fn [entity recursor])
   "bool" (fn [entity recursor])
   "string" (fn [entity recursor])
   "keyword" (fn [entity recursor])
   "symbol" (fn [entity recursor])
   "literal" (fn [entity recursor])
   "map" (fn [entity recursor])
   "list" (fn [entity recursor])
   "set" (fn [entity recursor])
   "vector" (fn [entity recursor])
   })

(defn- validate-element-to-entity [element entity-label entities]
  (let [type (namespace entity-label)
        entity (entities entity-label)
        validate ((validator-factories type) entity validate-element-to-entity)]
    (validate element )
    ))

(defn validate [grammar script]
  (let [{:keys [grammar/name grammar/root grammar/syntax]} (reduce #(assoc %2 (keyword (first %2)) (second %2)) {} grammar)]
    (validate-element-to-entity script root syntax)))