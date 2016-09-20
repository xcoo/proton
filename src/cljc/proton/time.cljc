(ns proton.time
  #?(:clj (:import [java.time.format DateTimeFormatter]
                   [java.time ZoneId])
     :cljs (:require [goog.i18n.DateTimeFormat])))

#?(:cljs (defrecord Formatter [instance zone]))

(defn datetime-formatter
  ([format-string]
   (datetime-formatter format-string "UTC"))
  ([format-string zone]
   #?(:clj (-> (DateTimeFormatter/ofPattern format-string)
               (.withZone (ZoneId/of zone)))
      :cljs (Formatter. (goog.i18n.DateTimeFormat. format-string) zone))))

(defn datetime
  [epoch-sec]
  ;; TODO support other type arguments
  #?(:clj (java.time.Instant/ofEpochSecond epoch-sec)
     :cljs (js/Date. (* epoch-sec 1000))))

(defn format-datetime-string
  [fmt dt]
  #?(:clj (.format fmt dt)
     :cljs (.format (:instance fmt) dt) ;; TODO support time zone
     ))
