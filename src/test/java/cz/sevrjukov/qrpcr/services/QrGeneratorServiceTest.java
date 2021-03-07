package cz.sevrjukov.qrpcr.services;


import com.google.zxing.WriterException;
import cz.sevrjukov.qrpcr.domain.QRGenerationRequest;
import cz.sevrjukov.qrpcr.domain.TestResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@ContextConfiguration(
		classes = {QrGeneratorService.class, DigitalSignatureService.class}
)
public class QrGeneratorServiceTest {

	@Autowired
	private QrGeneratorService tested;

	@Test
	public void testQrGeneration() throws Exception {

		final String tmpDir = "/tmp";
		final String fileName = LocalDateTime.now().toString() + ".png";

		final Path path = Paths.get(tmpDir, fileName);
		final File imgFile = path.toFile();
		System.out.println(path.toAbsolutePath().toString());

		final QRGenerationRequest request = QRGenerationRequest
				.builder()
				.firstName("Vaclav")
				.lastName("Novacek")
				.birthNumber("7256890245")
				.testId(100062565L)
				.testFacilityId(85050)
				.result(TestResult.NEGATIVE)
				.testTime(LocalDateTime.now())
				.build();

		final BufferedImage qrCode = tested.generateQrCode(request);

		ImageIO.write(qrCode, "png", imgFile);

	}
}
