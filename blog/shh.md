---
title: Shh: A CLI password manager
date: 2021-10-14
status: public
---

I think using a password manager is pretty important these days. But, while [Bitwarden](https://www.google.com/search?client=safari&rls=en&q=bitwarden&ie=UTF-8&oe=UTF-8) and [RememBear](https://www.remembear.com) get big recommendations from me, for my own use case I really wanted something simple and quick, even if it comes at a cost of some security. Which brings me to [Shh, a CLI password manager](https://github.com/askonomm/shh) I created. In Clojure, of course. But runs on Linux and Mac OS via a native binary!


What did I mean by "at a cost of some security"? Well, currently, the datastore is a EDN file in your home directory and contains all the info as-is, unencrypted. This means that the passwords are as safe as your computer is (which hopefully is secure?). But, encrypting of the data-store isn't a priority for me right now so feel free to make a pull request if you want it quick. 

I'm also planning on adding a password complexity prompt to it, so that you could choose from a variety of complexities like just letters, numbers, both, with special characters or not, and so on, so that you could also use it with services that have admittedly shitty security practices, because in reality if you're restricting passwords in some way, you're doing it wrong. Anyway, [go get it while it's hot](https://github.com/askonomm/shh). 
