package com.taf.automation.ui.support.events;

/**
 * Standard Events that are supported in JavaScript<BR>
 * See <a href="https://developer.mozilla.org/en-US/docs/Web/Events#standard_events">
 * https://developer.mozilla.org/en-US/docs/Web/Events#standard_events</a> for more details.<BR>
 */
public enum EventType {
    BLUR("blur"),
    CHANGE("change"),
    FOCUS("focus"),
    ;

    private final String event;

    EventType(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return event;
    }

}
