(ns proton.function)

(defn lcomp
  "Apply comp from left to right"
  [& fs]
  (apply comp (reverse fs)))

(defn- apply-fs [init-val fs run-fn]
  (if (seq fs)
    (reduce run-fn init-val fs)
    init-val))

(defn lcomp-apply
  "Apply the composition of fs from left to right"
  ([init-val fs]
   (apply-fs init-val fs #(%2 %1)))
  ([init-val fs opts]
   (apply-fs init-val fs #(%2 %1 opts))))
