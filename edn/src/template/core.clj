(ns template.core)

(defn- resolve-tpl [op-map data out-data tpl]
  (cond
    (map? tpl) (reduce (fn [out [k v]] (assoc out k (resolve-tpl op-map data [] v))) {} tpl)
    (vector? tpl) (let [[tag & attrs-content] tpl
                        attrs-content (reduce #(resolve-tpl op-map data %1 %2) out-data attrs-content)
                        attrs (apply merge (filter map? attrs-content))
                        attrs (when-not (nil? attrs) [attrs])
                        content (filter #(not (map? %)) attrs-content)]
                    (reduce #(if (nil? %2) %1 (into %1 %2)) [tag] [attrs content]))
    (list? tpl) (let [[op & args] tpl]
                  (cond
                    (map? op) (let [[col label] (first op)
                                    col (tpl col)]
                                (vec
                                  (reduce (fn [out elem]
                                            (let [new-data (if (map? elem)
                                                             (reduce (fn [obj [k v]] (assoc obj (keyword (name label) (name k)) v)) data elem)
                                                             (assoc data label elem))]
                                              (concat out (mapv (partial resolve-tpl op-map new-data) args))))
                                          out-data col)))
                    (list? op) (reduce #(into %1 [%2]) out-data (if (resolve-tpl op-map data [] op) (mapv (partial resolve-tpl op-map data) args) []))
                    (symbol? op) (let [func (op-map op)
                                       params (map (partial resolve-tpl op-map data) args)]
                                   (apply func params))))
    (keyword? tpl) (tpl data)
    :else tpl))

(defn build-template-factory [op-map]
  (fn build-template [tpl]
    (fn resolve-template [data]
      (resolve-tpl op-map data [] tpl))))

(let [resolve-tpl (fn resolve-tpl [op-map data out-data tpl]
                    (cond
                      (map? tpl) (reduce (fn [out [k v]] (assoc out k (resolve-tpl op-map data [] v))) {} tpl)
                      (vector? tpl) (let [[tag & attrs-content] tpl
                                          attrs-content (reduce #(resolve-tpl op-map data %1 %2) out-data attrs-content)
                                          attrs (apply merge (filter map? attrs-content))
                                          attrs (when-not (nil? attrs) [attrs])
                                          content (filter #(not (map? %)) attrs-content)]
                                      (reduce #(if (nil? %2) %1 (into %1 %2)) [tag] [attrs content]))
                      (list? tpl) (let [[op & args] tpl]
                                    (cond
                                      (map? op) (let [[col label] (first op)
                                                      col (tpl col)]
                                                  (vec
                                                    (reduce (fn [out elem]
                                                              (let [new-data (if (map? elem)
                                                                               (reduce (fn [obj [k v]] (assoc obj (keyword (name label) (name k)) v)) data elem)
                                                                               (assoc data label elem))]
                                                                (concat out (mapv (partial resolve-tpl op-map new-data) args))))
                                                            out-data col)))
                                      (list? op) (reduce #(into %1 [%2]) out-data (if (resolve-tpl op-map data [] op) (mapv (partial resolve-tpl op-map data) args) []))
                                      (symbol? op) (let [func (op-map op)
                                                         params (map (partial resolve-tpl op-map data) args)]
                                                     (apply func params))))
                      (keyword? tpl) (tpl data)
                      :else tpl))]
  (resolve-tpl
    {'not-nil? #(not (nil? %))}
    {:title "This is the title"
     :description "This is the description"
     :author "I am the author"
     :sections [{:title "Section One"}
                {:title "Section Two"
                 :description "This is the description for section two"}]}
    []
    '[html {lang "en"}
      [head
       [meta {charset "utf-8"}]
       [title :title]
       [meta {name "viewport" content "width=device-width, initial-scale=1.0"}]
       [meta {name "description" content :description}]
       [meta {name "author" content :author}]]
      [body
       ({:sections :element}
         [div {class "section"}
          [h1 :element/title]
          ((not-nil? :element/description)
            [p :element/description])])]]))
