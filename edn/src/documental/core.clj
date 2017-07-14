(ns documental.core
  (:require [clj-pdf.core :refer :all]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.xml :as xml]
            [xml-short.core :refer :all]
            [clojure.string :as s])
  (:import (java.io File FileInputStream FileNotFoundException)))

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

(defn make-poly
  ([points] (make-poly points {}))
  ([points opts] [:polygon (into opts {:points (s/join " " (map (partial s/join ",") points))})]))

(def ^:private svg-outer-args #{:under :translate :scale :rotate})
(def ^:private svg-inner-args #{:width :height :viewBox})

(def ^:private svg-funcs {:text make-text :line make-line :rect make-rect :circle make-circle :poly make-poly})

(defn read-funcs [func-map]
  (fn [shape]
    (let [label (first shape)
          func (func-map label)
          args (rest shape)]
      (if (nil? func)
        shape
        (apply func args)))))

(def read-svg (read-funcs svg-funcs))

(defn make-svg [opts & args]
  (let [outer (select-keys opts svg-outer-args)
        inner (select-keys opts svg-inner-args)
        svg-text (->> args
                      (map read-svg)
                      (into [:svg (into {:xmlns "http://www.w3.org/2000/svg"} inner)])
                      (x-pand)
                      (xml/emit-element)
                      (with-out-str)
                      (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE svg>"))]
    [:svg outer svg-text]))

(def ^:private pdf-funcs {:svg make-svg})

(def read-pdf (read-funcs pdf-funcs))

(defn -main [& [in out]]
    (try
      (let [[opts & doc]  (->> in
                               (io/resource)
                               (.toString)
                               (slurp)
                               (edn/read-string))]
        (->> out
             (io/file (io/resource ""))
             (io/output-stream)
             (pdf (into [opts] (map read-pdf doc))))
        "Completed")
      (catch FileNotFoundException fe
        (println "Cannot process")
        (println (.getMessage fe))
        )
      (catch Exception e
        (println (.getMessage e))
        (.printStackTrace e))))
