package com.erep.dist.erep;

import com.erep.dist.data.Player;

public enum eRepURL
{
	ARTICLE("http://www.erepublik.com/en/article/{id}/1/20"), 
	CITIZEN_ACCOUNT("http://www.erepublik.com/en/economy/citizen-accounts/{id}"),
	CITIZEN("http://www.erepublik.com/en/citizen/profile/{id}"), 
	PARTY_MEMBERS("http://www.erepublik.com/en/main/party-members/{id}"), 
	COUNTRY_LAWS("http://www.erepublik.com/en/country-administration/{id}/1"), 
	LAW("http://www.erepublik.com/en/main/law/Brazil/{id}"), 
	COUNTRY_ECONOMY("http://www.erepublik.com/en/country/economy/{id}"),
	DONATE_ITEMS("http://www.erepublik.com/en/economy/donate-items/{id}"),
	DONATE_ITEMS_ACTION("http://www.erepublik.com/en/economy/donate-items-action"),
	DONATE_MONEY("http://www.erepublik.com/en/economy/donate-money/{id}")
	;

	private String	url;

	private eRepURL(String url) {
		this.url = url;
	}

	public String getUrl(String id) {
		return this.url.replace("{id}", id);
	}
	
	public String getUrl(Player player) {
		return this.url.replace("{id}", player.getKey().getId()+"");
	}
}
