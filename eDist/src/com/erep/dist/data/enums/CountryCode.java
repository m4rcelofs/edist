package com.erep.dist.data.enums;

import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.convert.EnumConverter;

@DataTransferObject(converter = EnumConverter.class)
public enum CountryCode
{
	AL("Albania"),
	AR("Argentina"),
	AU("Australia"),
	AT("Austria"),
	BY("Belarus"),
	BE("Belgium"),
	BO("Bolivia"),
	BA("Bosnia and Herzegovina"),
	BR("Brazil"),
	BG("Bulgaria"),
	CA("Canada"),
	CL("Chile"),
	CN("China"),
	CO("Colombia"),
	HR("Croatia"),
	CY("Cyprus"),
	CZ("Czech Republic"),
	DK("Denmark"),
	EG("Egypt"),
	EE("Estonia"),
	FI("Finland"),
	FR("France"),
	DE("Germany"),
	GR("Greece"),
	HU("Hungary"),
	IN("India"),
	ID("Indonesia"),
	IR("Iran"),
	IE("Ireland"),
	IL("Israel"),
	IT("Italy"),
	JP("Japan"),
	LV("Latvia"),
	LT("Lithuania"),
	MY("Malaysia"),
	MX("Mexico"),
	ME("Montenegro"),
	NL("Netherlands"),
	NZ("New Zealand"),
	KP("North Korea"),
	NO("Norway"),
	PK("Pakistan"),
	PY("Paraguay"),
	PE("Peru"),
	PH("Philippines"),
	PL("Poland"),
	PT("Portugal"),
	TW("Republic of China (Taiwan)"),
	MK("Republic of Macedonia (FYROM)"),
	MD("Republic of Moldova"),
	RO("Romania"),
	RU("Russia"),
	SA("Saudi Arabia"),
	RS("Serbia"),
	SG("Singapore"),
	SK("Slovakia"),
	SI("Slovenia"),
	ZA("South Africa"),
	KR("South Korea"),
	ES("Spain"),
	SE("Sweden"),
	CH("Switzerland"),
	TH("Thailand"),
	TR("Turkey"),
	UA("Ukraine"),
	AE("United Arab Emirates"),
	GB("United Kingdom"),
	UY("Uruguay"),
	US("USA"),
	VE("Venezuela"),
	ZZ("Unknown");

	public String							countryName;

	private static Map<String, CountryCode>	nameToCode;

	private CountryCode(String country) {
		this.countryName = country;
	}

	public static CountryCode getCode(String countryName) {
		if (nameToCode == null)
			buildMap();
		if (countryName != null)
			countryName = countryName.toLowerCase().trim();
		if (nameToCode.containsKey(countryName))
			return nameToCode.get(countryName);
		return ZZ;
	}

	private static void buildMap() {
		nameToCode = new HashMap<String, CountryCode>();
		for (CountryCode code : CountryCode.values())
			nameToCode.put(code.countryName.toLowerCase(), code);
	}

}
