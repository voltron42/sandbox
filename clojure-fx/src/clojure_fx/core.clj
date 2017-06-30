(ns clojure-fx.core
  (:require [clojure.string :as s])
  (:import (javafx.scene.control Labeled ButtonBase Button PasswordField Label TextField)
           (javafx.geometry Pos Insets)
           (javafx.event EventHandler)
           (javafx.scene.layout Region GridPane HBox Pane StackPane)
           (javafx.scene.text Text)
           (javafx.scene.shape Shape)
           (javafx.scene.paint Color)
           (javafx.stage Stage)
           (javafx.scene Scene Parent Node Group)
           (javafx.application Platform)
           (com.sun.javafx.application PlatformImpl)
           (javafx.collections ObservableList)
           (javafx.animation Timeline KeyFrame KeyValue)
           (javafx.util Duration)))

;;http://docs.oracle.com/javafx/2/get_started/jfxpub-get_started.htm

(def ^:private positions (reduce (fn [prev pos] (assoc prev (keyword (s/lower-case pos)) pos)) {} (Pos/values)))

(def ^:private fields {:text            (fn [^Labeled labeled text] (.setText labeled text))
                       :on-action       (fn [^ButtonBase btn on-action]
                                          (.setOnAction btn (reify EventHandler
                                                              (handle [this event]
                                                                (on-action event)))))
                       :height          (fn [^Region region height] (.setPrefHeight region height))
                       :width           (fn [^Region region width] (.setPrefWidth region width))
                       :h-gap           (fn [^GridPane obj h-gap] (.setHgap obj h-gap))
                       :v-gap           (fn [^GridPane obj v-gap] (.setVgap obj v-gap))
                       :padding         (fn [^Region obj padding] (.setPadding obj (Insets. padding)))
                       :grid-alignment  (fn [^GridPane obj alignment] (.setAlignment obj (alignment positions)))
                       :text-value      (fn [^Text obj text] (.setText obj text))
                       :h-box-alignment (fn [^HBox hbox alignment] (.setAlignment hbox (alignment positions)))
                       :fill            (fn [^Shape obj fill] (.setFill obj (Color/valueOf (s/upper-case (name fill)))))
                       })

