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
(def archive
  (content {:from     "blog"
            :sort-by  :date
            :order    :desc
            :group-by #(group-content-by-year (:date %))}))


(defn render-posts
  "Renders blog posts."
  []
  [:div.content.home
   (for [year archive]
     [:div.year
      [:h3 (key year)]
      [:div.posts
       (for [post (val year)]
         [:div.post
          [:h2.post-title
           [:a {:href (:slug post)} (:title post)]]
          [:div.post-meta (format-date (:date post) "MMM dd")]])]])])


; Render page.
(document
  (load-partial "head" {'title "Asko Nõmm - Clojure / ClojureScript developer"})
  (load-partial "header")
  (render-posts)
  (load-partial "footer"))
