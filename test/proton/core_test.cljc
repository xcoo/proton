(ns proton.core-test
  (:require #?(:clj [clojure.test :refer [deftest is testing]]
               :cljs [cljs.test :refer-macros [deftest is testing]])
            [proton.core :as core]))

(deftest core

  (testing "as-int"
    (is (= (core/as-int "0") 0))
    (is (= (core/as-int "123") 123))
    (is (= (core/as-int "999999999") 999999999))
    (is (= (core/as-int "-45") -45))
    (is (= (core/as-int "+67") 67))
    (is (= (core/as-int "abc") nil))
    (is (= (core/as-int "") nil))
    (is (= (core/as-int nil) nil)))

  (testing "as-long"
    (is (= (core/as-long "0") 0))
    (is (= (core/as-long "123") 123))
    (is (= (core/as-long "999999999") 999999999))
    (is (= (core/as-long "-45") -45))
    (is (= (core/as-long "+67") 67))
    (is (= (core/as-long "abc") nil))
    (is (= (core/as-long "") nil))
    (is (= (core/as-long nil) nil)))

  (testing "random-string"
    (is (= (count (core/random-string 8)) 8))
    (is (not= (core/random-string 40) (core/random-string 40)))))
