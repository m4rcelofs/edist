package com.erep.dist.dwr.dto;

import com.erep.dist.data.DistConfig;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;

public class TransferHelper {

	public static PlayerDTO toDTO(Player player) {

		PlayerDTO dto = new PlayerDTO();

		dto.setAvatar(player.getAvatar());
		dto.setConfig(toDTO(player.getConfig()));
		dto.setErId(player.getErId());
		dto.setGroup(toBasicDTO(player.getGroup()));
		dto.setName(player.getName());
		dto.setRocketEnabled(player.getRocketEnabled());
		dto.setStatus(player.getStatus());

		return dto;
	}

	public static DistConfigDTO toDTO(DistConfig config) {
		DistConfigDTO dto = new DistConfigDTO();

		dto.setAutoSend(config.getAutoSend());
		dto.setCurrentMaxDailyFF(config.getCurrentMaxDailyFF());
		dto.setRocketAllowed(config.getRocketAllowed());

		return dto;
	}

	public static DistConfig fromDTO(DistConfigDTO dto) {
		DistConfig config = new DistConfig();
		config.setAutoSend(dto.getAutoSend());
		config.setCurrentMaxDailyFF(dto.getCurrentMaxDailyFF());
		config.setRocketAllowed(dto.getRocketAllowed());
		return config;
	}

	public static PlayerGroupDTO toBasicDTO(PlayerGroup group) {
		PlayerGroupDTO dto = new PlayerGroupDTO();

		dto.setGroupName(group.getGroupName());
		dto.setId(group.getId().getName());

		return dto;
	}

	public static PlayerGroupDTO toCompleteDTO(PlayerGroup group) {
		PlayerGroupDTO dto = toBasicDTO(group);
		dto.setConfig(toDTO(group.getConfig()));
		return dto;
	}

}
