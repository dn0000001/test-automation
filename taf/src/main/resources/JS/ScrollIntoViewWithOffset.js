//
// arguments[0] - WebElement (or DOM element)
// arguments[1] - Boolean - If true, the top of the element will be aligned to the top of the visible area of the scrollable ancestor.  
//                          If false, the bottom of the element will be aligned to the bottom of the visible area of the scrollable ancestor.
// arguments[2] - Offset
// 
arguments[0].scrollIntoView(arguments[1]);
window.scrollBy(0, arguments[2]);