/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo.thrift.repo;

import com.demo.thrift.model.Comic;
import java.util.List;

import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author phong
 */
@Repository
public class RedisRepository {

    private static final String KEY = "Comic";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void add(Comic comic) {
        
        redisTemplate.opsForHash().putIfAbsent(KEY, comic.getId(), comic);
    }
    public void addListComic(List<Comic> comics){
        for(Comic comic : comics){
            add(comic);
        }
    }

    public void update(Comic comic) {
        redisTemplate.opsForHash().put(KEY, comic.getId(), comic);
    }

    public void delete(String name) {
        redisTemplate.opsForHash().delete(KEY, name);
    }

    public Comic findComic(String name) {
        return (Comic)redisTemplate.opsForHash().get(KEY, name);
    }
    public Comic findComic(int id) {
        return (Comic)redisTemplate.opsForHash().get(KEY, id);
    }

    public Map<Object, Object> findAllComic() {
        return redisTemplate.opsForHash().entries(KEY);
    }

    
}
