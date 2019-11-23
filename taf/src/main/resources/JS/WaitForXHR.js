function reqCallBack(t) {
    document.getElementsByTagName('body')[0].setAttribute('ajaxcounter', ++ajaxCount)
}

function resCallback(t) {
    document.getElementsByTagName('body')[0].setAttribute('ajaxcounter', --ajaxCount)
}

function intercept() {
    XMLHttpRequest.prototype.send = function() {
        if (reqCallBack(this), this.addEventListener) {
            var t = this;
            this.addEventListener('readystatechange', function() {
                4 === t.readyState && resCallback(t)
            }, !1)
        } else {
            var e = this.onreadystatechange;
            e && (this.onreadystatechange = function() {
                4 === t.readyState && resCallbck(this), e()
            })
        }
        originalXhrSend.apply(this, arguments)
    }
}

var originalXhrSend = XMLHttpRequest.prototype.send,
    ajaxCount = 0;
document.getElementsByTagName('body')[0].hasAttribute('ajaxcounter') || intercept();
