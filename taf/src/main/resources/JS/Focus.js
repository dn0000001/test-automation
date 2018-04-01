var evt = document.createEvent('HTMLEvents');
evt.initEvent('focus', true, true);
arguments[0].dispatchEvent(evt);
