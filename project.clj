(defproject proton "0.2.2"
  :description "Utilities for Clojure/Script"
  :url "https://github.com/xcoo/proton"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies []
  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.3"]
                                  [org.clojure/clojurescript "1.10.844" :exclusions [org.clojure/clojure]]]
                   :plugins [[lein-cljsbuild "1.1.8"]
                             [lein-codox "0.10.7"]
                             [lein-figwheel "0.5.20" :exclusions [org.clojure/clojure]]
                             [lein-cloverage "1.2.2" :exclusions [org.clojure/clojure]]]
                   :global-vars {*warn-on-reflection* true}}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0"]]}
             :1.10 {:dependencies [[org.clojure/clojure "1.10.3"]]}
             :1.11 {:dependencies [[org.clojure/clojure "1.11.0-alpha1"]]}}
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
                                   :pretty-print true}}]}
  :codox {:output-path "docs/api"
          :source-uri "https://github.com/xcoo/proton/blob/{version}/{filepath}#L{line}"}
  :signing {:gpg-key "developer@xcoo.jp"})
