(ns proton.uri
  "URI parser function"
  (:require [clojure.string :as string]
            #?(:cljs [goog.Uri]))
  #?(:cljs (:require-macros [cljs.core :refer [exists?]]))
  #?(:clj (:import [java.net URI URLEncoder URLDecoder])))

(defn uri-encode
  [s]
  (some-> s
          str
          #?(:clj (URLEncoder/encode "UTF-8")
             :cljs js/encodeURIComponent)))

(defn uri-decode
  [s]
  (some-> s
          str
          #?(:clj (URLDecoder/decode "UTF-8")
             :cljs js/decodeURIComponent)))

(defn- split-param [param]
  (as-> param $
    (string/split $ #"=")
    (concat $ (repeat ""))
    (take 2 $)))

(defn- parse-query
  [^URI uri]
  (let [qstr (.getQuery uri)
        qstr (if (empty? qstr)
                nil
                qstr)]
    (when qstr
      (some->> (string/split qstr #"&")
               seq
               (map split-param)
               (mapcat (fn [[k v]] [(keyword k) (uri-decode v)]))
               (apply hash-map)))))

(defn- parse-fragment
  [^URI uri]
  (let [fragment (.getFragment uri)]
    (if (empty? fragment)
      nil
      fragment)))

(defn uri
  ([]
   #?(:clj nil
      :cljs (if (exists? js/window)
              (-> js/window
                  .-location
                  .-href
                  uri))))
  ([uri-string]
   ;; TODO validate uri-string with regex
   (let [o #?(:clj (URI. uri-string)
              :cljs (goog.Uri. uri-string))]
     {:scheme (.getScheme o)
      :host #?(:clj (.getHost o)
               :cljs (.getDomain o))
      :port (if (pos? (.getPort o))
              (.getPort o))
      :path (.getPath o)
      :query (parse-query o)
      :fragment (parse-fragment o)
      :user-info (if-not (empty? (.getUserInfo o))
                   (.getUserInfo o))})))
