(ns proton.string-test
  (:require #?(:clj [clojure.test :refer [are deftest is testing]]
               :cljs [cljs.test :refer-macros [are deftest is testing]])
            [proton.string :as string]))

(deftest prune-test
  (testing "prune"
    (are [args e] (= (apply string/prune args) e)
      ["Simple made easy"] "Simple ..."
      ["Simple made easy" 15] "Simple made ..."
      ["Simple made easy" 3] "..."
      ["Simple made easy" 10 :left] "Simple ..."
      ["Simple made easy" 10 :right] "...de easy"
      ["Simple made easy" 10 :both] "Simp...asy"
      ["Simple made easy" 9 :left "!!!"] "Simple!!!"))
  (testing "not prune"
    (are [args] (= (apply string/prune args) (first args))
      ["Simple"]
      [""]
      [nil]
      ["Simple made easy" 20]))
  (testing "error"
    (are [args] (thrown? #?(:clj Throwable, :cljs js/Error) (apply string/prune args))
      ["Simple made easy" 2]
      ["Simple made easy" 10 :front]
      ["Simple made easy" 3 :left "......"])))

(deftest split-at-test
  (testing "single splitting position"
    (are [s x e] (= (string/split-at s x) e)
      "clojure" 3  ["clo" "jure"]
      "clojure" 0  ["" "clojure"]
      "clojure" 7  ["clojure" ""]
      "clojure" -1 ["" "clojure"]
      "clojure" 8  ["clojure" ""]
      ""        3  ["" ""]))
  (testing "multiple splitting positions"
    (are [s x e] (= (string/split-at s x) e)
      "clojure" [3 5]  ["clo" "ju" "re"]
      "clojure" [0 3]  ["" "clo" "jure"]
      "clojure" [5 7]  ["cloju" "re" ""]
      "clojure" [-1 3] ["" "clo" "jure"]
      "clojure" [5 8]  ["cloju" "re" ""]
      "clojure" []     ["clojure"]
      ""        [3 5]  ["" "" ""]))
  (testing "error"
    (are [s x] (thrown? #?(:clj Throwable, :cljs js/Error) (string/split-at s x))
      "clojure" "3"
      "clojure" nil
      nil       3)))

(deftest rand-str-test
  (let [s (string/rand-str 8)]
    (is (string? s))
    (is (= (count s) 8))
    (is (re-matches #"[0-9A-Za-z]+" s)))
  (is (re-matches #"[0-9]+" (string/rand-str 8 :number)))
  (is (re-matches #"[0-9a-z]+" (string/rand-str 8 :number :lower-case-letter)))
  (is (not= (string/rand-str 40) (string/rand-str 40)))
  (is (= (string/rand-str 0) ""))
  (is (= (string/rand-str -1) "")))
