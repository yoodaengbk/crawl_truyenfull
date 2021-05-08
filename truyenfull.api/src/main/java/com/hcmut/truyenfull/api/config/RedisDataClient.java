/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.api.config;

import com.hcmut.truyenfull.lib.RedisDatasService;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author TAM
 */
public class RedisDataClient extends RedisDatasService.Client{
    
    public RedisDataClient(TProtocolFactory tProtocolFactory) throws TTransportException {
        super(tProtocolFactory.getProtocol(new THttpClient("http://localhost:8081/redisdata")));
    }
    
    
    
}
