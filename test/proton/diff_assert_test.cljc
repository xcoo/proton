(ns proton.diff-assert-test
  (:require #?(:clj [clojure.test :refer [is deftest testing]]
               :cljs [cljs.test :refer-macros [is deftest testing]])
            [proton.diff-assert]))

(deftest diff-assert-test
  (testing "diff-u= assert"
    (is (diff-u= {:a 1} {:a 1}))))
