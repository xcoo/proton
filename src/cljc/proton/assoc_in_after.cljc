(ns proton.assoc-in-after
  "Functions of `assoc-in` in a specific position")

(defn- contains-in? [m ks]
  (contains? (get-in m (butlast ks)) (last ks)))

(defn- assoc-in* [m ks v]
  (if (empty? ks)
    v
    (assoc-in m ks v)))

(defn assoc-in-after
  "Inserts `v` into `ks` after `before-ks` in `omap`"
  [omap ks v before-ks]
  (let [empty-omap (empty omap)]
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
