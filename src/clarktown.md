---
title: Clarktown
description: An extensible and modular zero-dependency Markdown parser written in Clojure for Clojure projects.
layout: page
---

An extensible and modular zero-dependency Markdown parser for projects written in Clojure.

- [Install](#install)
- [Usage](#usage)
- [Customization](#customization)
  - [Parsers](#parsers)
    - [Built-in parsers](#built-in-parsers)
    - [Creating renderers](#creating-renderers)
    - [Creating matchers](#creating-matchers)
    - [Creating parsers](#creating-parsers)
  - [Correctors](#correctors)
    - [Built-in correctors](#built-in-correctors) 
    - [Creating block separation correctors](#creating-block-separation-correctors)

## Install

### Leiningen/Boot

```
[com.github.askonomm/clarktown "2.0"]
```

### Clojure CLI/deps.edn

```
com.github.askonomm/clarktown {:mvn/version "2.0"}
```

## Usage

To get quickly up and running, run your Markdown with the `clarktown.core/render` function:

```clojure
(ns myapp.core
  (:require [clarktown.core :as clarktown]))

(clarktown/render "**Hello, world!**") ; => <p><strong>Hello, world!</strong></p>
```

## Customization

Clarktown is entirely customizable. You can create your own [parsers](#parsers) and [correctors](#correctors) to either entirely replace the ones used by default 
or simply add to the ones used by default thus enhancing the Markdown parsing capabilities to fit exactly _your_ needs.

You can get the default vector of parsers and map of correctors via:

- `clarktown.parsers/default-parsers`
- `clarktown.correctors/default-correctors`

To change which parsers and/or correctors Clarktown uses, simply pass the parsers and/or correctors to Clarktown like this:

```clojure
(clarktown/render "**Hello, world**" {:parsers []
                                      :correctors {}})
```

The additional parameter to `render` is a configuration map where you can specify (and thus overwrite) the vector of parsers Clarktown uses
as well as the map of correctors that Clarktown uses. Both are entirely optional and as such you specify both of them, one of them, or none of them.

### Parsers

...

#### Built-in parsers

- `clarktown.parsers/empty-block-parser`
- `clarktown.parsers/horizontal-line-block-parser`
- `clarktown.parsers/heading-block-parser`
- `clarktown.parsers/quote-block-parser`
- `clarktown.parsers/fenced-code-block-parser`
- `clarktown.parsers/indented-code-block-parser`
- `clarktown.parsers/list-block-parser`
- `clarktown.parsers/paragraph-block-parser`

All of these are bundled as `clarktown.parsers/default-parsers`.

#### Creating renderers

...

#### Creating matchers

A matcher is a function that takes in a block of Markdown and determines if it is something or if it isn't that something. Say you want to detect if a block is a code block or a quote block, well, you'd have a matcher for doing that.

An example matcher function that detects a fenced code block looks like this:

```clojure
(defn match?
  "Determines whether we're dealing with a code block."
  [block]
  (and (string/starts-with? block "```")
       (string/ends-with? block "```")))
```

Clarktown uses matchers to apply a certain set of [renderers](#renderers) to a Markdown block, because not all blocks are equal, and some require a different set and/or different order of renderers.

Feel free to [check out the built-in matchers](https://github.com/askonomm/clarktown/tree/master/src/clarktown/matchers) to get a better look at real-world usage of matchers.

#### Creating parsers

...

### Correctors

...

#### Built-in correctors

#### Creating block separation correctors

...