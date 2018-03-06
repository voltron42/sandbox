(ns fivelang.common.validation)

(defn min-count [bound]
  (fn [value]
    (try
      (<= bound (count value))
      (catch Throwable t
        false))))

(defn matches? [regex]
  (fn [value]
    (let [matches (re-matches regex value)
          matches (if (coll? matches) matches [matches])]
      (contains? (set matches) value))))

(defn named-as
  ([ns-regex name-regex]
   (fn [value]
     (try
       (and ((matches? ns-regex) (namespace value)) ((matches? name-regex) (name value)))
       (catch Throwable t
         false))))
  ([name-regex]
   (fn [value]
     (try
       (and (nil? (namespace value)) ((matches? name-regex) (name value)))
       (catch Throwable t
         false)))))

(defn verify-arity [func-map]
  ;todo
  )

(def procedures {}) ;todo

(def functions {}) ;todo

(def ID #"[a-zA-Z$_][a-zA-Z$_0-9]*")

(def valid-id ID)

(def qualified-name #"[a-zA-Z$_][a-zA-Z$_0-9]*([\.][a-zA-Z$_][a-zA-Z$_0-9]*)*")
