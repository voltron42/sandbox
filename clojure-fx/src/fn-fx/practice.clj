(ns fn-fx.practice
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.controls :as ui]))

(defn -main []
  (let [u (ui/stage
            :title "Hello World!"
            :shown true
            :scene (ui/scene
                     :root (ui/stack-pane
                             :id ::my-stack-pain
                             :pref-height 250.0
                             :pref-width 300.0
                             :children [(ui/button
                                          :id ::my-button
                                          :text "Say 'Hello World'"
                                          :on-action {:code #(println "Hello World")
                                                      :fn-fx/includes {:fn-fx/event #{:scene-x :scene-y}
                                                                       ::my-stack-pain #{:height :width :x :y}
                                                                       ::my-button #{:text}}})])))
        handler-fn (fn [{:keys [code ref fn-fx/includes]}]
                     (println "code: " code)
                     (println "ref: " ref)
                     (println "includes: " includes)
                     (println "event: " (:fn-fx/event includes))
                     )]
    (dom/app u handler-fn)))
