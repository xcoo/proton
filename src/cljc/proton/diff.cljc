(ns proton.diff
  (:require
   [clojure.data :refer [diff]]
   [clojure.pprint :refer [pprint]]
   [clojure.string :as string]))

(defn- blank-padding [origin size]
  (into origin (take size (repeat ""))))

(defn- align-length [a-diff b-diff]
  (let [ac (count a-diff)
        bc (count b-diff)
        compare-result (compare ac bc)]
    (cond
      (pos? compare-result) [a-diff (blank-padding b-diff (- ac bc))]
      (neg? compare-result) [(blank-padding a-diff (- bc ac)) b-diff]
      :else [a-diff b-diff])))

(defn- diffs->str [diffs]
  (apply map (fn [a b]
               (if (= a b)
                 a
                 (str "- " a "\n"
                      "+ " b))) diffs))

(defn diff-u
  "Recursively compares a and b, returning a diff string in unified format (like `diff -u` command).
   Comparison rules are same as clojure.data/diff"
  [a b]
  (if (every? (some-fn nil? map?) [a b])
    (some->> (diff a b)
             (take 2)
             (map #(and % (-> (pprint %)
                              with-out-str
                              (string/split #"\n"))))
             (apply align-length)
             (diffs->str)
             (seq)
             (string/join "\n"))
    (throw #?(:clj (IllegalArgumentException.
                    "a and b should be nil or map.")
              :cljs (js/Error. "a and b should be nil or map.")))))
