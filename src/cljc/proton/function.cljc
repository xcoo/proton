(ns proton.function)

(defn lcomp
  "Apply comp from left to right"
  [& fs]
  (apply comp (reverse fs)))

;; TODO can pass "options" (I had better use "reduce"?)
(defn lcomp-apply
  "Apply the composition of fs from left to right"
  ([init-val f & fs]
   (lcomp-apply init-val (cons f fs)))
  ([init-val fs]
   (if (fn? fs)
     (fs init-val)
     (if (seq fs)
       ((apply lcomp fs) init-val)
       init-val))))
