(ns fivelang.build.grammar
  (:require [clojure.spec.alpha :as s]
            [fivelang.common.grammar :refer :all]))

(comment "
BuildFile:
(\"package\" name=QualifiedName)?
importSection=XImportSection?
declarations+=Declaration*;
")
(s/def :build/build-file
  (s/and vector?
         (s/cat :package-name (s/? (s/and list?
                                          (s/cat :label #{'package}
                                                 :name :common/q-name)))
                :import-section :common/x-import-section
                :declarations :build/declarations)))

(comment "
Declaration:
Task | Parameter;
")
(s/def :build/declarations
  (s/* (s/or :task :build/task
             :parameter :build/parameter)))

(comment "
Parameter:
'param' type=JvmTypeReference? name=ValidID ('=' init=XExpression)?;
")
(s/def :build/parameter
  (s/and vector?
         (s/cat :label #{'param}
                :type (s/? :common/jvm-type-ref)
                :name :common/valid-id
                :init (s/? :common/x-expression))))

(comment  "
Task:
'task' name=ValidID
('depends' depends+=[Task|ValidID] (',' depends+=[Task|ValidID])*)?
action=XBlockExpression;
  ")
(s/def :build/task
  (s/and vector?
         (s/cat :label #{'task}
                :name :common/valid-id
                :depends (s/? (s/cat :label #{'depends}
                                     :depends (s/+ (s/or :task :build/task
                                                         :valid-id :common/valid-id))))
                :action :common/x-block-expression)))
