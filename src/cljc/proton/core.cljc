(ns proton.core)

;;; convert

(defn as-long
  [s]
  (if-not (nil? s)
    (let [[n _ _] (re-matches #"(|-|\+)(\d+)" s)]
      (if-not (nil? n)
        (try
          #?(:clj (Long. n)
             :cljs (let [num (js/parseInt n)]
                     (if (js/isNaN num) nil num)))
          (catch #?(:clj Exception
                    :cljs js/Object) e nil))))))

(defn as-int
  [s]
  (if-not (nil? s)
    (let [[n _ _] (re-matches #"(|-|\+)(\d+)" s)]
      (if-not (nil? n)
        (try
          #?(:clj (Integer. n)
             :cljs (let [r (js/parseInt n)]
                     (if (js/isNaN r) nil r)))
          #?(:clj (catch NumberFormatException e
                    (Long. n)))
          (catch #?(:clj Exception
                    :cljs js/Object) e nil))))))

;;; type check

(defn is-uuid?
  [o]
  #?(:clj (instance? java.util.UUID o)
     :cljs (uuid? o)))

;;; random string

(def ^:private alphabet-ascii-codes (concat (range 48 58) (range 66 91) (range 97 123)))

(defn random-string [length]
  (apply str (repeatedly length #(char (rand-nth alphabet-ascii-codes)))))

;;; error handling

(defn stack-trace-string
  [e]
  #?(:clj (map #(str % "\n")
               (.getStackTrace e))
     :cljs "" ;; TODO
     ))
