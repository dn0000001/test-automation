_WebElement = arguments[0];
_MouseEventType = arguments[1];
_Bubbles = arguments[2];
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

_View = _ViewString;
if (_ViewString == null || _ViewString == undefined || _ViewString == 'window') {
  _View = window;
}

var evt = new MouseEvent(_MouseEventType, {
  bubbles: _Bubbles,
  cancelable: _Cancelable,
  view: _View,
  detail: _Detail,
  screenX: _ScreenX,
  screenY: _ScreenY,
  clientX: _ClientX,
  clientY: _ClientY,
  ctrlKey: _CtrlKey,
  altKey: _AltKey,
  shiftKey: _ShiftKey,
  metaKey: _MetaKey,
  button: _Button,
  relatedTarget: _RelatedTarget
});
_WebElement.dispatchEvent(evt);
