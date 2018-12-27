package com.test.domainexchange.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class BroadcastController {
    private static Logger log = LoggerFactory.getLogger(BroadcastController.class);
    @RequestMapping("/broadcast")
    @ResponseBody
    public String sendMessage(){
        JSONObject request_msg = new JSONObject();
        request_msg.put("msg", "broadcast msg");
        request_msg.put("src_domain", src_domain);
        List<DomainRoute> new_domain_list = new LinkedList<>();
        DomainRoute domainRoute3 = new DomainRoute();
        domainRoute3.setBroadcast(true);
        new_domain_list.add(domainRoute3);
        JSONArray domain_array = JSONArray.parseArray(JSONObject.toJSONString(new_domain_list));
        request_msg.put("domain_route", domain_array);
        log.info("send msg to {}, msg {}", domainExchange, request_msg);
        rabbitTemplate.convertAndSend(domainExchange, "", request_msg);
        return "OK";
    }


    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.src-domain}")
    private String src_domain;
    private final String domainExchange = "domainExchange";
    private final String innerExchangeName = "licodeExchange";
}
