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
