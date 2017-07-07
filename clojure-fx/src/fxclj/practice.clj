(ns fxclj.practice
  (:require [fx-clj.core :as fx]))

(defn -main []
  (let [my-view (fx/compile-fx
                  [:stack-pane
                   {:pref-width 300
                    :pref-height 250}
                   [:button
                    {:text "Say 'Hello World'"
                     :on-action #(println "Hello World")}]])]
    (fx/sandbox my-view)))