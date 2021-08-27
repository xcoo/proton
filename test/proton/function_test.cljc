(ns proton.function-test
  (:require #?(:clj [clojure.test :refer [deftest is]]
               :cljs [cljs.test :refer-macros [deftest is]])
            [proton.function :as function]))

(deftest comp->-test
  (is (= ((comp (partial * 2) inc) 8) 18))
  (is (= ((function/comp-> inc (partial * 2)) 8) 18)))

(deftest step->-test
  (is (= (function/step-> 8 inc) 9))
  (is (= (function/step-> 8 inc (partial * 2)) 18))
  (is (= (function/step-> 8 [inc (partial * 2)]) 18)))
