---
title: 2021, year in review
description: 2 countries, 7 open source projects, and a whole lot of fun.
date: 2021-12-28
status: public
---

Phew, what a year, huh? I thought what better way to end it, but write up a nice review of the things I was up to in the past year as sort of a tl;dr for myself to look back on, so here goes. 

## The great unknown

I spent the first three months of the year in Lisbon, Portugal, working remotely for [GreenPowerMonitor](https://greenpowermonitor.com), as I had done for almost 3 years at that point. We built some really cool stuff for the renewable energy sector and I still look back to those days every now and again and think; "Cool!". In March however, I quit my job as it had served its purpose for me and I was ready for something else, whatever that else was, since I had nothing what-so-ever lined up. 

My colleagues were great, and I sometimes still miss them. But, I'm hoping to go to Barcelona soon, which is where most GPM colleagues live so maybe I can hit them up for a beer or something. Would be cool.

Then in the end of March I set out to Buenos Aires, Argentina, which was my first ever trip out of the European Union (okay, I've been to Andorra before, but that doesn't count). My vacationing ended after a month or so when I realized that savings apparently disappear a lot quicker when you don't have a home and live out of AirBnBs, and have all sorts of magic expenses that come with nomad-ism.

And so I started doing some consulting. I started doing work for some digital agencies in Estonia, and for a start-up in U.S, and it was decent and put food on the table, but didn't really fulfill me, so I was looking for another outlet.

## Fulfillment from building cool shit

I started working on open source stuff. A lot. I wrote a basic, proof-of-concept block editor in ClojureScript called [blocko](https://github.com/askonomm/blocko), A static site generator in Java called [Lava](https://github.com/askonomm/lava) (just to prove to myself that I can write Java) and a successor to Lava called [Babe](https://github.com/askonomm/babe) which I wrote in Clojure. 

Open source work is something I immensely enjoy. It reminds me how this whole development thing got started for me, out of curiosity for trying to figure things out and build something out of keypresses and sheer stubborness. And that was like, 15 years ago now, holy crap.

## Coherent, vivid, Fluent

Consulting is fun and all, but I really wanted to have some stability in my otherwise unstable life, which is why I cast out a net to the vast interworlds to catch some cool gig where I could build cool stuff with cool people and do it day in, day out, for a while. After a bit, I found [Fluent](https://fluent.to), a Swedish-based start-up, where I am writing Clojure/ClojureScript since September.

The stuff we work on there is quite complex and makes me feel dumb very often, which I suppose is good in that I'll probably come away from that job a better developer. The colleagues are great, the work is interesting and, most importantly, the working environment is very relaxed and humane, which is fucking amazing. I repeat, **fucking amazing**.

## The ever-growing addiction for building cool shit

While at [Fluent](https://fluent.to), I continued building open source projects on my free time. I built a zero-dependency HTTP router library for Clojure projects called [Ruuter](https://github.com/askonomm/ruuter), out of sheer frustration towards every existing HTTP router needing so many dependencies and moving parts. I built a CLI password manager called [Shh](https://github.com/askonomm/shh) in Clojure, and I wrote an extensible, modular Markdown parser for ESM and Deno.js projects called [Marky](https://github.com/askonomm/marky) in TypeScript.

Now while at this point my static site generator which I hold very dear to me worked great, I felt a sense of unhappiness with its binary size (GraalVM compiled JVM code ends up being around 20mb) and speed, while decent, is nothing compared to something like C, or Rust. And so I killed two birds with one stone, I learnt Rust and built the third iteration of my static site generated called [Oinky](https://oinky.io). 

It's mostly done and works (this site is created with it, and Oinky's site as well), but I'm still working on writing documentation, finishing up the website, and writing actual unit and integration tests for it, so I'd consider it a beta. It's very cool however, so [please go check it out](https://github.com/askonomm/oinky) if you're bored. Oh and feel free to insult my Rust-fu, so I could learn to do better!

## To conclude

All-in-all, not a bad year. I built a total of 7 open source projects, all of which I'm pretty happy with. I joined an awesome start-up where I continue to learn and become better everyday, in how I communicate, how I learn, and most importantly, how to build cool shit. 

I'm still in Argentina and any attempt at making a plan for something different is quite hard with the current state of the world and all, but maybe I'll be back in Europe next year, maybe not, who knows. One thing I do know for certain is I want to keep building awesome things, working with awesome people, and above all else - have fun. Life's too short to not have fun.