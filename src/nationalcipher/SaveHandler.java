package nationalcipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javalibrary.lib.OSIdentifier;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SaveHandler {

	public static Gson gson;
	
	public static void save() {
		try {
			File myFile = new File(OSIdentifier.getMyDataFolder("nationalcipher"), "savedata.txt");
			if(!myFile.exists())
				myFile.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(myFile));
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			Main.instance.definition.writeToMap(map);
	        String jsonString = gson.toJson(map);
	        
	        writer.append(jsonString);
	        writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void load() {
		try {
			File myFile = new File(OSIdentifier.getMyDataFolder("nationalcipher"), "savedata.txt");
			if(myFile.exists()) {
		        FileInputStream fIn = new FileInputStream(myFile);
		        BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
		        String aDataRow = "";
		        String aBuffer = "";
		        while ((aDataRow = myReader.readLine()) != null) 
		            aBuffer += aDataRow;
		        
		        
		        
		        if(!aBuffer.isEmpty()) {
					HashMap<String, Object> map = gson.fromJson(aBuffer, new TypeToken<HashMap<String, Object>>() {}.getType());
					Main.instance.definition.readFromMap(map);
		        }
		        
		        
		        myReader.close();
			}	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	static {
	    gson = new Gson();
	}
}
