(ns views.index
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-css]]
            [clj-time.core :as t]
            [clj-time.format :as tf]
            [clj-time.coerce :as tc]
            [util :refer [is-of-type?]]
            [views.elements :as e]))

(defn render [{global-meta :meta entries :entries}]
  (let [posts    (take 10 (reverse (sort-by :date-published (filter #(is-of-type? % "posts") entries))))
        pages    (sort-by :title (filter #(is-of-type? % "pages") entries))
        projects (sort-by :title (filter #(is-of-type? % "projects") entries))]
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
               [:p "Welcome on the new version of my home-page. I'm reworking it to be based on the Asciidoctor task in the Perun static site generator, of of the projects I contribute to. More on this is described in my dedicated post."]
               [:div {:id "toc-nav"}
                [:div {:id "toc-nav-item"}
                 [:a {:href "/feed.rss" :title "rss"}
                  [:i {:class "fa fa-rss" :aria-hidden "true"}]]]]]]

             [:div {:class "sect1"}
              [:a {:href "pages/index.html"} [:h2 "Pages"]]
              [:div {:class "sectionbody"}
               [:div {:class "ulist"}
                [:ul.items.columns.small-12
                 (for [page pages]
                   [:li
                    [:a {:href (str (:canonical-url page) "index.html")} (:title page)]])]]]]

             [:div {:class "sect1"}
              [:a {:href "posts/index.html"} [:h2 "Posts"]]
              [:div {:class "sectionbody"}
               [:div {:class "ulist"}
                [:ul.items.columns.small-12
                 (for [post posts]
                   [:li
                    [:a {:href (str (:canonical-url post) "index.html")} (:title post)]
                    (when (:date-published post)
                      (str " - "
                           (tf/unparse (tf/formatters :date)
                                       (tc/from-date (:date-published post)))))])
                 [:li "..."]]]]]


             [:div {:class "sect1"}
              [:a {:href "projects/index.html"} [:h2 "Projects"]]
              [:div {:class "sectionbody"}
               [:div {:class "ulist"}
                [:ul.items.columns.small-12
                 (for [project projects]
                   [:li
                    [:a {:href (str (:canonical-url project) "index.html")}(:title project)]])]]]]
             ]])))
