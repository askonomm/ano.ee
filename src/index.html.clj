(require '[clojure.string :as string])
(declare document)
(declare content)
(declare load-partial)
(declare format-date)


(defn group-content-by-year
  "Date is in year-month-day format, so we don't have
  to do anything fancy here, splitting works just fine."
  [date]
  (when date
    (-> (string/split date #"-")
        first)))


; Get all blog posts grouped by year.
(def posts
  (content {:from     "blog"
            :sort-by  :date
            :order    :desc}))


(defn render-posts
  "Renders blog posts."
  []
  [:div.content.posts
   [:div
    (for [post posts]
      [:div.post
       [:h2.post-title
        {:class (when (:link post) "external-link")}
        [:a {:href (if (:link post)
                     (:link post)
                     (str "/" (:slug post)))} (:title post)]]
       [:div.post-meta
        [:a {:href (str "/" (:slug post))} (format-date (:date post) "MMM dd, YYYY")]]
       [:div.post-entry (:entry post)]])]
   [:div.read-more
    [:a {:href "/archive"} "There's more to read in the archive."]]])


; Render page.
(document
  [:div.container
   (load-partial "head" {'title "Asko Nõmm - Clojure / ClojureScript developer"
                         'description "Thoughts on Clojure, ClojureScript and ever-changing world of software development."})
   (load-partial "header")
   (render-posts)]
  (load-partial "footer"))
