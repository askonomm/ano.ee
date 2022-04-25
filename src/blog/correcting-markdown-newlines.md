---
title: Correcting Markdown: Newlines
date: 2022-04-25
---

Part of the upcoming [2.0 release of Clarktown](https://github.com/askonomm/clarktown/milestone/2) are Correctors. Correctors, like the name would suggest, correct inputted Markdown. They are the middlemen which the input goes through before Markdown gets passed to the Parsers, which then do the job of converting Markdown into HTML.

In the future there will probably be many different types of Correctors, but at the time of writing this there's only one type: **Block Separation Correctors**. These correctors ensure that there are empty newlines where need-be so that the Parsers get correct blocks, because in Clarktown everything is a block, separated by two newlines (`\n\n` or `\newline\newline` in Clojure). 

## The problem 

Take for example the following Markdown:

```markdown
This is some paragraph text.
# This is some heading.
```

Since there's only one `\newline` between these two lines, Clarktown will think of it as one block, and the block Matcher (which identifies a block) will start from the beginning, see regular text, and think the whole thing is just a paragraph, and will render HTML like this:

```html
<p>This is some paragraph text.
# This is some heading.</p>
```

Where instead what _should_ be the end result is this:

```html
<p>This is some paragraph text.</p>

<h1>This is some heading.</h1>
```

Now while I personally do not write Markdown like that and nicely always add two newlines between blocks myself, some users will not do that, and for them the end result will be broken.

## The solution

Solution to this problem is to create correctors. Essentially we'll be splitting the entire Markdown input into a vector of lines, and going over each line. Then we run the correctors over each of those lines and they will determine if a fix is needed or not. Should there be a `\newline` above or bottom of the current line? Perhaps both? A corrector will answer these questions.

The type of heading block that starts with a hashbang is called an ATX heading block, so let's create a function that determines whether we should have an extra `\newline` on top of the block by feeding it all the lines, the current line, and the current index, like this:

```clojure
(defn empty-line-above?
  [lines line index])
```

First let's make sure that this line is indeed a ATX heading block line:

```clojure
(clojure.string/starts-with? "#")
```

Then let's make sure that this is not the very first line, because if it is then there's no need to add anything above.

```clojure
(> index 0)
```

Finally the important bit, which is to check if an actual new `\newline` is required or not:

```clojure
(not (= (-> (nth lines (- index 1))
	    clojure.string/trim)
	""))
```

You see `clojure.string/trim` removes any newlines, and so if we check what are the contents of the line previous to the current line, we should then get a result which is an empty string.

And so our final `empty-line-above?` corrector would be:

```clojure
(defn empty-line-above?
  [lines line index]
  (and (clojure.string/starts-with? "#")
       (> index 0)
       (not (= (-> (nth lines (- index 1))
		   clojure.string/trim)
	       ""))))
```

> [There's a bit more to the corrector of a ATX heading block](https://github.com/askonomm/clarktown/blob/master/src/clarktown/correctors/atx_heading_block.clj), such as the `empty-line-below?` function as well as detecting if we're in a code block, because we do not want to correct anything inside of a code block, but this here is the gist of it.

## Bundling the correctors

Once we have a bunch of correctors, we don't want to manually integrate them, so we'd rather create a map, like this:

```clojure
(def block-separation-correctors
  {:empty-line-above? [...]
   :empty-line-below? [...])
```

The vectors of each will include references to functions like the one we created above (the `empty-line-above?` function).

And we'll use these by running them over each line in our inputted Markdown, like so:

```clojure
(let [lines (clojure.string/split-lines "our markdown goes here")
      above-correctors (:empty-line-above? block-separation-correctors)
      below-correctors (:empty-line-below? block-separation-correctors)]
  (->> lines
       (map-indexed
	 (fn [index line]
	   (let [add-newline-above? (some #(true? (% lines line index)) above-correctors)
		 add-newline-below? (some #(true? (% lines lien index)) below-correctors)]
	     (cond
	       (and add-newline-above?
		    (not add-newline-below?))
	       (str \newline line)

	       (and add-newline-below?
		    (not add-newline-above?))
	       (str line \newline)

	       (and add-newline-above?
		    add-newline-below?)
	       (str \newline line \newline)

	       :else line))))))
```

And this mostly concludes how the `\newline` Markdown corrections are done in [Clarktown](https://github.com/askonomm/clarktown). You can check more by reading the [engine.clj](https://github.com/askonomm/clarktown/blob/master/src/clarktown/engine.clj) file.
