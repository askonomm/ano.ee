(declare format-date)
(declare content)


; Get all blog posts.
(def posts
  (content {:from    "blog"
            :sort-by :date
            :order   :desc}))


; Render page.
[:rss {:version    "2.0"
       :xmlns:atom "http://www.w3.org/2005/Atom"
       :xmlns:content "http://purl.org/rss/1.0/modules/content/"}
 [:channel
  [:atom:link {:href "https://ano.ee/feed.xml" :rel "self" :type "application/rss+xml"}]
  [:title "Asko Nõmm - Clojure / ClojureScript developer"]
  [:description "Blog of a Clojure / ClojureScript developer."]
  [:link "https://ano.ee"]
  (for [post posts]
    [:item
     [:title (:title post)]
     [:description (:description post)]
     (str "<content:encoded><![CDATA[" (:entry post) "]]></content:encoded>")  
     [:link (str "https://ano.ee/" (:slug post))]
     [:guid (str "https://ano.ee/" (:slug post))]
     [:pubDate (str (format-date (:date post) "dd MMM YYYY 00:00:00") " GMT")]])]]
