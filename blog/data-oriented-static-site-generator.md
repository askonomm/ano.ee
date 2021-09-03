---
title: Data oriented static site generator?!
date: 2021-09-10
---

A while ago I wrote a static site generator called [Lava](asad). Now, Lava is a quite common static site generator implementation I wrote in Java that takes in a bunch of Markdown files with YAML metadata and spits out HTML. The only thing "special" it has, that I thought was cool to implement, was that you could also create Handlebars templates as content files next to Markdown files, allowing you to be pretty creative and implement things like sitemaps and RSS feeds yourself, using the data available in the Handlebars templates.

The way it worked was pretty simple; let's say you wanted to create a `sitemap.xml` file that was dynamically generated with the available templating data, well, you'd simply create a `sitemap.xml.hbs` file and the generator itself would remove .hbs from the file and compile the end result into a file with that name, instead of creating a folder and putting a index.html in it like it would with Markdown files.

While this was cool in itself, it wasn't cool enough for me. Simply because the templating data available to the templates were static and there was no way I'd start implementing new stuff into the core generator constantly as I needed, bloating up the whole thing. _Lean, mean, generator machine_ is what I wanted. So this lead me to explore possible options on how to create something like that - and create something like that I did.

## Introducing Babe