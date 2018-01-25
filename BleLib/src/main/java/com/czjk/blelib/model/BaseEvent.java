package com.czjk.blelib.model;


public class BaseEvent {
    private EventType mEventType;
    public EventType getEventType() {
        return mEventType;
    }

    public BaseEvent(EventType mEventType) {
        this.mEventType = mEventType;
    }

    public enum EventType {
        RECONNECTION,
        STATE_CONNECTED,
        STATE_DISCONNECTED,
        STATE_CONNECT_FAILURE
    }
}
