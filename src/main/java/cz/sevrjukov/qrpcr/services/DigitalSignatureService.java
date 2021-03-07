package cz.sevrjukov.qrpcr.services;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;

@Service
public class DigitalSignatureService {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static final String keystorePath = "/tmp/qr_test_keystore.p12";
	private static final String keystorePathEc = "/tmp/qr_test_keystore_ec.p12";
	private static final String store_pass = "password";
	private static final String keypair_name = "signerKeyPair";

	public byte[] signDataRSA(final byte[] data) throws Exception {
		Signature signature = Signature.getInstance("SHA256withRSA", "BC");
		signature.initSign(loadPrivKeyRsa());
		signature.update(data);
		return signature.sign();
	}

	public byte [] signDataECDSA (final byte [] data) throws Exception {
		Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
		signature.initSign(loadPrivKeyEC());
		signature.update(data);
		return signature.sign();
	}

	private PrivateKey loadPrivKeyRsa() throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(new FileInputStream(keystorePath), store_pass.toCharArray());
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(keypair_name, store_pass.toCharArray());
		return privateKey;
	}

	private PrivateKey loadPrivKeyEC() throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(new FileInputStream(keystorePathEc), store_pass.toCharArray());
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(keypair_name, store_pass.toCharArray());
		return privateKey;
	}

}
