package org.gfg.NotificationService.consumer;

import org.gfg.NotificationService.worker.EmailWorker;
import org.gfg.enums.UserIdentifier;
import org.gfg.util.CommonConstants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumer {

    @Autowired
    EmailWorker emailWorker;

    @KafkaListener(topics = "USER_DETAILS_QUEUE", groupId = "email_notification")
    public void listenNewlyCreatedConfig(String data){
        System.out.println(data);
        JSONObject jsonObject = new JSONObject(data);
        String name = jsonObject.getString(CommonConstants.USER_NAME);
        String email = jsonObject.getString(CommonConstants.USER_EMAIL);
        String userIdentifierValue = jsonObject.getString(CommonConstants.USER_IDENTIFIER_VALUE);
        UserIdentifier userIdentifier = jsonObject.getEnum(UserIdentifier.class,CommonConstants.USER_IDENTIFIER);

        emailWorker.sendEmail(name,email,userIdentifier.toString(),userIdentifierValue);
        System.out.println("email has sent");
    }
}
