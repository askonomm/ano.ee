---
title: Browsers decode images differently
date: 2021-06-19
status: public
popular: yes
---

[In my last post](https://bien.ee/blog/series-of-pngs-and-no-canvas-that-fits/), I wrote about the HTML5 Canvas element and how sometimes just re-painting the DOM is more performant than using the HTML5 Canvas, at least for that specific use case. Following that topic, I'd like to put down some thoughts about how browsers decode images - and how they do it differently, which can make things a bit tricky for you if you want to deliver the same user experience for every user of your application.

So what does this mean exactly? Well, let's say that you have a single <code>img</code> tag on your web page, but you update the <code>src</code> attribute of it via JavaScript, and you do this often enough to trigger <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=705826">this bug</a> in Firefox. You can easily trigger it if you hook a scroll event to switching the <code>src</code> attribute so that on each scroll the image source updates, which should work just fine on Chrome, but on Firefox will start blinking.

Why does it start blinking? Well, it has everything to do with image decoding. The reason that it blinks on Firefox is that the image hasn't yet decoded when your scroll event is being triggered, but you are already attempting to display it - hence the blink. <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=705826#c43">There's a pretty easy solution for this that I also wrote about on the bug report</a> but the gist of the matter is that the <code><a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLImageElement/decode">HTMLImageElement</a></code><a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLImageElement/decode"> has a Promise called </a><code><a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLImageElement/decode">decode</a></code> and that you should not replace the <code>src</code> attribute until the decode finishes, which you can do like this:

```javascript
const imgUrl = 'yournewimage.png'; // your new image
const img = new Image(); // create temporary image

img.src = imgUrl; // add your new image as src on the temporary image

img.decode().then(() => { // wait until temporary image is decoded
    document.querySelector('img').src = imgUrl; // replace your actual element now
});
```

You see because in Firefox once the decode happens, even if it happens on an image element other than the one you are updating, the decoded result of that image is cached, and with it, the bug resolved.

## So I should always listen to the `decode` promise, right?

Technically yes, <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLImageElement/decode">the MDN recommends that</a> to know when it is safe to add the image to DOM, but what happens in Chrome with this code? Well, turns out it slows to a crawl and you're better off not using it. Now I don't think Chrome has implemented this feature in any other way from Firefox, except that for some unbeknownst reason it is a lot slower, but I do think that the two browsers decode images in a different way.

While in Firefox you will see an artifact in the form of a blink when the decode is taking place, I think in Chrome you'll just not see an updated image until that image has decoded, thus you don't see a blink and everything _feels_ smoother, even if it is probably just the same. I tried to find more information on the differences but was unsuccessful, so if you do know something please get in touch. For now, without knowing more, my best recommendation is to in such a case simply write one implementation targeting Firefox and the other Chrome, like this:

```javascript
const firefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
const imgUrl = 'yournewimage.png';
const img = new Image();

img.src = imgUrl;

if (firefox) {
   img.decode().then(() => {
      document.querySelector('img').src = imgUrl;
   });
} else {
   document.querySelector('img').src = imgUrl;
}
```

In Firefox we wait for the `decode` Promise to tell us when we can safely update the image `src` attribute, according to MDN spec. Otherwise, we'll just update the `src` regardless of waiting for the decode to happen or not. 

And that's how I unified the experience across Firefox and Chrome with this particular issue. It's actually funny because just recently I remember thinking that browsers had come such a long way in the past 10 years that if you write something in one it always works in the others. Well, _almost always_.