(ns fivelang.tortoise.grammar
  (:require [clojure.spec.alpha :as s]))

(comment "
Program :
  body=Body
  subPrograms+=SubProgram*;
")
(s/def :tortoise/program
  (s/and vector
         (s/cat :body :tortoise/body
                :sub-programs (s/* :tortoise/sub-program))))

(comment "
SubProgram:
  'sub' name=ValidID (':' returnType=JvmTypeReference)?
  (parameters += FullJvmFormalParameter)*
  body=Body;
")
(s/def :tortoise/sub-program
  (s/and vector?
         (s/cat :name :common/valid-id
                :return-type (s/or :void #{'void}
                                   :type :common/jvm-type-ref)
                :parameters (s/* :common/full-jvm-formal-param)
                :body :tortoise/body)))

(comment "
Body returns XBlockExpression:
  {XBlockExpression}
  'begin'
  (expressions+=XExpressionInsideBlock ';'?)*
  'end';
")
(s/def :tortoise/body
  (s/and vector?
         (s/coll-of :common/x-expression-inside-block)))

(comment "
Executable:
  Program | SubProgram;
")
(s/def :tortoise/executable
  (s/or :program :tortoise/program
        :sub-program :tortoise/sub-program))

