(ns examples.hello-world
  (:require [clojure-fx.core :as c]
            [clojure-fx.launch :refer :all]))

(defn -main [& args]
  (let [title "Hello World!"
        blueprint [:stack-pane
                   {:height 250
                    :width 300}
                   [:button
                    {:id :talk
                     :text "Say 'Hello World'"
                     :on-action (fn [_] (println "Hello World"))}]]]
    (c/launch-app title blueprint)))