(ns views.collection
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-css]]
            [clj-time.core :as t]
            [clj-time.format :as tf]
            [clj-time.coerce :as tc]
            [util :refer [is-of-type?]]
            [views.elements :as e]))

(defn render-generic
  [print-date? {global-meta :meta entries :entries}]
  (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
         [:head
          [:title (:site-title global-meta)]
          [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]]
         (include-css (str (:base-url global-meta) "assets/styles/rams.css"))
         (include-css "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css")
         e/toc-nav-css
         [:body {:class "article"}
          [:div {:id "header"}
           [:h1 "Nico Rikken"]]
          [:div {:id "content"}
           [:div {:id "preamble"}
            [:div {:class "sectionbody"}
             [:div {:class "paragraph"}
              [:p "Welcome on the new version of my home-page. I'm reworking it to be based on the Asciidoctor task in the Perun static site generator, of of the projects I contribute to. More on this is described in my dedicated post."]]
             (e/toc-nav global-meta)]]
           [:div {:class "sect1"}
            [:h2 "Pages"]
            [:div {:class "sectionbody"}
             [:div {:class "ulist"}
              [:ul.items.columns.small-12
               (for [entry entries]
                 [:li
                  [:a {:href (str (:canonical-url entry) "index.html")} (:title entry)]
                  (when (and print-date?
                             (:date-published entry))
                    (str " - "
                         (tf/unparse (tf/formatters :date)
                                     (tc/from-date (:date-published entry)))))])]]
             (e/toc-nav global-meta)]]]]))

(defn render [entries]
  (render-generic false entries))

(defn render-with-date [entries]
  (render-generic true entries))
