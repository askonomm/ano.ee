---
title: Ruuter: a tiny, zero-dependency HTTP router
date: 2021-10-03
status: public
---

I've been tinkering with an idea for a new project I want to build and as part of that project I need an HTTP router. I went and checked out the most popular HTTP routers for Clojure and came away disappointed - they are overly complex, have tons of dependencies and don't seem entirely nice or intuitive to use either. But because I like building stuff, I decided to build my own.

And the result is [Ruuter](https://github.com/askonomm/ruuter), a tiny, zero-dependency HTTP router designed to be plug-in for any Ring compatible HTTP server like [http-kit](https://github.com/http-kit/http-kit) and [Ring + Jetty](https://github.com/ring-clojure/ring), and it also works with [Babashka](https://github.com/babashka/babashka), using its built-in http-kit server. It has full CLJS support as well, although it wouldn't be as plug-in as it is in CLJ simply due to the lack of Ring-like HTTP servers for front-end JS and back-end JS (unless maybe someone would want to create one?), but [Macchiato](https://github.com/macchiato-framework) framework should be pretty close, so I imagine it shouldn't be too much work to make Ruuter work there.

## Look ma', no hands!

Here's an example using http-kit:

```clojure
(ns myapp.core
  (:require [ruuter.core :as ruuter]
            [org.httpkit.server :as http]))

(def routes [{:path "/"
              :method :get
              :response {:status 200
                         :body "Hi there!"}}
             {:path "/hello/:who"
              :method :get
              :response (fn [req]
                          {:status 200
                           :body (str "Hello, " (:who (:params req)))})}])

(defn -main []
  (http/run-server #(ruuter/route routes %) {:port 8080}))
```

I consider the project pretty much done, in that I don't think I will add any new features to it unless someone comes up with a convincing argument as to why a given feature would be worth it, but I will of course maintain it, do quality of life updates, and so on. I really like creating software that does one thing and does it well, and doesn't need to become a feature bloat to justify its existence. It's also why I love the Clojure community so much, because it shares these same views.

