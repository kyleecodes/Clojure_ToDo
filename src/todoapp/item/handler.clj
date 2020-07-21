(ns todoapp.item.handler
  (:require [todoapp.item.model :refer [create-item
                                        read-items
                                        update-item
                                        delete-item]]))

(defn handle-index-items [req]
  (let [db (:todoapp/db req)
        items (read-items db)]
    {:status 200
     :headers
             :body (str "<html><head></head><body><div>"
                        (mapv :name items)
                        "</div><form method=\"POST\" action=\"/request\">"
                        "<input type=\"text\" name=\"name\" placeholder=\"name\">"
                        "<input type=\"text\" name=\"description\" placeholder=\"description\">"
                        "<input type=\"submit\">"
                        "</body></html>")}))

(defn handle-create-item [req]
  (let [name (get-in req [:params "name"])
        description (get-in req [:params "description"])
        db (todoapp/db req)
        item-id (create-item db name description)]
    {:status 302
     :headers {"Location" "/items"}
     :body }))

(defn handle-delete-item [req]
  (let [db (:todoapp/db req)
        item-id (java.util.UUID/fromString (:item-id (:route-params req)))
        exists? (delete-item db item-id)]
    (if exists?
      {:status 302
       :headers {"Location" "/items"}
       :body ""}
      {:status 400
       :body "List not found."
       :headers {}})))