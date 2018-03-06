(ns fivelang.routing.grammar
  (:require [clojure.spec.alpha :as s]
            [fivelang.common.grammar :refer :all]
            [fivelang.common.validation :as v]))

(comment "
Model :
  importSection=XImportSection?
  declarations+=AbstractDeclaration*;
")
(s/def :routing/model
  (s/and vector?
         (s/cat :import-section :common/x-import-section
                :declarations :routing/abstract-declarations)))

(comment "
AbstractDeclaration :
  Dependency | Route;
")
(s/def :routing/abstract-declarations
  (s/* (s/or :dependency :routing/dependency
             :route :routing/route)))

(comment "\n
Dependency :
  'inject' annotations+=XAnnotation? type=JvmTypeReference name=ID;
")
(s/def :routing/dependency
  (s/and vector?
         (s/cat :label #{'inject}
                :annotations (s/* :common/x-annotation)
                :type :common/jvm-type-ref
                :name :common/id)))

(comment "\n
Route :
  requestType=RequestType url=URL
  ('when' condition=XExpression)?
  'do' call=XExpression;
")
(s/def :routing/route
  (s/and vector?
         (s/cat :request-type :routing/request-type
                :url :routing/url
                :when (s/? (s/cat :label #{'when}
                                  :condition :common/x-expression))
                :do (s/cat :label #{'do}
                           :call :common/x-expression))))

(comment "\n
enum RequestType :
  GET | POST | PUT | DELETE | HEAD;
")
(s/def :routing/request-type '#{GET POST PUT DELETE HEAD})

(comment "\n
/**
* matches URLs like
* 	'/foo/bar.html' or
* 	'/customer/:customerID/save'
*/
URL :
  {URL}
  ( '/' | ('/' (QualifiedName | variables+=Variable))*
  ('/' variables+=Variable wildcard?='*')?);
")
(s/def :routing/url
  (s/and vector?
         (s/coll-of (s/or :q-name (s/and string? (v/matches? qualified-name))
                          :var :routing/variable))))

(comment "
Variable :
  ':' name=ID;
")
(s/def :routing/variable
  (s/and keyword?
         (v/named-as ID)))
