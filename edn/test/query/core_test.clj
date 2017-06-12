(ns query.core-test
  (:require [clojure.test :refer :all]
            [query.core :refer [build-query]]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as s]))

(def db "This is the db. This variable doesn't matter for this test")

(deftest test-simple
  (let [actual (atom [])
        expected [db "SELECT * FROM quantity_limit"]]
    (with-redefs [jdbc/query #(reset! actual %&)]
      ((build-query db '{Select * From quantity_limit}) {})
      (is (= @actual expected)))))

(deftest test-count-and-one-var
  (let [actual (atom [])
        expected [db "SELECT count(*) as list_count FROM form.formulary_load WHERE file_load_id = ?" 12345]]
    (with-redefs [jdbc/query #(reset! actual %&)]
      ((build-query db '{Select {list_count (count *)} From form.formulary_load Where (= file_load_id :file-load-id)})
        {:file-load-id 12345})
      (is (= @actual expected)))))

(deftest test-large
  (let [actual (atom [])
        expected [db
                  (s/join " "
                  ["SELECT w.*"
                   "FROM form.formulary_load l"
                   "INNER JOIN form.formulary_webdav w"
                   "ON l.formulary_load_id = w.formulary_load_id"
                   "WHERE l.management_status = 'A'"
                   "AND l.effective_date <= ?"
                   "AND w.version = ?"
                   "AND w.rollup_drug_db = ?"
                   "AND l.publisher = ?"
                   "AND l.list_id = ?"
                   "AND l.type = ?"
                   "AND l.sub_type IS NULL"
                   "ORDER BY l.effective_date desc"])
                   "2016/5/16" "2.1" "FDB" "PUB" "LIST" "TYPE"]]
    (with-redefs [jdbc/query #(reset! actual %&)]
      ((build-query db '{Select w/*
                      From [l/form.formulary_load
                            Inner-Join w/form.formulary_webdav (= l/formulary_load_id w/formulary_load_id)]
                      Where (and (= l/management_status "A")
                                 (<= l/effective_date :effective-date)
                                 (= w/version :version)
                                 (= w/rollup_drug_db :rollup-drug-db)
                                 (= l/publisher :publisher)
                                 (= l/list_id :list-id)
                                 (= l/type :type)
                                 (is-null l/sub_type))
                      Order-By [{l/effective_date desc}]})
        {:effective-date "2016/5/16"
         :list-id "LIST"
         :publisher "PUB"
         :rollup-drug-db "FDB"
         :type "TYPE"
         :version "2.1"})
      (is (= @actual expected)))))

(deftest test-limit
  (let [actual (atom [])
        expected [db (s/join " "
                          ["SELECT *"
                           "FROM (SELECT *"
                           "FROM form.rfs_load"
                           "WHERE modified_date > ?"
                           "OR (modified_date = ?"
                           "AND rfs_load_id > ?)"
                           "ORDER BY modified_date rfs_load_id)"
                           "WHERE rownum <= ?"])
                  "2016/5/16"
                  "2017/8/21"
                  12345
                  2]]
    (with-redefs [jdbc/query #(reset! actual %&)]
      ((build-query db '{Select *
                      From form.rfs_load
                      Where (or (> modified_date :min-mod-date) (and (= modified_date :mod-date) (> rfs_load_id :rfs-load-id)))
                      Order-By [modified_date rfs_load_id]
                      Limit :limit})
        {:limit 2
         :min-mod-date "2016/5/16"
         :mod-date "2017/8/21"
         :rfs-load-id 12345})
      (is (= @actual expected)))))
