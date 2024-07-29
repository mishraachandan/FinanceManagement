package com.project.financemanagement.serviceimpl.fileimpl;

import com.project.financemanagement.request.MyObject;
import com.project.financemanagement.responseVo.CsvFileResponse;
import com.project.financemanagement.service.file.FileService;
import com.project.financemanagement.utility.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FileServiceImpl implements FileService {

    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class) ;
    @Override
    public CsvFileResponse uploadCsv(MultipartFile file) {
        logger.info("Received file: Name={}, Type={}, Size={}", file.getOriginalFilename(), file.getContentType(),
                file.getSize());
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv")) {
            logger.error("Unsupported file type: {}", file.getContentType());
            return new CsvFileResponse(null, HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        }
        try {
            List<MyObject> objects = CSVParser.parseCsvFile(file);
            return new CsvFileResponse(objects, HttpStatus.OK.value());

        } catch (IOException e) {
            logger.error("IOException occurred while processing file", e);
            return new CsvFileResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
            return new CsvFileResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void UploadExcel() {

    }
}
