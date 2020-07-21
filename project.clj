(defproject todoapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.2.1"]
                 [compojure "1.1.6"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [postgresql/postgresql "9.1-901.jdbc4"]
                 [hiccup "1.0.5"]]
  :min-lein-version "2.0.0"
  :uberjar-name "todoapp.jar"
  :main todoapp.web
  :profiles {:dev
             {:main todoapp.web/-dev-main}})
