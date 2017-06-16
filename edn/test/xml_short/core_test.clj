(ns xml-short.core-test
  (:require [clojure.test :refer :all]
            [xml-short.core :refer :all]))

(deftest cml-mapping-test
  (testing "Lets see if this works..."
    (is (= (x-pand '[html
                     [body
                      [h2 "Hello World!"]
                      hr
                      [p "The quick brown fox jumped over the lazy dog!"]]])
           {:tag :html
            :content [{:tag :body
                       :content [{:tag :h2
                                  :content ["Hello World!"]}
                                 {:tag :hr}
                                 {:tag :p
                                  :content ["The quick brown fox jumped over the lazy dog!"]}]}]}))))
