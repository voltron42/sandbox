(ns QL2.core)

(defn- validate-query
  ([query-spec]
    (validate-query query-spec {} {}))
  ([query-spec arg-spec arg-types]
    ; TODO
    ))

(defn- swap-args [query-spec args]
  ; TODO
  )

(defn- format-query [query-spec]
  ; TODO
  )

(defn- validate-args-and-format [query-spec args arg-validator]
  (arg-validator args)
  (let [[swapped-query-spec args] (swap-args query-spec args)
        _ (validate-query swapped-query-spec)
        query (format-query swapped-query-spec)]
    (into [query] args)))

(defn build-query
  ([query-spec args]
   (build-query query-spec args {}))
  ([query-spec args arg-spec]
   (build-query query-spec args arg-spec {}))
  ([query-spec args arg-spec arg-types]
    (let [arg-validator (validate-query query-spec arg-spec arg-types)]
      (validate-args-and-format query-spec args arg-validator))))

(defn build-inquisitor
  ([query-spec]
   (build-inquisitor query-spec {}))
  ([query-spec arg-spec]
   (build-inquisitor query-spec arg-spec {}))
  ([query-spec arg-spec arg-types]
   (let [arg-validator (validate-query query-spec arg-spec arg-types)]
     (fn [args]
       (validate-args-and-format query-spec args arg-validator)))))
