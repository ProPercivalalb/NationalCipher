package nationalcipher.cipher.stats;

import java.util.HashMap;

public class StatsOld {

	
	/**
	HashMap<String, DataHolder> portaAutokey = createOrGetList("Porta Autokey move to right");
	portaAutokey.put(IC_x1000, new DataHolder(39.32, 0.69));
	portaAutokey.put(IC_MAX_1to15_x1000, new DataHolder(40.98, 1.92));
	portaAutokey.put(IC_KAPPA_x1000, new DataHolder(50.74, 5.63));
	portaAutokey.put(IC_2_TRUE_x10000, new DataHolder(16.28, 1.60));
	portaAutokey.put(IC_2_FALSE_x10000, new DataHolder(16.30, 2.22));
	portaAutokey.put(LONG_REPEAT, new DataHolder(6.02, 1.30));
	portaAutokey.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.57, 3.01));
	portaAutokey.put(NORMAL_ORDER, new DataHolder(224.66, 28.04));
	portaAutokey.put(BIFID_0, new DataHolder(96.43, 11.10));
	portaAutokey.put(BIFID_MAX_3to15, new DataHolder(113.80, 16.20));
	portaAutokey.put(NICODEMUS_MAX_3to15, new DataHolder(40.85, 1.69));
	portaAutokey.put(TRIFID_MAX_3to15, new DataHolder(338.97, 289.56));
	portaAutokey.put(LOG_DIGRAPH, new DataHolder(429.15, 14.56));
	portaAutokey.put(LOG_DIGRAPH_PORTA, new DataHolder(523.10, 28.50));
	portaAutokey.put(LOG_DIGRAPH_AUTOKEY_BEAUFORT, new DataHolder(532.19, 32.92));
	portaAutokey.put(LOG_DIGRAPH_AUTOKEY_PORTA, new DataHolder(467.17, 22.21));
	portaAutokey.put(LOG_DIGRAPH_AUTOKEY_VARIANT, new DataHolder(532.37, 33.16));
	portaAutokey.put(LOG_DIGRAPH_AUTOKEY_VIGENERE, new DataHolder(532.25, 33.11));
	portaAutokey.put(LOG_DIGRAPH_PORTAX, new DataHolder(147.77, 202.64));
	 */
	
	/**
	HashMap<String, DataHolder> variantAutokey = createOrGetList("Variant Autokey");
	variantAutokey.put(IC_x1000, new DataHolder(39.78, 0.77));
	variantAutokey.put(IC_MAX_1to15_x1000, new DataHolder(41.39, 1.88));
	variantAutokey.put(IC_KAPPA_x1000, new DataHolder(51.57, 5.77));
	variantAutokey.put(IC_2_TRUE_x10000, new DataHolder(16.74, 1.62));
	variantAutokey.put(IC_2_FALSE_x10000, new DataHolder(16.71, 2.18));
	variantAutokey.put(LONG_REPEAT, new DataHolder(6.09, 1.23));
	variantAutokey.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.75, 3.02));
	variantAutokey.put(NORMAL_ORDER, new DataHolder(204.75, 24.62));
	variantAutokey.put(BIFID_0, new DataHolder(98.86, 11.57));
	variantAutokey.put(BIFID_MAX_3to15, new DataHolder(115.05, 16.44));
	variantAutokey.put(NICODEMUS_MAX_3to15, new DataHolder(41.48, 2.09));
	variantAutokey.put(TRIFID_MAX_3to15, new DataHolder(338.61, 287.27));
	variantAutokey.put(LOG_DIGRAPH, new DataHolder(440.75, 12.90));
	variantAutokey.put(LOG_DIGRAPH_PORTA, new DataHolder(520.13, 28.65));
	variantAutokey.put(LOG_DIGRAPH_AUTOKEY_BEAUFORT, new DataHolder(567.88, 25.07));
	variantAutokey.put(LOG_DIGRAPH_AUTOKEY_PORTA, new DataHolder(463.56, 22.81));
	variantAutokey.put(LOG_DIGRAPH_AUTOKEY_VARIANT, new DataHolder(759.68, 6.69));
	variantAutokey.put(LOG_DIGRAPH_AUTOKEY_VIGENERE, new DataHolder(533.70, 33.23));
	variantAutokey.put(LOG_DIGRAPH_PORTAX, new DataHolder(156.43, 214.49));
	**/
	
