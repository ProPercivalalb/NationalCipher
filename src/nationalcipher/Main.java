package nationalcipher;

import javalibrary.cipher.wip.RouteCipher;
import javalibrary.swing.FontUtil;

import javax.swing.UIManager;

/**
 * @author Alex Barter (10AS)
 */
public class Main {

	public static NationalCipher instance;
	
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		FontUtil.setGlobalUIFont("Courier New");
		
		//RouteCipher.decode("EJXCTEDECDAEWRIORFEONALEVSE");
		//RouteCipher.decode("EOEFROIRWEADCEDETCXJNALEVSE");
		//RouteCipher.decode("WEADERISCEVOREDELFEATCNOEJX");
		RouteCipher.decode("XJCENTOAEELDFEERVCOSDIEAREW");
		
		
		instance = new NationalCipher();
	}
	
}
