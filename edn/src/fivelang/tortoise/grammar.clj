(ns fivelang.tortoise.grammar)

(comment "
Program :
  body=Body
  subPrograms+=SubProgram*;

SubProgram:
  'sub' name=ValidID (':' returnType=JvmTypeReference)?
  (parameters += FullJvmFormalParameter)*
  body=Body;

Body returns XBlockExpression:
  {XBlockExpression}
  'begin'
  (expressions+=XExpressionInsideBlock ';'?)*
  'end';

Executable:
  Program | SubProgram;
")
