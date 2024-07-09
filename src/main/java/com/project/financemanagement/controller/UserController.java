package com.project.financemanagement.controller;


import com.project.financemanagement.entity.User;
import com.project.financemanagement.request.MyObject;
import com.project.financemanagement.request.UserDto;
import com.project.financemanagement.service.user.UserService;
import com.project.financemanagement.serviceimpl.userimpl.UserServiceImpl;
import com.project.financemanagement.utility.CSVParser;
import com.project.financemanagement.utility.ExcelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


//    @Autowired
//    private UserService userService;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private UserService userService;

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
            logger.error("Uploaded file is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Log file details
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        long fileSize = file.getSize();

        logger.info("Received file: Name={}, Type={}, Size={}", fileName, fileType, fileSize);

        // Validate file type by extension
        if (!fileName.endsWith(".csv")) {
            logger.error("Unsupported file type: {}", fileType);
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        try {
            List<MyObject> objects = CSVParser.parseCsvFile(file);
            return new ResponseEntity<>(objects, HttpStatus.OK);

        } catch (IOException e) {
            logger.error("IOException occurred while processing file", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/uploadExcel")
    @Cacheable("uploadFileExcel")
    public ResponseEntity<String> uploadFileExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            logger.error("Uploaded file is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Log file details
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        long fileSize = file.getSize();

        logger.info("Received file: Name={}, Type={}, Size={}", fileName, fileType, fileSize);

        // Validate file type by extension
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            logger.error("Unsupported file type: {}", fileType);
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        String filePath = saveFile(file);

        // Launch the job asynchronously
        runJobAsync(filePath);

        return ResponseEntity.ok("File uploaded successfully. Processing in background.");
    }

    @Async
    public void runJobAsync(String filePath) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", filePath)
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String saveFile(MultipartFile file) {
        String filePath = "D:\\financemanagement\\files" + file.getOriginalFilename();
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

}

