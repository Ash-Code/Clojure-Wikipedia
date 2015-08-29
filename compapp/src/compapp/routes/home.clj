(ns compapp.routes.home
  (:require [compojure.core :refer :all]
            [compapp.views.layout :as layout]
            [hiccup.form :refer :all]
            [compapp.models.db :as db]
)
 
)


(defn format-time [timestamp]
(-> "dd/MM/yyyy"
(java.text.SimpleDateFormat.)
(.format timestamp)))


(defn show-wiki []
[:ul.guests
(for [{:keys [body title timestamp]} (db/read-wikis)]
[:li
[:h2 title]
[:time (str "Added on " (format-time timestamp))]
[:blockquote body]

])])



(defn home [& [title body error]]
(layout/common
[:h1 "Welcome to Wikipedia"]
[:p error]
(show-wiki)
[:hr]
[:h1 "Add an entry "]
(form-to [:post "/"]
[:p "Title:"]
(text-field "title" title)
[:p "Body:"]
(text-area {:rows 10 :cols 40} "body" body)
[:br]
(submit-button "submit"))))

(defn save-entry [title body]
(cond
(empty? title)
(home title body "no title")
(empty? body)
(home title body "no body")
:else
(do
(db/save-wiki title body) (home))))

(defroutes home-routes
  (GET "/" [] (home))
(POST "/" [title body] (save-entry title body))
)


