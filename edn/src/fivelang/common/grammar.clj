(ns fivelang.common.grammar
  (:require [clojure.spec.alpha :as s]
            [fivelang.common.validation :as v]))

(s/def :common/id (s/and symbol? (v/named-as v/ID)))

(s/def :common/q-name (s/and symbol? (v/named-as v/qualified-name)))

(s/def :common/valid-id (s/and symbol? (v/named-as v/valid-id)))

(s/def :common/full-jvm-formal-param
  (s/cat :type :common/jvm-type-ref
         :name :common/valid-id))

(s/def :common/jvm-formal-parameter
  (s/cat :type (s/? :common/jvm-type-ref)
         :name :common/valid-id))

(s/def :common/jvm-type-ref any?)
;todo

(s/def :common/x-annotation any?)
;todo

(s/def :common/x-block-expression
  (s/and vector?
         (s/coll-of :common/x-expression)))

(s/def :common/x-expression
  (s/and list?
         (v/verify-arity v/functions)))

(s/def :common/x-expression-inside-block
  (s/and list?
         (s/or :assign :common/assign
               :for :common/for
               :if :common/if
               :procedure (v/verify-arity v/procedures))))

(s/def :common/for
  (s/cat :label #{'for}
         :variable-declaration :common/valid-id
         :for-expression :common/x-expression
         :each-body :common/x-block-expression))

(s/def :common/assign
  (s/cat :label #{'val}
         :variable-declaration :common/valid-id
         :init-expression :common/x-expression))

(s/def :common/if
  (s/cat :label #{'if}
         :test :common/x-expression
         :then :common/x-block-expression
         :elseif (s/* (s/cat :label #{'elseif}
                             :test :common/x-expression
                             :then :common/x-block-expression))
         :else (s/? (s/cat :label #{'else}
                           :then :common/x-block-expression))))

(s/def :common/x-import-section (s/* :common/x-import-declaration))

(s/def :common/x-import-declaration (s/and vector?
                                           (s/cat :label #{'import}
                                                  :import (s/or :complex-import (s/cat :extension? (s/? #{'extension})
                                                                                       :imported-type (s/or :declared :common/jvm-declared-type
                                                                                                            :q-name :common/q-name-w-static-import)
                                                                                       :name (s/or :wildcard #{'*}
                                                                                                   :member-name :common/valid-id))
                                                                :imported-type (s/or :declared :common/jvm-declared-type
                                                                                     :q-name :common/q-name)
                                                                :imported-namespace :common/q-name-wildcard))))
