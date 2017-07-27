(ns routing.server
  (:require [routing.core :as rc]
            [routing.controller :as ctrl]
            [org.httpkit.server :refer :all]))

(def routings '[[GET ["guess" :theGuess] {} (ctrl/handle-guess :theGuess)]
                [GET ["guess"] {} (ctrl/handle-guess (request/query "theGuess"))]
                [GET [] {} (ctrl/handle-guess nil)]
                [POST ["employee"] {"content-type" "application/json"} (ctrl/add-employee request/json)]
                [POST ["employee"] {"content-type" "application/x-www-form-urlencoded"} (ctrl/add-employee request/params)]
                [GET ["employee" :id] {} (ctrl/get-employee :id)]
                [GET ["employees"] {} (ctrl/get-employees)]
                [GET ["employees" :field :value] {} (ctrl/get-employees-by :field :value)]
                [PUT ["employee" :id] {"content-type" "application/json"} (ctrl/update-employee :id request/json)]
                [PUT ["employee" :id] {"content-type" "application/x-www-form-urlencoded"} (ctrl/update-employee :id request/params)]
                [DELETE ["employee" :id] {} (ctrl/delete-employee :id)]])

(def controller {:ctrl {:handle-guess     ctrl/handle-guess
                        :add-employee     ctrl/add-employee
                        :get-employee     ctrl/get-employee
                        :get-employees     ctrl/get-employees
                        :get-employees-by     ctrl/get-employees-by
                        :update-employee  ctrl/update-employee
                        :delete-employee  ctrl/delete-employee}})

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))

(defn -main [& args]
  ;; The #' is useful when you want to hot-reload code
  ;; You may want to take a look: https://github.com/clojure/tools.namespace
  ;; and http://http-kit.org/migration.html#reload
  (let [app (try
              (rc/build-app routings controller)
              (catch Throwable t
                (println (.getMessage t))
                (.printStackTrace t)))]
    (reset! server (run-server app {:port 8080}))))