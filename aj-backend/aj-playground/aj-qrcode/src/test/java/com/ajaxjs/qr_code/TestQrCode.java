package com.ajaxjs.qr_code;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class TestQrCode {
    @Test
    // Creates a single QR Code, then writes it to a PNG file and an SVG file.
    public void doBasicDemo() throws IOException {
        String text = "Hello, world!";          // User-supplied Unicode text
        QrCode.Ecc errCorLvl = QrCode.Ecc.LOW;  // Error correction level
        QrCode qr = QrCode.encodeText(text, errCorLvl);  // Make the QR Code symbol

        BufferedImage img = Utils.toImage(qr, 10, 4);          // Convert to bitmap image
        File imgFile = new File("hello-world-QR.png");   // File path for output
        ImageIO.write(img, "png", imgFile);              // Write image to file

        String svg = Utils.toSvgString(qr, 4, "#FFFFFF", "#000000");  // Convert to SVG XML code
        File svgFile = new File("c:\\temp\\hello-world-QR.svg");          // File path for output
        Files.write(svgFile.toPath(), svg.getBytes(StandardCharsets.UTF_8)); // Write image to file
    }

    // Creates a variety of QR Codes that exercise different features of the library, and writes each one to file.
    @Test
    public void doVarietyDemo() throws IOException {
        QrCode qr;

        // Numeric mode encoding (3.33 bits per digit)
        qr = QrCode.encodeText("314159265358979323846264338327950288419716939937510", QrCode.Ecc.MEDIUM);
        Utils.writePng(Utils.toImage(qr, 13, 1), "pi-digits-QR.png");

        // Alphanumeric mode encoding (5.5 bits per character)
        qr = QrCode.encodeText("DOLLAR-AMOUNT:$39.87 PERCENTAGE:100.00% OPERATIONS:+-*/", QrCode.Ecc.HIGH);
        Utils.writePng(Utils.toImage(qr, 10, 2), "alphanumeric-QR.png");

        // Unicode text as UTF-8
        qr = QrCode.encodeText("こんにちwa、世界！ αβγδ", QrCode.Ecc.QUARTILE);
        Utils.writePng(Utils.toImage(qr, 10, 3), "unicode-QR.png");

        // Moderately large QR Code using longer text (from Lewis Carroll's Alice in Wonderland)
        qr = QrCode.encodeText("Alice was beginning to get very tired of sitting by her sister on the bank, " + "and of having nothing to do: once or twice she had peeped into the book her sister was reading, " + "but it had no pictures or conversations in it, 'and what is the use of a book,' thought Alice " + "'without pictures or conversations?' So she was considering in her own mind (as well as she could, " + "for the hot day made her feel very sleepy and stupid), whether the pleasure of making a " + "daisy-chain would be worth the trouble of getting up and picking the daisies, when suddenly " + "a White Rabbit with pink eyes ran close by her.", QrCode.Ecc.HIGH);
        Utils.writePng(Utils.toImage(qr, 6, 10), "alice-wonderland-QR.png");
    }

    @Test
    // Creates QR Codes with manually specified segments for better compactness.
    public void doSegmentDemo() throws IOException {
        QrCode qr;
        List<QrSegment> segs;

        // Illustration "silver"
        String silver0 = "THE SQUARE ROOT OF 2 IS 1.";
        String silver1 = "41421356237309504880168872420969807856967187537694807317667973799";
        qr = QrCode.encodeText(silver0 + silver1, QrCode.Ecc.LOW);
        Utils.writePng(Utils.toImage(qr, 10, 3), "sqrt2-monolithic-QR.png");

        segs = Arrays.asList(QrSegment.makeAlphanumeric(silver0), QrSegment.makeNumeric(silver1));
        qr = QrCode.encodeSegments(segs, QrCode.Ecc.LOW);
        Utils.writePng(Utils.toImage(qr, 10, 3), "sqrt2-segmented-QR.png");

        // Illustration "golden"
        String golden0 = "Golden ratio φ = 1.";
        String golden1 = "6180339887498948482045868343656381177203091798057628621354486227052604628189024497072072041893911374";
        String golden2 = "......";
        qr = QrCode.encodeText(golden0 + golden1 + golden2, QrCode.Ecc.LOW);
        Utils.writePng(Utils.toImage(qr, 8, 5), "phi-monolithic-QR.png");

        segs = Arrays.asList(QrSegment.makeBytes(golden0.getBytes(StandardCharsets.UTF_8)), QrSegment.makeNumeric(golden1), QrSegment.makeAlphanumeric(golden2));
        qr = QrCode.encodeSegments(segs, QrCode.Ecc.LOW);
        Utils.writePng(Utils.toImage(qr, 8, 5), "phi-segmented-QR.png");

        // Illustration "Madoka": kanji, kana, Cyrillic, full-width Latin, Greek characters
        String madoka = "「魔法少女まどか☆マギカ」って、　ИАИ　ｄｅｓｕ　κα？";
        qr = QrCode.encodeText(madoka, QrCode.Ecc.LOW);
        Utils.writePng(Utils.toImage(qr, 9, 4, 0xFFFFE0, 0x303080), "madoka-utf8-QR.png");

        segs = Arrays.asList(QrSegmentAdvanced.makeKanji(madoka));
        qr = QrCode.encodeSegments(segs, QrCode.Ecc.LOW);
        Utils.writePng(Utils.toImage(qr, 9, 4, 0xE0F0FF, 0x404040), "madoka-kanji-QR.png");
    }

    @Test
    // Creates QR Codes with the same size and contents but different mask patterns.
    public void doMaskDemo() throws IOException {
        QrCode qr;
        List<QrSegment> segs;

        // Project Nayuki URL
        segs = QrSegment.makeSegments("https://www.nayuki.io/");
        qr = QrCode.encodeSegments(segs, QrCode.Ecc.HIGH, QrCode.MIN_VERSION, QrCode.MAX_VERSION, -1, true);  // Automatic mask
        Utils.writePng(Utils.toImage(qr, 8, 6, 0xE0FFE0, 0x206020), "project-nayuki-automask-QR.png");
        qr = QrCode.encodeSegments(segs, QrCode.Ecc.HIGH, QrCode.MIN_VERSION, QrCode.MAX_VERSION, 3, true);  // Force mask 3
        Utils.writePng(Utils.toImage(qr, 8, 6, 0xFFE0E0, 0x602020), "project-nayuki-mask3-QR.png");

        // Chinese text as UTF-8
        segs = QrSegment.makeSegments("維基百科（Wikipedia，聆聽i/ˌwɪkᵻˈpiːdi.ə/）是一個自由內容、公開編輯且多語言的網路百科全書協作計畫");
        qr = QrCode.encodeSegments(segs, QrCode.Ecc.MEDIUM, QrCode.MIN_VERSION, QrCode.MAX_VERSION, 0, true);  // Force mask 0
        Utils.writePng(Utils.toImage(qr, 10, 3), "unicode-mask0-QR.png");
        qr = QrCode.encodeSegments(segs, QrCode.Ecc.MEDIUM, QrCode.MIN_VERSION, QrCode.MAX_VERSION, 1, true);  // Force mask 1
        Utils.writePng(Utils.toImage(qr, 10, 3), "unicode-mask1-QR.png");
        qr = QrCode.encodeSegments(segs, QrCode.Ecc.MEDIUM, QrCode.MIN_VERSION, QrCode.MAX_VERSION, 5, true);  // Force mask 5
        Utils.writePng(Utils.toImage(qr, 10, 3), "unicode-mask5-QR.png");
        qr = QrCode.encodeSegments(segs, QrCode.Ecc.MEDIUM, QrCode.MIN_VERSION, QrCode.MAX_VERSION, 7, true);  // Force mask 7
        Utils.writePng(Utils.toImage(qr, 10, 3), "unicode-mask7-QR.png");
    }


}