	/**
	HashMap<String, DataHolder> vigenereAutokey = createOrGetList("Vigenere Autokey");
	vigenereAutokey.put(IC_x1000, new DataHolder(39.83, 0.72));
	vigenereAutokey.put(IC_MAX_1to15_x1000, new DataHolder(41.49, 1.95));
	vigenereAutokey.put(IC_KAPPA_x1000, new DataHolder(65.64, 8.77));
	vigenereAutokey.put(IC_2_TRUE_x10000, new DataHolder(16.90, 1.67));
	vigenereAutokey.put(IC_2_FALSE_x10000, new DataHolder(16.89, 2.28));
	vigenereAutokey.put(LONG_REPEAT, new DataHolder(6.23, 1.24));
	vigenereAutokey.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.65, 2.83));
	vigenereAutokey.put(NORMAL_ORDER, new DataHolder(195.42, 22.27));
	vigenereAutokey.put(BIFID_0, new DataHolder(99.95, 10.71));
	vigenereAutokey.put(BIFID_MAX_3to15, new DataHolder(115.96, 16.05));
	vigenereAutokey.put(NICODEMUS_MAX_3to15, new DataHolder(41.50, 2.25));
	vigenereAutokey.put(TRIFID_MAX_3to15, new DataHolder(347.07, 297.28));
	vigenereAutokey.put(LOG_DIGRAPH, new DataHolder(443.37, 13.38));
	vigenereAutokey.put(LOG_DIGRAPH_REVERSED, new DataHolder(154.42, 211.65));
	vigenereAutokey.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(536.48, 32.42));
	vigenereAutokey.put(LOG_DIGRAPH_VIGENERE, new DataHolder(537.40, 32.03));
	vigenereAutokey.put(LOG_DIGRAPH_VARIANT, new DataHolder(537.22, 31.73));
	vigenereAutokey.put(LOG_DIGRAPH_PORTA, new DataHolder(520.94, 28.25));
	vigenereAutokey.put(LOG_DIGRAPH_AUTOKEY_BEAUFORT, new DataHolder(533.24, 33.04));
	vigenereAutokey.put(LOG_DIGRAPH_AUTOKEY_PORTA, new DataHolder(464.16, 23.12));
	vigenereAutokey.put(LOG_DIGRAPH_AUTOKEY_VARIANT, new DataHolder(533.34, 33.19));
	vigenereAutokey.put(LOG_DIGRAPH_AUTOKEY_VIGENERE, new DataHolder(759.68, 6.68));
	vigenereAutokey.put(LOG_DIGRAPH_PORTAX, new DataHolder(148.22, 203.25)); 
	 */
	
	/**
	HashMap<String, DataHolder> beaufortAutokey = createOrGetList("Beaufort Autokey");
	beaufortAutokey.put(IC_x1000, new DataHolder(39.79, 0.76));
	beaufortAutokey.put(IC_MAX_1to15_x1000, new DataHolder(41.38, 1.90));
	beaufortAutokey.put(IC_KAPPA_x1000, new DataHolder(51.57, 5.69));
	beaufortAutokey.put(IC_2_TRUE_x10000, new DataHolder(16.72, 1.61));
	beaufortAutokey.put(IC_2_FALSE_x10000, new DataHolder(16.70, 2.19));
	beaufortAutokey.put(LONG_REPEAT, new DataHolder(6.09, 1.22));
	beaufortAutokey.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.73, 3.10));
	beaufortAutokey.put(NORMAL_ORDER, new DataHolder(206.71, 25.04));
	beaufortAutokey.put(BIFID_0, new DataHolder(98.74, 11.10));
	beaufortAutokey.put(BIFID_MAX_3to15, new DataHolder(115.28, 16.56));
	beaufortAutokey.put(NICODEMUS_MAX_3to15, new DataHolder(41.49, 2.11));
	beaufortAutokey.put(TRIFID_MAX_3to15, new DataHolder(338.71, 289.97));
	beaufortAutokey.put(LOG_DIGRAPH, new DataHolder(439.99, 12.54));
	beaufortAutokey.put(LOG_DIGRAPH_PORTA, new DataHolder(519.96, 28.57));
	beaufortAutokey.put(LOG_DIGRAPH_AUTOKEY_BEAUFORT, new DataHolder(759.68, 6.70));
	beaufortAutokey.put(LOG_DIGRAPH_AUTOKEY_PORTA, new DataHolder(464.34, 23.59));
	beaufortAutokey.put(LOG_DIGRAPH_AUTOKEY_VARIANT, new DataHolder(567.86, 24.67));
	beaufortAutokey.put(LOG_DIGRAPH_AUTOKEY_VIGENERE, new DataHolder(533.45, 33.42));
	beaufortAutokey.put(LOG_DIGRAPH_PORTAX, new DataHolder(155.88, 213.67));
	 */
	
