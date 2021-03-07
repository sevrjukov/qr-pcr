package cz.sevrjukov.qrpcr.domain;

public enum TestResult {


	POSITIVE(1),
	NEGATIVE(1),
	UNKNOWN(2);
	private int code;

	 TestResult(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
