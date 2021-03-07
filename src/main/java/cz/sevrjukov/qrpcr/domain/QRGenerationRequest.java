package cz.sevrjukov.qrpcr.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Builder
public class QRGenerationRequest {



	/*
	jmeno prijmeni, rodne cislo nebo cislo dokladu, ID testu, timestamp, vysledek, digitalni podpis

	 */

	private final String firstName;
	private final String lastName;
	private final String birthNumber;

	private final int testFacilityId;
	private final long testId;

	private final LocalDateTime testTime;
	private final TestResult result;


}
