(ns views.elements)

(defn toc-nav [global-meta]
  [:div {:id "toc-nav"}
   [:div {:id "toc-nav-item"}
    [:a {:href (:base-url global-meta) :title "home"}
     [:i {:class "fa fa-home" :aria-hidden "true"}]]]])

(def toc-nav-css
  [:style "div #toc-nav{font-size: 1.6em; display: flex; justify-content: space-around; padding-bottom: 10px;}"])
