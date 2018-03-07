(ns proton.pattern-test
  (:require #?(:clj [clojure.test :refer [deftest is are testing]]
               :cljs [cljs.test :refer-macros [deftest is are testing]])
            [proton.pattern :as pattern]))

(deftest valid-email?-test
  (testing "valid"
    (are [s] (pattern/valid-email? s)
      "test@example.com"
      "a@abc.jp" ; short
      "test+!&-_=?@example.com" ; including symbol chars
      "test..@example.com" ; not valid (violation rfc), but should allow for japanese moby
      ))
  (testing "invalid"
    (are [s] (not (pattern/valid-email? s))
      nil
      "" ; empty
      "example" ; only local-part
      "example.com" ; only domain-part
      "test_example.com" ; @ not found
      "test@example.com " ; extra spaced mail-address
      "<test@example.com>" ; valid value of "From:" header, but not valid mail-address
      "\"test\"@example.com" ; valid, but it disturb consistency check, so may not allow this
      "test%example.com" ; valid, but should not allow this! These are "source routing".
      "test%example.org@example.com")))

(deftest valid-password?-test
  (testing "valid"
    (are [s] (pattern/valid-password? s)
      "1234abcd"
      "ABCD!@#$"
      "%^&*()_+"
      "<>\\\"'"))
  (testing "invalid"
    (are [s] (not (pattern/valid-password? s))
      nil
      ""
      "password "
      "password\n"
      "password\t")))

(deftest valid-uuid?-test
  (is (not (pattern/valid-uuid? nil)))
  (is (not (pattern/valid-uuid? "")))
  (is (pattern/valid-uuid? "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee")))
