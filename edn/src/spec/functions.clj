(ns spec.functions)

(defn matches [pattern]
  (fn [value]
    (re-matches pattern value)))

(defn numeric-str []
  (matches #"\d+(\.\d+)?"))

(def dec-str (matches #"\d+\.\d+"))

(def int-str (matches #"\d+"))

