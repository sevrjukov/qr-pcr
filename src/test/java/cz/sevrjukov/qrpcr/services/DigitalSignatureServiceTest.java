package cz.sevrjukov.qrpcr.services;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@ContextConfiguration(
		classes = {DigitalSignatureService.class}
)
public class DigitalSignatureServiceTest {

	@Autowired
	private DigitalSignatureService tested;

	@Test
	public void testSignature() throws Exception {

		var contentToSign = "VACLAV:NOVACEK:7256890245:85050:100062565:1615140115:1:";

		byte[] signature = tested.signDataECDSA(contentToSign.getBytes(StandardCharsets.UTF_8));
		System.out.println(Hex.toHexString(signature));
	}
}
