//
// Use JavaScript to scroll to the bottom of the page
// 
var body = document.body;
var html = document.documentElement;
var height = Math.max(body.scrollHeight, body.offsetHeight, html.clientHeight,
		html.scrollHeight, html.offsetHeight);
window.scrollTo(0, height);