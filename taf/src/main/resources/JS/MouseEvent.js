_WebElement = arguments[0];
_MouseEventType = arguments[1];
_CanBubble = arguments[2];
_Cancelable = arguments[3];
_ViewString = arguments[4];
_Detail = arguments[5];
_ScreenX = arguments[6];
_ScreenY = arguments[7];
_ClientX = arguments[8];
_ClientY = arguments[9];
_CtrlKey = arguments[10];
_AltKey = arguments[11];
_ShiftKey = arguments[12];
_MetaKey = arguments[13];
_Button = arguments[14];
_RelatedTarget = arguments[15];

//
// The view parameter is a window object and passing the string "window" causes
// an exception. So, we need to convert the string to the appropriate object.
//
// NOTE: If any objects other than window need to be passed, then this needs to
// be set here.
//
if (_ViewString == null || _ViewString == undefined || _ViewString == "window")
	_View = window;

if ('createEvent' in document) {
	var evt = document.createEvent('MouseEvents');
	evt.initMouseEvent(_MouseEventType, _CanBubble, _Cancelable, _View,
			_Detail, _ScreenX, _ScreenY, _ClientX, _ClientY, _CtrlKey, _AltKey,
			_ShiftKey, _MetaKey, _Button, _RelatedTarget);
	_WebElement.dispatchEvent(evt);
} else
	_WebElement.fireEvent('on' + _MouseEventType);