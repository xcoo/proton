(ns proton.diff-test
  (:require #?(:clj [clojure.test :refer [are deftest testing]]
               :cljs [cljs.test :refer-macros [are deftest testing]])
            [proton.diff :as diff]))

(deftest diff-u-test
  (testing "diff -u"
    (are [a b e] (= (diff/diff-u a b) e)
      nil nil nil
      nil {} "- \n+ {}"
      {} nil "- {}\n+ "
      {} {} nil
      {} {:a 1} "- \n+ {:a 1}"
      {:a 1} {} "- {:a 1}\n+ "
      nil {:a 1} "- \n+ {:a 1}"
      {:a 1} nil "- {:a 1}\n+ "
      {:a 1} {:a 1} nil
      {:a 1} {:b 2} "- {:a 1}\n+ {:b 2}"
      {:a 1} {:a 1 :b 2} "- \n+ {:b 2}"
      {:a 1 :b 2} {:a 1} "- {:b 2}\n+ "))
  (testing "error"
    (are [a b] (thrown? #?(:clj Throwable, :cljs js/Error) (diff/diff-u a b))
      [] []
      nil []
      [] nil)))
