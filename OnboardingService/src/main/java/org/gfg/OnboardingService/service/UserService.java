package org.gfg.OnboardingService.service;

import org.apache.kafka.common.config.types.Password;
import org.gfg.OnboardingService.model.User;
import org.gfg.OnboardingService.repository.UserRepository;
import org.gfg.OnboardingService.request.UserCreationRequest;
import org.gfg.enums.UserStatus;
import org.gfg.util.CommonConstants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public User createUser(UserCreationRequest userCreationRequest){
        User user = new User();
        user.setName(userCreationRequest.getName());
        user.setEmail(userCreationRequest.getEmail());
        user.setMobNo(userCreationRequest.getMobNo());
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        user.setDob(userCreationRequest.getDob());
        user.setUserStatus(UserStatus.ACTIVE);
        user.setUserIdentifier(userCreationRequest.getUserIdentifier());
        user.setUserIdentifierValue(userCreationRequest.getUserIdentifierValue());

        System.out.println("Raw password: " + userCreationRequest.getPassword());
        String encodedPassword = passwordEncoder.encode(userCreationRequest.getPassword());
        System.out.println("Encoded password: " + encodedPassword);
        user.setPassword(encodedPassword);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.USER_NAME, user.getName());
        jsonObject.put(CommonConstants.USER_EMAIL, user.getEmail());
        jsonObject.put(CommonConstants.USER_MOBILE, user.getMobNo());
        jsonObject.put(CommonConstants.USER_IDENTIFIER, user.getUserIdentifier());
        jsonObject.put(CommonConstants.USER_IDENTIFIER_VALUE, user.getUserIdentifierValue());
        try {
            User dbUser = userRepository.save(user);
            jsonObject.put(CommonConstants.USER_ID,user.getUserId());
            Thread thread = new Thread(()->{  // It will create a new thread and push data in kafka
                kafkaTemplate.send(CommonConstants.USER_DETAILS_QUEUE_TOPIC,jsonObject.toString());
                System.out.println("data sent to kafka");
            });
            thread.start();
            return dbUser;
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
        return null;
    }
    public String findUserByUsername(String userName){

        User user  = userRepository.findByMobNo(userName);
        if(user == null){
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.USER_NAME,user.getMobNo());
        jsonObject.put(CommonConstants.USER_PASSWORD,user.getPassword());

        return jsonObject.toString();
    }
}
