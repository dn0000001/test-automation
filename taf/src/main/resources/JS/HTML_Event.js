_WebElement = arguments[0];
_HTML_EventType = arguments[1];
_Bubbles = arguments[2];
_Cancelable = arguments[3];

if ('createEvent' in document) {
	var evt = document.createEvent('HTMLEvents');
	evt.initEvent(_HTML_EventType, _Bubbles, _Cancelable);
	arguments[0].dispatchEvent(evt);
} else
	arguments[0].fireEvent('on' + _HTML_EventType);