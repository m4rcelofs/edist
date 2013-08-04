package com.erep.dist.erep;

public enum eRepURL
{
	ARTICLE("http://www.erepublik.com/en/article/{id}/1/20"), CITIZEN_ACCOUNT(
			"http://www.erepublik.com/en/economy/citizen-accounts/{id}"), CITIZEN(
			"http://www.erepublik.com/en/citizen/profile/{id}"), PARTY_MEMBERS(
			"http://www.erepublik.com/en/main/party-members/{id}"), COUNTRY_LAWS(
			"http://www.erepublik.com/en/country-administration/{id}/1"), LAW(
			"http://www.erepublik.com/en/main/law/Brazil/{id}"), COUNTRY_ECONOMY(
			"http://www.erepublik.com/en/country/economy/{id}");

	private String	url;

	private eRepURL(String url) {
		this.url = url;
	}

	public String getUrl(String id) {
		return this.url.replace("{id}", id);
	}
}
