/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo.thrift.controller;

import com.demo.thrift.model.Comic;
import com.hcmut.truyenfull.lib.RedisDatasService;
import com.demo.thrift.repo.RedisRepository;
import com.demo.thrift.crawl.crawlerRedisData;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author phong
 */
@RestController
public class StoryController implements RedisDatasService.Iface{

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private crawlerRedisData dataRedis;
    

    @Override
    public String GetComic(String name) throws TException {
        try {
            dataRedis.crawlerAllComic();
        } catch (IOException ex) {
            Logger.getLogger(StoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Comic> comics = (List<Comic>) redisRepository.findAllComic();
        int id = 0;
        for(Comic comic :comics){
            if(comic.getTitle().equals(name)){
               id = comic.getId();
            }
        }
        if(id != 0){
            return redisRepository.findComic(id).toString();
        }
        return "false";
        
    }

}
