//
// Append child div element with an attribute to the parent element
//
var parent = arguments[0];
var child = document.createElement("div");
child.setAttribute(arguments[1], arguments[2]);
parent.appendChild(child);
