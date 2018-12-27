package com.test.domainexchange.demo.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.test.domainexchange.demo.controller.DomainRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component(value="processMsgThread")
public class ProcessMsgThread extends Thread{
    @Value("${spring.rabbitmq.src-domain}")
    private String src_domain;
    private static Logger log = LoggerFactory.getLogger(ProcessMsgThread.class);
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private MsgHolder msgHolder;
    private final String outerExchangeName = "domainExchange";

    private int send_times = 0;

    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            try{
                String msg = msgHolder.popMsg();
                log.info("exchangeTest recv {}", msg);
                if(msg != null){
                    JSONObject response_msg = new JSONObject();
                    response_msg.put("src_domain",src_domain);
                    response_msg.put("msg", "broadcast response msg");
                    List<DomainRoute> new_domain_list = new LinkedList<>();
                    DomainRoute domainRoute3 = new DomainRoute();
                    domainRoute3.setBroadcast(true);
                    new_domain_list.add(domainRoute3);
                    JSONArray domain_array = JSONArray.parseArray(JSONObject.toJSONString(new_domain_list));
                    response_msg.put("domain_route", domain_array);

                    if(send_times==0) {
                        //log.info("exchangeTest send {} msg {}", outerExchangeName, response_msg);
                        //rabbitTemplate.convertAndSend(outerExchangeName, "", response_msg);
                        send_times = send_times+1;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
