(ns faultline.grammar
  (:require [clojure.spec.alpha :as s]
            [faultline.validation :as v]))

(s/def :fault/regression (s/keys :req-un [:fault/suites]
                                 :opt-un [:fault/configs]))

(s/def :fault/configs (s/or :single :fault/config
                            :sequential (s/and vector?
                                               (s/coll-of :fault/config))))

(s/def :fault/config (s/map-of :fault/variable :config/value))

(s/def :config/value
  (s/or :complex (s/and list?
                        (s/or :file (s/cat :label #{'file}
                                           :type '#{xml json text}
                                           :file-name :fault/templated-string)
                              :json (s/cat :label #{'json}
                                           :body :body/json)
                              :xml (s/cat :label #{'xml}
                                          :body :body/xml)
                              :plain-text (s/cat :label #{'text}
                                                 :body :fault/templated-string)))
        :text string?
        :number number?
        :boolean boolean?
        :variable :fault/variable))

(def valid-name #"[a-zA-Z][a-zA-Z0-9_-$?]*")

(s/def :fault/variable (s/and keyword? (v/named-as valid-name)))

(s/def :fault/suites (s/merge (s/map-of :suite/name :fault/suite)
                              (s/map-of :test/name :fault/test)))

(s/def :fault/suite (s/keys :req-un [:fault/suites]
                            :opt-un [:test/pre :test/post]))

(s/def :suite/name (s/and symbol (v/named-as valid-name)))

(s/def :test/name (s/and symbol (v/named-as valid-name)))

(s/def :fault/test (s/merge (s/keys :opt-un [:test/pre :test/post])
                            (s/or :endpoint (s/keys :req-un [:test/request :test/response])
                                  :cli (s/keys :req-un [:test/command-line]
                                               :opt-un [:test/console-out :test/result]))))

(s/def :fault/templated-string (s/or :simple string?
                                     :variable (s/and vector?
                                                      (s/coll-of (s/or :string string?
                                                                       :variable :fault/variable)))))

(s/def :test/command-line :fault/templated-string)

(s/def :test/console-out (s/and list?
                                (s/or :file (s/cat :label #{'file}
                                                   :file-name :fault/templated-string)
                                      :plain-text (s/cat :label #{'text}
                                                         :body :fault/templated-string))))

(s/def :test/result int?)

(s/def :test/request (s/keys :req-un [:request/url]
                             :opt-un [:request/method :endpoint/headers :endpoint/body]))

(s/def :request/url (s/or :simple string?
                          :variable (s/and vector?
                                           (s/cat :protocal #{"http://" "https://"}
                                                  :steps (s/+ (s/or :string string?
                                                                    :variable :fault/variable))))))

(s/def :request/method '#{GET POST PUT DELETE HEAD OPTIONS CONNECT PATCH})

(s/def :endpoint/headers (s/map-of string? (s/or :primitive string?
                                                 :variable :fault/variable)))

(s/def :endpoint/body (s/and list?
                            (s/or :file (s/cat :label #{'file}
                                               :type '#{xml json text}
                                               :file-name :fault/templated-string)
                                  :json (s/cat :label #{'json}
                                               :body :body/json)
                                  :xml (s/cat :label #{'xml}
                                              :body :body/xml)
                                  :plain-text (s/cat :label #{'text}
                                                     :body :fault/templated-string))))

(s/def :body/json (s/or :array (s/and vector? (s/coll-of :body/json))
                        :map (s/map-of string? :body/json)
                        :primitive (s/or :string string?
                                         :boolean boolean?
                                         :nil nil?
                                         :int int?
                                         :decimal decimal?
                                         :variable :fault/variable)))

(def xml-valid-name #"[a-wyzA-WYZ_][a-zA-Z0-9_-\.]*")

(s/def :xml/name (s/and symbol (s/or :namespace (v/named-as xml-valid-name xml-valid-name)
                                     :name-only (v/named-as xml-valid-name))))

(s/def :body/xml (s/and vector? (s/cat :name :xml/name
                                       :attrs (s/? (s/map-of :xml/name (s/or :primitive string?
                                                                             :variable :fault/variable)))
                                       :children (s/* (s/or :node :body/xml
                                                            :text string?
                                                            :variable :fault/variable)))))
(s/def :response/status (v/one-of (concat (range 100 104) (range 200 209) [226] (range 300 309) (range 400 419) (range 421 427) [428 429 431 451] (range 500 509) [510 511])))

(s/def :test/response (s/keys :opt-un [:response/status :endpoint/headers :endpoint/body]))

(s/def :test/pre (s/and
                   (s/keys :opt-un [:fault/configs :pre/db])
                   (v/min-count 1)))

(s/def :test/post
  ;todo
  any?)
