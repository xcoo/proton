(ns proton.pattern-test
  (:require #?(:clj [clojure.test :refer [deftest is testing]]
               :cljs [cljs.test :refer-macros [deftest is testing]])
            [proton.pattern :as pattern]))

(deftest validator

  (testing "valid-email?"
    ;; empty
    (is (not (pattern/valid-email? "")))
    ;; only local-part
    (is (not (pattern/valid-email? "example")))
    ;; only domain-part
    (is (not (pattern/valid-email? "example.com")))
    ;; valid short mail-address
    (is (pattern/valid-email? "a@abc.jp"))
    ;; valid mail-address
    (is (pattern/valid-email? "test@example.com"))
    ;; @ not found
    (is (not (pattern/valid-email? "test_example.com")))
    ;; extra spaced mail-address
    (is (not (pattern/valid-email? "test@example.com ")))
    ;; valid mail-address include symbol chars
    (is (pattern/valid-email? "test+!&-_=?@example.com"))
    ;; It's not valid(violation rfc), but it should allow for japanese moby
    (is (pattern/valid-email? "test..@example.com"))
    ;; It's valid value of "From:" header, but not valid mail-address
    (is (not (pattern/valid-email? "<test@example.com>")))
    ;; It's valid, but it disturb consistency check, so may not allow this
    (is (not (pattern/valid-email? "\"test\"@example.com")))
    ;; It's valid, but should not allow this! These are "source routing".
    (is (not (pattern/valid-email? "test%example.com")))
    (is (not (pattern/valid-email? "test%example.org@example.com"))))

  (testing "valid-password?"
    (is (not (pattern/valid-password? "")))
    (is (pattern/valid-password? "1234abcd"))
    (is (pattern/valid-password? "ABCD!@#$"))
    (is (pattern/valid-password? "%^&*()_+"))
    (is (pattern/valid-password? "<>\\\"'"))
    (is (not (pattern/valid-password? "password ")))
    (is (not (pattern/valid-password? "password\n")))
    (is (not (pattern/valid-password? "password\t")))))
