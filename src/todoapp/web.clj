(ns todoapp.web
  (:require [todoapp.item.model :as items])
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

(def db "jdbc:postgresql://localhost/cljtodo")

(defn greet [req]
  {:status 200
   :body "Hello"
   :headers {}})

(defn goodbye [req]
  {:status 200
   :body "Goodbye"
   :headers {}})

(defn request [req]
  {:status 200
   :body (pr-str req)
   :headers {}})

(defroutes app
           (GET "/" [] greet)
           (GET "/goodbye" [] goodbye)
           (GET "/request" [] handle-dump)
           (not-found "Page not found."))

(defn -main [port]
  (items/create-table db)
  (jetty/run-jetty app
                   {:port (Integer. port)}))

(defn -dev-main [port]
  (items/create-table db)
  (jetty/run-jetty (wrap-reload #'app)
                   {:port (Integer. port)}))