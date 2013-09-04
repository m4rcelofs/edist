package com.erep.dist.erep;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;

public class RankCalculator {

	public enum Rank {
		_1("Recruit", 0, 15),
		_2("Private", 15, 45),
		_3("Private*", 45, 80),
		_4("Private**", 80, 120),
		_5("Private***", 120, 170),
		_6("Corporal", 170, 250),
		_7("Corporal*", 250, 350),
		_8("Corporal**", 350, 450),
		_9("Corporal***", 450, 600),
		_10("Sergeant", 600, 800),
		_11("Sergeant*", 800, 1000),
		_12("Sergeant**", 1000, 1400),
		_13("Sergeant***", 1400, 1850),
		_14("Lieutenant", 1850, 2350),
		_15("Lieutenant*", 2350, 3000),
		_16("Lieutenant**", 3000, 3750),
		_17("Lieutenant***", 3750, 5000),
		_18("Captain", 5000, 6500),
		_19("Captain*", 6500, 9000),
		_20("Captain**", 9000, 12000),
		_21("Captain***", 12000, 15500),
		_22("Major", 15500, 20000),
		_23("Major*", 20000, 25000),
		_24("Major**", 25000, 31000),
		_25("Major***", 31000, 40000),
		_26("Commander", 40000, 52000),
		_27("Commander*", 52000, 67000),
		_28("Commander**", 67000, 85000),
		_29("Commander***", 85000, 110000),
		_30("Lt Colonel", 110000, 140000),
		_31("Lt Colonel*", 140000, 180000),
		_32("Lt Colonel**", 180000, 225000),
		_33("Lt Colonel***", 225000, 285000),
		_34("Colonel", 285000, 355000),
		_35("Colonel*", 355000, 435000),
		_36("Colonel**", 435000, 540000),
		_37("Colonel***", 540000, 660000),
		_38("General", 660000, 800000),
		_39("General*", 800000, 950000),
		_40("General**", 950000, 1140000),
		_41("General***", 1140000, 1350000),
		_42("Field Marshal", 1350000, 1600000),
		_43("Field Marshal*", 1600000, 1875000),
		_44("Field Marshal**", 1875000, 2185000),
		_45("Field Marshal***", 2185000, 2550000),
		_46("Supreme Marshal", 2550000, 3000000),
		_47("Supreme Marshal*", 3000000, 3500000),
		_48("Supreme Marshal**", 3500000, 4150000),
		_49("Supreme Marshal***", 4150000, 4900000),
		_50("National Force", 4900000, 5800000),
		_51("National Force*", 5800000, 7000000),
		_52("National Force**", 7000000, 9000000),
		_53("National Force***", 9000000, 11500000),
		_54("World Class Force", 11500000, 14500000),
		_55("World Class Force*", 14500000, 18000000),
		_56("World Class Force**", 18000000, 22000000),
		_57("World Class Force***", 22000000, 26500000),
		_58("Legendary Force", 26500000, 31500000),
		_59("Legendary Force*", 31500000, 37000000),
		_60("Legendary Force**", 37000000, 43000000),
		_61("Legendary Force***", 43000000, 50000000),
		_62("God of War", 50000000, 100000000),
		_63("God of War*", 100000000, 200000000),
		_64("God of War**", 200000000, 500000000),
		_65("God of War***", 500000000, 1000000000),
		_66("Titan", 1000000000, 2000000000),
		_67("Titan*", 2000000000, 4000000000l),
		_68("Titan**", 4000000000l, 10000000000l),
		_69("Titan***", 10000000000l, Long.MAX_VALUE)
		;
		
		public String name;
		public long lower;
		public long upper;
		
		private Rank(String name, long lower, long upper) {
			this.name = name;
			this.lower = lower;
			this.upper = upper;
		}
	}
	
	private static final RangeMap<Long, Integer> rankMultiplierMap;
	
	static {
		ImmutableRangeMap.Builder<Long, Integer> builder = ImmutableRangeMap.builder();
		for(Rank rank : Rank.values()) {
			Integer rankMultiplier = Integer.valueOf(rank.name().replace("_", ""));
			builder.put(Range.closedOpen(rank.lower, rank.upper), rankMultiplier);
		}
		rankMultiplierMap = builder.build();
	}
	
	public  Integer rankMultiplier(Long rankValue) {
		return rankMultiplierMap.get(rankValue);
	}
	
	
}
