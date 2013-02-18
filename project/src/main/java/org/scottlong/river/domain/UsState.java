package org.scottlong.river.domain;

public enum UsState {
	
	USA(-1, "USA"),

	AL(1, "Alabama"),
	MO(29, "Missouri"),
	AK(2, "Alaska"),
	MT(30, "Montana"),
	AZ(4, "Arizona"),
	NE(31, "Nebraska"),
	AR(5, "Arkansas"),
	NV(32, "Nevada"),
	CA(6, "California"),
	NH(33, "New Hampshire"),
	CO(8, "Colorado"),
	NJ(34, "New Jersey"),
	CT(9, "Connecticut"),
	NM(35, "New Mexico"),
	DE(10, "Delaware"),
	NY(36, "New York"),
	DC(11, "District of Columbia"),
	NC(37, "North Carolina"),
	FL(12, "Florida"),
	ND(38, "North Dakota"),
	GA(13, "Georgia"),
	OH(39, "Ohio"),
	OK(40, "Oklahoma"),
	OR(41, "Oregon"),
	HI(15, "Hawaii"),
	PA(42, "Pennsylvania"),
	ID(16, "Idaho"),
	RI(44, "Rhode Island"),
	IL(17, "Illinois"),
	SC(45, "South Carolina"),
	IN(18, "Indiana"),
	SD(46, "South Dakota"),
	IA(19, "Iowa"),
	TN(47, "Tennessee"),
	KS(20, "Kansas"),
	TX(48, "Texas"),
	KY(21, "Kentucky"),
	UT(49, "Utah"),
	LA(22, "Louisiana"),
	VT(50, "Vermont"),
	ME(23, "Maine"),
	VA(51, "Virginia"),
	MD(24, "Maryland"),
	WA(53, "Washington"),
	MA(25, "Massachusetts"),
	WV(54, "West Virginia"),
	MI(26, "Michigan"),
	WI(55, "Wisconsin"),
	MN(27, "Minnesota"),
	WY(56, "Wyoming"),
	MS(28, "Mississippi"),
	AS(60, "American Samoa"),
	FM(64, "Federated States of Micronesia"),
	GU(66, "Guam"),
	MH(68, "Marshall Islands"),
	MP(69, "Northern Mariana Islands"),
	PW(70, "Palau"),
	PR(72, "Puerto Rico"),
	UM(74, "U.S. Minor Outlying Islands"),
	VI(78, "Virgin Islands of the U.S.");

	int id;
	String name;

	private UsState(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
    	return id;
    }

	public String getName() {
		return name;
	}
	
	public static UsState forId(int id) {
		for (UsState state : UsState.values()) {
			if (state.getId() == id)
				return state;
		}
		return null;
	}

	public static UsState forCode(String code) {
		for (UsState state : UsState.values()) {
			if (state.name().equalsIgnoreCase(code))
				return state;
		}
		return null;
	}

}
