(set-env!
 :source-paths #{"src" "../../perun/perun-redo/src"}
 :resource-paths #{"resources"}
 :dependencies '[[clj-time "0.14.0"]
                 [perun "0.4.2-SNAPSHOT"]
                 [hiccup "1.0.5"]
                 [pandeiro/boot-http "0.6.3-SNAPSHOT"]])

(require '[clojure.string :as str]
         '[io.perun :refer :all]
         '[pandeiro.boot-http :refer [serve]]
         '[util :refer [is-of-type? has-tags?]])

(deftask build
  "Build the blog."
  []
  (comp
   (global-metadata)
   (asciidoctor)
   ;; (print-meta)
   (permalink)
   (render :renderer 'views.page/render
           :filterer
           #(or (is-of-type? % "posts")
                (is-of-type? % "pages")
                (is-of-type? % "projects")))
   (collection :renderer 'views.index/render :page "index.html") ;; maybe do by assortment task?
   (collection :renderer 'views.collection/render-with-date
               :filterer #(is-of-type? % "posts")
               :sortby :date-published
               :page "posts/index.html")
   (collection :renderer 'views.collection/render
               :filterer #(is-of-type? % "pages")
               :sortby :title
               :page "pages/index.html")
   (collection :renderer 'views.collection/render
               :filterer #(is-of-type? % "projects")
               :sortby :title
               :page "projects/index.html")
   (sitemap)
   (rss :description "Nico Rikken blog"
        :filterer #(is-of-type? % "posts"))
   (rss :filename "fsfe.rss"
        :filterer (partial has-tags? #{"Free software"
                                       "free software"}))
   (atom-feed :filterer :original)
   (target)
   ;; (show "-f")
   (notify)))

(deftask dev
  []
  (comp (watch)
        (build)
        (serve :resource-root "public")))
