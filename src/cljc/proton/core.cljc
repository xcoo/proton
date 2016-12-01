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

(defn as-double
  "Returns a new double number initialized to the value represented by s.
  as-double returns nil if s is an illegal string or nil."
  [s]
  (if-not (nil? s)
    (if-let [[n] (re-matches #"[\-\+]?\d+(\.\d+)?([eE][\-\+]?\d+)?" s)]
      (try
        #?(:clj (Double/parseDouble n)
           :cljs (let [r (js/parseFloat n)]
                   (if-not (js/isNaN r) r)))
        (catch #?(:clj Exception
                  :cljs js/Error) _)))))

(defn as-float
  "Returns a new float number initialized to the value represented by s.
  as-float returns nil if s is an illegal string or nil. as-float returns a
  double number if s is out of float range."
  [s]
  (if-not (nil? s)
    (if-let [[n] (re-matches #"[\-\+]?\d+(\.\d+)?([eE][\-\+]?\d+)?" s)]
      (try
        #?(:clj (let [r (Float/parseFloat n)]
                  (if (Float/isInfinite r)
                    (Double/parseDouble n)
                    r))
           :cljs (let [r (js/parseFloat n)]
                   (if-not (js/isNaN r) r)))
        (catch #?(:clj Exception
                  :cljs js/Error) _)))))

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

;;; bytes

(def ^:private ^"[B" hex-chars
  #?(:clj (byte-array (.getBytes "0123456789abcdef" "UTF-8"))
     :cljs (let [array (new js/Int8Array 16)]
             (doseq [i (range 10)]
               (aset array i (+ 48 i)))
             (doseq [i (range 6)]
               (aset array (+ i 10) (+ 97 i)))
             array)))

(def ^:private hex-val
  (zipmap "0123456789abcdef" (range 16)))

(defn bytes->hex
  "Convert Byte Array to Hex String"
  ^String
  [^"[B" data]
  (let [len (alength data)
        ^"[B" buffer #?(:clj (byte-array (* 2 len))
                        :cljs (new js/Int8Array (* 2 len)))]
    (loop [i 0]
      (when (< i len)
        (let [b (aget data i)]
          (aset buffer (* 2 i) (aget hex-chars (bit-shift-right (bit-and b 0xF0) 4)))
          (aset buffer (inc (* 2 i)) (aget hex-chars (bit-and b 0x0F))))
        (recur (inc i))))
    #?(:clj (String. buffer "UTF-8")
       :cljs (apply (.-fromCharCode js/String) (array-seq buffer)))))

(defn hex->bytes
  "Convert Hex String to Byte Array"
  ^"[B"
  [^String data]
  (let [data (.toLowerCase data)
        len #?(:clj (.length data)
               :cljs (.-length data))
        result #?(:clj (byte-array (quot len 2))
                  :cljs (new js/Int8Array (quot len 2)))]
    (loop [i 0]
      (if (< i len)
        (let [a (.charAt data i)
              b (.charAt data (inc i))
              v (+ (* (hex-val a) 16) (hex-val b))]
          (aset result (quot i 2) (unchecked-byte v))
          (recur (+ i 2)))))
    result))
