package com.taf.automation.ui.support.events;

/**
 * Mouse Events that are supported in JavaScript<BR>
 * See <a href="https://developer.mozilla.org/en-US/docs/Web/API/event.initMouseEvent">
 * https://developer.mozilla.org/en-US/docs/Web/API/event.initMouseEvent</a> for more details.<BR>
 */
public enum MouseEventType {
    MOUSE_OVER("mouseover"),
    MOUSE_OUT("mouseout"),
    ;

    private final String event;

    MouseEventType(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return event;
    }

}