	/**
	HashMap<String, DataHolder> vigenereSlidefair = createOrGetList("Vigenere Slidefair");
	vigenereSlidefair.put(IC_x1000, new DataHolder(40.24, 1.96));
	vigenereSlidefair.put(IC_MAX_1to15_x1000, new DataHolder(57.03, 7.28));
	vigenereSlidefair.put(IC_KAPPA_x1000, new DataHolder(56.91, 10.99));
	vigenereSlidefair.put(IC_2_TRUE_x10000, new DataHolder(18.60, 3.15));
	vigenereSlidefair.put(IC_2_FALSE_x10000, new DataHolder(25.44, 8.01));
	vigenereSlidefair.put(LONG_REPEAT, new DataHolder(6.73, 1.37));
	vigenereSlidefair.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(40.21, 6.22));
	vigenereSlidefair.put(NORMAL_ORDER, new DataHolder(239.96, 37.39));
	vigenereSlidefair.put(BIFID_0, new DataHolder(104.69, 16.41));
	vigenereSlidefair.put(BIFID_MAX_3to15, new DataHolder(138.08, 32.85));
	vigenereSlidefair.put(NICODEMUS_MAX_3to15, new DataHolder(42.93, 2.63));
	vigenereSlidefair.put(TRIFID_MAX_3to15, new DataHolder(473.19, 291.01));
	vigenereSlidefair.put(LOG_DIGRAPH, new DataHolder(421.81, 24.95));
	vigenereSlidefair.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(561.08, 27.13));
	vigenereSlidefair.put(LOG_DIGRAPH_VIGENERE, new DataHolder(609.08, 44.72));
	vigenereSlidefair.put(LOG_DIGRAPH_PORTA, new DataHolder(544.63, 25.42));
	vigenereSlidefair.put(LOG_DIGRAPH_SLIDEFAIR, new DataHolder(748.12, 7.88));
	vigenereSlidefair.put(LOG_DIGRAPH_PORTAX, new DataHolder(432.88, 20.27));
	 */
	
	/**
	HashMap<String, DataHolder> beaufortSlidefair = createOrGetList("Beaufort Slidefair");
	beaufortSlidefair.put(IC_x1000, new DataHolder(41.90, 3.46));
	beaufortSlidefair.put(IC_MAX_1to15_x1000, new DataHolder(56.86, 7.39));
	beaufortSlidefair.put(IC_KAPPA_x1000, new DataHolder(56.98, 11.21));
	beaufortSlidefair.put(IC_2_TRUE_x10000, new DataHolder(19.59, 4.39));
	beaufortSlidefair.put(IC_2_FALSE_x10000, new DataHolder(24.34, 8.20));
	beaufortSlidefair.put(LONG_REPEAT, new DataHolder(7.04, 1.58));
	beaufortSlidefair.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(44.29, 3.91));
	beaufortSlidefair.put(NORMAL_ORDER, new DataHolder(235.88, 29.91));
	beaufortSlidefair.put(BIFID_0, new DataHolder(113.57, 23.16));
	beaufortSlidefair.put(BIFID_MAX_3to15, new DataHolder(159.95, 56.30));
	beaufortSlidefair.put(NICODEMUS_MAX_3to15, new DataHolder(45.60, 3.83));
	beaufortSlidefair.put(TRIFID_MAX_3to15, new DataHolder(545.84, 350.39));
	beaufortSlidefair.put(LOG_DIGRAPH, new DataHolder(419.43, 25.37));
	beaufortSlidefair.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(607.34, 45.32));
	beaufortSlidefair.put(LOG_DIGRAPH_VIGENERE, new DataHolder(559.09, 27.18));
	beaufortSlidefair.put(LOG_DIGRAPH_PORTA, new DataHolder(542.33, 25.24));
	beaufortSlidefair.put(LOG_DIGRAPH_SLIDEFAIR, new DataHolder(747.08, 8.51));
	beaufortSlidefair.put(LOG_DIGRAPH_PORTAX, new DataHolder(431.79, 23.68));
	*/
	
