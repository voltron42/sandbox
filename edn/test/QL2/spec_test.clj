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
  (is (= nil (errors '{:from HDP_DATA})))
  )

(deftest test2
  (is (= nil (errors '{:select {listCount (count)}
                       :from form.formulary_load
                       :where (= file_load_id :file-load-id)})))
  )

(deftest test3
  (is (= nil (errors '{:select w.*
                       :from [form.formulary_load/l
                              [INNER form.formulary_webdav/w {l.formulary_load_id w.formulary_load_id}]]
                       :where (and
                                (= l.management_status "A")
                                (<= l.effective_date :effective-date)
                                (= w.version :version)
                                (= w.rollup_drug_db :rollup-drug-db)
                                (= l.publisher :publisher)
                                (= l.list_id :list-id)
                                (= l.type :type)
                                (nil? l.sub_type))
                       :order-by l.effective_date/desc})))
  )

(deftest test4
  (is (= nil (errors '{:from form.rfs_load
                       :where (or (> modified_date :min-mod-date)
                                  (and (= modified_date :mod-date)
                                       (> rfs_load_id :rfs-load-id)))
                       :order-by [modified_date rfs_load_id]
                       :limit :limit})))
  )

(deftest test5
  (is (= nil (errors '{:select [ehr_uid type sub_type list_id]
                       :from form.permissions
                       :where (and (= pbm_uid :pbm)
                                   (in ehr_uid [:ehr "*"])
                                   (nil? deactivated_timestamp))})))
  )

(deftest test6
  (is (= nil (errors '{:select [(custom-fn GET_SEQ_DC_NEXT_VAL "SEQ_RFS_LOAD_ID") "Add" (sysdate) l.publisher l.list_id w.document_id l.type l.sub_type l.effective_date w.rollup_drug_db w.rollup_drug_db_version ""]
                       :from [formulary_webdav/w
                              [INNER formulary_load/w {w.formulary_load_id l.formulary_load_id}]]
                       :where (and (= w.version "30")
                                   (= l.management_status "A")
                                   (not-nil? l.sub_type)
                                   (in [l.publisher, l.list_id, l.type, l.sub_type, w.rollup_drug_db, w.rollup_drug_db_version, l.effective_date]
                                       [MINUS
                                        {:select [m.publisher, m.list_id, m.type, m.sub_type, x.rollup_drug_db, x.rollup_drug_db_version,
                                                  m.effective_date]
                                         :from [formulary_webdav/x
                                                [INNER formulary_load/m {x.formulary_load_id m.formulary_load_id}]]
                                         :where (and (= w.version "30")
                                                     (= l.management_status "A")
                                                     (not-nil? l.sub_type))}
                                        {:select [g.publisher, g.list_id, g.type, g.sub_type, g.rollup_drug_db, g.rollup_drug_db_version {effective_date (format-date g.effective_date "YYYYMMDD")}]
                                         :from formulary_list/g
                                         :where (and (= g.status "A")
                                                     (not-nil? g.sub_type))}]))})))
  )

(deftest test-queries
  )

