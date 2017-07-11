(ns documental.core
  (:require [clj-pdf.core :refer :all]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.xml :as xml]
            [xml-short.core :refer :all])
  (:import (java.io File FileInputStream)))

(defn make-text
  ([text x y] (make-text text x y {}))
  ([text x y opts] [:text (into opts {:x x :y y}) text]))

(defn make-line
  ([x1 y1 x2 y2] (make-line x1 y1 x2 y2 {}))
  ([x1 y1 x2 y2 opts] [:line (into opts {:x1 x1 :y1 y1 :x2 x2 :y2 y2})]))

(defn make-rect
  ([x y width height] (make-rect x y width height {}))
  ([x y width height opts] [:rect (into opts {:x x :y y :width width :height height})])
  ([x y width height rx ry] (make-rect x y width height rx ry {}))
  ([x y width height rx ry opts] [:rect (into opts {:x x :y y :width width :height height :rx rx :ry ry})]))

(defn make-circle
  ([cx cy r] (make-circle cx cy r {}))
  ([cx cy r opts] [:circle (into opts {:cx cx :cy cy :r r})]))

(defn read-short [shape]
  (let [label (first shape)
        func ({:text make-text
               :line make-line
               :rect make-rect
               :circle make-circle} label)
        args (rest shape)]
    (apply func args)))

(defn -main [& [in out]]
  (try
    (pdf
      (into [{:left-margin 18
              :right-margin 18
              :top-margin 36
              :bottom-margin 36
              :size :letter
              :footer false}]
            (interpose [:pagebreak]
                       (map (fn [block]
                              [:svg {:under true}
                               (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE svg>"
                                    (with-out-str
                                      (xml/emit-element
                                        (x-pand
                                          (into [:svg {:xmlns "http://www.w3.org/2000/svg"
                                                       :width 612
                                                       :height 792}]
                                                (map read-short block))))))])
                            (->> in
                                 (io/resource)
                                 (.toString)
                                 (slurp)
                                 (edn/read-string)))))
      (io/output-stream (io/file (io/resource "") out)))
    (catch Exception e
      (println (.getMessage e))
      (.printStackTrace e))))
