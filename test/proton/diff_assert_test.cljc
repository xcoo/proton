(ns proton.diff-assert-test
  #?(:clj (:require [clojure.test :as t]
                    [proton.diff-assert])))

#?(:clj
   (t/deftest diff-assert-test
     (t/testing "diff-u= assert"
       (t/is (diff-u= {:a 1} {:a 1})))))