	/**
				
			HashMap<String, DataHolder> beaufort = createOrGetList("Beaufort");
			beaufort.put(IC_x1000, new DataHolder(42.11, 3.63));
			beaufort.put(IC_MAX_1to15_x1000, new DataHolder(66.53, 3.21));
			beaufort.put(IC_KAPPA_x1000, new DataHolder(69.01, 9.56));
			beaufort.put(IC_2_TRUE_x10000, new DataHolder(24.40, 7.77));
			beaufort.put(IC_2_FALSE_x10000, new DataHolder(30.53, 15.76));
			beaufort.put(LONG_REPEAT, new DataHolder(10.46, 2.93));
			beaufort.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(39.57, 11.73));
			beaufort.put(NORMAL_ORDER, new DataHolder(223.84, 31.38));
			beaufort.put(BIFID_0, new DataHolder(118.07, 25.77));
			beaufort.put(BIFID_MAX_3to15, new DataHolder(170.96, 56.25));
			beaufort.put(NICODEMUS_MAX_3to15, new DataHolder(44.91, 3.53));
			beaufort.put(TRIFID_MAX_3to15, new DataHolder(1166.81, 896.87));
			beaufort.put(LOG_DIGRAPH, new DataHolder(426.97, 25.89));
			beaufort.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(759.39, 6.56));
			beaufort.put(LOG_DIGRAPH_VIGENERE, new DataHolder(576.44, 23.69));
			beaufort.put(LOG_DIGRAPH_PORTA, new DataHolder(558.01, 20.38));
			
				
			HashMap<String, DataHolder> vigenere = createOrGetList("Vigenere");
			vigenere.put(IC_x1000, new DataHolder(42.99, 3.75));
			vigenere.put(IC_MAX_1to15_x1000, new DataHolder(66.55, 3.19));
			vigenere.put(IC_KAPPA_x1000, new DataHolder(68.97, 9.57));
			vigenere.put(IC_2_TRUE_x10000, new DataHolder(25.09, 8.09));
			vigenere.put(IC_2_FALSE_x10000, new DataHolder(31.01, 15.38));
			vigenere.put(LONG_REPEAT, new DataHolder(10.52, 2.95));
			vigenere.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(39.83, 11.540));
			vigenere.put(NORMAL_ORDER, new DataHolder(224.09, 32.97));
			vigenere.put(BIFID_MAX_3to15, new DataHolder(175.78, 58.23));
			vigenere.put(NICODEMUS_MAX_3to15, new DataHolder(45.71, 3.58));
			vigenere.put(TRIFID_MAX_3to15, new DataHolder(1184.37, 907.84));
			vigenere.put(LOG_DIGRAPH, new DataHolder(427.54, 25.46));
			vigenere.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(576.21, 23.67));
			vigenere.put(LOG_DIGRAPH_VIGENERE, new DataHolder(759.40, 6.53));
			vigenere.put(LOG_DIGRAPH_PORTA, new DataHolder(566.04, 22.52));
		
			
						
			HashMap<String, DataHolder> porta = createOrGetList("Porta");
			porta.put(IC_x1000, new DataHolder(42.19, 3.78));
			porta.put(IC_MAX_1to15_x1000, new DataHolder(66.52, 3.20));
			porta.put(IC_KAPPA_x1000, new DataHolder(68.83, 9.68));
			porta.put(IC_2_TRUE_x10000, new DataHolder(24.35, 7.94));
			porta.put(IC_2_FALSE_x10000, new DataHolder(30.22, 15.37));
			porta.put(LONG_REPEAT, new DataHolder(10.47, 3.00));
			porta.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(39.65, 12.03));
			porta.put(NORMAL_ORDER, new DataHolder(226.59, 35.47));
			porta.put(BIFID_0, new DataHolder(118.52, 26.41));
			porta.put(BIFID_MAX_3to15, new DataHolder(170.61, 56.75));
			porta.put(NICODEMUS_MAX_3to15, new DataHolder(45.02, 3.72));
			porta.put(TRIFID_MAX_3to15, new DataHolder(1180.48, 912.31));
			porta.put(LOG_DIGRAPH, new DataHolder(424.07, 30.38));
			porta.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(574.33, 23.31));
			porta.put(LOG_DIGRAPH_VIGENERE, new DataHolder(594.77, 23.25));
			porta.put(LOG_DIGRAPH_PORTA, new DataHolder(759.31, 6.74));
			
						
			HashMap<String, DataHolder> portax = createOrGetList("Portax");
			portax.put(IC_x1000, new DataHolder(42.56, 1.28));
			portax.put(IC_MAX_1to15_x1000, new DataHolder(48.14, 3.73));
			portax.put(IC_KAPPA_x1000, new DataHolder(56.56, 9.21));
			portax.put(IC_2_TRUE_x10000, new DataHolder(19.78, 2.95));
			portax.put(IC_2_FALSE_x10000, new DataHolder(21.08, 3.66));
			portax.put(LONG_REPEAT, new DataHolder(7.06, 1.46));
			portax.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(46.95, 3.65));
			portax.put(NORMAL_ORDER, new DataHolder(220.65, 16.14));
			portax.put(BIFID_0, new DataHolder(114.37, 13.63));
			portax.put(BIFID_MAX_3to15, new DataHolder(166.64, 51.45));
			portax.put(NICODEMUS_MAX_3to15, new DataHolder(44.96, 2.38));
			portax.put(TRIFID_MAX_3to15, new DataHolder(490.75, 653.48));
			portax.put(LOG_DIGRAPH, new DataHolder(437.86, 14.46));
			portax.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(544.10, 27.50));
			portax.put(LOG_DIGRAPH_VIGENERE, new DataHolder(544.55, 27.59));
			portax.put(LOG_DIGRAPH_PORTA, new DataHolder(523.71, 25.50));
			//portax.put(LOG_DIGRAPH_SLIDEFAIR, new DataHolder(542.32, 28.57));
			portax.put(LOG_DIGRAPH_PORTAX, new DataHolder(651.59, 42.82));
			
			HashMap<String, DataHolder> fourSquare = createOrGetList("Four Square");
			fourSquare.put(IC_x1000, new DataHolder(46.28, 1.73));
			fourSquare.put(IC_MAX_1to15_x1000, new DataHolder(54.24, 2.45));
			fourSquare.put(IC_KAPPA_x1000, new DataHolder(61.97, 6.78));
			fourSquare.put(IC_2_TRUE_x10000, new DataHolder(35.50, 2.92));
			fourSquare.put(IC_2_FALSE_x10000, new DataHolder(77.06, 7.25));
			fourSquare.put(LONG_REPEAT, new DataHolder(12.36, 1.17));
			fourSquare.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(21.98, 3.76));
			fourSquare.put(NORMAL_ORDER, new DataHolder(231.64, 27.05));
			fourSquare.put(BIFID_0, new DataHolder(135.38, 14.64));
			fourSquare.put(BIFID_MAX_3to15, new DataHolder(223.96, 21.69));
			fourSquare.put(NICODEMUS_MAX_3to15, new DataHolder(48.10, 2.46));
			fourSquare.put(TRIFID_MAX_3to15, new DataHolder(1082.92, 312.10));
			fourSquare.put(LOG_DIGRAPH, new DataHolder(449.68, 28.87));
			fourSquare.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(558.64, 25.79));
			fourSquare.put(LOG_DIGRAPH_VIGENERE, new DataHolder(558.64, 25.91));
			fourSquare.put(LOG_DIGRAPH_PORTA, new DataHolder(543.91, 23.25));
			fourSquare.put(TEXT_LENGTH_MULTIPLE, new DataHolder(2));
			
			HashMap<String, DataHolder> twoSquare = createOrGetList("Two Square");
			twoSquare.put(IC_x1000, new DataHolder(46.09, 2.29));
			twoSquare.put(IC_MAX_1to15_x1000, new DataHolder(52.91, 3.59));
			twoSquare.put(IC_KAPPA_x1000, new DataHolder(61.05, 7.58));
			twoSquare.put(IC_2_TRUE_x10000, new DataHolder(35.67, 3.08));
			twoSquare.put(IC_2_FALSE_x10000, new DataHolder(76.97, 7.25));
			twoSquare.put(LONG_REPEAT, new DataHolder(12.33, 1.17));
			twoSquare.put(LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(22.94, 3.72));
			twoSquare.put(NORMAL_ORDER, new DataHolder(190.32, 32.72));
			twoSquare.put(BIFID_0, new DataHolder(134.74, 18.48));
			twoSquare.put(BIFID_MAX_3to15, new DataHolder(224.40, 23.36));
			twoSquare.put(NICODEMUS_MAX_3to15, new DataHolder(47.74, 2.79));
			twoSquare.put(TRIFID_MAX_3to15, new DataHolder(1091.40, 326.08));
			twoSquare.put(LOG_DIGRAPH, new DataHolder(486.03, 30.99));
			twoSquare.put(LOG_DIGRAPH_BEAUFORT, new DataHolder(557.72, 26.88));
			twoSquare.put(LOG_DIGRAPH_VIGENERE, new DataHolder(558.48, 27.38));
			twoSquare.put(LOG_DIGRAPH_PORTA, new DataHolder(541.36, 24.30));
			twoSquare.put(TEXT_LENGTH_MULTIPLE, new DataHolder(2));
	 */
}
