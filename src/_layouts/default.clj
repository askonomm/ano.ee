(declare post)
(declare load-partial)
(declare document)
(declare format-date)


(defn render-post
  "Renders the current post."
  []
  [:div.content
   [:div.post
    [:h2.post-title
     [:a {:href (str "/" (:slug post))} (:title post)]]
    (when (:date post)
      [:div.post-meta
       [:a {:href (str "/" (:slug post))} (format-date (:date post) "MMM dd, YYYY")]])
    [:div.post-entry (:entry post)]]])


(document
  [:div.container
   (load-partial "head" {'title (:title post)
                         'description (:description post)})
   (load-partial "header")
   (render-post)]
  (load-partial "footer"))
