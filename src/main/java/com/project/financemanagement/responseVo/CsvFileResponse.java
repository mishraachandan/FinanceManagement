package com.project.financemanagement.responseVo;

import com.project.financemanagement.request.MyObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvFileResponse {
    private List<MyObject> objectList;
    private int httpStatusCode ;
}
