package com.erep.dist.dwr;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.data.Player;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.RocketQuality;
import com.erep.dist.dwr.TestDTO.TestEnum;

@RemoteProxy
public class TestService {

	@RemoteMethod
	public int theAnswer() {
		return 42;
	}

	@RemoteMethod
	public Double multiply(Double one, Double two) {
		return one * two;
	}

	@RemoteMethod
	public TestDTO testDTO(String id) {
		TestDTO dto = new TestDTO();

		dto.setCurrentMaxDailyFF(160);
		dto.setPlayerErId(id);
		dto.setStatus(PlayerStatus.ENABLED.name());
		dto.setTestEnum(TestEnum.a);
		dto.setRocketEnum(RocketQuality.Q1);

		return dto;
	}
}
