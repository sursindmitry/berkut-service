package com.sursindmitry.berkutservice.api;

import static com.sursindmitry.berkutservice.constant.ApiConstant.HELLO_API;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TestController.
 *
 * <p>
 * Контроллер для тестирования
 * </p>
 */
@RequestMapping(HELLO_API)
public interface TestController {

    @GetMapping
    String helloController();
}
