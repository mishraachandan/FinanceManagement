package com.project.financemanagement.controller;


import com.project.financemanagement.entity.User;
import com.project.financemanagement.request.MyObject;
import com.project.financemanagement.request.UserDto;
import com.project.financemanagement.responseVo.FileResponse;
import com.project.financemanagement.service.file.FileService;
import com.project.financemanagement.service.user.UserService;
import com.project.financemanagement.utility.ExcelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


//    @Autowired
//    private UserService userService;

    @Autowired
    private UserService userService;


    @Autowired
    private FileService fileService;

    @PostMapping(value = "/addUsers")
    public ResponseEntity<String> addUsers(@RequestBody List<UserDto> userDtoList){
        String msg = userService.registerUser(userDtoList);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

//    @PostMapping("/registration")
//    public String registerUser(User user) {
//        userService.(user);
//        return "redirect:/login";
//    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/byRole/{role}")
    @Cacheable("getDataByRole")
    public ResponseEntity<List<String>> getDataByRole(@PathVariable("role") String role) {
        List<String> list = userService.getByUserRole(role);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/uploadCsv")
    public ResponseEntity<List<MyObject>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            logger.error("Uploaded file is empty in uploadfile using csv.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Log file details
        FileResponse fileResponse = fileService.uploadCsv(file);
        if(fileResponse.getHttpStatusCode() == 200)            {
            return new ResponseEntity<>(fileResponse.getObjectList(), HttpStatus.OK);
        }
        else if(fileResponse.getHttpStatusCode() == 415){
            return new ResponseEntity<>(fileResponse.getObjectList(), HttpStatus.UNSUPPORTED_MEDIA_TYPE)  ;
        }
        return new ResponseEntity<>(fileResponse.getObjectList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/uploadExcel")
    @Cacheable("uploadFileExcel")
    public ResponseEntity<String> uploadFileExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            logger.error("Uploaded file is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Log file details
        FileResponse fileResponse = fileService.uploadExcel(file);
        if(fileResponse.getHttpStatusCode() == 200)            {
            return new ResponseEntity<>(fileResponse.getMsg(), HttpStatus.OK);
        }
        else if(fileResponse.getHttpStatusCode() == 415){
            return new ResponseEntity<>(fileResponse.getMsg(), HttpStatus.UNSUPPORTED_MEDIA_TYPE)  ;
        }
        return new ResponseEntity<>(fileResponse.getMsg(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

