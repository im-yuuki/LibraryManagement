package assignment.librarymanager.utils;

import com.google.zxing.EncodeHintType;
import javafx.scene.image.Image;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class QRGenerator {

	public static Image generateQRCode(String text) throws IOException, WriterException {
		String utf8Text = new String(text.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

		HashMap<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(utf8Text, BarcodeFormat.QR_CODE, 300, 300, hints);

		ByteArrayOutputStream bufferedImageStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bufferedImageStream);

		return new Image(new ByteArrayInputStream(bufferedImageStream.toByteArray()), 300, 300, true, true);
	}

}
