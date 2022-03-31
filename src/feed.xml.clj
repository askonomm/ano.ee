(declare xml)
(declare content)


; Get all blog posts.
(def posts
  (content {:from    "blog"
            :sort-by :date
            :order   :desc}))


; Render page.
(xml
  (for [post posts]
    []))