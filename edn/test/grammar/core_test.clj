(ns grammar.core-test
  (:require [grammar.core :refer :all]
            [clojure.test :refer :all]))

(deftest test-xml-short
  (is (validate
        '{grammar/name "XML Short"
          grammar/root vector/node
          grammar/syntax {vector/node (symbol/tag-name [map/attrs] [& #{vector/node symbol/tag-name string/value}])
                          map/attrs {symbol/attr-name literal/attr-value}}}
        '[html {lang "en"}
          [head
           [meta {charset "utf-8"}]
           [title "This is the title"]
           [meta {name "viewport" content "width=device-width, initial-scale=1.0"}]
           [meta {name "description" content "This is the description"}]
           [meta {name "author" content "I am the author"}]]
          [body
           [div {class "section"}
            [h1 "Section One"]]
           [div {class "section"}
            [h1 "Section Two"]
            [p "This is the description for section two"]]]])))