(comment
  {:problems ({:path [:simple :select :single-column :basic-column :column-name],
               :pred clojure.core/symbol?,
               :val '[(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID) Add (sysdate) l.publisher l.list_id w.document_id l.type l.sub_type l.effective_date w.rollup_drug_db w.rollup_drug_db_version ],
               :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/basic-column :QL2.spec/column-name],
               :in [:select]}
               {:path [:simple :select :single-column :basic-column :table-column-name],
                :pred clojure.core/symbol?,
                :val '[(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID) Add (sysdate) l.publisher l.list_id w.document_id l.type l.sub_type l.effective_date w.rollup_drug_db w.rollup_drug_db_version ],
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/basic-column :QL2.spec/table-dot-column-name],
                :in [:select]}
               {:path [:simple :select :single-column :table-star],
                :pred clojure.core/symbol?,
                :val '[(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID) Add (sysdate) l.publisher l.list_id w.document_id l.type l.sub_type l.effective_date w.rollup_drug_db w.rollup_drug_db_version ],
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/table-dot-star],
                :in [:select]}
               {:path [:simple :select :single-column :aliased],
                :pred clojure.core/symbol?,
                :val '[(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID) Add (sysdate) l.publisher l.list_id w.document_id l.type l.sub_type l.effective_date w.rollup_drug_db w.rollup_drug_db_version ],
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/aliased-column-name],
                :in [:select]}
               {:path [:simple :select :single-column :expression],
                :pred (QL2.spec-functions/exact-count 1),
                :val '[(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID) Add (sysdate) l.publisher l.list_id w.document_id l.type l.sub_type l.effective_date w.rollup_drug_db w.rollup_drug_db_version ],
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/aliased-expression],
                :in [:select]}
               {:path [:simple :select :multi-column :basic-column :column-name],
                :pred clojure.core/symbol?,
                :val '(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID),
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/basic-column :QL2.spec/column-name],
                :in [:select 0]}
               {:path [:simple :select :multi-column :basic-column :table-column-name],
                :pred clojure.core/symbol?,
                :val '(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID),
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/basic-column :QL2.spec/table-dot-column-name],
                :in [:select 0]}
               {:path [:simple :select :multi-column :table-star],
                :pred clojure.core/symbol?,
                :val '(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID),
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/table-dot-star],
                :in [:select 0]}
               {:path [:simple :select :multi-column :aliased],
                :pred clojure.core/symbol?,
                :val '(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID),
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/aliased-column-name],
                :in [:select 0]}
               {:path [:simple :select :multi-column :expression],
                :pred (QL2.spec-functions/exact-count 1),
                :val '(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID),
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/aliased-expression],
                :in [:select 0]}
               {:path [:simple :select :multi-column :basic-column :column-name],
                :pred clojure.core/symbol?,
                :val 'Add,
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/basic-column :QL2.spec/column-name],
                :in [:select 1]}
               {:path [:simple :select :multi-column :basic-column :table-column-name],
                :pred clojure.core/symbol?,
                :val 'Add,
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/basic-column :QL2.spec/table-dot-column-name],
                :in [:select 1]}
               {:path [:simple :select :multi-column :table-star],
                :pred clojure.core/symbol?,
                :val 'Add,
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/table-dot-star],
                :in [:select 1]}
               {:path [:simple :select :multi-column :aliased],
                :pred clojure.core/symbol?,
                :val 'Add,
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/aliased-column-name],
                :in [:select 1]}
               {:path [:simple :select :multi-column :expression],
                :pred (QL2.spec-functions/exact-count 1),
                :val Add,
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/aliased-expression],
                :in [:select 1]}
               {:path [:simple :select :multi-column :basic-column :column-name],
                :pred clojure.core/symbol?,
                :val (sysdate),
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/basic-column :QL2.spec/column-name],
                :in [:select 2]}
               {:path [:simple :select :multi-column :basic-column :table-column-name],
                :pred clojure.core/symbol?,
                :val (sysdate),
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/basic-column :QL2.spec/table-dot-column-name],
                :in [:select 2]}
               {:path [:simple :select :multi-column :table-star],
                :pred clojure.core/symbol?,
                :val (sysdate),
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/table-dot-star],
                :in [:select 2]}
               {:path [:simple :select :multi-column :aliased],
                :pred clojure.core/symbol?,
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/aliased-column-name],
                :in [:select 2]}
               {:path [:simple :select :multi-column :expression]
                :pred clojure.core/map?
                :val (sysdate)
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/aliased-expression]
                :in [:select 2]}
               {:path [:simple :select :multi-column :basic-column :column-name]
                :pred clojure.core/symbol?
                :val
                      :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/basic-column :QL2.spec/column-name]
                      :in [:select 11]}
               {:path [:simple :select :multi-column :basic-column :table-column-name]
                :pred clojure.core/symbol?
                :val
                      :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/basic-column :QL2.spec/table-dot-column-name]
                      :in [:select 11]}
               {:path [:simple :select :multi-column :table-star]
                :pred clojure.core/symbol?
                :val
                      :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/table-dot-star]
                      :in [:select 11]}
               {:path [:simple :select :multi-column :aliased]
                :pred clojure.core/symbol?
                :val
                      :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/aliased-column-name]
                      :in [:select 11]}
               {:path [:simple :select :multi-column :expression]
                :pred (QL2.spec-functions/exact-count 1)
                :val
                      :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/select :QL2.spec/column :QL2.spec/aliased-expression]
                      :in [:select 11]}
               {:path [:simple :where :var]
                :pred clojure.core/keyword?
                :val (and (= w.version 30)
                          (= l.management_status A)
                          (not-nil? l.sub_type)
                          (in [l.publisher l.list_id l.type l.sub_type w.rollup_drug_db w.rollup_drug_db_version l.effective_date]
                              [MINUS {:select [m.publisher m.list_id m.type m.sub_type x.rollup_drug_db x.rollup_drug_db_version m.effective_date]
                                      :from [formulary_webdav/x [INNER formulary_load/m {x.formulary_load_id m.formulary_load_id}]]
                                      :where (and (= w.version 30)
                                                  (= l.management_status A)
                                                  (not-nil? l.sub_type))}
                               {:select [g.publisher g.list_id g.type g.sub_type g.rollup_drug_db g.rollup_drug_db_version {effective_date (format-date g.effective_date YYYYMMDD)}]
                                :from formulary_list/g
                                :where (and (= g.status A)
                                            (not-nil? g.sub_type))}]))
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/variable-name]
                :in [:where]}
               {:path [:simple :where :bool]
                :pred #{true false}
                :val (and (= w.version 30)
                          (= l.management_status A)
                          (not-nil? l.sub_type)
                          (in [l.publisher l.list_id l.type l.sub_type w.rollup_drug_db w.rollup_drug_db_version l.effective_date]
                              [MINUS {:select [m.publisher m.list_id m.type m.sub_type x.rollup_drug_db x.rollup_drug_db_version m.effective_date]
                                      :from [formulary_webdav/x [INNER formulary_load/m {x.formulary_load_id m.formulary_load_id}]]
                                      :where (and (= w.version 30)
                                                  (= l.management_status A)
                                                  (not-nil? l.sub_type))}
                               {:select [g.publisher g.list_id g.type g.sub_type g.rollup_drug_db g.rollup_drug_db_version {effective_date (format-date g.effective_date YYYYMMDD)}]
                                :from formulary_list/g
                                :where (and (= g.status A)
                                            (not-nil? g.sub_type))}]))
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/boolean]
                :in [:where]}
               {:path [:simple :where :column :column-name]
                :pred clojure.core/symbol?
                :val (and (= w.version 30)
                          (= l.management_status A)
                          (not-nil? l.sub_type)
                          (in [l.publisher l.list_id l.type l.sub_type w.rollup_drug_db w.rollup_drug_db_version l.effective_date]
                              [MINUS {:select [m.publisher m.list_id m.type m.sub_type x.rollup_drug_db x.rollup_drug_db_version m.effective_date]
                                      :from [formulary_webdav/x [INNER formulary_load/m {x.formulary_load_id m.formulary_load_id}]]
                                      :where (and (= w.version 30)
                                                  (= l.management_status A)
                                                  (not-nil? l.sub_type))}
                               {:select [g.publisher g.list_id g.type g.sub_type g.rollup_drug_db g.rollup_drug_db_version {effective_date (format-date g.effective_date YYYYMMDD)}]
                                :from formulary_list/g
                                :where (and (= g.status A)
                                            (not-nil? g.sub_type))}]))
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/basic-column :QL2.spec/column-name]
                :in [:where]}
               {:path [:simple :where :column :table-column-name]
                :pred clojure.core/symbol?
                :val (and (= w.version 30)
                          (= l.management_status A)
                          (not-nil? l.sub_type)
                          (in [l.publisher l.list_id l.type l.sub_type w.rollup_drug_db w.rollup_drug_db_version l.effective_date]
                              [MINUS {:select [m.publisher m.list_id m.type m.sub_type x.rollup_drug_db x.rollup_drug_db_version m.effective_date]
                                      :from [formulary_webdav/x [INNER formulary_load/m {x.formulary_load_id m.formulary_load_id}]]
                                      :where (and (= w.version 30)
                                                  (= l.management_status A)
                                                  (not-nil? l.sub_type))}
                               {:select [g.publisher g.list_id g.type g.sub_type g.rollup_drug_db g.rollup_drug_db_version {effective_date (format-date g.effective_date YYYYMMDD)}]
                                :from formulary_list/g
                                :where (and (= g.status A)
                                            (not-nil? g.sub_type))}]))
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/basic-column :QL2.spec/table-dot-column-name]
                :in [:where]}
               {:path [:simple :where :expression :not :label]
                :pred #{(quote not)}
                :val and
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/not :QL2.spec/not]
                :in [:where 0]}
               {:path [:simple :where :expression :nil :label]
                :pred #{(quote nil?)}
                :val and
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/nil-check :QL2.spec/nil-check]
                :in [:where 0]}
               {:path [:simple :where :expression :comp :label]
                :pred (clojure.core/set (quote (clojure.core/= clojure.core/< clojure.core/> <> clojure.core/<= clojure.core/>=)))
                :val and
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/comparison :QL2.spec/comparison]
                :in [:where 0]}
               {:path [:simple :where :expression :conj :rest :var]
                :pred clojure.core/keyword?
                :val (not-nil? l.sub_type)
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/conjunction :QL2.spec/where :QL2.spec/where :QL2.spec/variable-name]
                :in [:where 3]}
               {:path [:simple :where :expression :conj :rest :bool]
                :pred #{true false}
                :val (not-nil? l.sub_type)
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/conjunction :QL2.spec/where :QL2.spec/where :QL2.spec/boolean]
                :in [:where 3]}
               {:path [:simple :where :expression :conj :rest :column :column-name]
                :pred clojure.core/symbol?
                :val (not-nil? l.sub_type)
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/conjunction :QL2.spec/where :QL2.spec/where :QL2.spec/basic-column :QL2.spec/column-name]
                :in [:where 3]}
               {:path [:simple :where :expression :conj :rest :column :table-column-name]
                :pred clojure.core/symbol?
                :val (not-nil? l.sub_type)
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/conjunction :QL2.spec/where :QL2.spec/where :QL2.spec/basic-column :QL2.spec/table-dot-column-name]
                :in [:where 3]}
               {:path [:simple :where :expression :conj :rest :expression :not :label]
                :pred #{(quote not)}
                :val not-nil?
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/conjunction :QL2.spec/where :QL2.spec/where :QL2.spec/not :QL2.spec/not]
                :in [:where 3 0]}
               {:path [:simple :where :expression :conj :rest :expression :nil :label]
                :pred #{(quote nil?)}
                :val not-nil?
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/conjunction :QL2.spec/where :QL2.spec/where :QL2.spec/nil-check :QL2.spec/nil-check]
                :in [:where 3 0]}
               {:path [:simple :where :expression :conj :rest :expression :comp :label]
                :pred (clojure.core/set (quote (clojure.core/= clojure.core/< clojure.core/> <> clojure.core/<= clojure.core/>=)))
                :val not-nil?
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/conjunction :QL2.spec/where :QL2.spec/where :QL2.spec/comparison :QL2.spec/comparison]
                :in [:where 3 0]}
               {:path [:simple :where :expression :conj :rest :expression :conj :label]
                :pred #{(quote and)
                        (quote or)}
                :val not-nil?
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/conjunction :QL2.spec/where :QL2.spec/where :QL2.spec/conjunction :QL2.spec/conjunction]
                :in [:where 3 0]}
               {:path [:simple :where :expression :conj :rest :expression :in :label]
                :pred #{(quote in)}
                :val not-nil?
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/conjunction :QL2.spec/where :QL2.spec/where :QL2.spec/where-in :QL2.spec/where-in]
                :in [:where 3 0]}
               {:path [:simple :where :expression :in :label]
                :pred #{(quote in)}
                :val and
                :via [:QL2.spec/query :QL2.spec/simple-query :QL2.spec/where :QL2.spec/where-in :QL2.spec/where-in]
                :in [:where 0]}
               {:path [:binary]
                :pred clojure.core/vector?
                :val '{:select [(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID) Add (sysdate) l.publisher l.list_id w.document_id l.type l.sub_type l.effective_date w.rollup_drug_db w.rollup_drug_db_version ]
                       :from [formulary_webdav/w [INNER formulary_load/w {w.formulary_load_id l.formulary_load_id}]]
                       :where (and (= w.version 30)
                                   (= l.management_status A)
                                   (not-nil? l.sub_type)
                                   (in [l.publisher l.list_id l.type l.sub_type w.rollup_drug_db w.rollup_drug_db_version l.effective_date]
                                       [MINUS {:select [m.publisher m.list_id m.type m.sub_type x.rollup_drug_db x.rollup_drug_db_version m.effective_date]
                                               :from [formulary_webdav/x [INNER formulary_load/m {x.formulary_load_id m.formulary_load_id}]]
                                               :where (and (= w.version 30)
                                                           (= l.management_status A)
                                                           (not-nil? l.sub_type))}
                                        {:select [g.publisher g.list_id g.type g.sub_type g.rollup_drug_db g.rollup_drug_db_version {effective_date (format-date g.effective_date YYYYMMDD)}]
                                         :from formulary_list/g
                                         :where (and (= g.status A)
                                                     (not-nil? g.sub_type))}]))}
                :via [:QL2.spec/query :QL2.spec/binary-query]
                :in []}
               {:path [:n-ary]
                :pred clojure.core/vector?
                :val '{:select [(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID) Add (sysdate) l.publisher l.list_id w.document_id l.type l.sub_type l.effective_date w.rollup_drug_db w.rollup_drug_db_version ]
                       :from [formulary_webdav/w [INNER formulary_load/w {w.formulary_load_id l.formulary_load_id}]]
                       :where (and (= w.version 30)
                                   (= l.management_status A)
                                   (not-nil? l.sub_type)
                                   (in [l.publisher l.list_id l.type l.sub_type w.rollup_drug_db w.rollup_drug_db_version l.effective_date]
                                       [MINUS {:select [m.publisher m.list_id m.type m.sub_type x.rollup_drug_db x.rollup_drug_db_version m.effective_date]
                                               :from [formulary_webdav/x [INNER formulary_load/m {x.formulary_load_id m.formulary_load_id}]]
                                               :where (and (= w.version 30)
                                                           (= l.management_status A)
                                                           (not-nil? l.sub_type))}
                                        {:select [g.publisher g.list_id g.type g.sub_type g.rollup_drug_db g.rollup_drug_db_version {effective_date (format-date g.effective_date YYYYMMDD)}]
                                         :from formulary_list/g
                                         :where (and (= g.status A)
                                                     (not-nil? g.sub_type))}]))}
                :via [:QL2.spec/query :QL2.spec/n-ary-query]
                :in []})
   :spec :QL2.spec/query
   :value '{:select [(custom-fn GET_SEQ_DC_NEXT_VAL SEQ_RFS_LOAD_ID) Add (sysdate) l.publisher l.list_id w.document_id l.type l.sub_type l.effective_date w.rollup_drug_db w.rollup_drug_db_version ]
            :from [formulary_webdav/w [INNER formulary_load/w {w.formulary_load_id l.formulary_load_id}]]
            :where (and (= w.version 30)
                        (= l.management_status A)
                        (not-nil? l.sub_type)
                        (in [l.publisher l.list_id l.type l.sub_type w.rollup_drug_db w.rollup_drug_db_version l.effective_date]
                            [MINUS {:select [m.publisher m.list_id m.type m.sub_type x.rollup_drug_db x.rollup_drug_db_version m.effective_date]
                                    :from [formulary_webdav/x [INNER formulary_load/m {x.formulary_load_id m.formulary_load_id}]]
                                    :where (and (= w.version 30)
                                                (= l.management_status A)
                                                (not-nil? l.sub_type))}
                             {:select [g.publisher g.list_id g.type g.sub_type g.rollup_drug_db g.rollup_drug_db_version {effective_date (format-date g.effective_date YYYYMMDD)}]
                              :from formulary_list/g
                              :where (and (= g.status A)
                                          (not-nil? g.sub_type))}]))}}
  )
