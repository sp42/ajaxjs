package com.ajaxjs.util.logger;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class TestSlf4J {
    @Test
    public void test() {
        Logger logger = LoggerFactory.getLogger(TestSlf4J.class);
        logger.info("Hello World 1");

        log.info("Hello World 2");
        log.info("Hello {}, it's {} day.2", "Frank", "good");
        log.info("Hello {}, it's {} day{}2", "Frank", "good", "?");

        log.debug("Hello World3");
        log.debug("Hello {}, it's {} day.3", "Frank", "good");
        log.debug("Hello {}, it's {} day{}3", "Frank", "good", "?");

        log.warn("Hello {}, it's {} day{}4", "Frank", "good", "?");
    }
}
