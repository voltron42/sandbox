(ns examples.login-form2
  (:require [clojure-fx.core :as c]))

(defn -main [& args]
  (let [blueprint [{:stylesheets ["login.css"]}
                   [:grid-pane
                    {:grid-alignment :center
                     :h-gap 10
                     :v-gap 10
                     :padding 25
                     :width 300
                     :height 275
                     :grid [[0 0 2 1]
                            [0 1]
                            [1 1]
                            [0 2]
                            [1 2]
                            [1 4]
                            [1 6]]}
                    [:text
                     {:id :welcome-text
                      :text-value "Welcome"}]
                    [:label {:text "User Name:"}]
                    [:text-field {:id :user-text-field}]
                    [:label {:text "Password:"}]
                    [:password-field {:id :pw-box}]
                    [:h-box
                     {:h-box-alignment :bottom-right}
                     [:button {:id :sign-in
                               :text "Sign in"}]]
                    [:text {:id :action-target}]]]
        controller (fn [context]
                     {:sign-in/on-action (fn [_] (let [{:keys [action-target]} context]
                                                   (c/update-elem action-target {:text-value "Sign in button pressed"})))})]
    (c/launch-app "Log-In Panel:" blueprint controller)))