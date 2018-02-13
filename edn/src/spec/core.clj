(ns spec.core
  (:require [clojure.string :as st]
            [clojure.spec.alpha :as s])
  (:import (clojure.lang ExceptionInfo)))

(s/def ::row-spec (s/+
                    (s/cat :label keyword?
                           :is-required (s/? #{:required})
                           :spec (constantly true))))

(defn build-row-parser [delim & row-spec]
  (if (s/valid? ::row-spec row-spec)
    (let [blueprint (loop [rs row-spec
                           labels #{}
                           out []]
                      (if (empty? rs)
                        out
                        (let [label (first rs)
                              rs (rest rs)
                              [is-required spec rs] (if (= :required (first rs))
                                                      [true (second rs) (drop 2 rs)]
                                                      [false (first rs) (rest rs)])
                              error (try
                                      (s/valid? spec "")
                                      nil
                                      (catch Throwable t
                                        t))]
                          (if (s/valid? labels label)
                            (throw (IllegalArgumentException. (str "Cannot repeat label: " label)))
                            (if (nil? error)
                              (recur rs
                                     (conj labels label)
                                     (conj out {:label label
                                                :required? is-required
                                                :spec spec}))
                              (throw error))))))]
      (println blueprint)
      (fn [row-str]
        (let [row (st/split row-str delim)
              [out errors] (loop [index 0
                                  r row
                                  bp blueprint
                                  out {}
                                  errors []]
                             (let [required-fields (filter :required? bp)
                                   required-labels (map :label required-fields)
                                   required-count (count required-fields)]
                               (if (and (empty? r) (zero? required-count))
                                 [out errors]
                                 (if (and (empty? bp) (not (empty? r)))
                                   [out (conj errors (str "Too Many Fields: " (count r)))]
                                   (if (and (empty? r) (pos-int? required-count))
                                     [out (conj errors (str "Not Enough Fields: " (apply str required-labels)))]
                                     (let [{:keys [label required? spec]} (first bp)
                                           field (first r)]
                                       (if (s/valid? spec field)
                                         (recur (inc index) (rest r) (rest bp) (assoc out label field) errors)
                                         (if required?
                                           (recur (inc index) (rest r) (rest bp) out
                                                  (conj errors (str "Field does not conform to required spec: "
                                                                    index " -> " label ": " field " - "
                                                                    (s/explain spec field))))
                                           (recur index r (rest bp) out errors)))))))))]
          (if (empty? errors)
            out
            (let [data {:row row-str
                        :errors errors}]
              (println "data: " data)
              (throw (ExceptionInfo. "Cannot parse:" data)))))))
    (throw (ExceptionInfo. "Invalid row-spec:" (s/explain-data ::row-spec row-spec)))))
