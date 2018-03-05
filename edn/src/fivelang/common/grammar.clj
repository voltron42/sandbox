(ns fivelang.common.grammar
  (:require [clojure.spec.alpha :as s]
            [fivelang.common.validation :as v]))

(s/def :common/id (s/and symbol? (v/named-as v/ID)))

(s/def :common/q-name (s/and symbol? (v/named-as v/qualified-name)))

(s/def :common/valid-id (s/and symbol? (v/named-as v/valid-id)))

(s/def :common/abstract-element any?)

(s/def :common/full-jvm-formal-param any?)
(s/def :common/jvm-formal-parameter any?)
(s/def :common/jvm-type-ref any?)
(s/def :common/x-annotation any?)
(s/def :common/x-block-expression any?)
(s/def :common/x-expression any?)
(s/def :common/x-import-section any?)

(s/def :common/x-expression-inside-block any?)              ;?
