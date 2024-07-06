package com.project.financemanagement.utility;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.project.financemanagement.controller.UserController;
import com.project.financemanagement.request.MyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class CSVParser {
    private CSVParser(){}
    private static final Logger logger = LoggerFactory.getLogger(CSVParser.class);
    public static List<MyObject> parseCsvFile(MultipartFile file) throws IOException {
        List<MyObject> myObjects = new ArrayList<>();

        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream())).withSkipLines(1).build()) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                MyObject obj = new MyObject();
                obj.setField1(values[0]);
                obj.setField2(values[1]);
                obj.setField3((values[2])); // Adjust as necessary
                obj.setField4((values[3])); // Adjust as necessary
                obj.setField5((values[4])); // Adjust as necessary
                // Add more fields as needed

                myObjects.add(obj);
            }
        } catch (CsvValidationException e) {
            logger.error("Error occurred in parsingCSVFile:: "+e.getMessage());
        }

        return myObjects;
    }
}
