(ns query.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.set :as set]))

(defn ns-symbol [ns-spec name-spec]
  (fn [value]
    (and (s/valid? ns-spec (namespace value)) (s/valid? name-spec (name value)))))

(defn keyscade [& key-sets]
  (fn [q]
    (let [q-keys (set (keys q))
          results (map #(let [key-set (if (set? %) % (if (coll? %) (set %) (set [%])))]
                          (not (empty? (set/intersection q-keys key-set)))) key-sets)]
      (every? false? (drop (count (take-while true? results)) results)))))

(defn conformer [ks&preds]
  (fn [q]
    (and
      (every? (fn [[k v]] (if (contains? q k) (s/valid? v (q k)) true))
              ks&preds)
      (empty? (set/difference (set (keys q)) (set (keys ks&preds)))))))

(s/def ::Select string?)

(s/def ::From int?)

(s/def ::Where keyword?)

(s/def ::Order-By boolean?)

(s/def ::Limit int?)

(s/def ::Query (s/and (conformer '{Select ::Select
                                   From ::From
                                   Where ::Where
                                   Order-By ::Order-By
                                   Limit ::Limit})
                      (keyscade 'Select
                                'From
                                '#{Where
                                   Order-By
                                   Limit})))