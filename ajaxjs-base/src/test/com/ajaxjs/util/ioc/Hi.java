package test.com.ajaxjs.util.ioc;

import com.ajaxjs.util.ioc.Bean;
import com.ajaxjs.util.ioc.Resource;

@Bean("hi")
public class Hi {
    @Resource(isNewInstance=true)
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