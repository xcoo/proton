(ns proton.test
  (:require [cljs.test]
            [proton.core-test]
            [proton.pattern-test]
            [proton.string-test]
            ;; [proton.time-test]
            [proton.uri-test]))

(enable-console-print!)

(defn -main
  [& args]
  (cljs.test/run-all-tests))

(set! *main-cli-fn* -main)
