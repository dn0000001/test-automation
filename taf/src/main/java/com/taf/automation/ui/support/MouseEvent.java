package com.taf.automation.ui.support;

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
        init(type, true, false, "window", 0, 0, 0, 0, 0, false, false, false, false, 0, null);
    }

    /**
     * Initialize all variables
     *
     * @param type          - the string to set the event's type to
     * @param bubbles       - whether or not the event can bubble
     * @param cancelable    - whether or not the event's default action can be prevented
     * @param view          - the Event's AbstractView. You should pass the window object here
     * @param detail        - the Event's mouse click count
     * @param screenX       - the Event's screen x coordinate
     * @param screenY       - the Event's screen y coordinate
     * @param clientX       - the Event's client x coordinate
     * @param clientY       - the Event's client y coordinate
     * @param ctrlKey       - whether or not control key was depressed during the Event
     * @param altKey        - whether or not alt key was depressed during the Event
     * @param shiftKey      - whether or not shift key was depressed during the Event
     * @param metaKey       - whether or not meta key was depressed during the Event
     * @param button        - the Event's mouse event.button
     * @param relatedTarget - the Event's related EventTarget. Only used with some event types (e.g. mouseover
     *                      and mouseout). In other cases, pass null.
     */
    @SuppressWarnings("squid:S00107")
    private void init(MouseEventType type, boolean bubbles, boolean cancelable, String view, int detail,
                      int screenX, int screenY, int clientX, int clientY, boolean ctrlKey, boolean altKey,
                      boolean shiftKey, boolean metaKey, int button, String relatedTarget) {
        this.type = type;
        this.bubbles = bubbles;
        this.cancelable = cancelable;
        this.view = view;
        this.detail = detail;
        this.screenX = screenX;
        this.screenY = screenY;
        this.clientX = clientX;
        this.clientY = clientY;
        this.ctrlKey = ctrlKey;
        this.altKey = altKey;
        this.shiftKey = shiftKey;
        this.metaKey = metaKey;
        this.button = button;
        this.relatedTarget = relatedTarget;
    }

    public MouseEventType getType() {
        return type;
    }

    public void setType(MouseEventType type) {
        this.type = type;
    }

    public boolean isBubbles() {
        return bubbles;
    }

    public void setBubbles(boolean bubbles) {
        this.bubbles = bubbles;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public int getDetail() {
        return detail;
    }

    public void setDetail(int detail) {
        this.detail = detail;
    }

    public int getScreenX() {
        return screenX;
    }

    public void setScreenX(int screenX) {
        this.screenX = screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public void setScreenY(int screenY) {
        this.screenY = screenY;
    }

    public int getClientX() {
        return clientX;
    }

    public void setClientX(int clientX) {
        this.clientX = clientX;
    }

    public int getClientY() {
        return clientY;
    }

    public void setClientY(int clientY) {
        this.clientY = clientY;
    }

    public boolean isCtrlKey() {
        return ctrlKey;
    }

    public void setCtrlKey(boolean ctrlKey) {
        this.ctrlKey = ctrlKey;
    }

    public boolean isAltKey() {
        return altKey;
    }

    public void setAltKey(boolean altKey) {
        this.altKey = altKey;
    }

    public boolean isShiftKey() {
        return shiftKey;
    }

    public void setShiftKey(boolean shiftKey) {
        this.shiftKey = shiftKey;
    }

    public boolean isMetaKey() {
        return metaKey;
    }

    public void setMetaKey(boolean metaKey) {
        this.metaKey = metaKey;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public String getRelatedTarget() {
        return relatedTarget;
    }

    public void setRelatedTarget(String relatedTarget) {
        this.relatedTarget = relatedTarget;
    }

}
