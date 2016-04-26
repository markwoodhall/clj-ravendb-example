(defproject clj-ravendb-example "0.1.0-SNAPSHOT"
  :description "An example project that shows how to use the clj-ravendb client"
  :url "http://github.com/markwoodhall/clj-ravendb-example"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [clj-ravendb "0.13.0"]
                 [cheshire "5.4.0"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [ring/ring-defaults "0.1.2"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler clj-ravendb-example.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
