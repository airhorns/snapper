(defproject snapper "1.0.0-SNAPSHOT"
  :description "snap a webpage and stream it using gifsockets for funsies"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [ring/ring-core "1.1.6"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [ring-reload-modified "0.1.1"]]
  :plugins [[lein-ring "0.7.5"]]
  :java-source-paths ["src/java"]
  :main snapper.core
  :ring {:handler snapper.core/app}
)
