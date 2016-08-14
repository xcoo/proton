(ns ^:figwheel-always proton.dev
  (:require [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(def -main (fn [] nil))

(set! *main-cli-fn* -main)
