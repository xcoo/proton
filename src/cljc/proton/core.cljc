(ns proton.core
  "More commonly used utilities"
  (:require [clojure.string :as string]))

;;; convert

(defn- sanitize-number-string
  [s]
  (-> s
      (string/replace #"," "")))

(defn as-long
  "Returns a new long number initialized to the value represented by s.
  as-int returns nil if s is an illegal string or nil."
  [s]
  {:pre [(or (nil? s) (string? s))]}
  (when-not (nil? s)
    (let [[n _ _] (re-matches #"(|-|\+)(\d+)" (sanitize-number-string s))]
      (when-not (nil? n)
        (try
          #?(:clj (Long/parseLong n)
             :cljs (let [num (js/parseInt n)]
                     (if (js/isNaN num) nil num)))
          (catch #?(:clj Exception
                    :cljs js/Object) e nil))))))

(defn as-int
  "Returns a new integer number initialized to the value represented by s.
  as-int returns nil if s is an illegal string or nil. as-long returns a
  long number if s is out of int range."
  [s]
  {:pre [(or (nil? s) (string? s))]}
  (when-not (nil? s)
    (let [[n _ _] (re-matches #"(|-|\+)(\d+)" (sanitize-number-string s))]
      (when-not (nil? n)
        (try
          #?(:clj (Integer/parseInt n)
             :cljs (let [r (js/parseInt n)]
                     (if (js/isNaN r) nil r)))
          #?(:clj (catch NumberFormatException e
                    (as-long n)))
          (catch #?(:clj Exception
                    :cljs js/Object) e nil))))))

(defn as-double
  "Returns a new double number initialized to the value represented by s.
  as-double returns nil if s is an illegal string or nil."
  [s]
  {:pre [(or (nil? s) (string? s))]}
  (when-not (nil? s)
    (when-let [[n] (re-matches #"[\-\+]?\d+(\.\d+)?([eE][\-\+]?\d+)?" (sanitize-number-string s))]
      (try
        #?(:clj (Double/parseDouble n)
           :cljs (let [r (js/parseFloat n)]
                   (when-not (js/isNaN r) r)))
        (catch #?(:clj Exception
                  :cljs js/Error) _)))))

(defn as-float
  "Returns a new float number initialized to the value represented by s.
  as-float returns nil if s is an illegal string or nil. as-float returns a
  double number if s is out of float range."
  [s]
  {:pre [(or (nil? s) (string? s))]}
  (when-not (nil? s)
    (when-let [[n] (re-matches #"[\-\+]?\d+(\.\d+)?([eE][\-\+]?\d+)?" (sanitize-number-string s))]
      (try
        #?(:clj (let [r (Float/parseFloat n)]
                  (if (Float/isInfinite r)
                    (Double/parseDouble n)
                    r))
           :cljs (let [r (js/parseFloat n)]
                   (if (js/isNaN r) nil r)))
        (catch #?(:clj Exception
                  :cljs js/Error) _)))))

(defn as-rational
  "Returns a new rational number initialized to the value represented by s such
  as \"1\", \"1/2\", and \"-1/2\". as-rational returns nil if s is an illegal
  string, nil, or division by zero."
  [s]
  {:pre [(or (nil? s) (string? s))]}
  (when-not (nil? s)
    (when-let [[_ n _ d] (re-matches #"([\-\+]?\d+)(/(\d+))?" (sanitize-number-string s))]
      (try
        (let [numerator ^long (as-long n)
              denominator ^long (as-long d)]
          (if denominator
            (when (pos? denominator)
              (/ numerator denominator))
            numerator))
        (catch #?(:clj Exception
                  :cljs js/Error) _)))))

(defn as-boolean
  "Returns a boolean represented by s. as-boolean returns true if s is \"true\"
  or \"yes\", ignoring case, false if \"false\" or \"no\", and nil otherwise."
  [s]
  {:pre [(or (nil? s) (string? s))]}
  (when-not (nil? s)
    (condp re-matches s
      #"(?i)(true|yes)" true
      #"(?i)(false|no)" false
      nil)))

;;; type check

(defn is-uuid?
  "Type check uuid object."
  [o]
  #?(:clj (instance? java.util.UUID o)
     :cljs (uuid? o)))

;;; random string

(def ^:private alphabet-ascii-codes (concat (range 48 58) (range 65 91) (range 97 123)))

(defn ^:deprecated random-string
  "DEPRECATED: Use 'proton.string/rand-str' instead.
  Generate random string from alphabets and numbers (i.e. 0-9, A-Z, and a-z)"
  [length]
  (apply str (repeatedly length #(char (rand-nth alphabet-ascii-codes)))))

;;; error handling

(defn stack-trace-string
  "Returns a flattened string of stacktraces from an exception"
  [e]
  #?(:clj (map #(str % "\n")
               (.getStackTrace ^Throwable e))
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
  "Convert byte array to hex string."
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
  "Convert hex string to bytes array."
  ^"[B"
  [^String data]
  (let [data (.toLowerCase data)
        len #?(:clj (.length data)
               :cljs (.-length data))
        result #?(:clj (byte-array (quot len 2))
                  :cljs (new js/Int8Array (quot len 2)))]
    (loop [i 0]
      (when (< i len)
        (let [a (.charAt data i)
              b (.charAt data (inc i))
              v (+ (* (hex-val a) 16) (hex-val b))]
          (aset result (quot i 2) (unchecked-byte v))
          (recur (+ i 2)))))
    result))

;;; math

(defn clip
  "Limits x in the interval of [xmin xmax]. A value smaller than xmin becomes
  xmin, and a value larger than xmax becomes xmax. If nil is supplied to
  xmin/xmax, its side will not be limited."
  [x xmin xmax]
  {:pre [(some? x)
         (or (some nil? [xmin xmax]) (<= xmin xmax))]}
  (cond-> x
    xmin (max xmin)
    xmax (min xmax)))

;;; merge

(defn deep-merge
  "Recursively merges maps."
  [& maps]
  (letfn [(m [& xs]
            (if (some #(and (map? %) (not (record? %))) xs)
              (apply merge-with m xs)
              (last xs)))]
    (reduce m maps)))
