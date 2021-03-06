package cz.sevrjukov.qrpcr.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import cz.sevrjukov.qrpcr.domain.QRGenerationRequest;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QrGeneratorService {

	private final DigitalSignatureService digitalSignatureService;

	private final String FIELD_SEPARATOR = ":";

	public BufferedImage generateQrCode(final QRGenerationRequest request) throws Exception {

		var barcodeText = prepareContent(request);

		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

		final QRCodeWriter barcodeWriter = new QRCodeWriter();
		final BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 400, 400, hintMap);

		return MatrixToImageWriter.toBufferedImage(bitMatrix);

	}

	private String prepareContent(final QRGenerationRequest request) throws Exception {

		final StringBuffer buffer = new StringBuffer();

		buffer.append(cleanInput(request.getFirstName()));
		buffer.append(FIELD_SEPARATOR);

		buffer.append(cleanInput(request.getLastName()));
		buffer.append(FIELD_SEPARATOR);

		buffer.append(cleanInput(request.getBirthNumber()));
		buffer.append(FIELD_SEPARATOR);

		buffer.append(request.getTestFacilityId());
		buffer.append(FIELD_SEPARATOR);

		buffer.append(request.getTestId());
		buffer.append(FIELD_SEPARATOR);

		buffer.append(request.getTestTime().toEpochSecond(ZoneOffset.UTC));
		buffer.append(FIELD_SEPARATOR);

		buffer.append(request.getResult().getCode());
		buffer.append(FIELD_SEPARATOR);

		final String contentToSign = buffer.toString();
		byte[] signatureBytes = digitalSignatureService.signDataECDSA(contentToSign.getBytes(StandardCharsets.UTF_8));

		final var signatureString = Hex.toHexString(signatureBytes).toUpperCase(Locale.ROOT);
		// only include first 16 bytes of signature, to save space in QR code
		buffer.append(signatureString);

		return buffer.toString();
	}


	private String cleanInput(final String input) {
		String cleaned = Normalizer.normalize(input, Normalizer.Form.NFD);
		cleaned = cleaned.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		cleaned = cleaned.trim();
		cleaned = cleaned.toUpperCase(Locale.ROOT);
		return cleaned;
	}


}
