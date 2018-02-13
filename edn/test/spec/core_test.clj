(ns spec.core-test
  (:require [clojure.test :refer :all]
            [spec.core :as sc]
            [spec.functions :as f])
  (:import (clojure.lang ExceptionInfo)))

(deftest test-row-parser
  (let [parse (sc/build-row-parser #"\|"
                                   :type :required #{"HDR"}
                                   :optional-field #{"hi"}
                                   :version :required f/int-str)]
    (println "parser built - running tests")
    (try
      (is (= (parse "HDR|1") {:type "HDR"
                              :version "1"}))
      (catch ExceptionInfo e
        (println (.getData e))
        ))
    (println "test 1 complete")
    (try
      (is (= (parse "HDR|hi|3") {:type "HDR"
                                 :option-field "hi"
                                 :version "3"}))
      (catch ExceptionInfo e
        (println (.getData e))
        ))
    (println "test 2 complete")
    ))