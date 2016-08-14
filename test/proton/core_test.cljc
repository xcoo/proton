(ns proton.core-test
  (:require #?(:clj [clojure.test :refer [deftest is testing]]
               :cljs [cljs.test :refer-macros [deftest is testing]])
            [proton.core :as core]))

(deftest core

  (testing "str->int"
    (is (= (core/str->int "0") 0))
    (is (= (core/str->int "123") 123))
    (is (= (core/str->int "999999999") 999999999))
    (is (= (core/str->int "-45") -45))
    (is (= (core/str->int "+67") 67))
    (is (= (core/str->int "abc") nil))
    (is (= (core/str->int "") nil))
    (is (= (core/str->int nil) nil)))

  (testing "transform-key"
    (is (= (core/transform-key {:c 3, :d 4} :a :b) {:c 3, :d 4}))
    (is (= (core/transform-key {:a 1} :a :b) {:b 1}))
    (is (= (core/transform-key {:a 1, :b 2} :a :b) {:b 1})))

  #?(:clj (testing "swap"
            (is (= (core/swap {:a 1, :b 2} :a inc) {:a 2, :b 2}))
            (is (= (core/swap {:a 1, :b 2} :c inc) {:a 1, :b 2}))))

  (testing "random-string"
    (is (= (count (core/random-string 8)) 8))
    (is (not= (core/random-string 40) (core/random-string 40)))))
