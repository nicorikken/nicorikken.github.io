(ns views.page
  (:require [hiccup.page :refer [html5 include-css]]
            [clj-time.format :as tf]
            [clj-time.coerce :as tc]
            [views.elements :as e]))

(defn render [{global-meta :meta posts :entries post :entry}]
  (html5 {:lang "en" :itemtype "http://schema.org/Blog"}
         [:head
          [:title (str (:site-title global-meta) "|" (:title post))]
          [:meta {:charset "utf-8"}]
          [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]
          (include-css (str (:base-url global-meta) "assets/styles/rams.css"))
          (include-css (str (:base-url global-meta) "assets/styles/coderay.css"))
          (include-css "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css")
          e/toc-nav-css]
         [:body {:class "article"}
          [:div {:id "header"}
           [:a {:href (:base-url global-meta)}[:h1 (:title post)]]
           [:div {:class "details"}
            (when-let [author (:author post)]
              [:span {:id "author" :class "author"} author])
            (when (:author post)
              [:br])
            (when-let [revdate (:date-published post)]
              [:span {:id "revdate"}
               (tf/unparse (tf/formatters :date)
                           (tc/from-date revdate))])]]
          [:div {:id "content"}
           (e/toc-nav global-meta)
           (:content post)
           (e/toc-nav global-meta)]]))

