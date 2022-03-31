(declare post)

[:div.post
 [:h2
  [:a {:href (:slug post)} (:title post)]]
 [:div.entry (:entry post)]]