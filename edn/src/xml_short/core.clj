(ns xml-short.core)

(defn x-pand [body]
  (let [bodynext (if (keyword? body) [body] body)
        [tag attrs & content] bodynext
        attrs (if (nil? attrs) {} attrs)
        [content attrs] (if (map? attrs) [content attrs] [(into [attrs] content) {}])
        content (if (or (nil? content) (empty? content))
                  content
                  (mapv #(if (string? %) % (x-pand %)) content))]
    (reduce (fn [out [k v]] (if (or (empty? v) (nil? v)) out (assoc out k v)))
            {:tag tag}
            {:attrs attrs :content content})))
