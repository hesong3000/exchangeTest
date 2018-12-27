package com.test.domainexchange.demo;

import com.test.domainexchange.demo.config.MQConstant;
import com.test.domainexchange.demo.config.MsgHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AMQPReceiver {
    private static Logger log = LoggerFactory.getLogger(AMQPReceiver.class);

    @Autowired
    MsgHolder msgHolder;

    @RabbitListener(containerFactory = "rabbitListenerContainerFactory",
            bindings = @QueueBinding(value = @Queue(value = MQConstant.MQ_QUEUE_NAME, durable = "false"),
                    exchange = @Exchange(value = MQConstant.MQ_EXCHANGE,
                            type = ExchangeTypes.DIRECT), key = MQConstant.MQ_BINDING_KEY))
    public void process(byte[] message){
        String msgRecv_str = new String(message);
        msgHolder.pushMsg(msgRecv_str);
    }
}