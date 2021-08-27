(ns proton.function)

(defn comp->
  "Apply comp from left to right"
  [& fs]
  (apply comp (reverse fs)))

(defn step->
  "Apply the composition of fs from left to right"
  ([init-val f & fs]
   (step-> init-val (cons f fs)))
  ([init-val fs]
   (if (fn? fs)
     (fs init-val)
     (if (seq fs)
       ((apply comp-> fs) init-val)
       init-val))))
