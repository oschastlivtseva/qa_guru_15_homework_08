package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipParseTest {

    ClassLoader cl = ZipParseTest.class.getClassLoader();

    String zipArchiveName = "test_zip.zip";
    String firstColumn = "term";
    String secondColumn = "translation";
    String firstWord = "извлекать";
    String firstTranslation = "to retrieve";
    String lastWord = "вызывать";
    String lastTranslation = "to invoke";

    @Test
    void ZipCheck() throws Exception {
        try (
                InputStream is = cl.getResourceAsStream(zipArchiveName);
                ZipInputStream zis = new ZipInputStream(is);
                ZipFile zipFile = new ZipFile("src/test/resources/" + zipArchiveName)
        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                System.out.println(entryName);
                if (entryName.endsWith(".csv")) {
                    try (CSVReader reader = new CSVReader(new InputStreamReader(zipFile.getInputStream(entry)))) {
                        List<String[]> content = reader.readAll();
                        String[] columnNames = content.get(0);
                        String[] firstTerm = content.get(1);
                        String[] lastTerm = content.get(10);
                        assertThat(columnNames[0]).isEqualTo(firstColumn);
                        assertThat(columnNames[1]).isEqualTo(secondColumn);
                        assertThat(firstTerm[0]).isEqualTo(firstWord);
                        assertThat(firstTerm[1]).isEqualTo(firstTranslation);
                        assertThat(lastTerm[0]).isEqualTo(lastWord);
                        assertThat(lastTerm[1]).isEqualTo(lastTranslation);
                        System.out.println("CSV - OK");
                    }
                } else if (entryName.endsWith(".pdf")) {
                    try (InputStream inputStream = zipFile.getInputStream(entry)) {
                        PDF pdf = new PDF(inputStream);
                        assertThat(pdf.text).contains(firstColumn + " " + secondColumn);
                        assertThat(pdf.text).contains(firstWord + " " + firstTranslation);
                        assertThat(pdf.text).contains(lastWord + " " + lastTranslation);
                        System.out.println("PDF - OK");
                    }
                } else if (entryName.endsWith(".xlsx")) {
                    try (InputStream inputStream = zipFile.getInputStream(entry)) {
                        XLS xls = new XLS(inputStream);
                        assertThat(
                                xls.excel.getSheetAt(0)
                                        .getRow(0)
                                        .getCell(0)
                                        .getStringCellValue()
                        ).isEqualTo(firstColumn);
                        assertThat(
                                xls.excel.getSheetAt(0)
                                        .getRow(10)
                                        .getCell(1)
                                        .getStringCellValue()
                        ).isEqualTo(lastTranslation);
                        System.out.println("XLS - OK");
                    }
                }
            }
        }
    }
}

