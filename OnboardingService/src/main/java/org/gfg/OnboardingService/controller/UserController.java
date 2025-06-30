package org.gfg.OnboardingService.controller;

import org.gfg.OnboardingService.model.User;
import org.gfg.OnboardingService.request.UserCreationRequest;
import org.gfg.OnboardingService.response.UserCreationResponse;
import org.gfg.OnboardingService.service.UserService;
import org.gfg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onboarding-service")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create/user")
    public ResponseEntity<UserCreationResponse> onboardUser(@RequestBody UserCreationRequest userCreationRequest) {


        UserCreationResponse userCreationResponse = new UserCreationResponse();
        userCreationResponse.setCode("002");

        if (StringUtil.isBlank(userCreationRequest.getEmail())) {
            userCreationResponse.setMsg("Email cannot be blank");
            return new ResponseEntity<>(userCreationResponse, HttpStatus.OK);
        } else if (StringUtil.isBlank(userCreationRequest.getMobNo())) {
            userCreationResponse.setMsg("Mobile No cannot be blank");
            return new ResponseEntity<>(userCreationResponse, HttpStatus.BAD_REQUEST);
        } else if (StringUtil.isBlank(userCreationRequest.getUserIdentifierValue()) || StringUtil.isBlank(userCreationRequest.getPassword())) {
            userCreationResponse.setMsg("Invalid Request");
            return new ResponseEntity<>(userCreationResponse, HttpStatus.BAD_REQUEST);
        } else {
            User user = userService.createUser(userCreationRequest);

            if (user == null) {
                userCreationResponse.setMsg("User is not created");
                userCreationResponse.setCode("02");
            }


            assert user != null;
            userCreationResponse.setEmail(user.getEmail());
            userCreationResponse.setName(user.getName());
            userCreationResponse.setCode("00");
        }
        return new ResponseEntity<>(userCreationResponse, HttpStatus.OK);
    }

    @GetMapping("/user/details/{userId}")
    public String getUserDetails(@PathVariable("userId") String username){
        return userService.findUserByUsername(username);
    }

}
