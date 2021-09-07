---
title: Data oriented static site generator?!
date: 2021-09-07
status: public
---

Some time ago I wrote a static site generator called [Lava](https://github.com/askonomm/lava). Lava is a pretty common static site generator implementation I wrote in Java that takes in a bunch of Markdown files with YAML metadata and spits out HTML. The only thing "special" it has, that I thought was cool to implement, was that you could also create Handlebars templates as content files next to Markdown files, allowing you to be pretty creative and implement things like sitemaps and RSS feeds yourself, using the data available in the Handlebars templates which are all the content and so forth.
 
The way it worked was pretty simple; let's say you wanted to create a `sitemap.xml` file that was dynamically generated with the available templating data, well, you'd simply create a `sitemap.xml.hbs` file and the generator itself would remove .hbs from the file and compile the end result into a file with that name, instead of creating a folder and putting an index.html in it like it would with Markdown files.

While this was cool in itself, it wasn't cool enough for me. Simply because the templating data available to the templates were static and there was no way I'd start implementing new stuff into the core generator constantly as I needed, bloating up the whole thing. _Lean, mean, generator machine_ is what I wanted. So this lead me to explore possible options on how to create something like that - **and create something like that I did.**

## Introducing Babe

This brings me to [Babe](https://github.com/askonomm/babe). A static site generator just like Lava, which also compiles both Markdown files and template files with templating data, except that in the heart of it all is a simple JSON file where you can pass static data to be available in the templates - as well as dynamic data. An example JSON file looks like this:

```json
{
  "site": {
    "url": "https://bien.ee",
    "title": "Bien"
  },
  "data": [
    {
      "name": "posts",
      "folder": "blog",
      "sortBy": "date",
      "order": "desc"
    }
  ]
}
```

You see everything that goes into the `site` object is static information that is available from within the template as `{{ site.[key] }}`, whereas everything that goes into the `data` object is more special. What I'm telling Babe to do with that `data` object, is to create a template variable called `posts` which would contain an array of every content item in the folder `blog`, sorted by YAML metadata `date`, in descending order. And then I can use that in any template file like this:

```html
<div class="posts">
    {% for post in data.posts %}
        <h2>{{ post.title }}</h2>
        {{ post.entry }}
    {% endfor %}
</div>
```

And I can create as many dynamically created template variables as I want just like that. There are some things to note however, such as that this is pretty limited still, currently only allowing to map a variable to a folder of contents, but that would include also all the sub-folders of that folder and there is no way to specify depth. Also, if you don't want to map a folder, but a specific content item instead, you currently couldn't do that. I intend to implement these things and more in the upcoming versions.

You probably noticed, but Babe doesn't use Handlebars templating and instead uses [Selmer](https://github.com/yogthos/Selmer). Selmer allows a lot more advanced templating logic than Handlebars, which is why I went with it. Another thing to note is that while Lava is written in Java, Babe is written in Clojure, so if hackability is a requirement, you got to learn some Clojure for this one.

***

**Note**: I don't really know how to call this thing. Data oriented static site generator is the best I could come up with, and yes I do realise that technically everything is data oriented, but feel free to get in touch if you know a better way to call it or if you think I should just call it a regular static site generator instead.