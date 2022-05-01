(declare include-css)
(declare title)
(declare description)

[:head
 [:meta {:charset "utf-8"}]
 [:meta {:name "viewport" :content "width=device-width"}]
 (when description
   [:meta {:name "description"
           :content description}])
 [:title title]
 [:link {:rel "icon" :href "/icon.svg"}]
 [:link {:rel "alternate" :type "application/rss+xml" :title "RSS Feed" :href "/feed.xml"}]
 (include-css "/style.css")
 [:script {:defer "true" :data-domain "ano.ee" :src "https://plausible.io/js/plausible.js"}]]
