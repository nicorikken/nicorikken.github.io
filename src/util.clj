(ns util)

(defn is-of-type?
  ;;via https://gitlab.com/200ok/200ok.gitlab.io/blob/master/build.boot
  [{:keys [permalink]} doc-type]
  (.startsWith permalink (str "/" doc-type)))

(defn has-tags?
  [test {:keys [tags]}]
  {:pre [(set? test)]}
  (not-empty (filter test tags)))
