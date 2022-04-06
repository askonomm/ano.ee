---
title: Clarktown: A modular and extensible Markdown parser for Clojure
date: 2022-04-06
---

Been a bit busy for the last 3 or so months, but I finally dusted off a project I started 4 months ago and took it to the finish line. Well, "finish" line. It's not _done-done_, but it's out and usable, and that project is [Clarktown](https://github.com/askonomm/clarktown).

I've been wanting a modular Markdown parser myself for a while, and while [markdown-clj](https://github.com/yogthos/markdown-clj) is great, it has a number of bugs and the code base is a bit too scary for me to try to fix some of them, so I went the easier path and created my own, though keep in mind that markdown-clj has a superior feature-set and is a lot more battle-tested than Clarktown.

Do note however that it is not fully [CommonMark spec](https://spec.commonmark.org/) compliant and that **it is not** my goal. Instead I'll be adding features I need or that people request, and let it grow organically towards that.
