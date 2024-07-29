package com.project.financemanagement.service.file;

import com.project.financemanagement.responseVo.CsvFileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    CsvFileResponse uploadCsv(MultipartFile file);
    void UploadExcel();
}
