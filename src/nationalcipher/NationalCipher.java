package nationalcipher;

import java.util.List;
import java.util.Map;

import javalibrary.cipher.stats.WordSplit;
import javalibrary.language.ILanguage;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.math.Units.Time;
import javalibrary.string.StringAnalyzer;
import javalibrary.string.StringTransformer;
import javalibrary.util.MapHelper;
import nationalciphernew.cipher.stats.StatCalculator;

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
