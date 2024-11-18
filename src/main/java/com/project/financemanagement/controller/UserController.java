package com.project.financemanagement.controller;


import com.project.financemanagement.entity.User;
import com.project.financemanagement.exception.CustomException;
import com.project.financemanagement.request.MyObject;
import com.project.financemanagement.request.UserDto;
import com.project.financemanagement.responseVo.FileResponse;
import com.project.financemanagement.service.file.FileService;
import com.project.financemanagement.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
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
    public ResponseEntity<String> welcomeUser() {
        String username = getLoggedInUsername();
        if (username != null) {
            return new ResponseEntity<>("Welcome, " + username.toUpperCase() + "!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Sorry, please check if username and password is correct or both details are filled.", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/UnsuccessfulLogin")
    public ResponseEntity<String> redirectLogin() {
        String username = getLoggedInUsername();
        if (username != null) {
            return new ResponseEntity<>("Welcome, " + username.toUpperCase() + "!" + " it seems we run into logging you in. Please" +
                    " check the password/username combination. And try login in again!. ", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>("Error, please check if username/password  both details are filled.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "No username entered.";
    }

    @GetMapping("/getDataByRole/{role}")
    public ResponseEntity<Page<String>> getDataByRole(
            @PathVariable("role") String role,  Authentication authentication,
            @RequestParam(defaultValue = "0") int page,           // default page 0
            @RequestParam(defaultValue = "10") int size,          // default size 10
            @RequestParam(defaultValue = "id") String sortBy,     // default sort by id
            @RequestParam(defaultValue = "asc") String sortDirection) { // default sort direction ascending
//        role = role.toUpperCase();


        // Fetch the username from the security context
        String username = authentication.getName();

        // Check if the user has the required role
        boolean hasRole = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));

        if (hasRole) {
                Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Call the service method with pagination and sorting
            Page<String> list = userService.getByUserRole(role, page, size, sort);
            // Simulating fetching data based on the role and pagination
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            throw new CustomException("Sorry u are not authorized");
        }
//        return null;
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

