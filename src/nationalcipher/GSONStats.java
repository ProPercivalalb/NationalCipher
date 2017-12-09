package nationalcipher;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import javalibrary.streams.FileReader;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.base.substitution.QuagmireI;
import nationalcipher.cipher.stats.CipherStatistics;
import nationalcipher.cipher.stats.DataHolder;

public class GSONStats {

	private static TreeMap<String, Object> map = new TreeMap<>();
	
	public static void main(String[] args) throws IOException {
		String cipherText = "00110001000100010001000100010001000100010001000100010001000100010001000100010001100010010101010101010101010101010101010101010101010101010101010101010101010101010100100010111011101110111011101110111011101110111011101110111011101110111011101110111010001000100010001000100010001000100010001000100010001000100010001000100010001000100010001000000000000000000000000000000000000000000000000000000000000000000000000000001000100000000000000000000000000000000000000000000000000000000000000000000000000000001000100001000111000001110001000100010001000011100100010111000111100111000111001110100010000101001001000100000101001100100101000010001101101001001000001001000100001000100010010001010001010000010001010101010001000100010101010010010000010001001000010010001000111110111100100000111110101010111110001000100010111100111000111100010000100010001001000101010001000001000101010101000100010001000101000001000001010000100001001000100010001010010001000010001010011010001000100010001010000010000010010001000010001000100100010100010001110100010100010100010011100100010100000111100100010111001110100010000000000000000000000000000000000000000000000000000000000000000000000000000000100010000000000000000000000000000000000000000000000000000000000000000000000000000010001010001000100010001000100010001000100010001000100010001000100010001000100010001010001010101010101010101010101010101010101010101010101010101010101010101010101010101000100111011101110111011101110111011101110111011101110111011101110111011101110111001000100100010001000100010001000100010001000100010001000100010001000100010001000100100";
		for(int i = 0; i < cipherText.length(); i += 5) {
			//for(int j = 0; j < 5; j++) {
			//	char ch = cipherText.charAt(i + j);
			//	if
			//}
			System.out.println(cipherText.substring(i, Math.min(cipherText.length(), i + 5)));
		}
		
		//JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(GSONStats.class.getResourceAsStream("/nationalcipher/resources/cipher_statistics.json"))));

		//recurssivelyRead(jsonReader, new ArrayList<String>(), 0);

		//jsonReader.close();
		/**TreeMap<String, Object> map = CipherStatistics.getOtherCipherStatistics();
		try (Writer writer = new FileWriter("Output.json")) {
		    Gson gson = new GsonBuilder()..setPrettyPrinting().create();
		    
		    gson.toJson(map, writer);
		}**/
	}
	
	public static void recurssivelyRead(JsonReader jsonReader, List<String> treeName, int count) throws IOException {
		jsonReader.beginObject();
	    while(jsonReader.hasNext()) {

	        String name = jsonReader.nextName();
	        treeName.set(count, name);
	        if(name.contains("[END]")) {
	        	name = name.replaceAll("[END]", "");
	        	treeName.set(count, name);
	        	HashMap<String, DataHolder> hill2x2 = createOrGetList(treeName);
	        	jsonReader.beginObject();
	            while (jsonReader.hasNext()) {
	                 String n = jsonReader.nextName();
	                 jsonReader.beginObject();
	 	             while (jsonReader.hasNext()) {
	 	                 String n = jsonReader.nextName();
	 	                 
	 	            }
	            }
	            jsonReader.endObject();
	        }
	        else
	        	recurssivelyRead(jsonReader, treeName, count++);
	    }
	    jsonReader.endObject();
	}
	
	public static HashMap<String, DataHolder> createOrGetList(List<String> keys) {
		TreeMap<String, Object> last = map;
		
		for(int i = 0; i < keys.size(); i++) {
			if(i == keys.size() - 1) {//Last Key
				last.put(keys.get(i), new HashMap<String, DataHolder>());
				return (HashMap<String, DataHolder>)last.get(keys.get(i));
			}
			else {
				if(last.containsKey(keys.get(i)))
					last = (TreeMap<String, Object>)last.get(keys.get(i));
				else {
					TreeMap<String, Object> newMap = new TreeMap<String, Object>();
					last.put(keys.get(i), newMap);
					last = newMap;
				}
			}
		}
		
		return null;
	}
}
