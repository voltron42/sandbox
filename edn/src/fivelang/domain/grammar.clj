(ns fivelang.domain.grammar
  (:require [clojure.spec.alpha :as s]
            [fivelang.common.grammar :refer :all]))

(comment "
MongoFile:
  importSection=XImportSection?
  elements+=AbstractElement*;
")

(s/def :domain/mongo-file
  (s/cat :import-section (s/? :common/x-import-section)
         :elements (s/* :domain/abstract-element)))

(comment "
AbstractElement:
  PackageDeclaration | MongoBean;
")

(s/def :domain/abstract-element
  (s/or :pkg-decl :domain/package-declaration
        :mongo-bean :domain/mongo-bean))

(comment "
PackageDeclaration:
  'package' name=QualifiedName '{'
    elements+=AbstractElement*
  '}';
")

(s/def :domain/package-declaration
  (s/or :package :common/q-name
        :elements (s/* :common/abstract-element)))

(comment "
MongoBean:
  name=ValidID '{'
    features+=AbstractFeature*
  '}';
")

(s/def :domain/mongo-mean
  (s/cat :name :common/valid-id
         :features (s/* :domain/abstract-feature)))

(comment "
AbstractFeature:
  MongoOperation | MongoProperty;
")

(s/def :domain/abstract-feature
  (s/or :operation :domain/mongo-operation
        :property :domain/mongo-property))

(comment "
MongoProperty:
  (type=JvmTypeReference | inlineType=MongoBean) (many?='*')? name=ValidID;
")

(s/def :domain/mongo-property
  (s/cat :type (s/or :type :common/jvm-type-ref
                     :inline-type :domain/mongo-bean)
         :many? (s/? '#{*})
         :name :commmon/valid-id))

(comment "
MongoOperation:
  =>(returnType=JvmTypeReference name=ValidID '(')
    (parameters+=FullJvmFormalParameter
      (',' parameters+=FullJvmFormalParameter)*
    )?
  ')'
  body=XBlockExpression;
")
