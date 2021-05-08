/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.api.controller;

import com.hcmut.truyenfull.api.config.DataClient;
import com.hcmut.truyenfull.api.config.RedisDataClient;
import javax.servlet.http.HttpServletRequest;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TAM
 */
@RestController
@RequestMapping("/api")
public class RedisDataController {
    @Autowired
    RedisDataClient redisDataClient;
    
    @Autowired
     DataClient dataClient;
    
    @GetMapping(value = "/redisData",produces = "application/json")
    public String getComic(HttpServletRequest request) throws TException{
        String name = request.getParameter("name");
        String result = redisDataClient.GetComic(name);
        if(result.equals("false")){
            return dataClient.GetComic(name);
        }else{
            return result;
        }
    }
    
}
