package com.erep.dist.dwr;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.convert.EnumConverter;

import com.erep.dist.data.enums.RocketQuality;

@DataTransferObject
public class TestDTO {

	@DataTransferObject(converter = EnumConverter.class)
	public enum TestEnum
	{
		a, b, c
	}

	@RemoteProperty
	private String			playerErId;

	@RemoteProperty
	private String			status;

	@RemoteProperty
	private Integer			currentMaxDailyFF;

	@RemoteProperty
	private TestEnum		testEnum;

	@RemoteProperty
	private RocketQuality	rocketEnum;

	public String getPlayerErId() {
		return playerErId;
	}

	public void setPlayerErId(String playerErId) {
		this.playerErId = playerErId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCurrentMaxDailyFF() {
		return currentMaxDailyFF;
	}

	public void setCurrentMaxDailyFF(Integer currentMaxDailyFF) {
		this.currentMaxDailyFF = currentMaxDailyFF;
	}

	public TestEnum getTestEnum() {
		return testEnum;
	}

	public void setTestEnum(TestEnum testEnum) {
		this.testEnum = testEnum;
	}

	public RocketQuality getRocketEnum() {
		return rocketEnum;
	}

	public void setRocketEnum(RocketQuality rocketEnum) {
		this.rocketEnum = rocketEnum;
	}
}
