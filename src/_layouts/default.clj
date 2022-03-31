(declare post)
(declare load-partial)
(declare document)


(defn render-post
  "Renders the current post."
  []
  [:div.content
   [:div.post
    [:h2.post-title
     [:a {:href (:slug post)} (:title post)]]
    [:div.post-meta (format-date (:date post) "MMM dd, YYYY")]
    [:div.post-entry (:entry post)]]])


(document
  (load-partial "head" {'title (:title post)})
  (load-partial "header")
  (render-post)
  (load-partial "footer"))
