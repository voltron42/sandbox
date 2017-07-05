(ns examples.hello-world
  (:require [clojure-fx.core :as c]))

(defn -main [& args]
  (let [title "Hello World!"
        blueprint [{:height 250
                    :width 300}
                   [:stack-pane
                    [:button
                     {:id :talk
                      :text "Say 'Hello World'"
                      :on-action (fn [_] (println "Hello World"))}]]]]
    (c/launch-app title blueprint)))