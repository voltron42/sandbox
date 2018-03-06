(ns fivelang.domain.grammar
  (:require [clojure.spec.alpha :as s]
            [fivelang.common.grammar :refer :all]))

(comment "
MongoFile:
  importSection=XImportSection?
  elements+=AbstractElement*;
")
(s/def :domain/mongo-file
  (s/and vector?
         (s/cat :import-section (s/? :common/x-import-section)
                :elements :domain/abstract-element)))

(comment "
AbstractElement:
  PackageDeclaration | MongoBean;
")
(s/def :domain/abstract-element
  (s/keys :opt-un [:domain/packages
                   :domain/beans]))

(comment "
PackageDeclaration:
  'package' name=QualifiedName '{'
    elements+=AbstractElement*
  '}';
")
(s/def :domain/packages
  (s/map-of :common/q-name
            :domain/abstract-element))

(comment "
MongoBean:
  name=ValidID '{'
    features+=AbstractFeature*
  '}';
")
(s/def :domain/beans
  (s/map-of :common/valid-id
            :domain/bean))

(s/def :domain/bean
  (s/map-of :commmon/valid-id
            :domain/abstract-feature))
(comment "
AbstractFeature:
  MongoOperation | MongoProperty;
")
(s/def :domain/abstract-feature
  (s/map-of :common/valid-id
            (s/or :operation :domain/mongo-operation
                  :property :domain/mongo-property)))

(comment "
MongoProperty:
  (type=JvmTypeReference | inlineType=MongoBean) (many?='*')? name=ValidID;
")
(s/def :domain/mongo-property
  (s/or :type :common/jvm-type-ref
        :many-type (s/and list?
                          (s/cat :label #{'*}
                                 :type :common/jvm-type-ref))
        :inline-type (s/and vector?
                            (s/cat :type-name (s/or :type :common/valid-id
                                                    :many-type (s/and list?
                                                                      (s/cat :label #{'*}
                                                                             :type :common/valid-id)))
                                   :body :domain/bean))))

(comment "
MongoOperation:
  =>(returnType=JvmTypeReference name=ValidID '(')
    (parameters+=FullJvmFormalParameter
      (',' parameters+=FullJvmFormalParameter)*
    )?
  ')'
  body=XBlockExpression;
")
(s/def :domain/mongo-property
  (s/cat :return-type :common/jvm-type-ref
         :parameters (s/* :common/full-jvm-formal-param)
         :body :common/x-block-expression))