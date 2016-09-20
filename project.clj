(defproject proton "0.1.0-SNAPSHOT"
  :description "Utilities library for Clojure/Script"
  :url "https://github.com/xcoo/proton"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies []
  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.8.0"]
                                  [org.clojure/clojurescript "1.9.229" :exclusions [org.clojure/clojure]]]
                   :plugins [[lein-cljsbuild "1.1.4"]
                             [lein-figwheel "0.5.4-7" :exclusions [org.clojure/clojure]]
                             ;; [lein-cloverage "1.0.6" :exclusions [org.clojure/clojure]]
                             ]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0-alpha12"]]}}
  :cljsbuild {:test-commands {"test" ["node" "target/test.js"]}
              :builds [{:id "dev"
                        :source-paths ["src/dev/cljs"]
                        :figwheel true
                        :compiler {:main proton.dev
                                   :output-to "target/dev_out/dev.js"
                                   :output-dir "target/dev_out"
                                   :target :nodejs
                                   :optimizations :none
                                   :source-map true}}
                       {:id "test"
                        :source-paths ["test"]
                        :compiler {:target :nodejs
                                   :hashbang false
                                   :output-to "target/test.js"
                                   :optimizations :simple
                                   :pretty-print true}}]})
