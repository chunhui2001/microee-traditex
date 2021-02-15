package com.microee.traditex.inbox.oem.constrants;

import java.util.Arrays;

public enum VENDER {

	// @formatter:off
    HBiTex("HBG", "HB Global"),
    HBiJTex("HBJ", "HB Japan"),
    JumpTrading("JumpTrading", "JumpTrading"),
    CumberLand("CumberLand", "CumberLand"),
    B2C2("B2C2", "B2C2"),
    Oanda("Oanda", "汇率对冲平台");
	// @formatter:on

	public String code;
	public String desc;

	VENDER(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static VENDER get(final String code) {
		return Arrays.asList(VENDER.values()).stream().filter(p -> p.name().equals(code) || p.code.equals(code))
				.findFirst().orElse(null);
	}
}
