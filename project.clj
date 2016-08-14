(defproject proton "0.1.0-SNAPSHOT"
  :description "Utilities library for Clojure/Script"
  :url "https://github.com/xcoo/proton"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies []
  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.8.0"]
                                  [org.clojure/clojurescript "1.9.198"]]
                   :plugins [[lein-cljsbuild "1.1.3"]
                             [lein-cloverage "1.0.6"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0-alpha10"]]}}
  :cljsbuild {:test-commands {"test" ["node" "target/test.js"]}
              :builds [{:source-paths ["test"]
                        :compiler {:target :nodejs
                                   :hashbang false
                                   :output-to "target/test.js"
                                   :optimizations :simple
                                   :pretty-print true}}]})
