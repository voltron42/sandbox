(ns routing.controller
  (:import (routing ServerException)))

(def data (atom {}))

(defn handle-guess [guess] (if (nil? guess) "I have no guess" (str "My guess is " guess)))

(defn add-employee [employee]
  (println (str "employee: " employee))
  (reset!
         data
         (let [id (employee "id")]
           (println (str "id: " id))
           (if (contains? @data id)
             (throw (ServerException.
                      400
                      (str "Record with id '" id "' already exists, cannot add record " employee)))
             (assoc @data id employee)))))

(defn get-employee [employee-id] (@data employee-id))

(defn get-employees [] (vec (vals @data)))

(defn get-employees-by [field value] (vec (filter #(= value (% field)) (vals @data))))

(defn update-employee [employee-id employee]
  (reset! data
               (let [record (assoc employee "id" employee-id)]
                 (if (contains? @data employee-id)
                   (assoc @data employee-id record)
                   (throw (ServerException.
                            400
                            (str "Record with id " employee-id " does not exist, cannot update record " record)))))))

(defn delete-employee [employee-id] (reset! data (dissoc @data employee-id)))