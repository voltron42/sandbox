(ns routing.spec-test
    (:require [clojure.test :refer :all]
              [clojure.spec.alpha :as s]
              [routing.spec :refer :all]
              [routing.server :as server]))

(deftest test-server-routings
    (println (s/explain :routing.spec/routings server/routings))
    (is (s/valid? :routing.spec/routings server/routings)))