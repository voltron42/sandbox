(ns fivelang.routing.grammar)

(comment "
Model :
  importSection=XImportSection?
  declarations+=AbstractDeclaration*;

AbstractDeclaration :
  Dependency | Route;

Dependency :
  'inject' annotations+=XAnnotation? type=JvmTypeReference name=ID;

Route :
  requestType=RequestType url=URL
  ('when' condition=XExpression)?
  'do' call=XExpression;

enum RequestType :
  GET | POST | PUT | DELETE | HEAD;

/**
* matches URLs like
* 	'/foo/bar.html' or
* 	'/customer/:customerID/save'
*/
URL :
  {URL}
  ( '/' | ('/' (QualifiedName | variables+=Variable))*
  ('/' variables+=Variable wildcard?='*')?);

Variable :
  ':' name=ID;
")
