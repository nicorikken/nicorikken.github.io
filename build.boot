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

;; (defn is-of-type?
;;   ;;via https://gitlab.com/200ok/200ok.gitlab.io/blob/master/build.boot
;;   [{:keys [permalink]} doc-type]
;;   (.startsWith permalink (str "/" doc-type)))

(deftask build
  "Build the blog."
  []
  (comp
   (global-metadata)
   ;; (markdown)
   (asciidoctor)
   (print-meta)
   (slug)
   (permalink)
   (render :renderer 'views.page/render
           :filterer
           #(or
             ;; (is-of-type? "posts" %)
             ;;              (is-of-type? "pages" %)
             ;;              (is-of-type? "projects" %)
                          (is-of-type? % "posts")
                          (is-of-type? % "pages")
                          (is-of-type? % "projects")
                          )
           )
   (collection :renderer 'views.index/render :page "index.html")
   ;; (sitemap)
   (rss :description "Nico Rikken blog")
   (atom-feed :filterer :original)
   (target)
   (show "-f")
   (notify)))

(deftask dev
  []
  (comp (watch)
        (build)
        (serve :resource-root "public")))
