_WebElement = arguments[0];
_HTML_EventType = arguments[1];
_Bubbles = arguments[2];
_Cancelable = arguments[3];
_Composed = arguments[4];

var event = new Event(_HTML_EventType, {'bubbles':_Bubbles, 'cancelable':_Cancelable, 'composed':_Composed});
_WebElement.dispatchEvent(event);
