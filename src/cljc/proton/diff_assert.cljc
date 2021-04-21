(ns proton.diff-assert
  (:require #?(:clj [clojure.test :refer [assert-expr do-report]]
               :cljs [cljs.test :refer-macros [assert-expr] :refer [do-report]])
            [proton.diff :as diff]))

(defmethod assert-expr 'diff-u= [msg form]
  `(let [actual# ~(nth form 1)
         expected# ~(nth form 2)
         diff# (diff/diff-u actual# expected#)]
     (if diff#
       (do-report {:type :fail :message diff#
                   :expected expected# :actual actual#})
       (do-report {:type :pass :message ~msg
                   :expected expected# :actual actual#}))
     (not diff#)))
