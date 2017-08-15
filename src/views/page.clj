(ns views.page
  (:require [hiccup.page :refer [html5 include-css]]))

(defn render [{global-meta :meta posts :entries post :entry}]
  (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
         [:head
          [:title (str (:site-title global-meta) "|" (:title post))]
          [:meta {:charset "utf-8"}]
          [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]
          (include-css (str (:base-url global-meta) "assets/styles/rams.css"))
          (include-css "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css")]
         [:body {:class "article"}
          [:div {:id "header"}
           [:h1 (:title post)]]
          [:div {:id "content"}
           (:content post)]]))

