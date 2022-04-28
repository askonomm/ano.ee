(def looking-for-job false)


[:div.footer
  [:p "Hi. I'm Asko Nõmm, a software engineer from Tallinn, Estonia, specializing in Clojure / ClojureScript. Check my open source work on " [:a {:href "https://github.com/askonomm"} "GitHub"] " and shoot me an e-mail to " [:a {:href "mailto:ano@ano.ee"} "ano@ano.ee"] ". "]
  [:p "If you're interested in pitching me a job offer then feel free to check out my " [:a {:href "/cv"} "resume"] "."
   (when-not looking-for-job
     " I'm not currently looking, but am always open to seeing what is out there.")] 
  [:script {:src "/highlight.min.js"}]
  [:script "hljs.highlightAll();"]]
