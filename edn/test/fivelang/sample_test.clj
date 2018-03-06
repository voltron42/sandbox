(ns fivelang.sample-test
  (:require [clojure.test :refer :all]
            [clojure.edn :as edn]
            [clojure.spec.alpha :as s]
            [fivelang.build.grammar :refer :all]
            [fivelang.common.grammar :refer :all]
            [fivelang.domain.grammar :refer :all]
            [fivelang.routing.grammar :refer :all]
            [fivelang.template.grammar :refer :all]
            [fivelang.tortoise.grammar :refer :all]
            ))

(deftest test-sample
  (let [{:keys [tortoise routing domain build import]} (edn/read-string (slurp "resources/fivelang.edn"))
        tortoise-data (s/explain-data :tortoise/program tortoise)
        routing-data (s/explain-data :routing/model routing)
        domain-data (s/explain-data :domain/mongo-file domain)
        build-data (s/explain-data :build/build-file build)
        import-data (s/explain-data :common/x-import-section import)
        ]
    (println (pr-str tortoise-data))
    (is (= nil tortoise-data))
    (println (pr-str routing-data))
    (is (= nil routing-data))
    (println (pr-str domain-data))
    (is (= nil domain-data))
    (println (pr-str build-data))
    (is (= nil build-data))
    (println (pr-str import-data))
    (is (= nil import-data))
    ))