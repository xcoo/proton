(ns proton.time-test
  (:require #?(:clj [clojure.test :refer [deftest is]]
               :cljs [cljs.test :refer-macros [deftest is]])
            [proton.time :as time]))

(deftest datetime-format-test
  (is (= (time/format-datetime-string (time/datetime-formatter "yyyy/MM/dd HH:mm:ss")
                                      (time/datetime 1474361101))
         "2016/09/20 08:45:01")))
