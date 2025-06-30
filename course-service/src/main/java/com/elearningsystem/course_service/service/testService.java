package com.elearningsystem.course_service.service;


import org.springframework.stereotype.Service;

@Service
public class testService {

    public String test(String name){
        return name.toUpperCase();
    }
}
