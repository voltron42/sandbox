(ns fivelang.template.grammar
  (:require [clojure.spec.alpha :as s]
            [fivelang.common.validation :as v]))

(comment "
TemplateFile:
  '<!--''«'
  ('package' package=QualifiedName)?
  importSection=XImportSection?
  params+=Parameter*
  body=RichString;
")
(s/def :template/template-file
  (s/cat :package (s/? (s/cat :label #{'package}
                              :name :common/q-name))
         :import-section (s/? :common/x-import-section)
         :params (s/and vector?
                        (s/coll-of :template/parameter))
         :body :template/rich-string))

(comment "
Parameter:
  annotations+=XAnnotation*
  'param' type=JvmTypeReference? name=ID ('=' defaultexp=XExpression)?;
")
(s/def :template/parameter
  (s/and vector?
         (s/cat :annotations (s/and vector?
                                    (s/coll-of :common/x-annotation))
                :label #{'param}
                :type (s/? :common/jvm-type-ref)
                :name :common/id
                :default-exp (s/? :common/x-expression))))

(comment "
RichString returns xbase::XBlockExpression:
  {RichString}
  expressions+=RichStringLiteral
  (expressions+=RichStringPart expressions+=RichStringLiteral)*;
")
(s/def :template/rich-string
  (s/and vector?
         (s/cat :first :template/rich-string-literal
                :rest (s/* (s/cat :part :template/rich-string-part
                                  :literal :template/rich-string-literal)))))

(comment "
RichStringLiteral returns xbase::XStringLiteral:
  {RichStringLiteral} value=TEXT;
")
(s/def :template/rich-string-literal string?)

(comment "
RichStringPart returns xbase::XExpression:
  XExpressionInsideBlock |
  RichStringForLoop |
  RichStringIf;
")
(s/def :template/rich-string-part
  (s/or :expression :common/x-expression
        :for :template/rich-string-for-loop
        :if :template/rich-string-if))

(comment "
RichStringForLoop returns xbase::XForLoopExpression:
  {RichStringForLoop}
  \"FOR\" declaredParam=JvmFormalParameter ':' forExpression=XExpression
  eachExpression=RichString
  \"ENDFOR\";
")
(s/def :template/rich-string-for-loop
  (s/and vector?
         (s/cat :for #{'FOR}
                :declared-param :common/jvm-formal-parameter
                :for-expression :common/x-expression
                :each-expression :template/rich-string)))

(comment "
RichStringIf returns xbase::XIfExpression:
  {RichStringIf}
  \"IF\" if=XExpression
  then=RichString
  (else=RichStringElseIf | \"ELSE\" else=RichString)?
  \"ENDIF\";
")
(comment "
RichStringElseIf returns xbase::XIfExpression:
  {RichStringIf}
  \"ELSEIF\"if=XExpression
  then=RichString
  (else=RichStringElseIf | \"ELSE\" else=RichString)?;
")
(s/def :template/rich-string-if
  (s/and vector?
         (s/cat :label #{'IF}
                :test :common/x-expression
                :then :template/rich-string
                :elseif (s/* (s/cat :label #{'ELSEIF}
                                    :if :common/x-expression
                                    :then :template/rich-string))
                :else (s/? (s/cat :label #{'ELSE}
                                  :else :template/rich-string)))))

