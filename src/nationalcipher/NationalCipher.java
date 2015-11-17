package nationalcipher;

import javalibrary.language.ILanguage;
import javalibrary.language.Languages;

/**
 * @author Alex Barter (10AS)
 */
public class NationalCipher {

	public UIDefinition definition;
	public ILanguage language;
	
	public NationalCipher() {
		this.definition = new UIDefinition();
		this.language = Languages.english;
		
		this.definition.initializeObjects();
		this.definition.placeObjects();
		this.definition.finalizeObjects();
		this.definition.end();
	}
}
