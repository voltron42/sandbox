(ns reserializer.core
  (:require [clojure.spec.alpha :as s]))

(s/def ::schema
  (s/keys :req-un [::root ::types])
  )

(s/def ::root
  (s/keys :req-un [])
  )

(s/def ::types (s/and vector? (s/coll-of ::type)))

(s/def ::type (s/keys :req-un [::name ::fields]))

(s/def ::fields (s/and vector? (s/coll-of ::field)))

(s/def ::field (s/or :p :primitive/field
                     :l :list/field
                     :o :obj/field))

(s/def ::primitive/field (s/keys :req-un [::name :primitive/type]))