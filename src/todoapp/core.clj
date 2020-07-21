(ns todoapp.core
  (:require [todoapp.item.model :as items]
            [todoapp.item.handler :as [handle-index-items
                                       handle-create-item
                                       handle-delete-item]])
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
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

(defroutes routes
           (GET "/" [] greet)
           (GET "/goodbye" [] goodbye)
           (ANY "/request" [] handle-dump)
           (GET "/items" [] handle-index-items)
           (POST "/items" [] handle-create-item)
           (DELETE "/items/:item-id" [] handle-delete-item)
           (not-found "Page not found."))

(defn wrap-db [hdlr]
  (fn [req]
    (hdlr (assoc req :todoapp/db db))))

(defn wrap-server [hdlr]
  (fn [req]
    (assoc-in (hdlr req) [:headers "Server"] "Clj To Do List")))

(def sim-methods {"PUT" :put
                  "DELETE" :delete})

(defn wrap-simulated-methods [hdlr]
  (fn [req]
    (if-let [method (and (= :post (:request-method req))
                         (sim-methods (get-in req [:params "_method"])))]
      (hdlr (assoc req :request-method method))
      (hdlr req))))

(def app
  (wrap-server
    (wrap-file-info
      (wrap-db
        (wrap-simulated-methods
          (wrap-params
            routes))
        "static"))))


(defn -main [port]
  (items/create-table db)
  (jetty/run-jetty app
                   {:port (Integer. port)}))

(defn -dev-main [port]
  (items/create-table db)
  (jetty/run-jetty (wrap-reload #'app)
                   {:port (Integer. port)}))