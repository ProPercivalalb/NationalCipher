package nationalcipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javalibrary.cipher.ADFGVX;
import javalibrary.cipher.Bazeries;
import javalibrary.cipher.Beaufort;
import javalibrary.cipher.Bifid;
import javalibrary.cipher.Columnar;
import javalibrary.cipher.ColumnarRow;
import javalibrary.cipher.FourSquare;
import javalibrary.cipher.Keyword;
import javalibrary.cipher.Myszkowski;
import javalibrary.cipher.Nicodemus;
import javalibrary.cipher.Playfair;
import javalibrary.cipher.Porta;
import javalibrary.cipher.ProgressiveKey;
import javalibrary.cipher.TwoSquare;
import javalibrary.cipher.Variant;
import javalibrary.cipher.Vigenere;
import javalibrary.cipher.VigenereAutoKey;
import javalibrary.cipher.VigenereProgressiveKey;
import javalibrary.cipher.auto.ColumnarAuto.PermutationTask;
import javalibrary.cipher.wip.Baconian;
import javalibrary.cipher.wip.BifidConjugated;
import javalibrary.cipher.wip.BifidPeriod;
import javalibrary.cipher.wip.Homophonic;
import javalibrary.fitness.QuadgramStats;
import javalibrary.language.ILanguage;
import javalibrary.language.Languages;
import javalibrary.math.ArrayHelper;
import javalibrary.string.NumberString;
import javalibrary.string.StringAnalyzer;
import javalibrary.string.StringTransformer;
import javalibrary.util.MapHelper;

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