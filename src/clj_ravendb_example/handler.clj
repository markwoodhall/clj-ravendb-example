(ns clj-ravendb-example.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [cheshire.core :refer [generate-string]]
            [clostache.parser :refer [render-resource]]
            [clj-ravendb.client :as rdb]))

(def ravendb-url "https://markwoodhall-cg5w.ravenhq.com")
(def ravendb-database "markwoodhall-clj-ravendb")

(def oauth-url "https://amazon-useast-2-oauth.ravenhq.com")
(def api-key "0482ed02-a7bf-409a-af30-ea3c177d1465")

(defn load-employee
  [id]
  #(let [client (rdb/client ravendb-url ravendb-database {:ssl-insecure? true :oauth-url oauth-url :api-key api-key})]
     (first (:results (rdb/load-documents client [(str "employees/" id)])))))

(defn load-employees
  []
  #(let [client (rdb/client ravendb-url ravendb-database {:ssl-insecure? true :oauth-url oauth-url :api-key api-key})]
     (:results (rdb/query-index client {:index "Raven/DocumentsByEntityName" :Tag "Employees"}))))

(defn employee-view
  [populate-with-fn]
  #(render-resource "templates/employee.mustache" (populate-with-fn)))

(defn employees-view
  [populate-with-fn]
  #(render-resource "templates/employees.mustache" {:employees (populate-with-fn)}))

(defn master-view
  [populate-with-fn]
  #(render-resource "templates/index.mustache" {:content (populate-with-fn)}))

(defn respond
  [with-function]
  (with-function))

(defroutes app-routes
  (GET "/" []
       (-> (load-employees)
           (employees-view)
           (master-view)
           (respond)))
  (GET "/employees/:id" [id]
       (-> id
           (load-employee)
           (employee-view)
           (master-view)
           (respond)))

  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults)) ()
