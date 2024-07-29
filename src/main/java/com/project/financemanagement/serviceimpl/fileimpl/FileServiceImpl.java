package com.project.financemanagement.serviceimpl.fileimpl;

import com.project.financemanagement.request.MyObject;
import com.project.financemanagement.responseVo.FileResponse;
import com.project.financemanagement.service.file.FileService;
import com.project.financemanagement.service.user.UserService;
import com.project.financemanagement.utility.CSVParser;
import com.project.financemanagement.utility.ExcelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;


@Service
public class FileServiceImpl implements FileService {

    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class) ;

    @Autowired
    private UserService userService;

    @Override
    public FileResponse uploadCsv(MultipartFile file) {
        logger.info("Received file: Name={}, Type={}, Size={}", file.getOriginalFilename(), file.getContentType(),
                file.getSize());
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv")) {
            logger.error("Unsupported file type: {}", file.getContentType());
            return new FileResponse(null, HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "");
        }
        try {
            List<MyObject> objects = CSVParser.parseCsvFile(file);
            return new FileResponse(objects, HttpStatus.OK.value(), "");

        } catch (IOException e) {
            logger.error("IOException occurred while processing file", e);
            return new FileResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "");
        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
            return new FileResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "");
        }
    }

    @Override
    public FileResponse uploadExcel(MultipartFile file) {

        logger.info("Received file: Name={}, Type={}, Size={}", file.getOriginalFilename(), file.getContentType(), file.getSize());

        // Validate file type by extension
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
            logger.error("Unsupported file type: {}", file.getContentType());
            return new FileResponse(null, HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "");
        }

        try (InputStream is = file.getInputStream()) {
            List<MyObject> objects = ExcelParser.parseExcelFile(is);

            // get the list of all the object or check if the object is present in database if not present then add
            // else tell that this entry is already present

            String msg = userService.registerUserByExcel(objects);
            return new FileResponse(objects, HttpStatus.OK.value(), msg);

        } catch (IOException e) {
            logger.error("IOException occurred while processing file", e);
            return new FileResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Sorry, Some internal server error occured.");
        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
            return new FileResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Sorry, Some internal server error occured.");
        }
    }
}
