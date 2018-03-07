(ns proton.uri-test
  (:require #?(:clj [clojure.test :refer [deftest is are]]
               :cljs [cljs.test :refer-macros [deftest is are]])
            [proton.uri :as uri]))

(deftest uri-encode-test
  (are [s e] (= (uri/uri-encode s) e)
    nil nil
    "" ""
    "ABC" "ABC"
    "テスト" "%E3%83%86%E3%82%B9%E3%83%88"))

(deftest uri-decode-test
  (are [s e] (= (uri/uri-decode s) e)
    nil nil
    "" ""
    "XYZ" "XYZ"
    "%E3%83%86%E3%82%B9%E3%83%88" "テスト"))

(deftest uri-test
  (is (nil? (uri/uri)))
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
          :user-info "user:pa55w0rd"})))
