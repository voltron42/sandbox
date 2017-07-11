(ns documental.core
  (:require [clj-pdf.core :refer :all]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.xml :as xml]
            [xml-short.core :refer :all])
  (:import (java.io File FileInputStream)))

(defn make-file [resource]
  (->> resource
       (io/resource)
       (.toString)))

(defn -main [& [in out]]
  (try
    (pdf
      [{:left-margin 18
        :right-margin 18
        :top-margin 36
        :bottom-margin 36
        :size :letter
        :footer false}
       [:svg {:under true}
        (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE svg>"
             (with-out-str
               (xml/emit-element
                 (x-pand
                   (into [:svg {:xmlns "http://www.w3.org/2000/svg"
                                :width 612
                                :height 792}]
                         (edn/read-string (slurp (make-file in))))))))]]
      out)
    (catch Exception e
      (println (.getMessage e))
      (.printStackTrace e))))
