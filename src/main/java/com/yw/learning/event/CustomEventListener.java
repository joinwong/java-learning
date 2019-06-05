package com.yw.learning.event;

import java.util.EventListener;

public class CustomEventListener implements EventListener {
    //事件发生后的回调方法
    public void fireCusEvent(CustomEvent e) {
        EventSourceObject eObject = (EventSourceObject) e.getSource();
        System.out.println("My name has been changed!");
        System.out.println("I got a new name,named \"" + eObject.getName() + "\"");
    }
}
