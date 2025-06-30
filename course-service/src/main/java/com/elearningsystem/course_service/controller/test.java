package com.elearningsystem.course_service.controller;

import com.elearningsystem.course_service.service.testService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    @Autowired
    testService testService ;

    @GetMapping("/test")
    public String firstTest() {
        return testService.test("testk");


    }

}
