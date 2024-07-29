package com.project.financemanagement.service.file;

import com.project.financemanagement.responseVo.FileResponse;
import org.springframework.web.multipart.MultipartFile;


public interface FileService {
    FileResponse uploadCsv(MultipartFile file);
    FileResponse uploadExcel(MultipartFile file);
}
