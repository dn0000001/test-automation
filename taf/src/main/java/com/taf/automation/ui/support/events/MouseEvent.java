package com.taf.automation.ui.support.events;

/**
 * Variables to trigger a Mouse Event in JavaScript<BR>
 * See <a href="https://developer.mozilla.org/en-US/docs/Web/API/event.initMouseEvent">
 * https://developer.mozilla.org/en-US/docs/Web/API/event.initMouseEvent</a> for more details.<BR>
 */
public class MouseEvent {
    private MouseEventType type;
    private boolean bubbles;
    private boolean cancelable;
    private String view;
    private int detail;
    private int screenX;
    private int screenY;
    private int clientX;
    private int clientY;
    private boolean ctrlKey;
    private boolean altKey;
    private boolean shiftKey;
    private boolean metaKey;
    private int button;
    private String relatedTarget;

    /**
     * Constructor - Initializes other variables to defaults for use
     *
     * @param type - Mouse Event type
     */
    public MouseEvent(MouseEventType type) {
        withType(type)
                .withBubbles(true).withCancelable(false).withView("window")
                .withDetail(0).withScreenX(0).withScreenY(0).withClientX(0).withClientY(0)
                .withCtrlKey(false).withAltKey(false).withShiftKey(false).withMetaKey(false).withButton(0)
                .withRelatedTarget(null);
    }

    public MouseEventType getType() {
        return type;
    }

    /**
     * @param type - the string to set the event's type to
     * @return MouseEvent
     */
    public MouseEvent withType(MouseEventType type) {
        this.type = type;
        return this;
    }

    public boolean isBubbles() {
        return bubbles;
    }

    /**
     * @param bubbles - whether or not the event can bubble
     * @return MouseEvent
     */
    public MouseEvent withBubbles(boolean bubbles) {
        this.bubbles = bubbles;
        return this;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    /**
     * @param cancelable - whether or not the event's default action can be prevented
     * @return MouseEvent
     */
    public MouseEvent withCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public String getView() {
        return view;
    }

    /**
     * @param view - the Event's AbstractView. You should pass the window object here
     * @return MouseEvent
     */
    public MouseEvent withView(String view) {
        this.view = view;
        return this;
    }

    public int getDetail() {
        return detail;
    }

    /**
     * @param detail - the Event's mouse click count
     * @return MouseEvent
     */
    public MouseEvent withDetail(int detail) {
        this.detail = detail;
        return this;
    }

    public int getScreenX() {
        return screenX;
    }

    /**
     * @param screenX - the Event's screen x coordinate
     * @return MouseEvent
     */
    public MouseEvent withScreenX(int screenX) {
        this.screenX = screenX;
        return this;
    }

    public int getScreenY() {
        return screenY;
    }

    /**
     * @param screenY - the Event's screen y coordinate
     * @return MouseEvent
     */
    public MouseEvent withScreenY(int screenY) {
        this.screenY = screenY;
        return this;
    }

    public int getClientX() {
        return clientX;
    }

    /**
     * @param clientX - the Event's client x coordinate
     * @return MouseEvent
     */
    public MouseEvent withClientX(int clientX) {
        this.clientX = clientX;
        return this;
    }

    public int getClientY() {
        return clientY;
    }

    /**
     * @param clientY - the Event's client y coordinate
     * @return MouseEvent
     */
    public MouseEvent withClientY(int clientY) {
        this.clientY = clientY;
        return this;
    }

    public boolean isCtrlKey() {
        return ctrlKey;
    }

    /**
     * @param ctrlKey - whether or not control key was depressed during the Event
     * @return MouseEvent
     */
    public MouseEvent withCtrlKey(boolean ctrlKey) {
        this.ctrlKey = ctrlKey;
        return this;
    }

    public boolean isAltKey() {
        return altKey;
    }

    /**
     * @param altKey - whether or not alt key was depressed during the Event
     * @return MouseEvent
     */
    public MouseEvent withAltKey(boolean altKey) {
        this.altKey = altKey;
        return this;
    }

    public boolean isShiftKey() {
        return shiftKey;
    }

    /**
     * @param shiftKey - whether or not shift key was depressed during the Event
     * @return MouseEvent
     */
    public MouseEvent withShiftKey(boolean shiftKey) {
        this.shiftKey = shiftKey;
        return this;
    }

    public boolean isMetaKey() {
        return metaKey;
    }

    /**
     * @param metaKey - whether or not meta key was depressed during the Event
     * @return MouseEvent
     */
    public MouseEvent withMetaKey(boolean metaKey) {
        this.metaKey = metaKey;
        return this;
    }

    public int getButton() {
        return button;
    }

    /**
     * @param button - the Event's mouse event.button
     * @return MouseEvent
     */
    public MouseEvent withButton(int button) {
        this.button = button;
        return this;
    }

    public String getRelatedTarget() {
        return relatedTarget;
    }

    /**
     * @param relatedTarget - the Event's related EventTarget. Only used with some event
     *                      types (e.g. mouseover and mouseout). In other cases, pass null.
     * @return MouseEvent
     */
    public MouseEvent withRelatedTarget(String relatedTarget) {
        this.relatedTarget = relatedTarget;
        return this;
    }

}
