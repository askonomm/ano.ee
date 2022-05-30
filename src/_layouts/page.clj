(declare post)
(declare load-partial)
(declare document)


(defn render-page
  "Renders the current page."
  []
  [:div.content
   [:div.page
    [:h2.post-title
     [:a {:href (str "/" (:slug post))} (:title post)]]
    [:div.post-entry (:entry post)]]])


(document
  [:div.container
   (load-partial "head" {'title (:title post)
                         'description (:description post)})
   (load-partial "header")
   (render-page)]
  (load-partial "footer"))
