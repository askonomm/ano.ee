---
title: Series of PNG's and no Canvas that fits
status: public
date: 2021-06-06
---

I was working on a client website where I needed to fix an animation that would respond to a user's scroll event. The client had it already working as a WebM video file that was hooked to a scroll event to either continue or go back based on the scroll position, but for some reason, it would have terrible lag which was the thing that needed to be fixed.
 
```javascript
var video = document.getElementById('hero-animation');
video.pause();

window.onscroll = function(){
    video.pause();
};

setInterval(function(){
    video.currentTime = window.pageYOffset / 400;
}, 40);
```

As you can see, it gets the `video` element and then pauses it on page load to stop autoplay, as well as during scroll to stop autoplay once more, and then it updates the `video.currentTime` with the result of `window.pageYOffset / 400` every 40 milliseconds.

This code along with the WebM video resulted in this animation:

<iframe src="https://player.vimeo.com/video/564445637?app_id=122963" width="260" height="240" frameborder="0" allow="autoplay; fullscreen; picture-in-picture" allowfullscreen title="1.mp4"></iframe>

There is terrible lag. Out of the 67 frames that the video has, it only seeks between maybe 4-8 frames, resulting in a major lag motion. Now I haven't had any experience building anything like this beforehand, so I had to do some research as to why the video would not change seek position smoothly between all the 67 frames.

## It's probably the video

I found out after a bit of googling that the most likely culprit isn't the code, but it's the video itself. Turns out that to make the video able to seek more frames, one needs to configure keyframe spacing accordingly - this is because whenever you update the `video.currentTime`, it makes the browser's video decoder search for the closest keyframe to the time position that you updated the `video.currentTime` with. And thus, if the nearest keyframe is far away, the video will lag terribly.

The solution to this problem is to re-encode the video using a tool such as `ffmpeg`. I tried many times, but ultimately gave up because the video format I needed was WebM, due to its ability to have transparent backgrounds which was a must for my use-case, but also because even when using mp4, I saw no marketable difference. Perhaps (probably) I did something wrong in the encoding of the video, ffmpeg is after all quite complex to someone who has never worked with it.

## Drawing the frames to canvas image by image

Okay, so if I couldn't make <code>ffmpeg</code> work in my favor, I figured I could go the Apple route and have the 67 frames as images drawn on <code>canvas</code> instead. Heavily consulting [the CSS-Tricks article](https://css-tricks.com/lets-make-one-of-those-fancy-scrolling-animations-used-on-apple-product-pages/) on how to make such a thing, I resulted in this animation:

<iframe src="https://player.vimeo.com/video/564445675?app_id=122963" width="394" height="360" frameborder="0" allow="autoplay; fullscreen; picture-in-picture" allowfullscreen="" title="2.mp4"></iframe>

While indeed the animation was a lot smoother, a new problem became immediately evident: the image is transparent, and the technique this animation uses is drawing one image over another which means it would not work at all because the requirement I had was that it needs to be transparent.

Alright, but surely if I clear the canvas before drawing a new image it would work, right? All I'd have to do is change the `updateImage` function like this:

```javascript
const updateImage = index => {
	// Clear canvas
	context.clearRect(0, 0, canvas.width, canvas.height);

	// Everything else stays the same
	img.src = currentFrame(index);
	context.drawImage(img, 0, 0);
}
```

This fixed the problem of images being drawn on top of each other, but it created a new one: blinking. You see clearing the entire canvas on each frame change is a costly operation and results in a sort of middle frame that is blank being drawn:

<iframe src="https://player.vimeo.com/video/564445724?app_id=122963" width="262" height="240" frameborder="0" allow="autoplay; fullscreen; picture-in-picture" allowfullscreen="" title="3.mp4"></iframe>

Couldn't get the video re-encoding to work, couldn't get <code>canvas</code> to work - so what is there left to try? Well, I figured I try a potential solution that in my head felt like it should not work due to the performance drawback being even bigger than clearing the canvas on each frame, and yet - it did work.


## Sometimes the best solution is the dumb one

What I did was I simply added all the frames as `img`'s inside a div called `hero-image-series`, with all the images (except the first one) being set to `display: none;` by default. Then based on the scroll position I would go over each `img` and add a class `visible` to the frame I currently want to show:

```javascript
const html = document.documentElement;
const frameCount = 67;

const currentFrame = index => (
  `https://website.com/images/hero/${index.toString().padStart(5, '0')}.png`
);

const loadImages = () => {
  for (let i = 1; i < frameCount; i++) {
     const img = new Image();
     img.src = currentFrame(i);
     document.querySelector('.hero-image-series').insertAdjacentElement('beforeend', img);
  }

  document.querySelector('.hero-image-series img').classList.add('visible');
}

loadImages();

window.addEventListener('scroll', () => {
  const scrollTop = html.scrollTop;
  const scrollFraction = scrollTop / 400;
  const frameIndex = Math.min(frameCount - 1, Math.floor(scrollFraction * frameCount));

  requestAnimationFrame(() => {
     const img = document.querySelector('.hero-image-series img[src="' + currentFrame(frameIndex + 1) + '"]');
     const visibleImg = document.querySelector('.hero-image-series img.visible img');

     if (img && img !== visibleImg) {
        document.querySelector('.hero-image-series img.visible').classList.remove('visible');
        img.classList.add('visible');
     }
  });
});
``` 


And it works remarkably well:

<iframe src="https://player.vimeo.com/video/564445763?app_id=122963" width="394" height="360" frameborder="0" allow="autoplay; fullscreen; picture-in-picture" allowfullscreen="" title="4.mp4"></iframe>

I never thought that re-painting the DOM on each frame change would be more performant than clearing the <code>canvas</code>, but turns out that at least, in this case, it is. Goes to show that sometimes the best solution, is the dumb one.