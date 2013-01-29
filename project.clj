(defproject snapper "1.0.0-SNAPSHOT"
  :description "snap a webpage and stream it using gifsockets for funsies"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [ring "1.1.8"]
                 [ring-reload-modified "0.1.1"]
                 [me.raynes/conch "0.4.0"]
                 [org.clojure/tools.trace "0.7.5"]]
  :plugins [[lein-ring "0.7.5"]]
  :java-source-paths ["src/java"]
  :main snapper.core
  :ring {:handler snapper.core/app})
