---
title: Perfectly perfect development environment
date: 2021-10-20
status: public
---

I'm sure you heard about [the new Macbooks that came out](https://www.apple.com/macbook-pro-14-and-16/). You can now get up to 64GB of RAM and some crazy 10-core CPU and a 32-core GPU. The thing is ridiculous, and so is its price, sitting at around ~$3500. Sounds like an ideal workhorse, and I sure would need one. Thing is, I recently joined a new company and in anticipation bought an entry-level M1 Macbook Pro, to upgrade from my old 2016 Intel-based one, thinking that it would be more than enough given the reviews of it.

What I didn't consider however was that I might be working on a large-scale app. And I did. I ended up working on a large-scale Clojure/Script application that has a habit of eating about half my RAM, with the rest being eaten by devtools in the browser. It still works, but sluggishly so, and by swapping memory to disk. A lot.

So with a new revolutionary computer already in my lap, do I really need to buy yet another revolutionary computer just to be comfortable? I thought, well, maybe not! I've been pondering the idea of moving my development environment entirely to the cloud for a while now, effectively making my computer just sort-of a client, but I've never been fond of the idea of having to learn Emacs of Vim, or worse yet, use some wonky mounted network drive that - surprise, surprise - makes [IntelliJ](https://www.jetbrains.com/idea/) eat even more memory than when the whole thing was local. 

## Problem #1, the IDE

Like I said, I really don't want to _learn_ an IDE. I'm having a hard enough time trying to keep up with my new team, all of whom are senior, expecting me to be senior as well (Am I? Sometimes I'm not so sure). I really-really don't want to throw yet-another-thing-to-learn into the mix. Thus, having a IDE I'm comfortable with is a must-have. I've been pretty okay with [IntelliJ](https://www.jetbrains.com/idea/) + [Cursive](https://cursive-ide.com), albeit it eats a ton of memory and isn't the snappiest thing to use. 

I tried a bunch of things, like using [Mountan Duck](https://cursive-ide.com) to mount the SSH connection locally as a drive, but that made IntelliJ use even more power than before all the while start-up time turned from seconds into minutes. I also tried doing a SFTP sync thing, where every file I change gets synced up - but that I managed to only get working one-way, so if something changed on-server it wouldn't sync back down to me. Not ideal either.

Eventually I learnt that [VS Code](https://code.visualstudio.com/) has a [Remote Development expansion pack](https://code.visualstudio.com/docs/remote/remote-overview) which runs a VS Code agent on the server, meaning that the files never actually touch my disk space, yet the editor keeps working as normal. It also automatically forwards all the ports, effectively making the remote server into my localhost. This stuff is crazy good, considering all the extensions and such keep also working as normal. 

Too good to be true? I tried it out a bunch of ways, with my own open source projects and a big work one, using [Calva](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.calva) for Clojure powers and it works just fine. REPL connection also works just fine. Calva in general, to me, is not as _polished_ as Intellij + Cursive, but it evolves rapidly and I'm sure it'll get there soon enough. Hurray, IDE problem solved!

## Problem #2, the server

I've been using [UpCloud](https://upcloud.com) before for VM's, so naturally I went with them again to set up my development environment. Their stuff just works, and I've been pretty happy with it. I created a VM in their San Jose region and started setting stuff up, during which I realized a slight issue - latency. You see, I'm in South America and while I thought USA would be a pretty direct line down, there was a considerable issue with latency still, which affected the speed at which VS Code's agent could stream data back to my client. 

I was actually surprised to see the effort VS Code put into a slow connection, trying its hardest to give instant feedback on the client, while sending info to the server in a sort-of a queue. But regardless, I felt the latency quite a bit, especially in how sluggishly Calva would format Clojure code, often making me break formatting because I was simply too fast for the VS Code agent to react.

Now, obviously I need to get a VM closer to where I am. But, UpCloud has no South America region. Digital Ocean also has no South America region. OVH Cloud, which [has a page indicating a region in Brazil](https://www.ovhcloud.com/en/vps/vps-brasil/), turns out to also not have one when you try to sign up for their service. After scanning Google for a bunch of local service providers, all of which ridiculously expensive, I found that AWS has a São Paulo region, which is right next door to Argentina, where I am. I got that, tried it out, and guess what - I feel no difference when comparing editing a local file and a file on my server. Awesome.

## Conclusion

Turns out that in 2021 you don't really need an all-powerful machine for programming work anymore, as long as you get a remote server close to where you are and manage to solve the IDE issue for yourself. I managed to solve it for myself using VS Code, and at a price of around ~$80 per month (at ~10h of use per day), making it out to be around ~$960 per year, for a 4 vCPU and 16gb RAM VM, which I can scale however I want depending on my need. And since I only pay for actual usage, and I'm pretty sure I won't be coding 10 hours per day, every day, for a whole year, I'm expecting the final price to be considerably lower than that.

Another cool thing that came from this is that now whenever I do actually buy a new computer, I no longer have to spend a day or two setting it all up. I just download VS Code and set-up SSH keys, and done. And when I'm traveling I can just change the EC2 region closest to wherever I happen to be at that time.
