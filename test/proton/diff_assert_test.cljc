(ns proton.diff-assert-test
  #?(:clj (:require [clojure.test :refer [is deftest testing]]
                    [proton.diff-assert])
     :cljs (:require-macros [cljs.test :refer [is deftest testing]]
                            [proton.diff-assert])))

(deftest diff-assert-test
  (testing "diff-u= assert"
    (is (diff-u= {:a 1} {:a 1}))))
