package com.sursindmitry.berkutservice.controller;

import com.sursindmitry.berkutservice.api.TestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * Реализация {@link TestController}.
 */
@RestController
@Slf4j
public class TestControllerImpl implements TestController {

    @Override
    public String helloController() {
        log.debug("Вызов метода helloController");
        log.info("Вызов метода helloController");
        return "Я работаю";
    }
}
