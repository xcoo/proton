(ns proton.string
  "String utilities.")

(defn prune
  "Returns pruned string that is shortened to the certain length and added dots.
  The leaving side can be selected from :left, :right, or :both."
  ([s] (prune s 10))
  ([s len] (prune s len :left))
  ([s len side] (prune s len side "..."))
  ([s len side dots]
   {:pre [(>= len (count dots))]}
   (let [n (count s)
         m (count dots)]
     (if (> n len)
       (apply str (case side
                    :left [(subs s 0 (- len m)) dots]
                    :right [dots (subs s (+ (- n len) m))]
                    :both (let [l (/ (- len m) 2)
                                sl (Math/ceil l)
                                el (Math/floor l)]
                            [(subs s 0 sl) dots (subs s (- n el))])))
       s))))
