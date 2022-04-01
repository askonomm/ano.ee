(declare format-date)
(declare content)


; Get all blog posts.
(def posts
  (content {:from    "blog"
            :sort-by :date
            :order   :desc}))


; Render page.
[:rss {:version    "2.0"
       :xmlns:atom "http://www.w3.org/2005/Atom"}
 [:channel
  [:atom:link {:href "https://bien.ee/feed.xml" :rel "self" :type "application/rss+xml"}]
  [:title "Bien"]
  [:description ""]
  [:link "https://bien.ee"]
  (for [post posts]
    [:item
     [:title (:title post)]
     [:description ""]
     [:link (str "https://bien.ee/" (:slug post))]
     [:guid (str "https://bien.ee/" (:slug post))]
     [:pubDate (str (format-date (:date post) "dd MMM YYYY 00:00:00") " GMT")]])]]