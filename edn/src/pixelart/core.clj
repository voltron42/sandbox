(ns pixelart.core)

(defn coordinate [id]
  [(- (int (first (namespace id))) (int \A))
   (- (int (first (name id))) (int \a))])

(defn build-image [span blueprint]
  (let [{:keys [xs ys]} (reduce (fn [{:keys [xs ys]} id]
                                  (let [[x y] (coordinate id)]
                                    {:xs (conj xs x)
                                     :ys (conj ys y)}))
                                       {:xs #{} :ys #{}}
                                       (flatten (vals blueprint)))
        [width height] (mapv (partial * span)
                             (map (partial apply max)
                                  [xs ys]))]
    (into [:svg {:width width :height height :viewBox "0 0 100% 100%"}]
          (vals (into (sorted-map)
                      (reduce (fn [out [color ids]]
                                (reduce (fn [prev id]
                                          (assoc
                                            prev
                                            id
                                            (let [[x y] (coordinate id)]
                                              [:rect {:id (str id)
                                                      :x (* span x)
                                                      :y (* span y)
                                                      :width span
                                                      :height span
                                                      :fill (str color)
                                                      :stroke :none}])))
                                        out
                                        ids))
                              {}
                              blueprint))))))