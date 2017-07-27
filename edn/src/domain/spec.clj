(ns domain.spec
  (:require [clojure.spec.alpha :as s]))

(s/def ::field-type (s/and symbol? #(or (nil? (namespace %)) (#{"?" "*" "+" "$"} (namespace %)))))

(s/def ::text-type (s/and symbol? #(= "text" (name %)) #(or (nil? (namespace %)) (#{"?" "*" "+"} (namespace %)))))

(s/def ::min int?)
(s/def ::max int?)
(s/def ::max-size int?)
(s/def ::format string?)

(s/def ::field-constraints (s/and not-empty (s/keys :opt-un [::max-size ::min ::max ::format])))

(s/def ::constrained-field (s/and vector? (s/or :constrained (s/tuple ::field-type ::field-constraints)
                                                :enum (s/cat :text-type ::text-type
                                                             :first-enum-val string?
                                                             :enum-vals (s/+ string?)))))

(s/def ::field (s/or :simple-field ::field-type
                     :constrained-field ::constrained-field))

(s/def :simple/entity (s/map-of simple-symbol? ::field))

(s/def :entity/id simple-symbol?)

(s/def :entity/unique (s/and vector? (s/coll-of simple-symbol?)))

(s/def ::entity-constraints (s/and not-empty (s/keys :opt-un [:entity/id :entity/unique])))

(s/def :constrained/entity (s/and vector? (s/tuple :simple/entity ::entity-constraints)))

(s/def ::entity (s/or :simple :simple/entity
                      :constrained :constrained/entity))

(s/def ::domain (s/map-of simple-symbol? ::entity))
