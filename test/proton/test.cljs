(ns proton.test
  (:require [cljs.test]
            [goog.object :as gobj]
            [proton.core-test]
            [proton.pattern-test]
            [proton.string-test]
            ;; [proton.time-test]
            [proton.uri-test]
            [proton.diff-test]))

(enable-console-print!)

(defn- exit
  [status]
  (if-let [nodejs-exit (and (exists? js/process) (gobj/get js/process "exit"))]
    (nodejs-exit status)
    (println "Exit function not found")))

(defmethod cljs.test/report [:cljs.test/default :end-run-tests] [m]
  (exit (if (cljs.test/successful? m) 0 1)))

(defn -main
  [& args]
  (cljs.test/run-all-tests))

(set! *main-cli-fn* -main)
