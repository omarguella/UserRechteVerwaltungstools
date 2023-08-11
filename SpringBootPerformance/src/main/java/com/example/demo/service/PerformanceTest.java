package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.Resource;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PerformanceTest {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/loaderio-c4986c2f1714efa54e93dbe57d0da13f/")
    public @ResponseBody String getLoaderToken() {
        return "loaderio-c4986c2f1714efa54e93dbe57d0da13f";
    }
        @GetMapping("/performanceTest")
    public List<User> performanceTest() {

        List<User> users = userRepository.findAll();
        List<User> duplicatedUsers = new ArrayList<>(users);
        while (duplicatedUsers.size() < 100) {
            duplicatedUsers.addAll(users);
        }
        return duplicatedUsers;
    }

}
