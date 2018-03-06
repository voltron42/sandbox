(ns fivelang.common.grammar
  (:require [clojure.spec.alpha :as s]
            [fivelang.common.validation :as v]))

(def ID #"[a-zA-Z$_][a-zA-Z$_0-9]*")

(def valid-id ID)

(def qualified-name #"[a-zA-Z$_][a-zA-Z$_0-9]*([\.][a-zA-Z$_][a-zA-Z$_0-9]*)*")

(s/def :common/id (s/and symbol? (v/named-as ID)))

(s/def :common/q-name (s/and symbol? (v/named-as qualified-name)))

(s/def :common/valid-id (s/and symbol? (v/named-as valid-id)))

(s/def :common/full-jvm-formal-param
  (s/and vector?
         (s/cat :name :common/valid-id
                :type :common/jvm-type-ref)))

(s/def :common/jvm-formal-parameter
  (s/or :name-only :common/valid-id
        :full :common/full-jvm-formal-param))

(s/def :common/jvm-type-ref :common/q-name)

(s/def :common/x-annotation
  (s/and list?
         (s/cat :label #{(symbol "@")}
                :type (s/or :annotation-type :common/jvm-annotation-type
                            :q-name :common/q-name)
                :value (s/? (s/or :pairs :common/x-annotation-element-value-pairs
                                  :list :common/x-annotation-element-value)))))

(s/def :common/x-annotation-element-value-pairs
  (s/map-of (s/or :op :common/jvm-operation
                  :id :common/valid-id)
            :common/x-annotation-element-value))

(s/def :common/x-annotation-element-values
  (s/or :single :common/x-annotation-or-expression
        :multi (s/and vector?
                      (s/coll-of :common/x-annotation-or-expression))))

(s/def :common/x-annotation-or-expression
  (s/or :annotation :common/x-annotation
        :expression :common/x-expression))

(s/def :common/x-block-expression
  (s/and vector?
         (s/coll-of :common/x-expression)))

(s/def :common/function
  (s/cat :func symbol?
         :args (s/* :common/function-arg)))

(s/def :common/x-expression
  (s/and list?
         :common/function))

(s/def :common/x-expression-inside-block
  (s/and list?
         (s/or :assign :common/assign
               :for :common/for
               :if :common/if
               :procedure :common/procedure)))

(s/def :common/procedure :common/function)

(s/def :common/function-arg
  (s/or :literal :common/literal
        :nil nil?
        :variable :common/variable
        :map (s/map-of :common/literal :common/function-arg)
        :array (s/and vector? (s/coll-of :common/function-arg))
        :q-name :common/q-name
        :expression (s/and list? :common/function)))

(s/def :common/variable keyword?)

(s/def :common/literal
  (s/or :number number?
        :string string?
        :boolean boolean?))

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

(s/def :common/jvm-declared-type any?)

(s/def :common/q-name-w-static-import any?)

(s/def :common/q-name-wildcard any?)