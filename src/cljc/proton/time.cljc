(ns proton.time
  "(WIP) Dates and times functions"
  #?(:clj (:import [java.time.format DateTimeFormatter]
                   [java.time ZoneId ZoneOffset Instant LocalDateTime])
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
  ([epoch-sec]
   ;; TODO support other type arguments
   #?(:clj (Instant/ofEpochSecond epoch-sec)
      :cljs (js/Date. (* epoch-sec 1000))))
  ([year month day]
   #?(:clj (.toInstant (LocalDateTime/of year month day 0 0 0)
                       ZoneOffset/UTC)
      :cljs nil))
  ([year month day hour minute second]
   #?(:clj (.toInstant (LocalDateTime/of year month day hour minute second)
                       ZoneOffset/UTC)
      :cljs nil)))

(defn timestamp-sec
  [datetime]
  #?(:clj (quot (.toEpochMilli datetime) 1000)
     :cljs nil))

(defn format-datetime-string
  [fmt dt]
  #?(:clj (.format fmt dt)
     :cljs (.format (:instance fmt) dt) ;; TODO support time zone
     ))
