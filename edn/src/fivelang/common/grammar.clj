(ns fivelang.common.grammar
  (:require [clojure.spec.alpha :as s]
            [fivelang.common.validation :as v]))

(s/def :common/id (s/and symbol? (v/named-as v/ID)))

(s/def :common/q-name (s/and symbol? (v/named-as v/qualified-name)))

(s/def :common/valid-id (s/and symbol? (v/named-as v/valid-id)))

(s/def :common/full-jvm-formal-param any?)
(s/def :common/jvm-formal-parameter any?)
(s/def :common/jvm-type-ref any?)
(s/def :common/x-annotation any?)
(s/def :common/x-block-expression any?)
(s/def :common/x-expression any?)

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
