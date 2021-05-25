(ns proton.assoc-in-after-test
  (:require #?(:clj [clojure.test :refer [is are deftest testing]]
               :cljs [cljs.test :refer-macros [is are deftest testing]])
            [proton.assoc-in-after :as aia]))

(deftest contains-in?-test
  (are [m ks r] (= (#'aia/contains-in? m ks)
                   r)
    {:a {:b 1}}
    [:a :b]
    true

    {:a {:b 1}}
    [:a :c]
    false))

(deftest assoc-in*-test
  (are [m ks v r] (= (#'aia/assoc-in* m ks v)
                     r)
    {:a {:b 1}}
    [:a :b]
    2
    {:a {:b 2}}

    {:a {:b 1}}
    []
    2
    2))

(deftest assoc-in-after-test
  (testing "When `ks` is empty, it returns just `v`"
    (is (= (aia/assoc-in-after {} [] 2 [])
           2)))
  (testing "When `before-ks` is empty, it's same `(assoc-in omap ks v)`"
    (is (= (aia/assoc-in-after {:a {:b 1}} [:a :b] 2 [])
           {:a {:b 2}})))
  (testing "When `before-ks` isn't empty and `ks` is in `omap`, it's same `(assoc-in omap ks v)`"
    (is (= (aia/assoc-in-after {:a {:b 1}} [:a :b] 2 [:a])
           {:a {:b 2}})))
  (testing "When the first of `ks` is in `omap`, it inserts `v` into `ks` after `before-ks`"
    (is (= (aia/assoc-in-after {:a {:b 1 :d 3}} [:a :c] 2 [:a :b])
           {:a {:b 1 :c 2 :d 3}})))
  (testing "When `before-ks` is `[:proton.assoc-in-after/first]`, it inserts `{ks v}` into the first of `omap`"
    (let [target (aia/assoc-in-after (array-map :a {:b 1}) [:c] 2 [:proton.assoc-in-after/first])]
      (is (= target
             {:c 2 :a {:b 1}}))
      (is (= (first target)
             [:c 2]))))
  (testing "When the first of `before-ks` is not in `omap`, it inserts `{ks v}` into the last of `omap`"
    (let [target (aia/assoc-in-after {:a {:b 1}} [:c] 2 [:b])]
      (is (= target
             {:a {:b 1} :c 2}))
      (is (= (last target)
             [:c 2]))))
  (testing "When the first of `before-ks` is in `omap`, it inserts `{ks v}` after `before-ks`"
    (let [target (aia/assoc-in-after {:a {:b 1} :d 3} [:c] 2 [:a])]
      (is (= target
             {:a {:b 1} :c 2 :d 3}))
      (is (= (second target)
             [:c 2])))))
