(ns template.core)

(defn- resolve-tpl [op-map data tpl]
  (cond
    (map? tpl) (reduce (fn [out [k v]] (assoc out k (resolve-tpl op-map data v))) {} tpl)
    (vector? tpl) (let [[tag & attrs-content] tpl
                        attrs-content (map (partial resolve-tpl op-map data) attrs-content)
                        attrs (apply merge (filter map? attrs-content))
                        attrs (when-not (nil? attrs) [attrs])
                        content (filter #(not (map? %)) attrs-content)]
                    (reduce #(if (nil? %2) %1 (into %1 %2)) [tag] [attrs content]))
    (list? tpl) (let [[op & args] tpl]
                  (cond
                    (map? op) (let [[col label] (first op)
                                    col (resolve-tpl op-map data col)]
                                (vec
                                  (reduce (fn [out elem]
                                            (let [new-data (if (map? elem)
                                                             (reduce (fn [obj [k v]] (assoc obj (keyword (name label) (name k)) v)) data elem)
                                                             (assoc data label elem))]
                                              (concat out (mapv (partial resolve-tpl op-map new-data) args)))) [] col)
                                  )
                                )
                    (list? op) (if (resolve-tpl op-map data op) (mapv (partial resolve-tpl op-map data) args) [])
                    (symbol? op) (let [func (op-map op)
                                       params (map (partial resolve-tpl op-map data) args)]
                                   (apply func params))))
    (keyword? tpl) (tpl data)
    :else tpl))

(defn build-template-factory [op-map]
  (fn build-template [tpl]
    (fn resolve-template [data]
      (resolve-tpl op-map data tpl))))