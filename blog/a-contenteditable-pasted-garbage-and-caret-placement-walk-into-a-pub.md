---
title: A contentEditable, pasted garbage and caret placement walk into a pub
date: 2021-07-22
popular: yes
---

Pasted garbage says to `contentEditable`; "Hey! I'd really like to become part of you" and `contentEditable` says back; "Not so fast, you! First we got to rinse you down!". And thus begins a story of how to make `contentEditable` take in a good ol' paste, parse that paste for anything we might not want, put the result of that parsing into the right place in `contentEditable` and place the caret just after that paste. Sounds easy, right? Right.

The filthy default of contentEditable behaviour
-----------------------------------------------

By default, `contentEditable` takes in just about anything you'd like to give it. If you copy text from anywhere that also has mark-up and styles (like a Word document) and then paste it into the `contentEditable`, it would gladly take all that mark-up and styles as well. But this isn't a great user experience if you're building a [content editor like I am](https://github.com/askonomm/blocko), so the best solution is to parse that paste and remove anything you might not want - which in my case was to remove all styles and only allow certain mark-up.

Rinsing down the paste
----------------------

Alright so let's create a simple `contentEditable` that also listens to the Paste event. I'll be doing this in ClojureScript as it is my favourite language, using Reagent for the React goodness as this is a React app, but all of this also applies for good ol' regular JS and React.js.

```clojure
(defn contentEditable []
  [:div
   {:contentEditable true
    :on-paste #(on-paste! %)}])
```

Don't you just love it how little code you have to write to make a React component in ClojureScript? I sure do, and this is totally NOT (_wink wink_) my way of saying you should try ClojureScript. Anyway, let's create the `on-paste!` function as well.

```clojure
(defn on-paste! [event])
```

Oh shoot, it's empty! Yeah so, I wanted to stop here because little did I know, there's now a standard Clipboard API that you should use to get the pasted user content - but it comes with a gotcha - as soon as you try to use it, the browser will ask the user to give your page permission to read clipboard data, which I found not very user friendly for something as simple as being able to paste text into an input seeing as it wont ask that when you paste text into an input using the default behaviour, but anyway, ce'st la vie.

So, retrieving the pasted content with the Clipboard API would look like this:

```clojure
(defn on-paste! [event]
  (.then 
   (.readText 
    (.-clipboard js/navigator)) 
    (fn [clip]
      ;; `clip` contains the pasted content
    ))))
```

