(ns fivelang.build.grammar
  (:require [clojure.spec.alpha :as s]))

(comment
  "
BuildFile:
(\"package\" name=QualifiedName)?
importSection=XImportSection?
declarations+=Declaration*;

Declaration:
Task | Parameter;

Parameter:
'param' type=JvmTypeReference? name=ValidID ('=' init=XExpression)?;

Task:
'task' name=ValidID
('depends' depends+=[Task|ValidID] (',' depends+=[Task|ValidID])*)?
action=XBlockExpression;
  ")
