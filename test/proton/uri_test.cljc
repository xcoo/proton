(ns proton.uri-test
  (:require #?(:clj [clojure.test :refer [deftest is testing]]
               :cljs [cljs.test :refer-macros [deftest is testing]])
            [proton.uri :as uri]))

(deftest uri-test

  (testing "URI encode/decode"
    ;; encoder
    (is (= (uri/uri-encode nil) nil))
    (is (= (uri/uri-encode "") ""))
    (is (= (uri/uri-encode "ABC") "ABC"))
    (is (= (uri/uri-encode "テスト") "%E3%83%86%E3%82%B9%E3%83%88"))
    ;; decoder
    (is (= (uri/uri-decode nil) nil))
    (is (= (uri/uri-decode "") ""))
    (is (= (uri/uri-decode "XYZ") "XYZ"))
    (is (= (uri/uri-decode "%E3%83%86%E3%82%B9%E3%83%88") "テスト")))

  (testing "URI parser"
    (is (= (uri/uri) nil))
    (is (= (uri/uri "http://localhost/path/to/file")
           {:scheme "http"
            :host "localhost"
            :port nil
            :path "/path/to/file"
            :query nil
            :fragment nil
            :user-info nil}))
    (is (= (uri/uri "https://user:pa55w0rd@example.com:3000/path/to/content?foo=1&bar=ABC&baz=%E3%83%86%E3%82%B9%E3%83%88#footer")
           {:scheme "https"
            :host "example.com"
            :port 3000
            :path "/path/to/content"
            :query {:foo "1" :bar "ABC" :baz "テスト"}
            :fragment "footer"
            :user-info "user:pa55w0rd"}))))
