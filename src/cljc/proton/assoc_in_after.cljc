(ns proton.assoc-in-after
  "Functions of `assoc-in` in a specific position")

(defn- contains-in? [m ks]
  (contains? (get-in m (butlast ks)) (last ks)))

(defn- assoc-in* [m ks v]
  (if (empty? ks)
    v
    (assoc-in m ks v)))

(defn assoc-in-after
  "Inserts `v` into `ks` after `before-ks` in `omap`.
`omap` is a nested associative structure, especially a map that can keep the order: `array-map`, [ordered-map](https://github.com/clj-commons/ordered), etc.
`array-map` cannot keep the order when there are many elements, so in that case, please consider using `ordered-map` etc.
`v` is new value, `ks` and `before-ks` are sequences of keys."
  [omap ks v before-ks]
  (let [empty-omap (with-meta (empty omap) nil)]
    (cond
      (empty? ks) v
      (empty? before-ks) (assoc-in* omap ks v)
      (contains-in? omap ks) (assoc-in* omap ks v)
      (contains? omap (first ks)) (update omap
                                          (first ks)
                                          assoc-in-after
                                          (rest ks)
                                          v
                                          (rest before-ks))
      (= ::first (first before-ks)) (into empty-omap
                                          (cons [(first ks)
                                                 (assoc-in* empty-omap
                                                            (rest ks)
                                                            v)]
                                                (seq omap)))
      (not (contains? omap (first before-ks))) (assoc-in* omap ks v)
      :else (into empty-omap
                  (mapcat (fn [[k child]]
                            (if (= (first before-ks) k)
                              [[k child]
                               [(first ks)
                                (assoc-in* empty-omap (rest ks) v)]]
                              [[k child]]))
                          (seq omap))))))
