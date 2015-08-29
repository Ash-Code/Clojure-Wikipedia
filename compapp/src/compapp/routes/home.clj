(ns compapp.routes.home
  (:require [compojure.core :refer :all]
            [compapp.views.layout :as layout]
            [hiccup.form :refer :all]
            [compapp.models.db :as db]
)
 
)



(defn show-wiki []
[:ul.guests
(for [{:keys [body title timestamp]} (db/read-wikis)]
[:li
[:h2 title]
[:blockquote body]
[:time timestamp]])])


(defn home [& [title body error]]
(layout/common
[:h1 "Wiki"]
[:p error]
(show-wiki)
[:hr]
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