(def ^:private structs [{:id :stack-pane
                         :build [#(StackPane.) #(-> %2) #(.add (.getChildren %1) %2)]
                         :update [:height :width]
                         :class StackPane}
                        {:id :button
                         :build [#(Button.)]
                         :update [:text :on-action]
                         :class Button}
                        {:id     :grid-pane
                         :build  [#(GridPane.) #(mapv vector %2 (:grid %1)) (fn [^GridPane child-list [^Node child [^Long col-index ^Long row-index ^Long col-span ^Long row-span]]]
                                                                              (if (nil? col-span)
                                                                                (.add child-list child col-index row-index)
                                                                                (.add child-list child col-index row-index col-span row-span)))]
                         :update [:grid-alignment :h-gap :v-gap :padding :width :height]
                         :class  GridPane}
                        {:id    :text
                         :class Text
                         :build [#(Text.)]
                         :update [:text-value :fill]}
                        {:id    :label
                         :class Label
                         :build [#(Label.)]
                         :update [:text]}
                        {:id    :text-field
                         :class TextField
                         :build [#(TextField.)]
                         :update []}
                        {:id    :password-field
                         :class PasswordField
                         :build [#(PasswordField.)]
                         :update []}
                        {:id    :h-box
                         :class HBox
                         :build [#(HBox.) #(-> %2) #(.add (.getChildren %1) %2)]
                         :update [:h-box-alignment]}
                        {:id    :group
                         :class Group
                         :build [#(Group.) ]}])

(defn- check-and-apply [tag]
  (fn [obj opts]
    (when (contains? opts tag)
      ((tag fields) obj (tag opts)))))

(defn- add-to-context [context {:keys [id]} obj]
  (when-not (nil? id)
    (.setId obj id)
    (reset! context (assoc @context id obj)))
  obj)

(defn- build-updater [& tags]
  (let [updaters (mapv check-and-apply tags)]
    (fn [opts obj]
      (reduce #(%2 obj opts) nil updaters)
      obj)))

(defn- apply-children [^Pane pane children add-func]
  (loop [children children]
    (when-not (empty? children)
      (let [child (first children)]
        (add-func pane child))
      (recur (rest children))))
  pane)

(defn build-builder
  ([new-fn update-fn]
   (fn [context options]
     (->> (new-fn)
          (add-to-context context options)
          (update-fn options))))
  ([new-fn update-fn children-fn child-fn]
   (let [init-builder (build-builder new-fn update-fn)]
     (fn [context options & children]
       (-> (init-builder context options)
           (apply-children (children-fn options children) child-fn))))))

(defn- build-types [types]
  (mapv (fn [{:keys [id build update class]}]
          (let [updater (apply build-updater update)
                [new-fn children-fn child-fn] build
                builder (apply build-builder (filter some? (vector new-fn updater children-fn child-fn)))]
            {:id id :build builder :update updater :class class})) types))

(def ^:private types (build-types structs))

(defn- fetch-by [field value] (first (filter #(= value (% field)) types)))

(defn- fetch-by-tag [tag] (fetch-by :id tag))

(defn- fetch-by-class [^Class class] (fetch-by :class class))

(defn- recurse-view [context [tag attrs & children]]
  (let [attrs (if (nil? attrs) {} attrs)
        [children attrs] (if (map? attrs) [children attrs] [(into [attrs] children) {}])
        children (if (or (nil? children) (empty? children))
                   children
                   (mapv #(if (or (vector? %) (symbol? %)) (recurse-view context %) %) children))
        func (:build (fetch-by-tag tag))
        view (apply func (into [context attrs] children))]
    view))

(defn- build-view [blueprint]
  (let [context (atom {})
        view (recurse-view context blueprint)]
    {:view view :context @context}))

(defn update-elem [elem options]
  (let [updater (:update (fetch-by-class (type elem)))]
    (updater options elem)))

(defn- apply-controller [context ctrl]
  (let [apply-func (fn [prev [k v]]
                     (assoc prev k
                                 (try
                                   (let [[id attr] (mapv keyword [(namespace k) (name k)])
                                         elem (context id)]
                                     (update-elem elem (assoc {} attr v)))
                                   "complete"
                                   (catch Throwable e
                                     (println (.getMessage e))
                                     (.getMessage e)))))]
    (reduce apply-func {} ctrl)))

(defn- apply-to-timeline [animation-seq timeline]
  (mapv (fn [frames]
          (reduce (fn [duration values]
                    (.add (.getChildren timeline)
                          (KeyFrame. (if (keyword? duration)
                                       (Duration/valueOf (s/upper-case (name duration)))
                                       (Duration. duration))
                                     (into-array KeyValue
                                                 (mapv (fn [prop val]
                                                         (KeyValue. prop value))
                                                       values)))))
                  nil frames))
        animation-seq))

(defn launch-app
  ([^String title root] (launch-app title root nil nil))
  ([^String title controller root] (launch-app title root controller nil))
  ([^String title root controller animate-fn] (println "initializing environment")
                           (Platform/runLater #(let [{:keys [view context]} (build-view root)
                                                     scene (Scene. view)
                                                     stage (Stage.)
                                                     timeline (Timeline.)]
                                                 (when-not (nil? controller)
                                                   (apply-controller context (controller context)))
                                                 (.setScene stage scene)
                                                 (.setTitle stage title)
                                                 (when-not (nil? animate-fn)
                                                   (apply-to-timeline (animate-fn context) timeline)
                                                   (.play timeline))
                                                 (.show stage)
                                                 stage))))
