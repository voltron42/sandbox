(ns template.core-test
  (:require [clojure.test :refer :all]
            [template.core :refer :all]))

(deftest test-example
  (let [example-tpl '[html {lang "en"}
                      [head
                       [meta {charset "utf-8"}]
                       [title :title]
                       [meta {name "viewport" content "width=device-width, initial-scale=1.0"}]
                       [meta {name "description" content :description}]
                       [meta {name "author" content :author}]]
                      [body
                       ({:sections :element}
                         [div {class "section"}
                          [h1 :element/title]
                          ((not-nil? :element/description)
                            [p :element/description])])]]
        example-data {:title "This is the title"
                      :description "This is the description"
                      :author "I am the author"
                      :sections [{:title "Section One"}
                                 {:title "Section Two"
                                  :description "This is the description for section two"}]}
        expected-result '[html {lang "en"}
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
                            [p "This is the description for section two"]]]]]
        (is (= expected-result (((build-template-factory {'not-nil? some?}) example-tpl) example-data)))))

(def out '[html {lang "en"}
           [head
            [meta {charset "utf-8"}]
            [title "This is the title"]
            [meta {name "viewport" content "width=device-width, initial-scale=1.0"}]
            [meta {name "description" content "This is the description"}]
            [meta {name "author" content "I am the author"}]]
           [body
            [[div {class "section"}
              [h1 "Section One"]
              []]
             [div {class "section"}
              [h1 "Section Two"]
              [[p "This is the description for section two"]]]]]])
