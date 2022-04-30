package com.ajaxjs.convert;

import com.google.common.base.Strings;
import com.google.common.net.MediaType;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.Canvas;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Base64;

/**
 * 
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class PdfConverter {
	public static final String BASE64_PREFIX = "data:";
	public static final float DEFAULT_FONT_SIZE = 80F;

	public static final String[] OFFICE_CONTENT_TYPES = new String[] { "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
			"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint",
			"application/vnd.openxmlformats-officedocument.presentationml.presentation" };

	public static final String KIND = "pdf";

	private String libreofficePath;

	public MediaType convert(ConvertInput input, String convertParamsStr, Path outputFile) throws Exception {
		PdfParams convertParams = null;
		if (!Strings.isNullOrEmpty(convertParamsStr)) {
			convertParams = new ObjectMapper().readValue(convertParamsStr, PdfParams.class);
		} else {
			convertParams = new PdfParams();
		}

		Path inputFile = input.file();

		if (isOfficeFile(input.status().getContentType())) {
			Path convertedFile = outputFile.getParent().resolve(com.google.common.io.Files.getNameWithoutExtension(input.file().getFileName().toString()) + ".pdf");

			CommandLine cmdLine = CommandLine
					.parse(String.format("soffice --headless --convert-to pdf %s --outdir  %s", input.file().toAbsolutePath().toString(), outputFile.getParent().toAbsolutePath()));
			DefaultExecutor executor = new DefaultExecutor();
			executor.setStreamHandler(new PumpStreamHandler(new OutputStream() {
				@Override
				public void write(byte[] b) throws IOException {
					write(b, 0, b.length);
				}

				@Override
				public void write(byte[] b, int off, int len) throws IOException {
					log.info(new String(b, off, len));
				}

				@Override
				public void write(int b) throws IOException {
					write(new byte[] { (byte) b });
				}
			}));

			if (executor.execute(cmdLine) != 0)
				throw new IllegalStateException("Failed execute libreoffice convert-to pdf, libreoffice process exit value=");

			Files.move(convertedFile, outputFile, StandardCopyOption.REPLACE_EXISTING);
			inputFile = outputFile;
		}

		// 加水印
		if (convertParams.getWatermark() != null) {
			Path convertedFile = outputFile.getParent().resolve(com.google.common.io.Files.getNameWithoutExtension(input.file().getFileName().toString()) + ".pdf");
			Files.deleteIfExists(convertedFile);

			try (FileOutputStream outStream = new FileOutputStream(convertedFile.toFile()); FileInputStream inStream = new FileInputStream(inputFile.toFile())) {
				setWatemark(convertParams.getWatermark(), inStream, outStream);
			}

			Files.move(convertedFile, outputFile, StandardCopyOption.REPLACE_EXISTING);
		}

		return MediaType.parse("application/pdf");
	}

	public void setWatemark(PdfParams.Watermark watermark, InputStream inStream, OutputStream outStream) throws Exception {
		ImageData watermarkImage = null;
		String watermarkText = null;

		if (!Strings.isNullOrEmpty(watermark.getImage())) {
			if (watermark.getImage().startsWith(BASE64_PREFIX)) {
				String data = watermark.getImage().substring(watermark.getImage().indexOf(",") + 1);
				watermarkImage = ImageDataFactory.create(Base64.getDecoder().decode(data));
			} else
				watermarkImage = ImageDataFactory.create(new URL(watermark.getImage()));
		} else
			watermarkText = watermark.getText();

		PdfReader reader = new PdfReader(inStream);
		PdfDocument doc = new PdfDocument(reader, new PdfWriter(outStream));

		for (int iPage = 1; iPage <= doc.getNumberOfPages(); iPage++) {
			PdfPage page = doc.getPage(iPage);
			Rectangle pageSize = page.getPageSize();
			Canvas canvas = new Canvas(new PdfCanvas(page), doc, pageSize);

			if (watermarkImage != null)
				throw new UnsupportedOperationException();
			else {
				canvas.setFontSize(MoreObjects.firstNonNull(watermark.getFontSize(), DEFAULT_FONT_SIZE));
				canvas.setFontColor(DeviceGray.GRAY, 0.5F);

				if (Strings.isNullOrEmpty(watermark.getFont()))
					canvas.setFont(PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H", true));
				else
					canvas.setFont(PdfFontFactory.createFont(watermark.getFont()));

				canvas.showTextAligned(watermarkText, pageSize.getWidth() / 2, pageSize.getHeight() / 2, TextAlignment.CENTER, 0.5F);
			}
		}

		doc.close();
	}

	static boolean isOfficeFile(String contentType) {
		if (!Strings.isNullOrEmpty(contentType))
			return Arrays.asList(OFFICE_CONTENT_TYPES).stream().filter(it -> contentType.contains(it)).findAny().isPresent();

		return false;
	}
}
