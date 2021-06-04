(ns proton.diff-assert
  #?(:clj (:require [clojure.test :as t]
                    [proton.diff :as diff])))

#?(:clj
   (defmethod t/assert-expr 'diff-u= [msg form]
     `(let [actual# ~(nth form 1)
            expected# ~(nth form 2)
            diff# (diff/diff-u actual# expected#)]
        (if diff#
          (t/do-report {:type :fail :message diff#
                        :expected expected# :actual actual#})
          (t/do-report {:type :pass :message ~msg
                        :expected expected# :actual actual#}))
        (not diff#))))
