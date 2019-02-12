(ns proton.string
  "String utilities."
  (:refer-clojure :exclude [split-at])
  (:require [proton.core :as core]))

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

(defn split-at
  "Returns a vector of substrings split at the position. You can supply an
  integer or list as the splitting position."
  [s x]
  (let [len (count s)
        subs* (fn
                ([s start] (subs s (core/clip start 0 len)))
                ([s start end] (subs s (core/clip start 0 len) (core/clip end 0 len))))]
    (cond
      (integer? x) [(subs* s 0 x) (subs* s x)]
      (sequential? x) (loop [[n & r] (cons 0 x), ret []]
                        (if (seq r)
                          (recur r (conj ret (subs* s n (first r))))
                          (conj ret (subs* s n))))
      :else (throw #?(:clj (IllegalArgumentException.
                            "Splitting position should be an integer or list.")
                      :cljs (js/Error. "Splitting position should be an integer or list."))))))

(def ^:private ascii-code-map
  {:number (range 48 58)
   :letter (concat (range 65 91) (range 97 123))
   :upper-case-letter (range 65 91)
   :lower-case-letter (range 97 123)})

(defn rand-str
  "Generates a random string of the certain length. The generated string
  consists of numbers and letters (i.e. 0-9, A-Z, and a-z). You can change the
  characters by supplying character types from :number, :letter,
  :upper-case-letter, or :lower-case-letter."
  [len & char-types]
  (let [char-types (or char-types [:number :letter])
        ascii-codes (distinct (mapcat ascii-code-map char-types))]
    (apply str (repeatedly len #(char (rand-nth ascii-codes))))))
