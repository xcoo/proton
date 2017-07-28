(ns proton.core-test
  (:require #?(:clj [clojure.test :refer [deftest is testing]]
               :cljs [cljs.test :refer-macros [deftest is testing]])
            [proton.core :as core]))

(def example-hash "cfc7749b96f63bd31c3c42b5c471bf756814053e847c10f3eb003417bc523d30")

(deftest core-test

  (testing "as-int"
    (is (= (core/as-int "0") 0))
    (is (= (core/as-int "123") 123))
    (is (= (core/as-int "999999999") 999999999))
    (is (= (core/as-int "12,345,678") 12345678))
    (is (= (core/as-int "-45") -45))
    (is (= (core/as-int "+67") 67))
    (is (= (core/as-int "abc") nil))
    (is (= (core/as-int "") nil))
    (is (= (core/as-int nil) nil)))

  (testing "as-long"
    (is (= (core/as-long "0") 0))
    (is (= (core/as-long "123") 123))
    (is (= (core/as-long "999999999") 999999999))
    (is (= (core/as-long "12,345,678") 12345678))
    (is (= (core/as-long "-45") -45))
    (is (= (core/as-long "+67") 67))
    (is (= (core/as-long "abc") nil))
    (is (= (core/as-long "") nil))
    (is (= (core/as-long nil) nil)))

  (testing "as-float"
    (is (= (core/as-float "0") #?(:clj (float 0.0)
                                  :cljs 0.0)))
    (is (= (core/as-float "0.123") #?(:clj (float 0.123)
                                      :cljs 0.123)))
    (is (= (core/as-float "-3.45") #?(:clj (float -3.45)
                                      :cljs -3.45)))
    (is (= (core/as-float "+5.67") #?(:clj (float 5.67)
                                      :cljs 5.67)))
    (is (= (core/as-float "-1.2e-5") #?(:clj (float -1.2e-5)
                                        :cljs -1.2e-5)))
    (is (= (core/as-float "4,321.23") #?(:clj (float 4321.23)
                                         :cljs 4321.23)))
    (is (nil? (core/as-float "abc")))
    (is (nil? (core/as-float "")))
    (is (nil? (core/as-float nil))))

  (testing "as-double"
    (is (= (core/as-double "0") 0.0))
    (is (= (core/as-double "0.123") 0.123))
    (is (= (core/as-double "-3.45") -3.45))
    (is (= (core/as-double "+5.67") 5.67))
    (is (= (core/as-double "-1.2e-5") -1.2e-5))
    (is (= (core/as-double "4,321.23") 4321.23))
    (is (nil? (core/as-double "1.23.45")))
    (is (nil? (core/as-double "abc")))
    (is (nil? (core/as-double "")))
    (is (nil? (core/as-double nil))))

  (testing "as-boolean"
    (is (true? (core/as-boolean "true")))
    (is (true? (core/as-boolean "True")))
    (is (true? (core/as-boolean "yes")))
    (is (true? (core/as-boolean "YES")))
    (is (false? (core/as-boolean "false")))
    (is (false? (core/as-boolean "False")))
    (is (false? (core/as-boolean "no")))
    (is (false? (core/as-boolean "NO")))
    (is (nil? (core/as-boolean "0")))
    (is (nil? (core/as-boolean "")))
    (is (nil? (core/as-boolean nil))))

  (testing "is-uuid?"
    (is (not (core/is-uuid? 1)))
    (is (not (core/is-uuid? "abc")))
    (is (core/is-uuid? #uuid "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee")))

  (testing "random-string"
    (is (= (count (core/random-string 8)) 8))
    (is (not= (core/random-string 40) (core/random-string 40))))

  (testing "bytes"
    (is (= example-hash
           (-> example-hash
               core/hex->bytes
               core/bytes->hex)))))
