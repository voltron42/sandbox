(ns QL2.spec
  (:require [clojure.spec.alpha :as s]
            [QL2.spec-functions :as f]))

(def alias-pattern #"[a-zA-Z][a-zA-Z0-9]*([_][a-zA-Z][a-zA-Z0-9]*)*")

(def variable-name-pattern #"[a-zA-Z][a-zA-Z0-9]*([-][a-zA-Z][a-zA-Z0-9]*)*")

(def custom-function-name-pattern #"[a-zA-Z][a-zA-Z0-9]*([_][a-zA-Z][a-zA-Z0-9]*)*")

(def column-name-pattern #"[a-zA-Z][a-zA-Z0-9]*([_][a-zA-Z][a-zA-Z0-9]*)*")

(def table-dot-star-pattern #"[a-zA-Z][a-zA-Z0-9]*[\.][\*]")

(def table-dot-column-name-pattern #"([a-zA-Z][a-zA-Z0-9]*[\.])?[a-zA-Z][a-zA-Z0-9]*([_][a-zA-Z][a-zA-Z0-9]*)*")

(def table-name-pattern #"([a-zA-Z][a-zA-Z0-9]*[\.])?[a-zA-Z][a-zA-Z0-9]*([_][a-zA-Z][a-zA-Z0-9]*)*")

(def order-pattern #"(asc|desc)")

(s/def ::alias (s/and symbol? (f/named-as alias-pattern)))

(s/def ::variable-name (s/and keyword? (f/named-as variable-name-pattern)))

(s/def ::column-name (s/and symbol? (f/named-as column-name-pattern)))

(s/def ::table-dot-column-name (s/and symbol? (f/named-as table-dot-column-name-pattern)))

(s/def ::aliased-column-name (s/and symbol? (f/named-as table-dot-column-name-pattern alias-pattern)))

(s/def ::table-dot-star (s/and symbol? (f/named-as table-dot-star-pattern)))

(s/def ::number number?)

(s/def ::string string?)

(s/def ::boolean #{true false})

(s/def ::nil nil?)

(s/def ::not (s/cat :label #{'not}
                    :arg ::where))

(s/def ::nil-check (s/cat :label #{'nil? 'not-nil?}
                          :arg ::where))

(s/def ::comparable (s/or :var ::variable-name
                          :literal ::literal
                          :column ::column))

(s/def ::comparison (s/cat :label (set '(= < > <> <= >=))
                           :left ::comparable
                           :right ::comparable))

(s/def ::conjunction (s/cat :label #{'and 'or}
                            :first ::where
                            :rest (s/+ ::where)))

(s/def ::where-in (s/cat :label #{'in}
                         :column (s/or :single ::basic-column
                                       :multi (s/and vector?
                                                     (s/coll-of ::basic-column)))
                         :collection (s/or :var ::variable-name
                                           :single-dim-list (s/and vector?
                                                                   (s/coll-of (s/or :literal ::literal
                                                                                    :var ::variable-name)))
                                           :two-dim-list (s/and vector?
                                                                (s/coll-of (s/or :var ::variable-name
                                                                                 :list (s/or :literal ::literal
                                                                                             :var ::variable-name))))
                                           :query ::query)))

(s/def ::misc-expression (s/or :custom (s/cat :label #{'custom-fn}
                                              :func-name (f/named-as custom-function-name-pattern)
                                              :args (s/* (s/or :literal ::literal
                                                               :var ::variable-name
                                                               :column ::basic-column)))
                               :sysdate (s/cat :label #{'sysdate})
                               :format-date (s/cat :label #{'format-date}
                                                   :col-name ::basic-column
                                                   :format-str (f/valid-date-format))))

(s/def ::where (s/or :var ::variable-name
                     :bool ::boolean
                     :column ::basic-column
                     :expression (s/and list?
                                        (s/or :not ::not
                                              :nil ::nil-check
                                              :comp ::comparison
                                              :conj ::conjunction
                                              :in ::where-in
                                              :misc ::misc-expression))))

(s/def ::literal (s/or :number ::number
                       :string ::string
                       :boolean ::boolean
                       :nil ::nil))

(s/def ::aggregate-expression (s/cat :func-name #{'avg 'min 'max 'median 'sum}
                                     :column ::basic-column))

(s/def ::count-expression (s/cat :func-name #{'count}))

(s/def ::column-expression (s/and list?
                                  (s/or :aggr ::aggregate-expression
                                        :count ::count-expression
                                        :misc ::misc-expression)))

(s/def ::column-untyped (s/or :var-name ::variable-name
                              :expression ::column-expression))

(s/def ::aliased-expression (s/and (f/exact-count 1)
                                   (s/map-of
                                     ::alias
                                     (s/or :literal ::literal
                                           :untyped ::column-untyped
                                           ))))

(s/def ::basic-column (s/or :column-name ::column-name
                            :table-column-name ::table-dot-column-name))

(s/def ::column
  (s/or :basic-column ::basic-column
        :table-star ::table-dot-star
        :aliased ::aliased-column-name
        :literal ::literal
        :untyped ::column-untyped
        :expression ::aliased-expression))

(s/def ::select (s/or :single-column ::column
                      :multi-column (s/and vector?
                                           (f/min-count 1)
                                           (s/coll-of ::column)
                                           (f/no-repeat-columns-or-aliases))))

(s/def ::table-name (s/and symbol? (f/named-as table-name-pattern)))

(s/def ::aliased-table-name (s/and symbol? (f/named-as table-name-pattern alias-pattern)))

(s/def ::table (s/or :name ::table-name
                     :inner-query ::query))

(s/def ::aliased-table (s/or :name ::aliased-table-name
                             :inner-query (s/and (f/exact-count 1)
                                                 (s/map-of ::alias ::query))))

(s/def ::additional-table (s/and vector?
                                 (s/cat :join #{'INNER 'LEFT-OUTER 'RIGHT-OUTER}
                                        :table ::aliased-table
                                        :on (s/and (f/min-count 1)
                                                   (s/map-of ::table-dot-column-name
                                                             ::table-dot-column-name)))))

(s/def ::from (s/or :single (s/or :table ::table
                                  :aliased ::aliased-table)
                    :join (s/and vector?
                                 (s/cat :first ::aliased-table
                                        :additional (s/+ ::additional-table)))))

(s/def ::group-by (s/and vector?
                         (f/min-count 1)
                         (s/coll-of ::column)))

(s/def ::ordered-column (s/or :column ::basic-column
                              :ordered (f/named-as column-name-pattern order-pattern)
                              :dot-ordered (f/named-as table-dot-column-name-pattern order-pattern)))

(s/def ::order-by (s/or :single ::ordered-column
                        :multi (s/and vector? (f/min-count 2)
                                      (s/coll-of ::ordered-column))))

(s/def ::limit (s/or :var ::variable-name
                     :literal integer?))

(s/def ::offset (s/or :var ::variable-name
                      :literal integer?))

(s/def ::distinct #{true})

(s/def ::simple-query (s/keys :req-un [::from]
                              :opt-un [::select
                                       ::where
                                       ::group-by
                                       ::order-by
                                       ::limit
                                       ::offset
                                       ::distinct]))

(s/def ::binary-query (s/and vector?
                             (s/cat :label #{'MINUS}
                                    :left ::query
                                    :right ::query)))

(s/def ::n-ary-query (s/and vector?
                             (s/cat :label #{'UNION 'UNION-ALL 'INTERSECT}
                                    :first ::query
                                    :rest (s/+ ::query))))

(s/def ::query (s/or :simple ::simple-query
                     :binary ::binary-query
                     :n-ary ::n-ary-query))