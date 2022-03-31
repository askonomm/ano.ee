---
title: How I created a Markdown parser
description: When tokenization proves to be too difficult, regex can come handy in solving complex problems with ease.
date: 2021-11-18
---

Let me begin this post by saying that I'm no genius, and I've peeked under the hood of a few other Markdown parsers, all of which with complexity that leaves me quite stunned. So when I set out to create my very own Markdown parser, I knew I had to figure out a simpler approach, and I think I did, even if it doesn't include a full feature-set of a mainstream Markdown parser (yet).

## Breaking everything into blocks

When you look at Markdown content, it quickly becomes evident that everything sits within a block of sorts, separated by line breaks. Paragraphs, headings, blockquotes, lists, you name it - it is separated by 2 line breaks. A quick win is to just break everything into an array of blocks, so let's do just that.

```javascript
const content = "... markdown content goes here";
const blocks = content.split(/\n\n/);
```

Hooray, we have a bunch of blocks. Now what? Well, I reasoned that since each block can only be of one particular type, like for example a paragraph or a list, if we break everything into blocks, we can then evaluate what type each block is and then apply parsing and rendering accordingly. 

Let's start by mapping over our blocks, detecting the simplest block I can come up with, the horizontal line block, and parse that:

```javascript
const blocks = content.split(/\n\n/);

const parsedBlocks = blocks.map((block) => {
  // Horizontal line block?
  if (block === "***") {
    return `<hr>`;
  }

  return block;
});
```

Pretty simple, right? But it will quickly get very complex to manage this way, so I took it a step further and abstracted this parsing into what I call "parsers" that have two distinct things: **matchers and renderers**. Matchers try to identify what block we are dealing with, and renderers will parse and render that block if the matcher returned `true`. Each block can have one matcher, but any number of renderers (you'll see soon enough why not just one). A matcher and renderers are what make up a parser, and parsers are what make up this machine. 

This approach has a few things going for it: firstly, it's easier to manage. Each block has its own set of functions that deal with it, thus breaking the whole thing down to smaller, more manageable pieces. Secondly, it makes it modular, which means that I can allow users to pick and choose exactly the feature-set they want, and they can easily add new features to it if they wished to do so.

## Making it all modular

Let's start by refactoring the existing functionality we already have into a set of functions, more specifically three functions:

* A function that determines whether we're dealing with a horizontal line block
* A function that renders the horizontal line block
* A function that takes in Markdown content and returns the parsed result based on available parsers

```javascript
// Detect horizontal line block
function isHorizontalLineBlock(block) {
  return block === "***";
}

// Render horizontal line block
function horizontalLineBlock(block) {
  return `<hr>`;
}

// Compose an array of parsers
const parsers = [{
  matcher: isHorizontalLineBlock,
  renderers: [horizontalLineBlock]
}];

// And finally, our parser itself
function markdownToHTML(markdown) {
  // Create blocks
  const blocks = content.split(/\n\n/);

  // Parse blocks
  const parsedBlocks = blocks.map((block) => {
    // Let's find a parser that has a matcher that matches
    const parser = parsers.find((parser) => parser.matcher(block));

    // If match was found, let's run our renderers over `block`
    if (parser) {
      for (const renderer of match.renderers) {
        block = renderer(block);
      }
    }

    return block;
  });

  // And at last, join the blocks together for one big block.
  return parsedBlocks.join("");
}
```

And with this, we now have a functioning Markdown parser that can be used like this:

```javascript
markdownToHTML("***"); // >> "<hr>"
```

That being said, the only thing it can turn into HTML is a horizontal line block, so it's not all that useful. Well, alright, let's create another parser!

## Parsing headings

Headings in Markdown are created by using octothorp characters (#). One octothorp means `<h1>`, two means `<h2>` and so on. Let's start by creating a function that would determine if we're dealing with a heading block:

```javascript
function isHeadingBlock(block) {
  return block.startsWith("#");
}
```

And then the star of the show, our renderer:

```javascript
function headingBlock(block) {
  // Makes sure we just have a single line to deal with
  const singleLineBlock = block.replaceAll("\n", "").trim();

  // Gets the octothorp part of the string, because the actual 
  // heading is separated by a space from octothorps
  const sizeIndicators = singleLineBlock.split(" ")[0].trim();

  // Our heading size is equivalent to the amount of octothorps
  const size = sizeIndicators.length;

  // Our heading itself is everything except the octothorps.
  const value = singleLineBlock.split(" ").slice(1).join(" ").trim();

  return `<h${size}>${value}</h${size}>`;
}
```

Now, all we have to do is plug it into our `parsers` array to sit alongside our horizontal line parser, like so:

```javascript
// Compose an array of parsers
const parsers = [{
  matcher: isHorizontalLineBlock,
  renderers: [horizontalLineBlock]
}, {
  matcher: isHeadingBlock,
  renderers: [headingBlock]
}];
```

And we can create headings like this:

```javascript
markdownToHTML(`# Heading goes here`); // > "<h1>Heading goes here</h1>"
```

## Inline renderers

I told you I would come back to the reason why I wanted to have multiple renderers, and inline renderers are why. Some of the renderers I make I also want to re-use between blocks. 

For example, let's say I have a bold text renderer, well I'd like to render text bold in several places, like headings, paragraphs, you name it, and because when a matcher returns `true` we run the block through the entire array of renderers, we can easily do this. 

Let's create a renderer for bold text:

```javascript
function bold(block) {
  const matches = block.match(/\*\*.*?\*\*/g);

  if (matches) {
    for (const match of matches) {
      const value = match.substring(2, match.length - 2);
      const replacement = `<strong>${value}</strong>`;

      block = block.replace(match, replacement);
    }
  }

  return block;
}
```

And let's create another one for italic text:

```javascript
function italic(block) {
  const matches = block.match(/_.*?_/g);

  if (matches) {
    for (const match of matches) {
      const value = match.substring(1, match.length - 1);
      const replacement = `<em>${value}</em>`;

      block = block.replace(match, replacement);
    }
  }

  return block;
}
```

Now if we have a heading block and we also want it to be able to render bold and italic text, we simply add it to the list of renderers:

```javascript
// Compose an array of parsers
const parsers = [{
  matcher: isHorizontalLineBlock,
  renderers: [horizontalLineBlock]
}, {
  matcher: isHeadingBlock,
  renderers: [bold, italic, headingBlock]
}];
```

## Without further ado, Marky

I think this is a pretty simple approach to creating a Markdown parser, one that relies heavily on regex, but one that is entirely modular and easy to develop and extend if need be. This very blog post you read right now is rendered using my Markdown parser [Marky](https://github.com/askonomm/marky), which is a Markdown parser written in TypeScript, available for [Deno](https://deno.land/) and as ESM, runs both on the back-end and front-end, and features most of the Markdown spec built-in and is in active development.

I've extended it to be able to highlight code blocks as well using [highlight.js](https://highlightjs.org/) for this very blog, without having it baked into Marky itself, illustrating perfectly how nice it is to have a modular Markdown parser. Anyway, I'm pretty happy with the result, and hey, I may not be a genius, but I created a freaking Markdown parser am I right? 😎