Now the `clip` is the actual paste, along with all of its horrible formatting and styles, so I went along and used the [sanitize-html](https://www.npmjs.com/package/sanitize-html) NPM package to clean it right up (I do want to build a native Clojure version of this at one point, but for now this works just swell!). So, with that package, the `on-paste!` function would look like this:

```clojure
(defn on-paste! [event]
  (.then 
   (.readText 
    (.-clipboard js/navigator)) 
    (fn [clip]
      (let [pasted-content (parse-html clip)]
        ;; do something with `pasted-content` here
      )))))
```

And the `parse-html` function would look like this:

```clojure
(ns your-app
  (:require ["sanitize-html" :as sanitize-html]))

(defn parse-html [html]
  (sanitize-html
   html
   (clj->js
    {:allowedTags ["b" "strong" "i" "em" "a" "u"]
     :allowedAttributes {"a" ["href"]}})))
```

Which, as I'm sure you can tell, only allows the tags `b`, `strong`, `i`, `em`, `a`, `u` and would only allow attributes on the `a` tag and only if that attribute is `href`. Pretty cool right? I sure think so.

Putting the paste in the right place
------------------------------------

Woah! That rhymed! Maybe I could have a career in hip hop after all haha! Right, so now that we have the paste and we've successfully cleaned it from any garbage it might have, we have to put that paste somehow into our `contentEditable`.

How do we do that? Do we simply insert it into the DOM element? That's not very React-y now is it. What if we create a local state for the content and just modify that? That sounds a lot better, actually. Let's do just that by going back to our React component and changing it to look like this:

```clojure
(ns your-app
  (:require [reagent.core :as r]))
  
(defn contentEditable []
  (let [content (r/atom "")]
    (fn []
      [:div
       {:contentEditable true
        :on-paste #(on-paste! content %)
        :on-input #(reset! content (.-innerHTML (.-target %)))
        :dangerouslySetInnerHTML {:__html @content}}])))
```

As you can see, we create a Reagent atom and set it as an empty string, which we then dereference into the `contentEditable` content using the `:dangerouslySetInnerHTML` attribute. On every change to the content (the `:on-input` event), we update the `content` atom so that it is always up to date with what is actually inside the `contentEditable`, and finally notice the `on-paste!` call - we now pass the `content` along to it as well, so that the `on-paste!` function would be aware of what is the current content.

So now all we need to do to paste the content into the right place, is to change the `on-paste!` function to be aware of where your caret was when the paste happened and insert the paste there. The `on-paste!` function will then look like this:

```clojure
(defn on-paste! [content event]
  (.then 
   (.readText 
    (.-clipboard js/navigator)) 
    (fn [clip]
      (let [pasted-content (parse-html clip)
            selection (.getSelection js/window)
            offset (.-anchorOffset selection)
            new-content (string->string @content pasted-content offset)]
        (reset! content new-content))))))
```

So check this out, we get the current selection via `(.getSelection js/window)` which then allows us to get the caret offset using `(.-anchorOffset selection)`, and that offset is key! That's how many index-based characters from the beginning of the text your caret was when you made the paste, and so that's also where we need to put the pasted content. I made a helper function called `string->string` for exactly that, and it looks like this:

```clojure
(defn string->string [string inserted-string index]
  (let [split-beginning (subs string 0 index)
        split-end (subs string index)]
    (str split-beginning inserted-string split-end)))
```

Which takes the original content as `string`, then the content you want to insert into it as `inserted-string` and finally the `index` at which you want to insert that new content. It would then return the final string.

And as you saw in the end of the `on-paste!` function we called `reset!`, which basically just overwrites the `content` atom with the new content, prompting a re-render of the component, and thus now the `contentEditable` has the pasted content with all of the garbage removed in the right place as desired.

Why you got to Caret me like that?
----------------------------------

One thing you may have noticed is that when pasting content the caret itself will end up in the wrong place - or rather the right place, which is to say that the caret will stay where it was, but you probably expect it to end up just AFTER the pasted content, as that's how it usually works. This happens because while the content of the `contentEditable` changed, the caret position did not, so we have to make it change ourselves.

Thankfully this is easier than one would think, we just have to take the current caret offset and add to it the number of characters that the pasted content has. Let's say that your caret was at offset 10, and the pasted string has a length of 7, then naturally we want 10 + 7, which means that the caret will be the 18th character.

To do this, we have to turn our component into a class component, because that's how you get lifecycle events in Reagent. Why? Because we need to able to place the caret AFTER the component has rendered, not before, as we won't yet have the updated text in the `contentEditable` otherwise and caret placement will throw an error for index being out of bounds. So, with that in mind, the updated component would look like this:

```clojure
(ns your-app
  (:require [reagent.core :as r]))
  
(defn contentEditable []
  (let [ref (r/atom nil)
        content (r/atom "")
        caret-location (r/atom nil)]
    (r/create-class
     {:component-did-update
      #(place-caret! ref content caret-location)
      :reagent-render
      (fn []
          [:div
           {:contentEditable true
            :ref #(fn [el] (reset! ref el))
            :on-paste #(on-paste! content caret-location %)
            :on-input #(reset! content (.-innerHTML (.-target %)))
            :dangerouslySetInnerHTML {:__html @content}}])})))
```

Aye! You can see that we're also passing to the `on-paste!` function a new state variable called `caret-location`, which by default will be `nil`, and we'll use that to know where to put the caret with our `place-caret!` function you can see is being called from within the `:component-did-update` lifecycle event. We also create a new state called `ref`, which will hold the actual DOM element of our `contentEditable` so that we know in what element do we focus our cursor in.

Our updated `on-paste!` function should look like this now:

```clojure
(defn on-paste! [content caret-location event]
  (.then 
   (.readText 
    (.-clipboard js/navigator)) 
    (fn [clip]
      (let [pasted-content (parse-html clip)
            selection (.getSelection js/window)
            offset (.-anchorOffset selection)
            new-content (string->string @content pasted-content offset)]
        (reset! content new-content)
        (reset! caret-location (+ offset (count pasted-content)))))))
```

So now the `caret-location` will hold a value that is whatever the offset was when you pasted + the length of the pasted content, so it should now appear right after the paste. Well, not yet - we still have to create our `place-caret!` function, so let's go ahead create it looking like this:

```clojure
(defn place-caret! [ref content caret-location]
  (when (and (not (nil? @caret-location))
             (>= (count @content) @caret-location)
             (first (.-childNodes @ref)))
    (let [selection (.getSelection js/window)
          range (.createRange js/document)]
      (.setStart range (first (.-childNodes @ref)) @caret-location)
      (.collapse range true)
      (.removeAllRanges selection)
      (.addRange selection range)
      (.focus @ref)
      (reset! caret-location nil))))
```

What this function does is that it takes a `ref`, which is the DOM element e.g our `contentEditable`, the `content` and `caret-location` states and it will then make sure that the `content` is not longer than `caret-location` (because if it is, we won't be able to change caret location because the index is out of bounds) and we check that the `caret-location` is not `nil`, because it's by default `nil`, so that we could only invoke caret placement when we want to, which in our case is during paste.

After all is good, we get the current selection, create a new range, set the start of the range to be our `caret-location`, collapse that range, remove all existing ranges from `selection` and add our new one instead, and then we'll focus on the `ref` element and reset the `caret-location` state.