(ns examples.circles
  (:require [clojure-fx.core :as c]))

(defn -main [& args]
  (let [title "Circles"
        blueprint [{:width 800
                    :height 600
                    :fill :black}
                   [:group
                    [:group
                     {:id :circles
                      :effect [:box-blur {:width 10 :height 10 :iter 3}]}
                     (mapv #(vector :circle {}) (range 30))
                     ]
                    [:rect
                     {:id :colors
                      :width :scene/width
                      :height :scene/height
                      :blend-mode :overlay
                      :bind/scene [:width-property :height-property]
                      :fill [:linear-gradient
                             {:start-x 0
                              :start-y 1
                              :end-x 1
                              :end-y 0
                              :proportional true
                              :cycle-method :no-cycle}
                             [:stop {:offset 0.0 :color :hex/#f8bd55}]
                             [:stop {:offset 0.14 :color :hex/#c0fe56}]
                             [:stop {:offset 0.28 :color :hex/#5dfbc1}]
                             [:stop {:offset 0.43 :color :hex/#64c2f8}]
                             [:stop {:offset 0.57 :color :hex/#be4af7}]
                             [:stop {:offset 0.71 :color :hex/#ed5fc2}]
                             [:stop {:offset 0.85 :color :hex/#f8bd55}]
                             [:stop {:offset 1.0 :color :hex/#ef504c}]
                             ]}]
                    [:group
                     {:id :blend-mode-group}
                     [:group
                      [:rect
                       {:width :scene/width
                        :height :scene/height
                        :fill :color/black}]
                      :bind/circles]
                     :bind/colors]]]
        controller (fn [context] {})
        animate (fn [context] (reduce #(concat %1 [[:zero (assoc {} (.translateXProperty %2) (rand-int 800) (.translateYProperty %2) (rand-int 600))]
                                                   [40000 (assoc {} (.translateXProperty %2) (rand-int 800) (.translateYProperty %2) (rand-int 600))]])
                                      [] (->> context (:circles) (.getChildren))))]
    (c/launch-app title blueprint controller animate)))