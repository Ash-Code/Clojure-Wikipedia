(ns compapp.models.db
(:require [clojure.java.jdbc :as sql])
(:import java.sql.DriverManager))

(def db {:classname "org.sqlite.JDBC",
:subprotocol "sqlite",
:subname "db.sq3"})

(defn train
[]
(println "Coo")
)

(defn read-wikis []
(sql/with-connection
db
(sql/with-query-results res
["SELECT * FROM wikipedia ORDER BY timestamp DESC"]
(doall res))))

(defn save-wiki [title body]
(sql/with-connection
db
(sql/insert-values
:wikipedia
[:title :body :timestamp]
[title body (new java.util.Date)])))

(defn create-wikipedia-table []
(sql/with-connection
db
(sql/create-table
:wikipedia
[:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
[:timestamp "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
[:title "TEXT"]
[:body "TEXT"])

(sql/do-commands "CREATE INDEX timestamp_index ON wikipedia (timestamp)")))

