package ru.mccarl.moneytransfer.api.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by vrudometkin on 24/11/2017.
 */
@RestController
@Configuration
@RequestMapping(produces = "application/json")
public class Controller {


    @GetMapping("/{query}")
    public ResponseEntity getInfo(@PathVariable String query) throws Exception{

        return ResponseEntity.ok().build();
    }



}
