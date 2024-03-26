(ns proton.core-test
  (:require #?(:clj [clojure.test :refer [deftest is are testing]]
               :cljs [cljs.test :refer-macros [deftest is are testing]])
            [proton.core :as core]))

(deftest as-long-test
  (are [s e] (= (core/as-long s) e)
    "0"          0
    "123"        123
    "999999999"  999999999
    "12,345,678" 12345678
    "-45"        -45
    "+67"        67)
  (are [s] (nil? (core/as-long s))
    "abc"
    ""
    nil)
  (are [s] (thrown? #?(:clj AssertionError :cljs js/Error) (core/as-long s))
    123
    9.87
    {:foo "bar"}
    [1 2 3])
  (testing "different between clj and cljs"
    (is (= (core/as-long "292999988888999999888888")
           #?(:clj nil :cljs 292999988888999999888888)))))

(deftest as-int-test
  (are [s e] (= (core/as-int s) e)
    "0"          0
    "123"        123
    "999999999"  999999999
    "12,345,678" 12345678
    "-45"        -45
    "+67"        67)
  (are [s] (nil? (core/as-int s))
    "abc"
    ""
    nil)
  (are [s] (thrown? #?(:clj AssertionError :cljs js/Error) (core/as-int s))
    123
    9.87
    {:foo "bar"}
    [1 2 3])
  (testing "different between clj and cljs"
    (is (= (core/as-int "292999988888999999888888")
           #?(:clj nil :cljs 292999988888999999888888)))))

(deftest as-double-test
  (are [s e] (= (core/as-double s) e)
    "0"        0.0
    "0.123"    0.123
    "-3.45"    -3.45
    "+5.67"    5.67
    "-1.2e-5"  -1.2e-5
    "4,321.23" 4321.23)
  (are [s] (nil? (core/as-double s))
    "1.23.45"
    "abc"
    ""
    nil)
  (are [s] (thrown? #?(:clj AssertionError :cljs js/Error) (core/as-double s))
    123
    9.87
    {:foo "bar"}
    [1 2 3]))

(deftest as-float-test
  (are [s e] (= (core/as-float s) e)
    "0"        #?(:clj (float 0.0)     :cljs 0.0)
    "0.123"    #?(:clj (float 0.123)   :cljs 0.123)
    "-3.45"    #?(:clj (float -3.45)   :cljs -3.45)
    "+5.67"    #?(:clj (float 5.67)    :cljs 5.67)
    "-1.2e-5"  #?(:clj (float -1.2e-5) :cljs -1.2e-5)
    "4,321.23" #?(:clj (float 4321.23) :cljs 4321.23))
  (are [s] (nil? (core/as-float s))
    "abc"
    ""
    nil)
  (are [s] (thrown? #?(:clj AssertionError :cljs js/Error) (core/as-float s))
    123
    9.87
    {:foo "bar"}
    [1 2 3]))

(deftest as-rational-test
  (are [s e] (= (core/as-rational s) e)
    "1"           1
    "-1"          -1
    "1/2"         #?(:clj 1/2 :cljs 0.5)
    "-1/2"        #?(:clj -1/2 :cljs -0.5)
    "2/1"         2
    "0"           0
    "0/2"         0
    "1,000/2,000" #?(:clj 1/2 :cljs 0.5))
  (are [s] (nil? (core/as-rational s))
    "2/"
    "/2"
    "abc"
    ""
    nil
    "2/0"
    "-2/0")
  (are [s] (thrown? #?(:clj AssertionError :cljs js/Error) (core/as-rational s))
    #?(:clj 1/2 :cljs 0.5)
    1.5
    {:foo "bar"}
    [1 2 3]))

(deftest as-boolean-test
  (are [s] (true? (core/as-boolean s))
    "true"
    "True"
    "yes"
    "YES")
  (are [s] (false? (core/as-boolean s))
    "false"
    "False"
    "no"
    "NO")
  (are [s] (nil? (core/as-boolean s))
    "0"
    ""
    nil)
  (are [s] (thrown? #?(:clj AssertionError :cljs js/Error) (core/as-boolean s))
    true
    false
    123
    {:foo "bar"}
    [1 2 3]))

(deftest is-uuid?-test
  (is (not (core/is-uuid? 1)))
  (is (not (core/is-uuid? "abc")))
  (is (core/is-uuid? #uuid "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee")))

(deftest random-string-test
  (is (= (count (core/random-string 8)) 8))
  (is (not= (core/random-string 40) (core/random-string 40))))

(def example-hash "cfc7749b96f63bd31c3c42b5c471bf756814053e847c10f3eb003417bc523d30")

(deftest bytes-test
  (is (= example-hash (-> example-hash
                          core/hex->bytes
                          core/bytes->hex))))

(deftest clip-test
  (are [x xmin xmax e] (= (core/clip x xmin xmax) e)
    5 3   7   5
    5 6   7   6
    5 3   4   4
    5 3   3   3
    5 nil 7   5
    5 3   nil 5
    5 nil nil 5)
  (are [x xmin xmax] (thrown? #?(:clj Throwable :cljs js/Error) (core/clip x xmin xmax))
    5   3 1
    nil 3 7))

(deftest deep-merge-test
  (is (= (core/deep-merge {:foo {:bar 1}} {:foo {:baz 2}})
         {:foo {:bar 1 :baz 2}}))
  (is (= (core/deep-merge {:foo {:bar 1}} {:baz 2})
         {:foo {:bar 1} :baz 2}))
  (is (= (core/deep-merge {:foo 1} {:foo {:baz 2}})
         {:foo {:baz 2}})))
