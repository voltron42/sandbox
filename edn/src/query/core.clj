(ns query.core
  (:require [clojure.string :as s]
            [clojure.java.jdbc :as jdbc]))

(def ^:private clause-list '[Select From Where Order-By])

(def ^:private joins {'Inner-Join "INNER JOIN"
                      'Left-Outer-Join "LEFT OUTER JOIN"
                      'Right-Outer-Join "RIGHT OUTER JOIN"})

(defn unary [term] #(str (s/upper-case term) "(" % ")"))

(defn binary [term] #(str %1 " " (s/upper-case term) " " %2))

(defn n-ary [term] #(str "(" (s/join (str " " (s/upper-case term) " ") (map str %&)) ")"))

(defn map-ops [fnc ops] (reduce #(assoc %1 %2 (fnc %2)) {} ops))

(def ops (merge (map-ops unary '[count])
                (map-ops binary '[= <= >])
                (map-ops n-ary '[and or])
                {'is-null #(str % " IS NULL")}))

(defn- resolve-field [field-symbol] (let [nm (name field-symbol)
                                          ns (namespace field-symbol)]
                                      (str (if (nil? ns) "" (str ns ".")) nm)))

(defn- resolve-table [table] (let [nm (name table)
                                   ns (namespace table)]
                               (str nm (if (nil? ns) "" (str " " ns)))))

(defn- resolve-expression [expr all-params]
  (cond
    (list? expr) (let [op (first expr)
                       op-func (ops op)
                       {:keys [args param-list]} (reduce
                                                   (fn [{:keys [args param-list]} param]
                                                     (let [{:keys [query params]} (resolve-expression param param-list)]
                                                       {:args (concat args (vector query))
                                                              :param-list params}))
                                                   {:args [] :param-list all-params}
                                                   (rest expr))]
                   {:query (apply op-func args)
                           :params param-list})
    (symbol? expr) {:query (resolve-field expr) :params all-params}
    (keyword? expr) {:query "?" :params (concat all-params (vector expr))}
    (string? expr) {:query (str "'" expr "'") :params all-params}
    :else {:query (str expr) :params all-params}))

(defn- resolve-column [params column]
  (cond
    (symbol? column)   {:query (resolve-field column)
                               :params params}
    (map? column) (let [[[k v]] (into [] column)
                        {:keys [query params]} (resolve-column params v)]
                    {:query (str query " as " k)
                            :params params})
    (list? column) (resolve-expression column params)))

(defn- resolve-sorter [column params]
  {:query (cond
            (symbol? column) (resolve-field column)
            (map? column) (let [[[k v]] (into [] column)] (str (resolve-field k) " " (s/upper-case (name v)))))
          :params params})

(defn- resolve-join [[type table on] params]
  (let [{:keys [query params]} (resolve-expression on params)]
    {:query (str (joins type) " " (resolve-table table) " ON " query) :params params}))

(defn- resolve-where [clause params] (resolve-expression clause params))

(defn- resolve-list [step-fn list-fn wrap-fn delim]
  (fn [q p]
    (let [{:keys [query params]} (reduce
                                   (fn [{:keys [query params]} elem]
                                     (let [prev-q query
                                           {:keys [query params]} (step-fn params elem)]
                                       {:query (concat prev-q (vector query))
                                        :params params}))
                                   {:query [] :params p}
                                   (list-fn q p))]
      {:query (wrap-fn (s/join delim query)) :params params})))

(def ^:private clause-funcs {'Select (resolve-list
                                         #(resolve-column %1 %2)
                                         (fn [qu _] (if (vector? qu) qu (vector qu)))
                                         #(str "SELECT " %)
                                         ", ")
                             'From (fn [q p]
                                     (let [tables (if (vector? q) q (vector q))]
                                       ((resolve-list
                                          #(resolve-join %2 %1)
                                          (constantly (partition 3 (rest tables)))
                                          #(str "FROM " (resolve-table (first tables)) " " %)
                                          " ")
                                         q p)))
                             'Order-By (resolve-list
                                           #(resolve-sorter %2 %1)
                                           (fn [qu _] (if (vector? qu) qu (vector qu)))
                                           #(str "ORDER BY " %)
                                           ", ")
                             'Where #(let [{:keys [query params]} (resolve-where %1 %2)] {:query (str "WHERE " query) :params params})})

(fn [q p]
  (resolve-list
    #((clause-funcs %2) (q %2) %1)
    (constantly (filter #(contains? (set (keys q)) %) clause-list))
    identity
    " "))

(defn- resolve-query [query paramlist] ((resolve-list
                                          #((clause-funcs %2) (query %2) %1)
                                          (constantly (filter #(contains? (set (keys query)) %) clause-list))
                                          identity
                                          " ")
                                         query paramlist))

(defn- resolve-limit [{:keys [query params]} limit]
  (let [[l p] (cond (keyword? limit) ["?" [limit]]
                    (number? limit) [(str limit) []])]
    {:query (str "SELECT * FROM (" query ") WHERE rownum <= " l) :params (concat params p)}))

(defn- clean-space [statement]
  (->> (s/split statement #" ")
       (filter #(not (s/blank? %)))
       (s/join " ")))

(defn build-query [db q]
  (let [result (resolve-query q [])
        {:keys [query params]} (if (contains? q 'Limit) (resolve-limit result (q 'Limit)) result)]
    (fn [argmap]
      (apply jdbc/query (into [db (clean-space query)] (map #(argmap %) params))))))