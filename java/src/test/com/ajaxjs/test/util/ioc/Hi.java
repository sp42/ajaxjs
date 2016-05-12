package com.ajaxjs.test.util.ioc;

import com.ajaxjs.util.ioc.Bean;
import com.ajaxjs.util.ioc.Resource;

@Bean("hi")
public class Hi {
    @Resource
    private Person person;

    /**
     * Say Hello
     */
    public String sayHello() {
        return "Hello " + person.getName();
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}