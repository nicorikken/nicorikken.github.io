(set-env!
 :source-paths #{"src" "../../perun/perun-redo/src"}
 :resource-paths #{"resources"}
 :dependencies '[[clj-time "0.14.0"]
                 [perun "0.4.2-SNAPSHOT"]
                 [hiccup "1.0.5"]
                 [pandeiro/boot-http "0.6.3-SNAPSHOT"]])

(require '[clojure.string :as str]
         '[io.perun :refer :all]
         ;'[io.perun.example.index :as index-view]
         ;'[io.perun.example.post :as post-view]
         ;; '[views.index :as index-view]
         '[pandeiro.boot-http :refer [serve]]
         '[util :refer [is-of-type?]])

(deftask build
  "Build the blog."
  []
  (comp
   (global-metadata)
   ;; (markdown)
   (asciidoctor)
   ;; (print-meta)
   ;; (slug)  ;;TODO does not work properly for pages and projects
   (permalink)
   (render :renderer 'views.page/render
           :filterer
           #(or (is-of-type? % "posts")
                (is-of-type? % "pages")
                (is-of-type? % "projects")))
   (collection :renderer 'views.index/render :page "index.html") ;; maybe do by assortment task?
   (collection :renderer 'views.collection/render-with-date
               :filterer #(or (is-of-type? % "posts"))
               :sortby :date-published ;;TODO how to do a reverse sort?
               :page "posts/index.html")
   (collection :renderer 'views.collection/render
               :filterer #(or (is-of-type? % "pages"))
               :sortby :title
               :page "pages/index.html")
   (collection :renderer 'views.collection/render
               :filterer #(or (is-of-type? % "projects"))
               :sortby :title
               :page "projects/index.html")
   (sitemap)
   (rss :description "Nico Rikken blog")
   ;; (rss :filename "fsfe.xml" :filterer TODO)
   (atom-feed :filterer :original)
   (target)
   ;; (show "-f")
   (notify)))

(deftask dev
  []
  (comp (watch)
        (build)
        (serve :resource-root "public")))
