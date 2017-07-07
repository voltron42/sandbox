(ns pixelart.core-test
  (:require [clojure.test :refer :all]
            [pixelart.core :refer :all]))

(deftest test-build-image
  (is (= (build-image
           10
           '{:#000 [E/a F/a G/a H/a I/a
                    D/b J/b K/b
                    C/c I/c L/c
                    B/d E/d H/d L/d M/d N/d
                    A/e D/e F/e G/e K/e L/e M/e
                    A/e D/e ]
             :#090 [E/b F/b G/b I/b
                    D/c E/c H/c C/d
                    D/d F/d G/d K/d
                    B/e C/e J/e]
             :#ffa [H/b
                    F/c G/c J/c K/c
                    I/d J/d
                    E/e H/e I/e]})
         '[svg {:width 140 :height 160}
           [rect {:id :E/a :x 40 :y 0 :width 10 :height 10 :fill "#000" :stroke :none}]
           [rect {:id :F/a :x 50 :y 0 :width 10 :height 10 :fill "#000" :stroke :none}]
          ])))