package org.example.cdcproject.controller;

import jakarta.transaction.Transactional;
import org.example.cdcproject.dao.Customer;
import org.example.cdcproject.serivce.CustomerSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private CustomerSerivce registerSerivce;

    @Autowired
    public CustomerController(CustomerSerivce registerSerivce){
        this.registerSerivce = registerSerivce;
    }

    @PostMapping("/register")
    public String userRegister(@RequestBody Customer customer){
        registerSerivce.register(customer);
        return "register sucess!";
    }

    @Transactional
    @PostMapping("/delete")
    public String userDelete(@RequestBody Customer customer){
        registerSerivce.delete(customer);
        return "삭제 요청!";
    }
}
