package nationalcipher.cipher.stats;

import java.util.HashMap;
import java.util.TreeMap;

import nationalcipher.lib.StatisticsLib;

public class CipherStatistics extends StatisticsLib {

	private static TreeMap<String, Object> map;
	
	public static TreeMap<String, Object> getOtherCipherStatistics() {
		if(map == null) {
			map = new TreeMap<String, Object>();
			//HashMap<String, DataHolder>
			HashMap<String, DataHolder> hill2x2 = createOrGetList("Hill", "2x2");
			hill2x2.put(IC_x1000, new DataHolder(42.00, 2.61)); //Min: 35.58 Max: 74.33
			hill2x2.put(IC_MAX_1to15_x1000, new DataHolder(47.18, 5.07)); //Min: 39.63 Max: 72.02
			hill2x2.put(IC_2_TRUE_x10000, new DataHolder(32.75, 2.99)); //Min: 19.41 Max: 56.57
			hill2x2.put(IC_2_FALSE_x10000, new DataHolder(76.77, 7.28)); //Min: 58.64 Max: 91.37
			hill2x2.put(IC_3_FALSE_x100000, new DataHolder(40.67, 16.80)); //Min: 4.23 Max: 209.64
			hill2x2.put(IC_KAPPA_x1000, new DataHolder(56.24, 8.47)); //Min: 39.01 Max: 117.28
			hill2x2.put(LOG_DIGRAPH, new DataHolder(435.93, 26.44)); //Min: 337.00 Max: 671.00
			//hill2x2.put(LOG_DIGRAPH_REVERSED, new DataHolder(435.41, 29.07)); //Min: 320.00 Max: 661.00
			hill2x2.put(LONG_REPEAT, new DataHolder(11.07, 1.48)); //Min: 4.84 Max: 24.11
			hill2x2.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(21.86, 3.43)); //Min: 7.00 Max: 47.00
			hill2x2.put(NORMAL_ORDER, new DataHolder(215.51, 30.18)); //Min: 40.00 Max: 306.00
			hill2x2.put(TEXT_LENGTH_MULTIPLE, new DataHolder(2));
			
			HashMap<String, DataHolder> hill3x3 = createOrGetList("Hill", "3x3");
			hill3x3.put(IC_x1000, new DataHolder(39.19, 0.79)); //Min: 35.12 Max: 47.30
			hill3x3.put(IC_MAX_1to15_x1000, new DataHolder(41.99, 2.28)); //Min: 37.04 Max: 61.11
			hill3x3.put(IC_2_TRUE_x10000, new DataHolder(19.03, 2.05)); //Min: 7.76 Max: 32.45
			hill3x3.put(IC_2_FALSE_x10000, new DataHolder(18.89, 2.49)); //Min: 2.34 Max: 40.12
			hill3x3.put(IC_3_FALSE_x100000, new DataHolder(169.82, 58.61)); //Min: 80.33 Max: 375.91
			hill3x3.put(IC_KAPPA_x1000, new DataHolder(51.43, 6.54)); //Min: 39.60 Max: 112.90
			hill3x3.put(LOG_DIGRAPH, new DataHolder(428.97, 16.43)); //Min: 337.00 Max: 538.00
			//hill3x3.put(LOG_DIGRAPH_REVERSED, new DataHolder(429.12, 18.01)); //Min: 326.00 Max: 517.00
			hill3x3.put(LONG_REPEAT, new DataHolder(10.46, 1.35)); //Min: 6.17 Max: 16.41
			hill3x3.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.89, 3.07)); //Min: 20.00 Max: 81.00
			hill3x3.put(NORMAL_ORDER, new DataHolder(221.35, 28.74)); //Min: 68.00 Max: 316.00
			hill3x3.put(TEXT_LENGTH_MULTIPLE, new DataHolder(3));
			
			HashMap<String, DataHolder> cadenusx = createOrGetList("Cadenus", "w/x");
			cadenusx.put(IC_x1000, new DataHolder(64.81, 7.69)); //Min: 57.75 Max: 153.86
			cadenusx.put(IC_MAX_1to15_x1000, new DataHolder(66.22, 7.63)); //Min: 57.75 Max: 159.93
			cadenusx.put(IC_2_TRUE_x10000, new DataHolder(66.37, 70.90)); //Min: 24.87 Max: 985.88
			cadenusx.put(IC_2_FALSE_x10000, new DataHolder(66.14, 70.10)); //Min: 20.20 Max: 930.32
			cadenusx.put(IC_3_FALSE_x100000, new DataHolder(193.88, 459.83)); //Min: 0.00 Max: 6788.13
			cadenusx.put(IC_KAPPA_x1000, new DataHolder(109.06, 39.35)); //Min: 64.44 Max: 368.00
			cadenusx.put(LOG_DIGRAPH, new DataHolder(630.82, 29.40)); //Min: 432.00 Max: 763.00
			cadenusx.put(LOG_DIGRAPH_REVERSED, new DataHolder(473.01, 277.33)); //Min: 0.00 Max: 761.00
			cadenusx.put(LONG_REPEAT, new DataHolder(16.40, 9.74)); //Min: 0.00 Max: 109.47
			cadenusx.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.57, 1.72)); //Min: 22.00 Max: 62.00
			cadenusx.put(NORMAL_ORDER, new DataHolder(83.50, 29.86)); //Min: 22.00 Max: 150.00
			cadenusx.put(TEXT_LENGTH_MULTIPLE, new DataHolder(25));
			
			HashMap<String, DataHolder> cadenus = createOrGetList("Cadenus", "!w/x");
			cadenus.put(IC_x1000, new DataHolder(66.14, 2.73)); //Min: 58.38 Max: 75.46
			cadenus.put(IC_MAX_1to15_x1000, new DataHolder(68.47, 3.52)); //Min: 61.25 Max: 116.19
			cadenus.put(IC_2_TRUE_x10000, new DataHolder(44.07, 4.22)); //Min: 20.61 Max: 79.96
			cadenus.put(IC_2_FALSE_x10000, new DataHolder(44.42, 6.05)); //Min: 0.00 Max: 108.11
			cadenus.put(IC_3_FALSE_x100000, new DataHolder(31.48, 15.17)); //Min: 0.00 Max: 268.46
			cadenus.put(IC_KAPPA_x1000, new DataHolder(80.65, 7.91)); //Min: 63.85 Max: 160.00
			cadenus.put(LOG_DIGRAPH, new DataHolder(658.00, 9.13)); //Min: 593.00 Max: 761.00
			cadenus.put(LOG_DIGRAPH_REVERSED, new DataHolder(488.87, 289.30)); //Min: 0.00 Max: 770.00
			cadenus.put(LONG_REPEAT, new DataHolder(11.86, 1.43)); //Min: 0.00 Max: 25.22
			cadenus.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.43, 2.44)); //Min: 15.00 Max: 75.00
			cadenus.put(NORMAL_ORDER, new DataHolder(76.69, 31.99)); //Min: 24.00 Max: 166.00
			cadenus.put(TEXT_LENGTH_MULTIPLE, new DataHolder(25));
			
			HashMap<String, DataHolder> beaufortSlidefair = createOrGetList("Slidefair", "Beaufort");
			beaufortSlidefair.put(IC_x1000, new DataHolder(41.86, 3.40)); //Min: 34.91 Max: 62.52
			beaufortSlidefair.put(IC_MAX_1to15_x1000, new DataHolder(56.99, 7.41)); //Min: 40.63 Max: 76.51
			beaufortSlidefair.put(IC_2_TRUE_x10000, new DataHolder(19.62, 4.37)); //Min: 6.37 Max: 46.24
			beaufortSlidefair.put(IC_2_FALSE_x10000, new DataHolder(24.23, 8.09)); //Min: 3.16 Max: 71.67
			beaufortSlidefair.put(IC_3_FALSE_x100000, new DataHolder(17.28, 13.32)); //Min: 0.00 Max: 163.93
			beaufortSlidefair.put(IC_KAPPA_x1000, new DataHolder(57.16, 11.13)); //Min: 37.91 Max: 112.50
			beaufortSlidefair.put(LOG_DIGRAPH, new DataHolder(419.53, 25.15)); //Min: 289.00 Max: 531.00
			beaufortSlidefair.put(LOG_DIGRAPH_REVERSED, new DataHolder(422.77, 27.03)); //Min: 307.00 Max: 545.00
			beaufortSlidefair.put(LONG_REPEAT, new DataHolder(7.03, 1.56)); //Min: 0.00 Max: 14.38
			beaufortSlidefair.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(44.38, 3.87)); //Min: 18.00 Max: 71.00
			beaufortSlidefair.put(NORMAL_ORDER, new DataHolder(235.75, 30.16)); //Min: 134.00 Max: 326.00
			beaufortSlidefair.put(LOG_DIGRAPH_SLIDEFAIR_BEAUFORT, new DataHolder(747.42, 8.51)); //Min: 710.00 Max: 775.27
			beaufortSlidefair.put(LOG_DIGRAPH_SLIDEFAIR_VARIANT, new DataHolder(553.56, 26.85)); //Min: 509.75 Max: 686.67
			beaufortSlidefair.put(LOG_DIGRAPH_SLIDEFAIR_VIGENERE, new DataHolder(553.88, 26.67)); //Min: 509.00 Max: 693.33
			
			HashMap<String, DataHolder> variantSlidefair = createOrGetList("Slidefair", "Variant");
			variantSlidefair.put(IC_x1000, new DataHolder(40.23, 1.94)); //Min: 35.28 Max: 54.64
			variantSlidefair.put(IC_MAX_1to15_x1000, new DataHolder(57.03, 7.23)); //Min: 37.65 Max: 77.23
			variantSlidefair.put(IC_2_TRUE_x10000, new DataHolder(18.56, 3.16)); //Min: 7.17 Max: 35.95
			variantSlidefair.put(IC_2_FALSE_x10000, new DataHolder(25.43, 8.01)); //Min: 6.33 Max: 64.50
			variantSlidefair.put(IC_3_FALSE_x100000, new DataHolder(16.46, 12.88)); //Min: 0.00 Max: 163.93
			variantSlidefair.put(IC_KAPPA_x1000, new DataHolder(56.45, 10.88)); //Min: 38.88 Max: 125.00
			variantSlidefair.put(LOG_DIGRAPH, new DataHolder(422.12, 25.51)); //Min: 327.00 Max: 579.00
			variantSlidefair.put(LOG_DIGRAPH_REVERSED, new DataHolder(427.09, 31.96)); //Min: 323.00 Max: 628.00
			variantSlidefair.put(LONG_REPEAT, new DataHolder(6.74, 1.36)); //Min: 0.00 Max: 13.31
			variantSlidefair.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(40.22, 6.14)); //Min: 5.00 Max: 70.00
			variantSlidefair.put(NORMAL_ORDER, new DataHolder(239.82, 37.68)); //Min: 80.00 Max: 322.00
			variantSlidefair.put(LOG_DIGRAPH_SLIDEFAIR_BEAUFORT, new DataHolder(554.14, 26.65)); //Min: 514.50 Max: 691.43
			variantSlidefair.put(LOG_DIGRAPH_SLIDEFAIR_VARIANT, new DataHolder(748.43, 7.98)); //Min: 716.15 Max: 769.25
			variantSlidefair.put(LOG_DIGRAPH_SLIDEFAIR_VIGENERE, new DataHolder(748.52, 7.86)); //Min: 717.33 Max: 768.38
			
			HashMap<String, DataHolder> vigenereSlidefair = createOrGetList("Slidefair", "Vigenere");
			vigenereSlidefair.put(IC_x1000, new DataHolder(40.23, 1.92)); //Min: 34.51 Max: 55.96
			vigenereSlidefair.put(IC_MAX_1to15_x1000, new DataHolder(57.17, 7.32)); //Min: 40.03 Max: 76.02
			vigenereSlidefair.put(IC_2_TRUE_x10000, new DataHolder(18.64, 3.19)); //Min: 8.76 Max: 42.03
			vigenereSlidefair.put(IC_2_FALSE_x10000, new DataHolder(25.40, 7.99)); //Min: 3.16 Max: 68.15
			vigenereSlidefair.put(IC_3_FALSE_x100000, new DataHolder(16.91, 13.29)); //Min: 0.00 Max: 128.10
			vigenereSlidefair.put(IC_KAPPA_x1000, new DataHolder(56.72, 10.95)); //Min: 38.46 Max: 118.75
			vigenereSlidefair.put(LOG_DIGRAPH, new DataHolder(421.89, 24.81)); //Min: 318.00 Max: 605.00
			vigenereSlidefair.put(LOG_DIGRAPH_REVERSED, new DataHolder(426.44, 31.20)); //Min: 327.00 Max: 617.00
			vigenereSlidefair.put(LONG_REPEAT, new DataHolder(6.71, 1.33)); //Min: 0.00 Max: 13.31
			vigenereSlidefair.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(40.22, 6.29)); //Min: 7.00 Max: 70.00
			vigenereSlidefair.put(NORMAL_ORDER, new DataHolder(239.49, 37.85)); //Min: 80.00 Max: 330.00
			vigenereSlidefair.put(LOG_DIGRAPH_SLIDEFAIR_BEAUFORT, new DataHolder(554.22, 26.75)); //Min: 510.25 Max: 694.29
			vigenereSlidefair.put(LOG_DIGRAPH_SLIDEFAIR_VARIANT, new DataHolder(748.67, 7.85)); //Min: 712.50 Max: 772.80
			vigenereSlidefair.put(LOG_DIGRAPH_SLIDEFAIR_VIGENERE, new DataHolder(748.52, 8.04)); //Min: 704.33 Max: 772.50
			
			HashMap<String, DataHolder> beaufortAutokey = createOrGetList("Autokey", "Beaufort");
			beaufortAutokey.put(IC_x1000, new DataHolder(39.80, 0.77)); //Min: 35.64 Max: 44.41
			beaufortAutokey.put(IC_MAX_1to15_x1000, new DataHolder(41.37, 1.89)); //Min: 38.82 Max: 62.86
			beaufortAutokey.put(IC_2_TRUE_x10000, new DataHolder(16.72, 1.58)); //Min: 10.21 Max: 28.30
			beaufortAutokey.put(IC_2_FALSE_x10000, new DataHolder(16.68, 2.14)); //Min: 5.75 Max: 34.81
			beaufortAutokey.put(IC_3_FALSE_x100000, new DataHolder(11.37, 13.78)); //Min: 0.00 Max: 178.89
			beaufortAutokey.put(IC_KAPPA_x1000, new DataHolder(51.67, 5.75)); //Min: 39.69 Max: 92.39
			beaufortAutokey.put(LOG_DIGRAPH, new DataHolder(440.05, 12.71)); //Min: 381.00 Max: 495.00
			beaufortAutokey.put(LOG_DIGRAPH_REVERSED, new DataHolder(151.06, 209.34)); //Min: 0.00 Max: 518.00
			beaufortAutokey.put(LONG_REPEAT, new DataHolder(6.10, 1.20)); //Min: 0.00 Max: 12.42
			beaufortAutokey.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.74, 2.95)); //Min: 26.00 Max: 73.00
			beaufortAutokey.put(NORMAL_ORDER, new DataHolder(206.95, 25.40)); //Min: 126.00 Max: 278.00
			beaufortAutokey.put(LOG_DIGRAPH_AUTOKEY_BEAUFORT, new DataHolder(759.59, 6.66)); //Min: 736.25 Max: 774.10
			beaufortAutokey.put(LOG_DIGRAPH_AUTOKEY_PORTA, new DataHolder(464.20, 23.00)); //Min: 438.60 Max: 586.67
			beaufortAutokey.put(LOG_DIGRAPH_AUTOKEY_VARIANT, new DataHolder(567.29, 24.57)); //Min: 535.50 Max: 690.67
			beaufortAutokey.put(LOG_DIGRAPH_AUTOKEY_VIGENERE, new DataHolder(533.37, 33.17)); //Min: 483.50 Max: 691.33
			
			HashMap<String, DataHolder> portaAutokey = createOrGetList("Autokey", "Porta");
			portaAutokey.put(IC_x1000, new DataHolder(39.32, 0.68)); //Min: 34.94 Max: 45.74
			portaAutokey.put(IC_MAX_1to15_x1000, new DataHolder(40.96, 1.90)); //Min: 38.12 Max: 62.12
			portaAutokey.put(IC_2_TRUE_x10000, new DataHolder(16.29, 1.64)); //Min: 6.29 Max: 27.42
			portaAutokey.put(IC_2_FALSE_x10000, new DataHolder(16.26, 2.22)); //Min: 2.39 Max: 38.22
			portaAutokey.put(IC_3_FALSE_x100000, new DataHolder(11.47, 14.61)); //Min: 0.00 Max: 190.82
			portaAutokey.put(IC_KAPPA_x1000, new DataHolder(50.75, 5.65)); //Min: 40.09 Max: 93.17
			portaAutokey.put(LOG_DIGRAPH, new DataHolder(428.91, 14.65)); //Min: 367.00 Max: 503.00
			portaAutokey.put(LOG_DIGRAPH_REVERSED, new DataHolder(147.20, 204.01)); //Min: 0.00 Max: 492.00
			portaAutokey.put(LONG_REPEAT, new DataHolder(6.02, 1.30)); //Min: 0.00 Max: 12.42
			portaAutokey.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.50, 2.86)); //Min: 16.00 Max: 80.00
			portaAutokey.put(NORMAL_ORDER, new DataHolder(224.47, 28.31)); //Min: 130.00 Max: 308.00
			portaAutokey.put(LOG_DIGRAPH_AUTOKEY_BEAUFORT, new DataHolder(532.18, 31.99)); //Min: 483.79 Max: 672.67
			portaAutokey.put(LOG_DIGRAPH_AUTOKEY_PORTA, new DataHolder(616.85, 147.33)); //Min: 442.79 Max: 775.71
			portaAutokey.put(LOG_DIGRAPH_AUTOKEY_VARIANT, new DataHolder(532.39, 33.03)); //Min: 483.07 Max: 689.33
			portaAutokey.put(LOG_DIGRAPH_AUTOKEY_VIGENERE, new DataHolder(532.28, 32.78)); //Min: 484.73 Max: 676.00
			
			HashMap<String, DataHolder> variantAutokey = createOrGetList("Autokey", "Variant");
			variantAutokey.put(IC_x1000, new DataHolder(39.79, 0.77)); //Min: 35.48 Max: 43.56
			variantAutokey.put(IC_MAX_1to15_x1000, new DataHolder(41.39, 1.87)); //Min: 38.80 Max: 61.85
			variantAutokey.put(IC_2_TRUE_x10000, new DataHolder(16.75, 1.64)); //Min: 9.61 Max: 35.43
			variantAutokey.put(IC_2_FALSE_x10000, new DataHolder(16.71, 2.22)); //Min: 5.75 Max: 37.97
			variantAutokey.put(IC_3_FALSE_x100000, new DataHolder(11.42, 14.19)); //Min: 0.00 Max: 178.89
			variantAutokey.put(IC_KAPPA_x1000, new DataHolder(51.63, 5.76)); //Min: 40.55 Max: 92.39
			variantAutokey.put(LOG_DIGRAPH, new DataHolder(440.80, 12.92)); //Min: 382.00 Max: 517.00
			variantAutokey.put(LOG_DIGRAPH_REVERSED, new DataHolder(151.16, 209.43)); //Min: 0.00 Max: 483.00
			variantAutokey.put(LONG_REPEAT, new DataHolder(6.12, 1.23)); //Min: 0.00 Max: 11.40
			variantAutokey.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.74, 2.95)); //Min: 26.00 Max: 74.00
			variantAutokey.put(NORMAL_ORDER, new DataHolder(204.98, 24.77)); //Min: 106.00 Max: 274.00
			variantAutokey.put(LOG_DIGRAPH_AUTOKEY_BEAUFORT, new DataHolder(568.11, 24.42)); //Min: 532.67 Max: 690.67
			variantAutokey.put(LOG_DIGRAPH_AUTOKEY_PORTA, new DataHolder(463.90, 22.99)); //Min: 437.86 Max: 584.57
			variantAutokey.put(LOG_DIGRAPH_AUTOKEY_VARIANT, new DataHolder(759.63, 6.65)); //Min: 736.25 Max: 774.47
			variantAutokey.put(LOG_DIGRAPH_AUTOKEY_VIGENERE, new DataHolder(533.99, 32.91)); //Min: 487.31 Max: 685.33
			
			HashMap<String, DataHolder> vigenereAutokey = createOrGetList("Autokey", "Vigenere");
			vigenereAutokey.put(IC_x1000, new DataHolder(39.84, 0.74)); //Min: 36.41 Max: 47.81
			vigenereAutokey.put(IC_MAX_1to15_x1000, new DataHolder(41.52, 2.01)); //Min: 38.59 Max: 59.92
			vigenereAutokey.put(IC_2_TRUE_x10000, new DataHolder(16.88, 1.69)); //Min: 8.41 Max: 31.83
			vigenereAutokey.put(IC_2_FALSE_x10000, new DataHolder(16.87, 2.25)); //Min: 6.33 Max: 38.22
			vigenereAutokey.put(IC_3_FALSE_x100000, new DataHolder(11.85, 14.23)); //Min: 0.00 Max: 190.82
			vigenereAutokey.put(IC_KAPPA_x1000, new DataHolder(65.58, 8.70)); //Min: 45.28 Max: 118.01
			vigenereAutokey.put(LOG_DIGRAPH, new DataHolder(442.89, 13.09)); //Min: 387.00 Max: 511.00
			vigenereAutokey.put(LOG_DIGRAPH_REVERSED, new DataHolder(152.26, 211.00)); //Min: 0.00 Max: 513.00
			vigenereAutokey.put(LONG_REPEAT, new DataHolder(6.23, 1.22)); //Min: 0.00 Max: 12.15
			vigenereAutokey.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.60, 2.85)); //Min: 24.00 Max: 67.00
			vigenereAutokey.put(NORMAL_ORDER, new DataHolder(195.94, 22.13)); //Min: 122.00 Max: 282.00
			vigenereAutokey.put(LOG_DIGRAPH_AUTOKEY_BEAUFORT, new DataHolder(533.36, 33.31)); //Min: 482.58 Max: 688.67
			vigenereAutokey.put(LOG_DIGRAPH_AUTOKEY_PORTA, new DataHolder(463.89, 23.86)); //Min: 435.90 Max: 594.73
			vigenereAutokey.put(LOG_DIGRAPH_AUTOKEY_VARIANT, new DataHolder(533.27, 33.13)); //Min: 485.20 Max: 686.00
			vigenereAutokey.put(LOG_DIGRAPH_AUTOKEY_VIGENERE, new DataHolder(759.59, 6.73)); //Min: 736.09 Max: 774.64
			
			HashMap<String, DataHolder> beaufort = createOrGetList("Beaufort");
			beaufort.put(IC_x1000, new DataHolder(42.02, 3.53)); //Min: 36.49 Max: 60.38
			beaufort.put(IC_MAX_1to15_x1000, new DataHolder(66.59, 3.20)); //Min: 51.54 Max: 76.97
			beaufort.put(IC_2_TRUE_x10000, new DataHolder(24.55, 7.86)); //Min: 10.22 Max: 56.56
			beaufort.put(IC_2_FALSE_x10000, new DataHolder(30.56, 15.41)); //Min: 3.16 Max: 92.01
			beaufort.put(IC_3_FALSE_x100000, new DataHolder(50.17, 48.68)); //Min: 0.00 Max: 380.39
			beaufort.put(IC_KAPPA_x1000, new DataHolder(69.11, 9.74)); //Min: 40.14 Max: 118.01
			beaufort.put(LOG_DIGRAPH, new DataHolder(427.12, 26.45)); //Min: 295.00 Max: 533.00
			beaufort.put(LOG_DIGRAPH_REVERSED, new DataHolder(146.47, 203.46)); //Min: 0.00 Max: 526.00
			beaufort.put(LONG_REPEAT, new DataHolder(10.40, 2.88)); //Min: 0.00 Max: 21.20
			beaufort.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(39.32, 11.92)); //Min: 6.00 Max: 68.00
			beaufort.put(NORMAL_ORDER, new DataHolder(224.29, 31.48)); //Min: 116.00 Max: 312.00
			beaufort.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(759.74, 6.58)); //Min: 736.09 Max: 774.64
			beaufort.put(LOG_DIGRAPH_PORTA, new DataHolder(558.38, 20.65)); //Min: 521.60 Max: 673.40
			beaufort.put(LOG_DIGRAPH_PORTAX, new DataHolder(148.38, 205.96)); //Min: 0.00 Max: 559.00
			beaufort.put(LOG_DIGRAPH_VARIANT, new DataHolder(576.71, 23.25)); //Min: 541.44 Max: 690.67
			beaufort.put(LOG_DIGRAPH_VIGENERE, new DataHolder(576.84, 23.53)); //Min: 541.44 Max: 696.67
			
			HashMap<String, DataHolder> porta = createOrGetList("Porta");
			porta.put(IC_x1000, new DataHolder(42.25, 3.82)); //Min: 35.87 Max: 70.69
			porta.put(IC_MAX_1to15_x1000, new DataHolder(66.56, 3.18)); //Min: 51.54 Max: 76.97
			porta.put(IC_2_TRUE_x10000, new DataHolder(24.58, 8.08)); //Min: 10.22 Max: 81.37
			porta.put(IC_2_FALSE_x10000, new DataHolder(29.96, 15.07)); //Min: 3.16 Max: 92.01
			porta.put(IC_3_FALSE_x100000, new DataHolder(49.68, 48.22)); //Min: 0.00 Max: 380.39
			porta.put(IC_KAPPA_x1000, new DataHolder(68.88, 9.60)); //Min: 43.48 Max: 118.01
			porta.put(LOG_DIGRAPH, new DataHolder(424.35, 30.25)); //Min: 244.00 Max: 537.00
			porta.put(LOG_DIGRAPH_REVERSED, new DataHolder(145.19, 201.84)); //Min: 0.00 Max: 542.00
			porta.put(LONG_REPEAT, new DataHolder(10.41, 2.89)); //Min: 0.00 Max: 25.29
			porta.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(39.36, 12.19)); //Min: 2.00 Max: 71.00
			porta.put(NORMAL_ORDER, new DataHolder(227.19, 35.30)); //Min: 116.00 Max: 320.00
			porta.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(575.25, 22.92)); //Min: 533.38 Max: 694.67
			porta.put(LOG_DIGRAPH_PORTA, new DataHolder(759.66, 6.73)); //Min: 736.09 Max: 774.64
			porta.put(LOG_DIGRAPH_PORTAX, new DataHolder(145.88, 202.51)); //Min: 0.00 Max: 526.00
			porta.put(LOG_DIGRAPH_VARIANT, new DataHolder(594.95, 22.62)); //Min: 549.25 Max: 696.43
			porta.put(LOG_DIGRAPH_VIGENERE, new DataHolder(595.53, 22.93)); //Min: 553.90 Max: 699.33
			
			HashMap<String, DataHolder> portax = createOrGetList("Portax");
			portax.put(IC_x1000, new DataHolder(42.77, 1.26)); //Min: 37.04 Max: 52.71
			portax.put(IC_MAX_1to15_x1000, new DataHolder(48.63, 3.74)); //Min: 39.96 Max: 63.69
			portax.put(IC_2_TRUE_x10000, new DataHolder(19.43, 1.71)); //Min: 8.54 Max: 33.63
			portax.put(IC_2_FALSE_x10000, new DataHolder(20.95, 3.37)); //Min: 3.09 Max: 45.39
			portax.put(IC_3_FALSE_x100000, new DataHolder(12.72, 9.66)); //Min: 0.00 Max: 139.76
			portax.put(IC_KAPPA_x1000, new DataHolder(54.79, 6.67)); //Min: 42.13 Max: 108.70
			portax.put(LOG_DIGRAPH, new DataHolder(438.81, 14.72)); //Min: 375.00 Max: 512.00
			portax.put(LOG_DIGRAPH_REVERSED, new DataHolder(437.86, 16.21)); //Min: 367.00 Max: 542.00
			portax.put(LONG_REPEAT, new DataHolder(6.76, 1.06)); //Min: 0.00 Max: 12.15
			portax.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(46.65, 3.66)); //Min: 22.00 Max: 76.00
			portax.put(NORMAL_ORDER, new DataHolder(219.68, 15.77)); //Min: 154.00 Max: 278.00
			portax.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(546.34, 28.84)); //Min: 503.08 Max: 685.33
			portax.put(LOG_DIGRAPH_PORTA, new DataHolder(525.09, 26.00)); //Min: 481.50 Max: 657.33
			portax.put(LOG_DIGRAPH_PORTAX, new DataHolder(663.47, 41.26)); //Min: 471.00 Max: 713.00
			portax.put(LOG_DIGRAPH_VARIANT, new DataHolder(546.62, 28.31)); //Min: 501.73 Max: 678.67
			portax.put(LOG_DIGRAPH_VIGENERE, new DataHolder(546.80, 28.51)); //Min: 496.64 Max: 685.33
			portax.put(TEXT_LENGTH_MULTIPLE, new DataHolder(2));
			
			HashMap<String, DataHolder> variant = createOrGetList("Variant");
			variant.put(IC_x1000, new DataHolder(42.11, 3.65)); //Min: 36.18 Max: 62.96
			variant.put(IC_MAX_1to15_x1000, new DataHolder(66.56, 3.19)); //Min: 51.54 Max: 76.97
			variant.put(IC_2_TRUE_x10000, new DataHolder(24.44, 7.95)); //Min: 9.61 Max: 64.22
			variant.put(IC_2_FALSE_x10000, new DataHolder(30.21, 15.46)); //Min: 0.00 Max: 92.01
			variant.put(IC_3_FALSE_x100000, new DataHolder(49.20, 46.13)); //Min: 0.00 Max: 380.39
			variant.put(IC_KAPPA_x1000, new DataHolder(69.03, 9.75)); //Min: 44.70 Max: 124.22
			variant.put(LOG_DIGRAPH, new DataHolder(426.96, 25.72)); //Min: 318.00 Max: 593.00
			variant.put(LOG_DIGRAPH_REVERSED, new DataHolder(146.57, 203.59)); //Min: 0.00 Max: 580.00
			variant.put(LONG_REPEAT, new DataHolder(10.41, 2.91)); //Min: 0.00 Max: 21.56
			variant.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(39.64, 11.87)); //Min: 2.00 Max: 75.00
			variant.put(NORMAL_ORDER, new DataHolder(223.77, 32.07)); //Min: 90.00 Max: 324.00
			variant.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(576.18, 22.98)); //Min: 541.44 Max: 696.00
			variant.put(LOG_DIGRAPH_PORTA, new DataHolder(567.50, 21.75)); //Min: 525.00 Max: 679.33
			variant.put(LOG_DIGRAPH_PORTAX, new DataHolder(148.52, 206.13)); //Min: 0.00 Max: 520.00
			variant.put(LOG_DIGRAPH_VARIANT, new DataHolder(759.76, 6.53)); //Min: 736.09 Max: 774.64
			variant.put(LOG_DIGRAPH_VIGENERE, new DataHolder(759.74, 6.59)); //Min: 736.38 Max: 774.64
			
			HashMap<String, DataHolder> vigenere = createOrGetList("Vigenere");
			vigenere.put(IC_x1000, new DataHolder(42.11, 3.63)); //Min: 36.05 Max: 63.19
			vigenere.put(IC_MAX_1to15_x1000, new DataHolder(66.55, 3.21)); //Min: 51.54 Max: 76.97
			vigenere.put(IC_2_TRUE_x10000, new DataHolder(24.40, 7.92)); //Min: 9.61 Max: 58.20
			vigenere.put(IC_2_FALSE_x10000, new DataHolder(30.04, 15.28)); //Min: 6.33 Max: 92.01
			vigenere.put(IC_3_FALSE_x100000, new DataHolder(47.32, 45.42)); //Min: 0.00 Max: 380.39
			vigenere.put(IC_KAPPA_x1000, new DataHolder(68.84, 9.57)); //Min: 43.96 Max: 118.01
			vigenere.put(LOG_DIGRAPH, new DataHolder(427.82, 26.26)); //Min: 338.00 Max: 587.00
			vigenere.put(LOG_DIGRAPH_REVERSED, new DataHolder(145.92, 202.62)); //Min: 0.00 Max: 577.00
			vigenere.put(LONG_REPEAT, new DataHolder(10.42, 2.89)); //Min: 0.00 Max: 21.56
			vigenere.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(39.70, 11.85)); //Min: 2.00 Max: 72.00
			vigenere.put(NORMAL_ORDER, new DataHolder(223.29, 32.66)); //Min: 104.00 Max: 322.00
			vigenere.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(576.47, 23.67)); //Min: 541.44 Max: 690.67
			vigenere.put(LOG_DIGRAPH_PORTA, new DataHolder(566.66, 22.35)); //Min: 523.25 Max: 670.58
			vigenere.put(LOG_DIGRAPH_PORTAX, new DataHolder(148.42, 205.99)); //Min: 0.00 Max: 538.00
			vigenere.put(LOG_DIGRAPH_VARIANT, new DataHolder(759.74, 6.61)); //Min: 736.09 Max: 774.64
			vigenere.put(LOG_DIGRAPH_VIGENERE, new DataHolder(759.72, 6.62)); //Min: 736.09 Max: 774.64
			
			HashMap<String, DataHolder> beaufortNicodemus = createOrGetList("Nicodemus", "Beaufort");
			beaufortNicodemus.put(IC_x1000, new DataHolder(41.37, 2.51)); //Min: 35.48 Max: 54.70
			beaufortNicodemus.put(IC_MAX_1to15_x1000, new DataHolder(46.82, 6.40)); //Min: 38.10 Max: 74.64
			beaufortNicodemus.put(IC_2_TRUE_x10000, new DataHolder(17.92, 2.70)); //Min: 7.86 Max: 34.54
			beaufortNicodemus.put(IC_2_FALSE_x10000, new DataHolder(17.88, 3.03)); //Min: 3.16 Max: 44.30
			beaufortNicodemus.put(IC_3_FALSE_x100000, new DataHolder(8.29, 6.37)); //Min: 0.00 Max: 109.29
			beaufortNicodemus.put(IC_KAPPA_x1000, new DataHolder(62.84, 7.94)); //Min: 43.48 Max: 136.65
			beaufortNicodemus.put(LOG_DIGRAPH, new DataHolder(427.68, 23.64)); //Min: 309.00 Max: 535.00
			beaufortNicodemus.put(LOG_DIGRAPH_REVERSED, new DataHolder(146.90, 203.87)); //Min: 0.00 Max: 542.00
			beaufortNicodemus.put(LONG_REPEAT, new DataHolder(6.14, 1.11)); //Min: 0.00 Max: 13.53
			beaufortNicodemus.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.48, 2.62)); //Min: 30.00 Max: 73.00
			beaufortNicodemus.put(NORMAL_ORDER, new DataHolder(223.57, 31.44)); //Min: 100.00 Max: 320.00
			beaufortNicodemus.put(NICODEMUS_MAX_3to15, new DataHolder(66.57, 3.23)); //Min: 52.38 Max: 96.97
			
			HashMap<String, DataHolder> portaNicodemus = createOrGetList("Nicodemus", "Porta");
			portaNicodemus.put(IC_x1000, new DataHolder(41.49, 2.67)); //Min: 36.02 Max: 58.44
			portaNicodemus.put(IC_MAX_1to15_x1000, new DataHolder(46.09, 6.67)); //Min: 37.67 Max: 74.35
			portaNicodemus.put(IC_2_TRUE_x10000, new DataHolder(18.33, 2.81)); //Min: 7.86 Max: 36.76
			portaNicodemus.put(IC_2_FALSE_x10000, new DataHolder(18.40, 3.23)); //Min: 3.16 Max: 43.00
			portaNicodemus.put(IC_3_FALSE_x100000, new DataHolder(8.63, 6.96)); //Min: 0.00 Max: 163.93
			portaNicodemus.put(IC_KAPPA_x1000, new DataHolder(64.43, 7.93)); //Min: 42.99 Max: 124.22
			portaNicodemus.put(LOG_DIGRAPH, new DataHolder(426.53, 27.65)); //Min: 296.00 Max: 530.00
			portaNicodemus.put(LOG_DIGRAPH_REVERSED, new DataHolder(146.44, 203.39)); //Min: 0.00 Max: 518.00
			portaNicodemus.put(LONG_REPEAT, new DataHolder(6.30, 1.15)); //Min: 0.00 Max: 15.37
			portaNicodemus.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.52, 2.67)); //Min: 26.00 Max: 81.00
			portaNicodemus.put(NORMAL_ORDER, new DataHolder(227.18, 34.74)); //Min: 110.00 Max: 322.00
			portaNicodemus.put(NICODEMUS_MAX_3to15, new DataHolder(66.53, 3.23)); //Min: 53.33 Max: 80.81
			
			HashMap<String, DataHolder> variantNicodemus = createOrGetList("Nicodemus", "Variant");
			variantNicodemus.put(IC_x1000, new DataHolder(41.37, 2.50)); //Min: 35.79 Max: 54.11
			variantNicodemus.put(IC_MAX_1to15_x1000, new DataHolder(46.90, 6.55)); //Min: 38.35 Max: 75.34
			variantNicodemus.put(IC_2_TRUE_x10000, new DataHolder(17.93, 2.69)); //Min: 8.65 Max: 38.76
			variantNicodemus.put(IC_2_FALSE_x10000, new DataHolder(17.98, 3.07)); //Min: 0.00 Max: 40.61
			variantNicodemus.put(IC_3_FALSE_x100000, new DataHolder(8.22, 6.23)); //Min: 0.00 Max: 72.57
			variantNicodemus.put(IC_KAPPA_x1000, new DataHolder(62.80, 7.84)); //Min: 42.74 Max: 118.01
			variantNicodemus.put(LOG_DIGRAPH, new DataHolder(428.13, 23.01)); //Min: 344.00 Max: 543.00
			variantNicodemus.put(LOG_DIGRAPH_REVERSED, new DataHolder(146.60, 203.44)); //Min: 0.00 Max: 548.00
			variantNicodemus.put(LONG_REPEAT, new DataHolder(6.10, 1.11)); //Min: 0.00 Max: 13.89
			variantNicodemus.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.46, 2.71)); //Min: 21.00 Max: 82.00
			variantNicodemus.put(NORMAL_ORDER, new DataHolder(223.26, 32.23)); //Min: 104.00 Max: 327.00
			variantNicodemus.put(NICODEMUS_MAX_3to15, new DataHolder(66.55, 3.21)); //Min: 52.38 Max: 84.85
			
			HashMap<String, DataHolder> vigenereNicodemus = createOrGetList("Nicodemus", "Vigenere");
			vigenereNicodemus.put(IC_x1000, new DataHolder(41.38, 2.51)); //Min: 35.40 Max: 55.23
			vigenereNicodemus.put(IC_MAX_1to15_x1000, new DataHolder(46.85, 6.54)); //Min: 36.88 Max: 74.85
			vigenereNicodemus.put(IC_2_TRUE_x10000, new DataHolder(17.96, 2.69)); //Min: 4.72 Max: 34.96
			vigenereNicodemus.put(IC_2_FALSE_x10000, new DataHolder(17.94, 3.17)); //Min: 2.39 Max: 41.14
			vigenereNicodemus.put(IC_3_FALSE_x100000, new DataHolder(8.16, 6.01)); //Min: 0.00 Max: 72.57
			vigenereNicodemus.put(IC_KAPPA_x1000, new DataHolder(62.95, 7.91)); //Min: 42.94 Max: 118.01
			vigenereNicodemus.put(LOG_DIGRAPH, new DataHolder(427.76, 23.49)); //Min: 316.00 Max: 550.00
			vigenereNicodemus.put(LOG_DIGRAPH_REVERSED, new DataHolder(146.40, 203.18)); //Min: 0.00 Max: 532.00
			vigenereNicodemus.put(LONG_REPEAT, new DataHolder(6.14, 1.14)); //Min: 0.00 Max: 14.38
			vigenereNicodemus.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.52, 2.78)); //Min: 23.00 Max: 76.00
			vigenereNicodemus.put(NORMAL_ORDER, new DataHolder(223.68, 31.92)); //Min: 116.00 Max: 322.00
			vigenereNicodemus.put(NICODEMUS_MAX_3to15, new DataHolder(66.52, 3.19)); //Min: 52.38 Max: 81.48
			
			HashMap<String, DataHolder> beaufortProgressive = createOrGetList("Progressive Key", "Same Period", "Beaufort");
			beaufortProgressive.put(IC_x1000, new DataHolder(38.57, 0.76)); //Min: 34.55 Max: 55.09
			beaufortProgressive.put(IC_MAX_1to15_x1000, new DataHolder(42.96, 4.89)); //Min: 37.89 Max: 76.97
			beaufortProgressive.put(IC_2_TRUE_x10000, new DataHolder(15.34, 1.71)); //Min: 5.50 Max: 37.96
			beaufortProgressive.put(IC_2_FALSE_x10000, new DataHolder(15.82, 3.36)); //Min: 0.00 Max: 59.94
			beaufortProgressive.put(IC_3_FALSE_x100000, new DataHolder(8.65, 9.56)); //Min: 0.00 Max: 198.40
			beaufortProgressive.put(IC_KAPPA_x1000, new DataHolder(51.49, 6.47)); //Min: 37.27 Max: 105.59
			beaufortProgressive.put(LOG_DIGRAPH, new DataHolder(427.76, 13.24)); //Min: 344.00 Max: 523.00
			beaufortProgressive.put(LOG_DIGRAPH_REVERSED, new DataHolder(146.28, 202.69)); //Min: 0.00 Max: 497.00
			beaufortProgressive.put(LONG_REPEAT, new DataHolder(5.65, 1.20)); //Min: 0.00 Max: 14.38
			beaufortProgressive.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(48.28, 4.17)); //Min: 2.00 Max: 80.00
			beaufortProgressive.put(NORMAL_ORDER, new DataHolder(223.22, 28.39)); //Min: 114.00 Max: 320.00

			HashMap<String, DataHolder> portaProgressive = createOrGetList("Progressive Key", "Same Period", "Porta");
			portaProgressive.put(IC_x1000, new DataHolder(38.66, 0.71)); //Min: 34.94 Max: 51.26
			portaProgressive.put(IC_MAX_1to15_x1000, new DataHolder(42.76, 4.84)); //Min: 37.82 Max: 75.54
			portaProgressive.put(IC_2_TRUE_x10000, new DataHolder(15.66, 1.53)); //Min: 7.08 Max: 34.77
			portaProgressive.put(IC_2_FALSE_x10000, new DataHolder(16.12, 2.94)); //Min: 2.39 Max: 51.52
			portaProgressive.put(IC_3_FALSE_x100000, new DataHolder(9.21, 9.09)); //Min: 0.00 Max: 145.14
			portaProgressive.put(IC_KAPPA_x1000, new DataHolder(52.19, 6.83)); //Min: 39.72 Max: 94.77
			portaProgressive.put(LOG_DIGRAPH, new DataHolder(430.15, 12.31)); //Min: 361.00 Max: 493.00
			portaProgressive.put(LOG_DIGRAPH_REVERSED, new DataHolder(147.78, 204.77)); //Min: 0.00 Max: 536.00
			portaProgressive.put(LONG_REPEAT, new DataHolder(5.80, 1.21)); //Min: 0.00 Max: 14.99
			portaProgressive.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(48.13, 4.43)); //Min: 10.00 Max: 80.00
			portaProgressive.put(NORMAL_ORDER, new DataHolder(216.78, 26.92)); //Min: 110.00 Max: 318.00
			
			HashMap<String, DataHolder> variantProgressive = createOrGetList("Progressive Key", "Same Period", "Variant");
			variantProgressive.put(IC_x1000, new DataHolder(38.56, 0.68)); //Min: 34.87 Max: 47.33
			variantProgressive.put(IC_MAX_1to15_x1000, new DataHolder(43.04, 5.00)); //Min: 38.27 Max: 76.97
			variantProgressive.put(IC_2_TRUE_x10000, new DataHolder(15.35, 1.73)); //Min: 6.29 Max: 36.97
			variantProgressive.put(IC_2_FALSE_x10000, new DataHolder(15.70, 2.99)); //Min: 2.39 Max: 60.88
			variantProgressive.put(IC_3_FALSE_x100000, new DataHolder(8.52, 9.21)); //Min: 0.00 Max: 154.54
			variantProgressive.put(IC_KAPPA_x1000, new DataHolder(51.53, 6.46)); //Min: 38.52 Max: 92.52
			variantProgressive.put(LOG_DIGRAPH, new DataHolder(427.14, 13.25)); //Min: 343.00 Max: 529.00
			variantProgressive.put(LOG_DIGRAPH_REVERSED, new DataHolder(146.58, 203.11)); //Min: 0.00 Max: 500.00
			variantProgressive.put(LONG_REPEAT, new DataHolder(5.61, 1.21)); //Min: 0.00 Max: 15.12
			variantProgressive.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(48.23, 4.14)); //Min: 5.00 Max: 76.00
			variantProgressive.put(NORMAL_ORDER, new DataHolder(223.02, 29.01)); //Min: 116.00 Max: 322.00

			HashMap<String, DataHolder> vigenereProgressive = createOrGetList("Progressive Key", "Same Period", "Vigenere");
			vigenereProgressive.put(IC_x1000, new DataHolder(38.58, 0.77)); //Min: 35.17 Max: 52.11
			vigenereProgressive.put(IC_MAX_1to15_x1000, new DataHolder(43.01, 5.00)); //Min: 36.77 Max: 76.97
			vigenereProgressive.put(IC_2_TRUE_x10000, new DataHolder(15.36, 1.76)); //Min: 6.00 Max: 39.88
			vigenereProgressive.put(IC_2_FALSE_x10000, new DataHolder(15.74, 3.07)); //Min: 0.00 Max: 65.36
			vigenereProgressive.put(IC_3_FALSE_x100000, new DataHolder(8.45, 8.80)); //Min: 0.00 Max: 145.14
			vigenereProgressive.put(IC_KAPPA_x1000, new DataHolder(51.64, 6.58)); //Min: 39.09 Max: 99.38
			vigenereProgressive.put(LOG_DIGRAPH, new DataHolder(427.38, 13.26)); //Min: 364.00 Max: 532.00
			vigenereProgressive.put(LOG_DIGRAPH_REVERSED, new DataHolder(146.46, 202.94)); //Min: 0.00 Max: 492.00
			vigenereProgressive.put(LONG_REPEAT, new DataHolder(5.65, 1.22)); //Min: 0.00 Max: 14.71
			vigenereProgressive.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(48.28, 4.10)); //Min: 13.00 Max: 75.00
			vigenereProgressive.put(NORMAL_ORDER, new DataHolder(222.92, 28.55)); //Min: 112.00 Max: 312.00
			
			HashMap<String, DataHolder> fractionatedMorse = createOrGetList("Fractionated Morse");
			fractionatedMorse.put(IC_x1000, new DataHolder(57.53, 1.50)); //Min: 53.84 Max: 61.78
			fractionatedMorse.put(IC_MAX_1to15_x1000, new DataHolder(59.29, 1.89)); //Min: 56.28 Max: 67.25
			fractionatedMorse.put(IC_2_TRUE_x10000, new DataHolder(57.22, 2.57)); //Min: 51.30 Max: 63.34
			fractionatedMorse.put(IC_2_FALSE_x10000, new DataHolder(57.68, 3.29)); //Min: 51.26 Max: 67.96
			fractionatedMorse.put(IC_3_FALSE_x100000, new DataHolder(103.28, 24.20)); //Min: 32.76 Max: 174.37
			fractionatedMorse.put(IC_KAPPA_x1000, new DataHolder(70.24, 8.16)); //Min: 60.09 Max: 100.46
			fractionatedMorse.put(LOG_DIGRAPH, new DataHolder(428.02, 48.31)); //Min: 255.00 Max: 576.00
			fractionatedMorse.put(LOG_DIGRAPH_REVERSED, new DataHolder(262.88, 211.72)); //Min: 0.00 Max: 580.00
			fractionatedMorse.put(LONG_REPEAT, new DataHolder(19.48, 0.89)); //Min: 17.62 Max: 21.74
			fractionatedMorse.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.43, 2.02)); //Min: 37.00 Max: 52.00
			fractionatedMorse.put(NORMAL_ORDER, new DataHolder(229.47, 26.68)); //Min: 132.00 Max: 324.00
			
			HashMap<String, DataHolder> playfair = createOrGetList("Digraph Substitution", "Playfair");
			playfair.put(IC_x1000, new DataHolder(50.37, 2.82)); //Min: 41.15 Max: 62.68
			playfair.put(IC_MAX_1to15_x1000, new DataHolder(53.45, 3.26)); //Min: 44.97 Max: 73.16
			playfair.put(IC_2_TRUE_x10000, new DataHolder(39.93, 3.80)); //Min: 25.75 Max: 59.03
			playfair.put(IC_2_FALSE_x10000, new DataHolder(76.88, 7.37)); //Min: 60.65 Max: 95.92
			playfair.put(IC_3_FALSE_x100000, new DataHolder(53.17, 18.66)); //Min: 0.00 Max: 195.23
			playfair.put(IC_KAPPA_x1000, new DataHolder(62.89, 6.94)); //Min: 46.82 Max: 109.76
			playfair.put(LOG_DIGRAPH, new DataHolder(449.58, 42.83)); //Min: 304.00 Max: 605.00
			playfair.put(LOG_DIGRAPH_REVERSED, new DataHolder(448.26, 44.98)); //Min: 295.00 Max: 608.00
			playfair.put(LONG_REPEAT, new DataHolder(13.12, 1.28)); //Min: 0.00 Max: 18.29
			playfair.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(30.40, 3.64)); //Min: 8.00 Max: 48.00
			playfair.put(NORMAL_ORDER, new DataHolder(232.68, 33.07)); //Min: 123.00 Max: 331.00
			playfair.put(TEXT_LENGTH_MULTIPLE, new DataHolder(2));
			playfair.put(DOUBLE_LETTER_EVEN, new DataHolder(false));
			playfair.put(MAX_UNIQUE_CHARACTERS, new DataHolder(25));
			
			HashMap<String, DataHolder> fourSquare = createOrGetList("Digraph Substitution", "Four Square");
			fourSquare.put(IC_x1000, new DataHolder(46.24, 1.72)); //Min: 39.02 Max: 53.82
			fourSquare.put(IC_MAX_1to15_x1000, new DataHolder(54.21, 2.44)); //Min: 50.32 Max: 67.92
			fourSquare.put(IC_2_TRUE_x10000, new DataHolder(35.43, 2.96)); //Min: 22.52 Max: 47.60
			fourSquare.put(IC_2_FALSE_x10000, new DataHolder(76.93, 7.27)); //Min: 58.64 Max: 91.37
			fourSquare.put(IC_3_FALSE_x100000, new DataHolder(47.18, 16.16)); //Min: 0.00 Max: 163.93
			fourSquare.put(IC_KAPPA_x1000, new DataHolder(61.92, 6.74)); //Min: 50.20 Max: 97.83
			fourSquare.put(LOG_DIGRAPH, new DataHolder(449.57, 28.82)); //Min: 350.00 Max: 545.00
			fourSquare.put(LOG_DIGRAPH_REVERSED, new DataHolder(449.29, 31.43)); //Min: 337.00 Max: 560.00
			fourSquare.put(LONG_REPEAT, new DataHolder(12.33, 1.18)); //Min: 7.69 Max: 15.25
			fourSquare.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(22.08, 3.81)); //Min: 8.00 Max: 43.00
			fourSquare.put(NORMAL_ORDER, new DataHolder(232.13, 27.10)); //Min: 131.00 Max: 311.00
			fourSquare.put(MAX_UNIQUE_CHARACTERS, new DataHolder(25));
			
			HashMap<String, DataHolder> twoSquare = createOrGetList("Digraph Substitution", "Two Square");
			twoSquare.put(IC_x1000, new DataHolder(46.10, 2.25)); //Min: 39.18 Max: 58.37
			twoSquare.put(IC_MAX_1to15_x1000, new DataHolder(52.91, 3.57)); //Min: 43.62 Max: 78.57
			twoSquare.put(IC_2_TRUE_x10000, new DataHolder(35.67, 3.10)); //Min: 21.74 Max: 52.37
			twoSquare.put(IC_2_FALSE_x10000, new DataHolder(76.84, 7.28)); //Min: 58.64 Max: 92.40
			twoSquare.put(IC_3_FALSE_x100000, new DataHolder(46.08, 17.66)); //Min: 0.00 Max: 166.96
			twoSquare.put(IC_KAPPA_x1000, new DataHolder(61.02, 7.35)); //Min: 44.87 Max: 123.46
			twoSquare.put(LOG_DIGRAPH, new DataHolder(485.88, 30.91)); //Min: 372.00 Max: 593.00
			twoSquare.put(LOG_DIGRAPH_REVERSED, new DataHolder(503.94, 34.83)); //Min: 372.00 Max: 632.00
			twoSquare.put(LONG_REPEAT, new DataHolder(12.33, 1.15)); //Min: 0.00 Max: 16.30
			twoSquare.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(22.94, 3.75)); //Min: 5.00 Max: 43.00
			twoSquare.put(NORMAL_ORDER, new DataHolder(190.71, 33.16)); //Min: 86.00 Max: 325.00
			twoSquare.put(MAX_UNIQUE_CHARACTERS, new DataHolder(25));
			
			HashMap<String, DataHolder> triSquare = createOrGetList("Tri Square");
			triSquare.put(IC_x1000, new DataHolder(42.41, 1.05)); //Min: 38.67 Max: 49.55
			triSquare.put(IC_MAX_1to15_x1000, new DataHolder(47.47, 2.14)); //Min: 41.86 Max: 58.66
			triSquare.put(IC_2_TRUE_x10000, new DataHolder(20.83, 1.12)); //Min: 10.90 Max: 27.34
			triSquare.put(IC_2_FALSE_x10000, new DataHolder(20.88, 1.61)); //Min: 7.00 Max: 35.97
			triSquare.put(IC_3_FALSE_x100000, new DataHolder(30.96, 7.90)); //Min: 0.00 Max: 143.33
			triSquare.put(IC_KAPPA_x1000, new DataHolder(53.28, 4.89)); //Min: 42.42 Max: 95.83
			triSquare.put(LOG_DIGRAPH, new DataHolder(473.90, 19.35)); //Min: 402.00 Max: 550.00
			triSquare.put(LOG_DIGRAPH_REVERSED, new DataHolder(256.81, 236.13)); //Min: 0.00 Max: 565.00
			triSquare.put(LONG_REPEAT, new DataHolder(7.45, 0.61)); //Min: 0.00 Max: 12.50
			triSquare.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.45, 1.76)); //Min: 27.00 Max: 65.00
			triSquare.put(NORMAL_ORDER, new DataHolder(194.46, 28.14)); //Min: 107.00 Max: 291.00
			triSquare.put(TEXT_LENGTH_MULTIPLE, new DataHolder(3));
			triSquare.put(MAX_UNIQUE_CHARACTERS, new DataHolder(25));
			
			HashMap<String, DataHolder> railfence = createOrGetList("Transposition", "Railfence");
			railfence.put(IC_x1000, new DataHolder(66.05, 2.72)); //Min: 60.87 Max: 74.50
			railfence.put(IC_MAX_1to15_x1000, new DataHolder(68.16, 3.22)); //Min: 61.66 Max: 89.67
			railfence.put(IC_2_TRUE_x10000, new DataHolder(45.07, 4.93)); //Min: 29.42 Max: 69.26
			railfence.put(IC_2_FALSE_x10000, new DataHolder(45.20, 5.78)); //Min: 22.15 Max: 80.36
			railfence.put(IC_3_FALSE_x100000, new DataHolder(32.93, 14.72)); //Min: 0.00 Max: 131.19
			railfence.put(IC_KAPPA_x1000, new DataHolder(81.10, 8.02)); //Min: 64.52 Max: 125.32
			railfence.put(LOG_DIGRAPH, new DataHolder(656.59, 8.12)); //Min: 619.00 Max: 682.00
			railfence.put(LOG_DIGRAPH_REVERSED, new DataHolder(225.26, 311.93)); //Min: 0.00 Max: 686.00
			railfence.put(LONG_REPEAT, new DataHolder(12.09, 1.50)); //Min: 0.00 Max: 18.90
			railfence.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.37, 1.78)); //Min: 35.00 Max: 62.00
			railfence.put(NORMAL_ORDER, new DataHolder(73.71, 28.63)); //Min: 22.00 Max: 140.00
			
			HashMap<String, DataHolder> myszkowski = createOrGetList("Transposition", "Myszkowski");
			myszkowski.put(IC_x1000, new DataHolder(66.05, 2.72)); //Min: 60.87 Max: 74.50
			myszkowski.put(IC_MAX_1to15_x1000, new DataHolder(68.15, 3.13)); //Min: 60.87 Max: 92.79
			myszkowski.put(IC_2_TRUE_x10000, new DataHolder(45.19, 5.14)); //Min: 26.73 Max: 86.77
			myszkowski.put(IC_2_FALSE_x10000, new DataHolder(45.45, 6.08)); //Min: 12.66 Max: 91.15
			myszkowski.put(IC_3_FALSE_x100000, new DataHolder(33.19, 16.30)); //Min: 0.00 Max: 219.18
			myszkowski.put(IC_KAPPA_x1000, new DataHolder(81.33, 7.65)); //Min: 66.25 Max: 130.43
			myszkowski.put(LOG_DIGRAPH, new DataHolder(660.63, 12.10)); //Min: 617.00 Max: 766.00
			myszkowski.put(LOG_DIGRAPH_REVERSED, new DataHolder(225.34, 312.03)); //Min: 0.00 Max: 694.00
			myszkowski.put(LONG_REPEAT, new DataHolder(12.13, 1.66)); //Min: 0.00 Max: 27.93
			myszkowski.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.33, 2.04)); //Min: 24.00 Max: 68.00
			myszkowski.put(NORMAL_ORDER, new DataHolder(73.71, 28.63)); //Min: 22.00 Max: 140.00
			
			HashMap<String, DataHolder> phillipsRow = createOrGetList("Phillips", "Rows");
			phillipsRow.put(IC_x1000, new DataHolder(51.88, 2.75)); //Min: 42.24 Max: 65.21
			phillipsRow.put(IC_MAX_1to15_x1000, new DataHolder(55.24, 3.04)); //Min: 46.29 Max: 76.39
			phillipsRow.put(IC_2_TRUE_x10000, new DataHolder(35.64, 3.92)); //Min: 15.61 Max: 52.63
			phillipsRow.put(IC_2_FALSE_x10000, new DataHolder(35.81, 4.66)); //Min: 3.16 Max: 66.89
			phillipsRow.put(IC_3_FALSE_x100000, new DataHolder(38.88, 18.28)); //Min: 0.00 Max: 190.82
			phillipsRow.put(IC_KAPPA_x1000, new DataHolder(63.32, 7.15)); //Min: 47.26 Max: 125.00
			phillipsRow.put(LOG_DIGRAPH, new DataHolder(439.30, 37.10)); //Min: 291.00 Max: 561.00
			phillipsRow.put(LOG_DIGRAPH_REVERSED, new DataHolder(150.22, 209.12)); //Min: 0.00 Max: 545.00
			phillipsRow.put(LONG_REPEAT, new DataHolder(12.48, 1.61)); //Min: 0.00 Max: 20.17
			phillipsRow.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.28, 1.94)); //Min: 32.00 Max: 65.00
			phillipsRow.put(NORMAL_ORDER, new DataHolder(240.00, 25.53)); //Min: 147.00 Max: 327.00
			phillipsRow.put(MAX_UNIQUE_CHARACTERS, new DataHolder(25));
			
			HashMap<String, DataHolder> phillipsCol = createOrGetList("Phillips", "Columns");
			phillipsCol.put(IC_x1000, new DataHolder(51.88, 2.78)); //Min: 41.54 Max: 63.24
			phillipsCol.put(IC_MAX_1to15_x1000, new DataHolder(55.24, 3.01)); //Min: 46.29 Max: 74.80
			phillipsCol.put(IC_2_TRUE_x10000, new DataHolder(35.58, 3.90)); //Min: 19.82 Max: 54.57
			phillipsCol.put(IC_2_FALSE_x10000, new DataHolder(35.78, 4.55)); //Min: 9.49 Max: 55.09
			phillipsCol.put(IC_3_FALSE_x100000, new DataHolder(38.96, 18.47)); //Min: 0.00 Max: 214.67
			phillipsCol.put(IC_KAPPA_x1000, new DataHolder(63.29, 7.18)); //Min: 47.99 Max: 135.87
			phillipsCol.put(LOG_DIGRAPH, new DataHolder(439.49, 36.29)); //Min: 312.00 Max: 558.00
			phillipsCol.put(LOG_DIGRAPH_REVERSED, new DataHolder(151.08, 210.30)); //Min: 0.00 Max: 551.00
			phillipsCol.put(LONG_REPEAT, new DataHolder(12.48, 1.63)); //Min: 0.00 Max: 19.19
			phillipsCol.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.32, 1.96)); //Min: 30.00 Max: 68.00
			phillipsCol.put(NORMAL_ORDER, new DataHolder(239.62, 25.92)); //Min: 143.00 Max: 323.00
			phillipsCol.put(MAX_UNIQUE_CHARACTERS, new DataHolder(25));
			
			HashMap<String, DataHolder> phillipsRowCol = createOrGetList("Phillips", "Rows & Columns");
			phillipsRowCol.put(IC_x1000, new DataHolder(47.06, 2.16)); //Min: 39.75 Max: 63.42
			phillipsRowCol.put(IC_MAX_1to15_x1000, new DataHolder(51.66, 2.79)); //Min: 43.84 Max: 78.27
			phillipsRowCol.put(IC_2_TRUE_x10000, new DataHolder(27.45, 2.71)); //Min: 15.01 Max: 41.13
			phillipsRowCol.put(IC_2_FALSE_x10000, new DataHolder(27.73, 3.45)); //Min: 0.00 Max: 50.17
			phillipsRowCol.put(IC_3_FALSE_x100000, new DataHolder(24.71, 13.79)); //Min: 0.00 Max: 217.71
			phillipsRowCol.put(IC_KAPPA_x1000, new DataHolder(57.99, 6.68)); //Min: 43.69 Max: 124.22
			phillipsRowCol.put(LOG_DIGRAPH, new DataHolder(439.81, 29.37)); //Min: 312.00 Max: 546.00
			phillipsRowCol.put(LOG_DIGRAPH_REVERSED, new DataHolder(150.74, 209.46)); //Min: 0.00 Max: 557.00
			phillipsRowCol.put(LONG_REPEAT, new DataHolder(10.12, 1.43)); //Min: 0.00 Max: 15.13
			phillipsRowCol.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.10, 2.21)); //Min: 21.00 Max: 72.00
			phillipsRowCol.put(NORMAL_ORDER, new DataHolder(241.57, 25.93)); //Min: 139.00 Max: 323.00
			phillipsRowCol.put(MAX_UNIQUE_CHARACTERS, new DataHolder(25));
			
			HashMap<String, DataHolder> caesar = createOrGetList("Simple Substitution", "Caesar Shift");
			caesar.put(IC_x1000, new DataHolder(66.05, 2.72)); //Min: 60.87 Max: 74.50
			caesar.put(IC_MAX_1to15_x1000, new DataHolder(68.38, 3.08)); //Min: 62.30 Max: 76.97
			caesar.put(IC_2_TRUE_x10000, new DataHolder(76.71, 6.70)); //Min: 63.11 Max: 94.06
			caesar.put(IC_2_FALSE_x10000, new DataHolder(76.99, 7.24)); //Min: 60.13 Max: 92.01
			caesar.put(IC_3_FALSE_x100000, new DataHolder(168.14, 61.95)); //Min: 0.00 Max: 380.39
			caesar.put(IC_KAPPA_x1000, new DataHolder(80.69, 8.86)); //Min: 67.38 Max: 118.01
			caesar.put(LOG_DIGRAPH, new DataHolder(419.22, 49.38)); //Min: 303.00 Max: 536.00
			caesar.put(LOG_DIGRAPH_REVERSED, new DataHolder(144.43, 201.92)); //Min: 0.00 Max: 525.00
			caesar.put(LONG_REPEAT, new DataHolder(23.91, 2.04)); //Min: 20.26 Max: 29.59
			caesar.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.67, 1.56)); //Min: 45.00 Max: 56.00
			caesar.put(NORMAL_ORDER, new DataHolder(232.01, 24.12)); //Min: 149.00 Max: 297.00
			caesar.put(LOG_DIGRAPH_CAESAR, new DataHolder(760.04, 6.70)); //Min: 736.92 Max: 774.11
			caesar.put(LOG_DIGRAPH_AFFINE, new DataHolder(602.31, 9.15)); //Min: 585.63 Max: 621.52
			
			HashMap<String, DataHolder> keyword = createOrGetList("Simple Substitution", "Generic");
			keyword.put(IC_x1000, new DataHolder(66.05, 2.72)); //Min: 60.87 Max: 74.50
			keyword.put(IC_MAX_1to15_x1000, new DataHolder(68.38, 3.08)); //Min: 62.30 Max: 76.97
			keyword.put(IC_2_TRUE_x10000, new DataHolder(76.71, 6.70)); //Min: 63.11 Max: 94.06
			keyword.put(IC_2_FALSE_x10000, new DataHolder(76.99, 7.24)); //Min: 60.13 Max: 92.01
			keyword.put(IC_3_FALSE_x100000, new DataHolder(168.14, 61.95)); //Min: 0.00 Max: 380.39
			keyword.put(IC_KAPPA_x1000, new DataHolder(80.69, 8.86)); //Min: 67.38 Max: 118.01
			keyword.put(LOG_DIGRAPH, new DataHolder(426.40, 58.37)); //Min: 218.00 Max: 594.00
			keyword.put(LOG_DIGRAPH_REVERSED, new DataHolder(147.15, 206.55)); //Min: 0.00 Max: 616.00
			keyword.put(LONG_REPEAT, new DataHolder(23.91, 2.04)); //Min: 20.26 Max: 29.59
			keyword.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.67, 1.56)); //Min: 45.00 Max: 56.00
			keyword.put(NORMAL_ORDER, new DataHolder(225.46, 28.28)); //Min: 113.00 Max: 312.00
			keyword.put(LOG_DIGRAPH_CAESAR, new DataHolder(539.81, 22.21)); //Min: 479.52 Max: 623.87
			keyword.put(LOG_DIGRAPH_AFFINE, new DataHolder(576.99, 15.98)); //Min: 535.98 Max: 643.21
			
			HashMap<String, DataHolder> affine = createOrGetList("Simple Substitution", "Affine");
			affine.put(IC_x1000, new DataHolder(66.05, 2.72)); //Min: 60.87 Max: 74.50
			affine.put(IC_MAX_1to15_x1000, new DataHolder(68.38, 3.08)); //Min: 62.30 Max: 76.97
			affine.put(IC_2_TRUE_x10000, new DataHolder(76.71, 6.70)); //Min: 63.11 Max: 94.06
			affine.put(IC_2_FALSE_x10000, new DataHolder(76.99, 7.24)); //Min: 60.13 Max: 92.01
			affine.put(IC_3_FALSE_x100000, new DataHolder(168.14, 61.95)); //Min: 0.00 Max: 380.39
			affine.put(IC_KAPPA_x1000, new DataHolder(80.69, 8.86)); //Min: 67.38 Max: 118.01
			affine.put(LOG_DIGRAPH, new DataHolder(429.37, 59.85)); //Min: 248.00 Max: 618.00
			affine.put(LOG_DIGRAPH_REVERSED, new DataHolder(147.42, 207.11)); //Min: 0.00 Max: 612.00
			affine.put(LONG_REPEAT, new DataHolder(23.91, 2.04)); //Min: 20.26 Max: 29.59
			affine.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.67, 1.56)); //Min: 45.00 Max: 56.00
			affine.put(NORMAL_ORDER, new DataHolder(225.51, 27.86)); //Min: 117.00 Max: 319.00
			affine.put(LOG_DIGRAPH_CAESAR, new DataHolder(543.08, 24.88)); //Min: 494.98 Max: 621.52
			affine.put(LOG_DIGRAPH_AFFINE, new DataHolder(760.04, 6.70)); //Min: 736.92 Max: 774.11
			
			HashMap<String, DataHolder> substitution = createOrGetList("Substitution");
			substitution.put(IC_x1000, new DataHolder(66.01, 2.71));
			substitution.put(IC_MAX_1to15_x1000, new DataHolder(68.32, 3.07));
			substitution.put(IC_KAPPA_x1000, new DataHolder(80.63, 8.91));
			substitution.put(IC_2_TRUE_x10000, new DataHolder(76.80, 6.70));
			substitution.put(IC_2_FALSE_x10000, new DataHolder(77.11, 7.21));
			substitution.put(LONG_REPEAT, new DataHolder(23.97, 2.01));
			substitution.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.65, 1.57));
			substitution.put(NORMAL_ORDER, new DataHolder(225.32, 27.91));
			substitution.put(BIFID_0, new DataHolder(271.18, 26.95));
			substitution.put(BIFID_MAX_3to15, new DataHolder(474.12, 76.01));
			substitution.put(NICODEMUS_MAX_3to15, new DataHolder(66.02, 8.48));
			substitution.put(TRIFID_MAX_3to15, new DataHolder(3739.10, 1076.86));
			substitution.put(LOG_DIGRAPH, new DataHolder(428.49, 60.44));
			substitution.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(581.67, 24.57));
			substitution.put(LOG_DIGRAPH_VIGENERE, new DataHolder(582.17, 25.37));
			substitution.put(LOG_DIGRAPH_PORTA, new DataHolder(563.44, 24.75));

			HashMap<String, DataHolder> seriatedPlayfair = createOrGetList("Seriated Playfair");
			seriatedPlayfair.put(IC_x1000, new DataHolder(49.11, 2.50));
			seriatedPlayfair.put(IC_MAX_1to15_x1000, new DataHolder(50.98, 3.01));
			seriatedPlayfair.put(IC_KAPPA_x1000, new DataHolder(61.54, 6.77));
			seriatedPlayfair.put(IC_2_TRUE_x10000, new DataHolder(26.42, 2.85));
			seriatedPlayfair.put(IC_2_FALSE_x10000, new DataHolder(26.67, 3.58));
			seriatedPlayfair.put(LONG_REPEAT, new DataHolder(8.59, 1.26));
			seriatedPlayfair.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.44, 2.34));
			seriatedPlayfair.put(NORMAL_ORDER, new DataHolder(232.46, 32.85));
			seriatedPlayfair.put(BIFID_0, new DataHolder(150.77, 19.73));
			seriatedPlayfair.put(BIFID_MAX_3to15, new DataHolder(227.30, 70.01));
			seriatedPlayfair.put(NICODEMUS_MAX_3to15, new DataHolder(50.70, 3.00));
			seriatedPlayfair.put(TRIFID_MAX_3to15, new DataHolder(552.35, 348.77));
			seriatedPlayfair.put(LOG_DIGRAPH, new DataHolder(450.54, 39.54));
			seriatedPlayfair.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(551.36, 28.03));
			seriatedPlayfair.put(LOG_DIGRAPH_VIGENERE, new DataHolder(552.18, 27.97));
			seriatedPlayfair.put(LOG_DIGRAPH_PORTA, new DataHolder(538.39, 25.16));
			seriatedPlayfair.put(TEXT_LENGTH_MULTIPLE, new DataHolder(2));
			seriatedPlayfair.put(DOUBLE_LETTER_EVEN_2to40, new DataHolder(false));
			
			HashMap<String, DataHolder> solitaire = createOrGetList("Solitaire");
			solitaire.put(IC_x1000, new DataHolder(38.46, 0.42));
			solitaire.put(IC_MAX_1to15_x1000, new DataHolder(49.92, 5.73));
			solitaire.put(IC_KAPPA_x1000, new DataHolder(40.03, 1.68));
			solitaire.put(IC_2_TRUE_x10000, new DataHolder(14.79, 0.87));
			solitaire.put(IC_2_FALSE_x10000, new DataHolder(14.78, 1.63));
			solitaire.put(LONG_REPEAT, new DataHolder(5.14, 0.89));
			solitaire.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.62, 2.96));
			solitaire.put(NORMAL_ORDER, new DataHolder(222.46, 29.75));
			solitaire.put(BIFID_0, new DataHolder(92.37, 10.50));
			solitaire.put(BIFID_MAX_3to15, new DataHolder(105.27, 13.88));
			solitaire.put(NICODEMUS_MAX_3to15, new DataHolder(40.00, 2.58));
			solitaire.put(TRIFID_MAX_3to15, new DataHolder(233.81, 159.81));
			solitaire.put(LOG_DIGRAPH, new DataHolder(427.60, 12.22));
			solitaire.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(531.78, 32.80));
			solitaire.put(LOG_DIGRAPH_VIGENERE, new DataHolder(531.75, 33.00));
			solitaire.put(LOG_DIGRAPH_PORTA, new DataHolder(517.67, 28.99));
			
			/**HashMap<String, DataHolder> swagman = createOrGetList("Swagman");
			swagman.put(IC_x1000, new DataHolder(65.81, 2.76));
			swagman.put(IC_MAX_1to15_x1000, new DataHolder(68.04, 3.04));
			swagman.put(IC_KAPPA_x1000, new DataHolder(78.30, 7.05));
			swagman.put(IC_2_TRUE_x10000, new DataHolder(45.58, 5.10));
			swagman.put(IC_2_FALSE_x10000, new DataHolder(43.78, 4.60));
			swagman.put(LONG_REPEAT, new DataHolder(12.18, 1.33));
			swagman.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(47.02, 4.58));
			swagman.put(NORMAL_ORDER, new DataHolder(69.88, 29.27));
			swagman.put(BIFID_MAX_3to15, new DataHolder(339.89, 65.01));
			swagman.put(NICODEMUS_MAX_3to15, new DataHolder(67.10, 8.68));
			swagman.put(TRIFID_MAX_3to15, new DataHolder(1151.34, 476.91));**/

			HashMap<String, DataHolder> amsco = createOrGetList("Transposition", "AMSCO");
			amsco.put(IC_x1000, new DataHolder(66.00, 2.71));
			amsco.put(IC_MAX_1to15_x1000, new DataHolder(68.12, 3.15));
			amsco.put(IC_KAPPA_x1000, new DataHolder(80.85, 7.47));
			amsco.put(IC_2_TRUE_x10000, new DataHolder(46.67, 4.14));
			amsco.put(IC_2_FALSE_x10000, new DataHolder(46.68, 4.95));
			amsco.put(LONG_REPEAT, new DataHolder(12.36, 1.22));
			amsco.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.51, 1.71));
			amsco.put(NORMAL_ORDER, new DataHolder(72.55, 30.11));
			amsco.put(BIFID_0, new DataHolder(277.92, 31.40));
			amsco.put(BIFID_MAX_3to15, new DataHolder(307.49, 48.82));
			amsco.put(NICODEMUS_MAX_3to15, new DataHolder(66.88, 8.69));
			amsco.put(TRIFID_MAX_3to15, new DataHolder(930.32, 328.88));
			amsco.put(LOG_DIGRAPH, new DataHolder(691.99, 7.79));
			amsco.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(579.08, 25.30));
			amsco.put(LOG_DIGRAPH_VIGENERE, new DataHolder(693.02, 7.01));
			amsco.put(LOG_DIGRAPH_PORTA, new DataHolder(565.05, 22.86));
			
			HashMap<String, DataHolder> bifid = createOrGetList("Bifid", "P:3-15", "Single");
			bifid.put(IC_x1000, new DataHolder(45.71, 3.08));
			bifid.put(IC_MAX_1to15_x1000, new DataHolder(51.69, 4.42));
			bifid.put(IC_KAPPA_x1000, new DataHolder(60.60, 8.31));
			bifid.put(IC_2_TRUE_x10000, new DataHolder(24.18, 4.71));
			bifid.put(IC_2_FALSE_x10000, new DataHolder(27.41, 14.12));
			bifid.put(LONG_REPEAT, new DataHolder(8.47, 1.91));
			bifid.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(47.21, 7.22));
			bifid.put(NORMAL_ORDER, new DataHolder(192.41, 27.67));
			bifid.put(BIFID_0, new DataHolder(149.14, 69.68));
			bifid.put(BIFID_MAX_3to15, new DataHolder(334.40, 126.10));
			bifid.put(LOG_DIGRAPH, new DataHolder(488.75, 29.66));
			
			HashMap<String, DataHolder> bifid0 = createOrGetList("Bifid", "P:0", "Single");
			bifid0.put(IC_x1000, new DataHolder(45.71, 3.06));
			bifid0.put(IC_MAX_1to15_x1000, new DataHolder(47.56, 3.54));
			bifid0.put(IC_KAPPA_x1000, new DataHolder(64.95, 8.69));
			bifid0.put(IC_2_TRUE_x10000, new DataHolder(24.30, 3.34));
			bifid0.put(IC_2_FALSE_x10000, new DataHolder(24.70, 3.98));
			bifid0.put(LONG_REPEAT, new DataHolder(8.64, 1.31));
			bifid0.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.17, 2.64));
			bifid0.put(NORMAL_ORDER, new DataHolder(195.67, 27.71));
			bifid0.put(BIFID_0, new DataHolder(375.02, 86.87));
			bifid0.put(BIFID_MAX_3to15, new DataHolder(158.13, 24.58));
			bifid0.put(LOG_DIGRAPH, new DataHolder(485.04, 29.57));

			HashMap<String, DataHolder> cmBifid = createOrGetList("Bifid", "P:3-15", "Conjugated");
			cmBifid.put(IC_x1000, new DataHolder(45.68, 3.12)); //Min: 37.84 Max: 66.84
			cmBifid.put(IC_MAX_1to15_x1000, new DataHolder(51.98, 4.35)); //Min: 41.29 Max: 79.17
			cmBifid.put(IC_2_TRUE_x10000, new DataHolder(24.26, 4.82)); //Min: 10.22 Max: 51.57
			cmBifid.put(IC_2_FALSE_x10000, new DataHolder(27.95, 15.04)); //Min: 3.16 Max: 92.01
			cmBifid.put(IC_3_FALSE_x100000, new DataHolder(29.74, 45.10)); //Min: 0.00 Max: 380.39
			cmBifid.put(IC_KAPPA_x1000, new DataHolder(60.42, 8.26)); //Min: 44.21 Max: 130.43
			cmBifid.put(LOG_DIGRAPH, new DataHolder(485.04, 29.57)); //Min: 324.00 Max: 564.00
			cmBifid.put(LOG_DIGRAPH_REVERSED, new DataHolder(154.16, 214.02)); //Min: 0.00 Max: 533.00
			cmBifid.put(LONG_REPEAT, new DataHolder(8.50, 1.98)); //Min: 0.00 Max: 17.18
			cmBifid.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(46.95, 7.56)); //Min: 9.00 Max: 72.00
			cmBifid.put(NORMAL_ORDER, new DataHolder(231.95, 27.02)); //Min: 125.00 Max: 321.00
			cmBifid.put(BIFID_0, new DataHolder(131.74, 21.57)); //Min: 19.29 Max: 250.77
			cmBifid.put(BIFID_MAX_3to15, new DataHolder(347.12, 120.85)); //Min: 135.03 Max: 574.12
			
			HashMap<String, DataHolder> cmBifid0 = createOrGetList("Bifid", "P:0", "Conjugated");
			cmBifid0.put(IC_x1000, new DataHolder(45.82, 3.09)); //Min: 37.50 Max: 62.16
			cmBifid0.put(IC_MAX_1to15_x1000, new DataHolder(47.53, 3.56)); //Min: 40.33 Max: 75.09
			cmBifid0.put(IC_2_TRUE_x10000, new DataHolder(24.27, 3.33)); //Min: 10.21 Max: 44.77
			cmBifid0.put(IC_2_FALSE_x10000, new DataHolder(24.72, 4.06)); //Min: 3.16 Max: 53.80
			cmBifid0.put(IC_3_FALSE_x100000, new DataHolder(19.23, 12.92)); //Min: 0.00 Max: 163.93
			cmBifid0.put(IC_KAPPA_x1000, new DataHolder(64.87, 8.67)); //Min: 46.15 Max: 125.65
			cmBifid0.put(LOG_DIGRAPH, new DataHolder(485.04, 29.57)); //Min: 350.00 Max: 548.00
			cmBifid0.put(LOG_DIGRAPH_REVERSED, new DataHolder(154.54, 214.59)); //Min: 0.00 Max: 541.00
			cmBifid0.put(LONG_REPEAT, new DataHolder(8.67, 1.31)); //Min: 0.00 Max: 13.89
			cmBifid0.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.16, 2.78)); //Min: 12.00 Max: 70.00
			cmBifid0.put(NORMAL_ORDER, new DataHolder(232.04, 27.02)); //Min: 121.00 Max: 323.00
			cmBifid0.put(BIFID_0, new DataHolder(374.11, 86.62)); //Min: 96.45 Max: 617.28
			cmBifid0.put(BIFID_MAX_3to15, new DataHolder(157.91, 24.59)); //Min: 111.19 Max: 335.59
			
			HashMap<String, DataHolder> enigma = createOrGetList("Enigma", "NOPLUGBOARD");
			enigma.put(IC_x1000, new DataHolder(38.50, 0.43));
			enigma.put(IC_MAX_1to15_x1000, new DataHolder(40.07, 1.69));
			enigma.put(IC_KAPPA_x1000, new DataHolder(49.87, 5.66));
			enigma.put(IC_2_TRUE_x10000, new DataHolder(14.82, 0.89));
			enigma.put(IC_2_FALSE_x10000, new DataHolder(14.85, 1.72));
			enigma.put(LONG_REPEAT, new DataHolder(5.15, 0.90));
			enigma.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.56, 3.09));
			enigma.put(NORMAL_ORDER, new DataHolder(248.33, 27.69));
			enigma.put(BIFID_0, new DataHolder(92.89, 10.65));
			enigma.put(BIFID_MAX_3to15, new DataHolder(105.43, 12.67));
			enigma.put(NICODEMUS_MAX_3to15, new DataHolder(40.15, 1.95));
			enigma.put(TRIFID_MAX_3to15, new DataHolder(234.66, 160.42));
			enigma.put(LOG_DIGRAPH, new DataHolder(418.27, 12.27));
			enigma.put(LOG_DIGRAPH_PORTA, new DataHolder(518.66, 29.14));
			enigma.put(LOG_DIGRAPH_AUTOKEY_BEAUFORT, new DataHolder(531.92, 33.05));
			enigma.put(LOG_DIGRAPH_AUTOKEY_PORTA, new DataHolder(464.62, 23.28));
			enigma.put(LOG_DIGRAPH_AUTOKEY_VARIANT, new DataHolder(531.94, 32.90));
			enigma.put(LOG_DIGRAPH_AUTOKEY_VIGENERE, new DataHolder(531.81, 32.98));
			enigma.put(LOG_DIGRAPH_PORTAX, new DataHolder(149.94, 205.56));
			
			HashMap<String, DataHolder> bazeries = createOrGetList("Bazeries");
			bazeries.put(IC_x1000, new DataHolder(66.21, 2.70));
			bazeries.put(IC_MAX_1to15_x1000, new DataHolder(68.47, 3.22));
			bazeries.put(IC_KAPPA_x1000, new DataHolder(78.50, 7.52));
			bazeries.put(IC_2_TRUE_x10000, new DataHolder(63.89, 6.21));
			bazeries.put(IC_2_FALSE_x10000, new DataHolder(64.93, 8.15));
			bazeries.put(LONG_REPEAT, new DataHolder(19.16, 2.07));
			bazeries.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.40, 1.70));
			bazeries.put(NORMAL_ORDER, new DataHolder(238.77, 20.61));
			bazeries.put(BIFID_0, new DataHolder(272.98, 29.53));
			bazeries.put(BIFID_MAX_3to15, new DataHolder(405.23, 44.16));
			bazeries.put(NICODEMUS_MAX_3to15, new DataHolder(67.62, 2.96));
			bazeries.put(TRIFID_MAX_3to15, new DataHolder(2133.32, 631.66));
			bazeries.put(LOG_DIGRAPH, new DataHolder(485.48, 36.77));
			bazeries.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(579.48, 25.50));
			bazeries.put(LOG_DIGRAPH_VIGENERE, new DataHolder(578.51, 25.18));
			bazeries.put(LOG_DIGRAPH_PORTA, new DataHolder(555.82, 25.00));
			
			HashMap<String, DataHolder> trifid = createOrGetList("Trifid");
			trifid.put(IC_x1000, new DataHolder(39.94, 2.34));
			trifid.put(IC_MAX_1to15_x1000, new DataHolder(46.20, 4.32));
			trifid.put(IC_KAPPA_x1000, new DataHolder(54.23, 7.88));
			trifid.put(IC_2_TRUE_x10000, new DataHolder(17.11, 2.63));
			trifid.put(IC_2_FALSE_x10000, new DataHolder(17.60, 3.56));
			trifid.put(LONG_REPEAT, new DataHolder(6.41, 1.77));
			trifid.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(48.72, 3.73));
			trifid.put(NORMAL_ORDER, new DataHolder(0.10, 4.28));
			trifid.put(BIFID_0, new DataHolder(0.04, 2.17));
			trifid.put(BIFID_MAX_3to15, new DataHolder(0.05, 3.05));
			trifid.put(NICODEMUS_MAX_3to15, new DataHolder(0.01, 0.68));
			trifid.put(TRIFID_MAX_3to15, new DataHolder(1598.23, 1572.71));
			trifid.put(LOG_DIGRAPH, new DataHolder(417.69, 25.51));
			trifid.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(0.00, 0.00));
			trifid.put(LOG_DIGRAPH_VIGENERE, new DataHolder(0.00, 0.00));
			trifid.put(LOG_DIGRAPH_PORTA, new DataHolder(0.00, 0.00));
			
			/**
			normalEnglish.put(new StatisticRange(StatisticType.MAX_IOC, 73.0D, 11.0D));
			normalEnglish.put(new StatisticRange(StatisticType.MAX_KAPPA, 95.0D, 19.0D));
			normalEnglish.put(new StatisticRange(StatisticType.DIGRAPHIC_IOC, 72.0D, 18.0D));
			normalEnglish.put(new StatisticRange(StatisticType.EVEN_DIGRAPHIC_IOC, 73.0D, 24.0D));
			normalEnglish.put(new StatisticRange(StatisticType.LONG_REPEAT_3, 22.0D, 5.0D));
			normalEnglish.put(new StatisticRange(StatisticType.LONG_REPEAT_ODD, 50.0D, 6.0D));
			normalEnglish.put(new StatisticRange(StatisticType.LOG_DIGRAPH, 756.0D, 13.0D));
			normalEnglish.put(new StatisticRange(StatisticType.SINGLE_LETTER_DIGRAPH, 303.0D, 23.0D));**/
			System.out.println(map);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, DataHolder> createOrGetList(String... keys) {
		TreeMap<String, Object> last = map;
		
		for(int i = 0; i < keys.length; i++) {
			if(i == keys.length - 1) {//Last Key
				last.put(keys[i], new HashMap<String, DataHolder>());
				return (HashMap<String, DataHolder>)last.get(keys[i]);
			}
			else {
				if(last.containsKey(keys[i]))
					last = (TreeMap<String, Object>)last.get(keys[i]);
				else {
					TreeMap<String, Object> newMap = new TreeMap<String, Object>();
					last.put(keys[i], newMap);
					last = newMap;
				}
			}
		}
		
		return null;
	}
}
