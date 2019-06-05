package com.yw.learning.event;

public class Application {

    public static void main(String... args) {
        EventSourceObject object = new EventSourceObject();

        object.addCusListener(new CustomEventListener(){
            @Override
            public void fireCusEvent(CustomEvent e) {
                super.fireCusEvent(e);
            }
        });

        object.setName("Jack");
    }
}
