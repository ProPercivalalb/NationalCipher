package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import nationalcipher.LoadElement;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.ui.IApplication;

public class CipherAttack implements LoadElement {
	
	private String displayName;
	private List<DecryptionMethod> methods;
	
	public CipherAttack(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public void setAttackMethods(DecryptionMethod... methods) {
		this.methods = Arrays.asList(methods);
	}

	//When the attack ends naturally or the thread is stopped forcefully
	public void onTermination(boolean forced) {
		
	}
	
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		
	}
	
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	public final List<DecryptionMethod> getAttackMethods() {
		return this.methods == null ? Arrays.asList() : this.methods;
	}

	@Override
	public void write(HashMap<String, Object> map) {
		
	}

	@Override
	public void read(HashMap<String, Object> map) {
		
	}
}
