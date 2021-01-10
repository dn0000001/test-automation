package com.taf.automation.ui.support.events;

/**
 * Variables to trigger a standard Event in JavaScript<BR>
 * See <a href="https://developer.mozilla.org/en-US/docs/Web/API/Event/Event">
 * https://developer.mozilla.org/en-US/docs/Web/API/Event/Event</a> for more details.<BR>
 */
public class Event {
    private EventType type;

    /**
     * A Boolean indicating whether the event bubbles
     */
    private boolean bubbles;

    /**
     * A Boolean indicating whether the event can be cancelled
     */
    private boolean cancelable;

    /**
     * The read-only composed property of the Event interface returns a Boolean which indicates whether or not the
     * event will propagate across the shadow DOM boundary into the standard DOM.
     * See <a href="https://developer.mozilla.org/en-US/docs/Web/API/Event/composed">
     * https://developer.mozilla.org/en-US/docs/Web/API/Event/composed</a> for more details
     */
    private boolean composed;

    public Event() {
        withBubbles(false).withCancelable(false).withComposed(false);
    }

    public Event(EventType type) {
        withType(type).withBubbles(false).withCancelable(false).withComposed(false);
    }

    public EventType getType() {
        return type;
    }

    public Event withType(EventType type) {
        this.type = type;
        return this;
    }

    public boolean isBubbles() {
        return bubbles;
    }

    public Event withBubbles(boolean bubbles) {
        this.bubbles = bubbles;
        return this;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public Event withCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public boolean isComposed() {
        return composed;
    }

    public Event withComposed(boolean composed) {
        this.composed = composed;
        return this;
    }

}
