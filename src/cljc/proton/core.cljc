(ns proton.core)

#?(:clj (defn str->long [s]
          (if-not (nil? s)
            (try
              (let [[n _ _] (re-matches #"(|-|\+)(\d+)" s)]
                (Long. n))
              (catch Exception e
                nil))
            nil)))

(defn str->int [s]
  (if-not (nil? s)
    (try
      (let [[n _ _] (re-matches #"(|-|\+)(\d+)" s)]
        #?(:clj (Integer. n)
           :cljs (let [r (js/parseInt n)]
                   (if (js/isNaN r) nil r))))
      #?(:clj (catch NumberFormatException e
                (str->long s)))
      (catch #?(:clj Exception
                :cljs js/Object) e
             nil))
    nil))

(defn transform-key
  [v k new-k]
  (if (contains? v k)
    (-> v
        (assoc new-k (k v))
        (dissoc k))
    v))

#?(:clj (defmacro swap
          [m k f]
          `(if-not (nil? (get ~m ~k))
             (assoc ~m ~k (~f (get ~m ~k)))
             ~m)))

(def ^:private ascii-codes (concat (range 48 58) (range 66 91) (range 97 123)))

(defn random-string [length]
  (apply str (repeatedly length #(char (rand-nth ascii-codes)))))

(defn stack-trace-string
  [e]
  #?(:clj (map #(str % "\n")
               (.getStackTrace e))
     :cljs "" ;; TODO
     ))
