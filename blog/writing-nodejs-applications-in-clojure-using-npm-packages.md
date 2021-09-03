---
title: Writing Node.js applications in Clojure and using NPM packages
date: 2021-06-26
---

After spending almost 3 years writing Clojure(Script) for my client [GreenPowerMonitor](https://greenpowermonitor.com), I feel very comfortable with it and find the overall visual aesthetic of the language unlike anything else I've ever seen. This, of course, is subjective to each and every person, but to me writing Clojure is like writing poetry - graceful, elegant, and yet effective at carrying meaning.

I'm a generalist and write in a plethora of languages overall, but if someone asked me what language crossed my fingers most often, I'd have to say that language is JavaScript. Can't get around it. From small fixes in WordPress themes to React.js applications, pretty much everywhere I wander JavaScript finds me. But, while I have nothing against writing JavaScript, if I could somehow just write Clojure instead, well ... that would be really awesome.

Meet ClojureScript
------------------

[Like its website says](https://clojurescript.org/), ClojureScript is a Clojure compiler that targets JavaScript. With it, you're able to write Clojure and output JavaScript files. With seamless JavaScript interop, you get the best of both worlds. Just like Clojure runs on the JVM so you get both Clojure and Java superpowers, with ClojureScript you get both Clojure and JavaScript superpowers.

There are many well-known packages built for it, such as [Reagent](https://github.com/reagent-project/reagent) that make it easy to write React.js applications in Clojure, and if you couple that with [Re-frame](https://github.com/day8/re-frame) state management, you're basically unstoppable in the front-end world, and able to write software that will outlast generations of JavaScript. Sort-of.

You see incorporating third-party JavaScript libraries in ClojureScript applications has always been something of a pain. Historically you had to resort to including script tags on your HTML manually as well as creating the externs for them manually so that accessing JavaScript objects from Clojure would not result in a complaint in your console that said "Cannot infer target type". Suffice it to say, this was a less than ideal workflow.

Meet Shadow-CLJS
----------------

[Shadow-CLJS](https://github.com/thheller/shadow-cljs) is a ClojureScript compiler that makes all of this compilation stuff super easy and even has the ability to set different targets which makes it easy for you to write Clojure for the browser, Clojure for Node.js (both application and module!), and even Clojure for React Native. Pretty much anything that runs JavaScript can be written instead in Clojure using Shadow-CLJS.

But the best of Shadow-CLJS? **You can use any NPM library in your Clojure code using Shadow-CLJS!** That's right, you now not only have access to any ClojureScript library written out there but also every NPM one. This, to me at least, entirely changes the game. Why would I want to write JavaScript if I can get away with less code that's more concise, and to me at least, aesthetically more pleasing? I wouldn't! Those tiny JavaScript fixes and things in WordPress themes? React.js apps? React Native? And even Node.js? Yeah, I'd much prefer to write all of that in Clojure.

Creating a basic Node.js application
------------------------------------

Okay enough with the chit-chat, like the title of the post says, let's create a simple barebones Node.js application in ClojureScript using Shadow-CLJS. First and foremost, run `npx create-cljs-project {project-name}` and it will set up everything for you in the `{project-name}` folder.  Once you've done that, make sure to open up your `shadow-cljs.edn` file and see that the `:target` is set to `:node-script` (this creates a Node.js application). Your `shadow-cljs.edn` file should look similar to this:

    {:source-paths
     ["src"]
    
     :dependencies []
    
     :builds
     {:app
      {:main project-name.core/main
       :target :node-script
       :compiler-options {:infer-externs :auto}
       :output-to "project-name.js"}}}

What this does is that it will run the `main` function in `project-name.core` namespace and outputs the result in `project-name.js` file, which we can then run with Node. Now, let's write the most basic possible Node.js app, before setting up Express.js. Open up your `src/{project-name}/core.cljs` file and make it look like this:

    (ns project-name.core)
    
    (defn main [& args]
      (js/console.log "Hi there!"))

And run it using `npx shadow-cljs watch app`, which launches the watcher. Now you can open a new tab in your console or just a new console alongside that process, and run `node project-name.js` and it should print "Hi there!" into your console, and if it does congrats! You just made a Node.js application in Clojure! If you want to see what the final code would look like simply run `npx shadow-cljs release app` and check out the `project-name.js` in your root directory (you can configure this in your `shadow-cljs.edn` where the `:output-to` is.

Creating an Express.js application
----------------------------------

You know how we install NPM packages right? Yup, exactly. It's that simple. Simply run `npm install express` and you're done. Now at the top of your `core.cljs` file, simply import the package you just installed, like this:

    (ns project-name.core
      (:require ["express" :as express]))

Do you see how simple that is?!? Every NPM package needs to be imported using quotation marks for the package name and then assigning that with `:as` into a ClojureScript variable/object. You can [read more specific information about the imports here](https://shadow-cljs.github.io/docs/UsersGuide.html#_using_npm_packages). Now let's make something by actually using Express.js!

    (ns bloggo.core
      (:require ["express" :as express]))
    
    ; This atom will hold our server instance, for a simple
    ; reason of being able to destroy it later.
    (defonce server (atom nil))
    
    (defn routes 
      "Pretty self-explanatory, but for more details I would
      consult the express documentation that you can find 
      from https://expressjs.com/en/guide/routing.html."
      [^js app]
      (.get app "/" (fn [req res] (.send res "Hi there!"))
    
    (defn start-server 
      "Configures the use of sessions as well as routes and 
      then starts the server on the port specified in ENV or 
      if not found, then it tries the port 3000."
      []
      (let [app (express)
            prod? (= (.get app "env") "production")
            port (if (nil? (.-PORT (.-env js/process)))
                   3000
                   (int (.-PORT (.-env js/process))))]
        (when prod? (.set app "trust proxy" 1))
        (.use app "/assets" (.static express "assets"))
        (routes app)
        (.listen app port (fn [] (prn "Listening ...")))))
    
    (defn start! 
      "Starts the server, as well as updates the `server` atom 
      with the server instance so that we could later stop it."
      []
      (reset! server (start-server)))
    
    (defn stop! 
      "Closes the `server` connection as well as sets the `server`
      atom to `nil`."
      []
      (.close @server)
      (reset! server nil))
    
    (defn main 
      "Main entrypoint to the app."
      [& args]
      (start!))

This is a very basic Express.js application that has only one route and displays "Hi there!" when visited said route. Now modify your `shadow-cljs.edn` to enable hot reloading by calling `stop!` and `start!` accordingly on every reload. Your `shadow-cljs.edn` should look then like this:

    {:source-paths
     ["src"]
    
     :dependencies []
    
     :builds
     {:app
      {:main project-name.core/main
       :target :node-script
       :compiler-options {:infer-externs :auto}
       :output-to "project-name.js"
       :devtools
       {:after-load project-name.core/start!
        :before-load project-name.core/stop!}}}}

Now when you run the app with `npx shadow-cljs watch app` and alongside it run `node project-name.js`, navigate to `localhost:3000` and you should be greeted with "Hi there!" coming straight from Express.js. And you can use any other NPM package just like we used Express here as well, which is really really cool if you ask me.