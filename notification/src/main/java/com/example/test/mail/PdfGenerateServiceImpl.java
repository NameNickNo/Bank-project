package com.example.test.mail;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;

@Component
public class PdfGenerateServiceImpl implements PdfGenerateService {


    @SneakyThrows
    @Override
    public void generate() {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk("Hello World", font);

        document.add(chunk);
        document.close();
    }
}
