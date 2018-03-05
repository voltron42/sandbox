(ns fivelang.template.grammar)

(comment "
TemplateFile:
  '<!--''«'
  ('package' package=QualifiedName)?
  importSection=XImportSection?
  params+=Parameter*
  body=RichString;

Parameter:
  annotations+=XAnnotation*
  'param' type=JvmTypeReference? name=ID ('=' defaultexp=XExpression)?;

RichString returns xbase::XBlockExpression:
  {RichString}
  expressions+=RichStringLiteral
  (expressions+=RichStringPart expressions+=RichStringLiteral)*;

RichStringLiteral returns xbase::XStringLiteral:
  {RichStringLiteral} value=TEXT;

RichStringPart returns xbase::XExpression:
  XExpressionInsideBlock |
  RichStringForLoop |
  RichStringIf;

RichStringForLoop returns xbase::XForLoopExpression:
  {RichStringForLoop}
  \"FOR\" declaredParam=JvmFormalParameter ':' forExpression=XExpression
  eachExpression=RichString
  \"ENDFOR\";

RichStringIf returns xbase::XIfExpression:
  {RichStringIf}
  \"IF\" if=XExpression
  then=RichString
  (else=RichStringElseIf | \"ELSE\" else=RichString)?
  \"ENDIF\";

RichStringElseIf returns xbase::XIfExpression:
  {RichStringIf}
  \"ELSEIF\"if=XExpression
  then=RichString
  (else=RichStringElseIf | \"ELSE\" else=RichString)?;

terminal TEXT : '»' (!'«')* (EOF|'«');
")
