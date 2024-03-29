(ns proton.function-test
  (:require #?(:clj [clojure.test :refer [deftest is]]
               :cljs [cljs.test :refer-macros [deftest is]])
            [proton.function :as function]))

(deftest lcomp-test
  (is (= ((comp (partial * 2) inc) 8) 18))
  (is (= ((function/lcomp inc (partial * 2)) 8) 18)))

(deftest comp-apply-test
  (is (= (function/comp-apply 8 [inc]) 9))
  (is (= (function/comp-apply 8 [inc (partial * 2)]) 17))
  (is (= (function/comp-apply 8 [- (partial + 3) (partial * 2)]) -19))
  (is (= (function/comp-apply 8 [(fn [n opts] (if (:twice opts) (* n 2) n))] {:twice true}) 16))
  (is (= (function/comp-apply 8 [(fn [n opts] (if (:twice opts) (* n 2) n))] {}) 8)))

(deftest lcomp-apply-test
  (is (= (function/lcomp-apply 8 [inc]) 9))
  (is (= (function/lcomp-apply 8 [inc (partial * 2)]) 18))
  (is (= (function/lcomp-apply 8 [- (partial + 3) (partial * 2)]) -10))
  (is (= (function/lcomp-apply 8 [(fn [n opts] (if (:twice opts) (* n 2) n))] {:twice true}) 16))
  (is (= (function/lcomp-apply 8 [(fn [n opts] (if (:twice opts) (* n 2) n))] {}) 8)))
