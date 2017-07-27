(ns routing.spec
  (:require [clojure.spec.alpha :as s]))

(s/def ::method '#{GET POST PUT DELETE HEAD PATCH OPTIONS})
(s/def ::path (s/and vector? (s/* (s/or :string string?
                                        :keyword keyword?))))
(s/def ::headers (s/map-of string? string?))
(s/def ::ctrl (s/and list? (s/cat :func symbol?
                                  :args (s/* (s/or :symbol symbol?
                                                   :expression ::ctrl
                                                   :keyword keyword?
                                                   :string string?
                                                   :nil nil?)))))
(s/def ::routings (s/+ (s/tuple ::method ::path ::headers ::ctrl)))