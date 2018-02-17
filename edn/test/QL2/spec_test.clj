(ns QL2.spec-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [QL2.spec :as q]))

(defn- errors [query]
  (let [data (s/explain-data ::q/query query)]
    (when-not (nil? data)
      (println (into {} data)))
    data))

(deftest test1
  (is (= nil (errors '{:from HDP_DATA}))))

(deftest test2
  (is (= nil (errors '{:select {listCount (count)}
                       :from form.load
                       :where (= file_load_id :file-load-id)}))))

(deftest test3
  (is (= nil (errors '{:select w.*
                       :from [form.load/l
                              [INNER form.webdav/w {l.load_id w.load_id}]]
                       :where (and
                                (= l.management_status "A")
                                (<= l.effective_date :effective-date)
                                (= w.version :version)
                                (= w.db :db)
                                (= l.publisher :publisher)
                                (= l.list_id :list-id)
                                (= l.type :type)
                                (nil? l.sub_type))
                       :order-by l.effective_date/desc}))))

(deftest test4
  (is (= nil (errors '{:from form.load
                       :where (or (> modified_date :min-mod-date)
                                  (and (= modified_date :mod-date)
                                       (> load_id :load-id)))
                       :order-by [modified_date load_id]
                       :limit :limit}))))

(deftest test5
  (is (= nil (errors '{:select [ehr_uid type sub_type list_id]
                       :from form.permissions
                       :where (and (= pbm_uid :pbm)
                                   (in ehr_uid [:ehr "*"])
                                   (nil? deactivated_timestamp))}))))

(deftest test6
  (is (= nil (errors '{:select [(custom-fn GET_SEQ_DC_NEXT_VAL "SEQ_RFS_LOAD_ID") "Add" (sysdate)
                                l.publisher l.list_id w.document_id l.type l.sub_type
                                l.effective_date w.db w.db_version ""]
                       :from [webdav/w
                              [INNER load/w {w.load_id l.load_id}]]
                       :where (and (= w.version "30")
                                   (= l.management_status "A")
                                   (not-nil? l.sub_type)
                                   (in [l.publisher l.list_id l.type l.sub_type w.db w.db_version l.effective_date]
                                       [MINUS
                                        {:select [m.publisher m.list_id m.type m.sub_type x.db x.db_version m.effective_date]
                                         :from [webdav/x
                                                [INNER load/m {x.load_id m.load_id}]]
                                         :where (and (= w.version "30")
                                                     (= l.management_status "A")
                                                     (not-nil? l.sub_type))}
                                        {:select [g.publisher g.list_id g.type g.sub_type g.db g.db_version {effective_date (format-date g.effective_date "YYYYMMDD")}]
                                         :from list/g
                                         :where (and (= g.status "A")
                                                     (not-nil? g.sub_type))}]))}))))
