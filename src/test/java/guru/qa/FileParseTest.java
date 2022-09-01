package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import guru.qa.domain.Car;
import org.junit.jupiter.api.Test;


import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

import static org.assertj.core.api.Assertions.assertThat;

public class FileParseTest {

    ClassLoader classLoader = FileParseTest.class.getClassLoader();


    @Test
    void pdfZipTest() throws Exception {
        InputStream is = classLoader.getResourceAsStream("keyMap.zip");
        ZipInputStream zis = new ZipInputStream(is);
        ZipFile zipFile = new ZipFile(new File("src/test/resources/" + "keyMap.zip"));
        ZipEntry entry;
        while((entry = zis.getNextEntry()) != null){
            try(InputStream stream = zipFile.getInputStream(entry)){
                PDF pdf = new PDF(stream);
                assertThat(pdf.text).contains("Windows & Linux keymap");
            }
        }

    }

    @Test
    void xlsZipTest() throws Exception {
       InputStream is = classLoader.getResourceAsStream("prajs_ot_2508.zip");
       ZipInputStream zis = new ZipInputStream(is);
       ZipFile zipFile = new ZipFile(new File("src/test/resources/" + "prajs_ot_2508.zip"));
       ZipEntry entry;
       while((entry = zis.getNextEntry()) != null){
           try(InputStream stream = zipFile.getInputStream(entry)){
               XLS xls = new XLS(stream);
               assertThat(xls.excel.getSheetAt(0)
                       .getRow(22)
                       .getCell(2)
                       .getStringCellValue()).contains("Бумага для цветной печати");
           }
       }
    }


    @Test
    void csvZipTest() throws Exception{
       InputStream is =  classLoader.getResourceAsStream("prostoprimer.zip");
       ZipInputStream zis = new ZipInputStream(is);
       ZipFile zipFile = new ZipFile(new File("src/test/resources/" + "prostoprimer.zip"));
       ZipEntry entry;
       while((entry = zis.getNextEntry()) != null){

           try(InputStream stream = zipFile.getInputStream(entry);
               CSVReader reader = new CSVReader(new InputStreamReader(stream, UTF_8))){
               List<String[]> csv = reader.readAll();
               assertThat(csv).contains(
                       new String[]{"Emanuil", "Forest", "Moscow", "Arbat 1"},
                       new String[]{"John", "McLayn", "Bryansk", "Sovetskyay 5"});

           }
       }
    }


    @Test
    void jsonTest() throws Exception{
        InputStream is = classLoader.getResourceAsStream("Car.json");
        ObjectMapper objectMapper = new ObjectMapper();
        Car car = objectMapper.readValue(is, Car.class);
        assertThat(car.getMark()).isEqualTo("Zhiguli");
        assertThat(car.getManufacturer()).isEqualTo("Russia");
        assertThat(car.getOwner()).isEqualTo("Mr. Andrey");
    }
}
