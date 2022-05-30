(require '[clojure.string :as string])
(declare load-partial)
(declare document)
(declare content)
(declare format-date)


(def gigs
  (let [items (content {:from "cv/gigs"
                        :sort-by :until
                        :order :desc})
        ongoing-items (filterv #(not (:until %)) items)
        finished-items (filterv #(:until %) items)]
    (concat ongoing-items finished-items)))


(defn render-gig-company
  [{:keys [company company-link]}]
  (if company-link
    [:a {:href company-link} company]
    company))


(defn render-gigs
  []
  [:div.gigs
    (for [gig gigs]
      [:div.gig
       [:h3 
        (str (:position gig) " at ") 
        (render-gig-company gig)]
       [:div.meta 
        (format-date (:from gig) "MMM YYYY")
        " &mdash; "
        (if (:until gig) 
          (format-date (:until gig) "MMM YYYY")
          "Present")
        " &bull; "
        (:location gig)
        (when (:remote gig)
          [:span.remote "remote"])]
       [:div.entry (:entry gig)]])])


(def languages
  ["Clojure" "ClojureScript" "JavaScript" "TypeScript"
   "Rust" "Java" "C#" "PHP" "Swift"])


(def tools
  ["Re-frame" "Reagent" "Re-om" "Shadow-CLJS" "Lein"
   "Clojars" "Node.js" "Deno.js" "NPM" "Yarn" "Laravel" 
   "Packagist" "MySQL" "SQLite" "Flat-file stores"])


(defn render-content
  []
  [:div.content.cv
   [:h2.main-title "Curriculum Vitae"]
   [:h2 "About"]
   (let [start-year (Integer/parseInt (first (string/split (:from (last gigs)) #"-")))
         date ^java.util.Calendar (java.util.GregorianCalendar.)
         year (.get date (.-YEAR java.util.Calendar))
         years-of-experience (- year start-year)]
     [:p (str "I'm a Software Engineer with " years-of-experience " years of work experience specializing in Clojure / ClojureScript.")])
   [:h2 "Work experience"]
   (render-gigs)
   [:h2 "Languages"]
   (map-indexed
     (fn [index item]
       [:span
        [:span.item item]
        (if-not (= index (- (count languages) 1))
          ", "
          ".")])
     languages)
   [:h2 "Tools"]
   (map-indexed
     (fn [index item]
       [:span
        [:span.item item]
        (if-not (= index (- (count tools) 1))
          ", "
          ".")])
     tools)
   [:h2 "Contact information &amp; Links"]
   [:p "You can get in touch via e-mail at " [:a {:href "mailto:ano@ano.ee"} "ano@ano.ee"] "."]
   [:ul
    [:li [:a {:href "https://www.linkedin.com/in/asko-nomm/"} "LinkedIn"]]
    [:li [:a {:href "https://github.com/askonomm"} "GitHub"]]]])
     
        
       

(document
  [:div.container
   (load-partial "head" {'title "Curriculum Vitae - Asko Nõmm"
                         'description "Software Engineer specializing in Clojure / ClojureScript."})
   (load-partial "header")
   (render-content)])

