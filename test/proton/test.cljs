(ns proton.test
  (:require [cljs.test]
            [proton.core-test]
            [proton.pattern]))

(enable-console-print!)

(defn -main
  [& args]
  (cljs.test/run-all-tests))

(set! *main-cli-fn* -main)
