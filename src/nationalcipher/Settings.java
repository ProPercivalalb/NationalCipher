package nationalcipher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.ButtonGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javalibrary.language.ILanguage;
import javalibrary.language.Languages;
import javalibrary.lib.OSIdentifier;
import javalibrary.streams.FileReader;
import javalibrary.string.StringTransformer;

public class Settings {

	public ILanguage language;
	public ButtonGroup languageGroup;
	public int keywordCreation;
	public ButtonGroup keywordCreationGroup;
	public List<Double> simulatedAnnealing;
	
	private List<LoadElement> loadElements;
	private Gson gson;
	
	public Settings() {
		this.language = Languages.english;
		this.keywordCreation = 0;
		this.simulatedAnnealing = Arrays.asList(20.0D, 0.1D, 500.0D);
		
		this.loadElements = new ArrayList<LoadElement>();
		this.gson = new Gson();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
	        public void run() {
				writeToFile();
	        }
		}));
		new Thread(new Runnable() {
			@Override
	        public void run() {
				while(!Thread.interrupted()) {
					try {
						Thread.sleep(60 * 1000);
					} 
					catch(InterruptedException e) {
						e.printStackTrace();
					}
					writeToFile();
				}
	        }
		}, "SAVE-THREAD").start();
	}
	
	public ILanguage getLanguage() {
		return this.language;
	}
	
	public int getKeywordCreationId() {
		return this.keywordCreation;
	}
	
	public double getSATempStart() {
		return this.simulatedAnnealing.get(0);
	}
	
	public double getSATempStep() {
		return this.simulatedAnnealing.get(1);
	}
	
	public int getSACount() {
		return this.simulatedAnnealing.get(2).intValue();
	}
		
		
	private void writeToFile() {
		try {
			File file = new File(OSIdentifier.getMyDataFolder("nationalcipher"), "savedata.txt");
			if(!file.exists()) file.createNewFile();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			HashMap<String, Object> map = new HashMap<String, Object>();
				
			//WRITE
			map.put("language", this.language.getName());
			map.put("keyword_creation", this.keywordCreation);
			map.put("simulated_annealing", this.simulatedAnnealing);
			
			for(LoadElement loadElement : this.loadElements)
				loadElement.write(map);
			
		    writer.append(gson.toJson(map));
		    writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	public void readFromFile() {
		try {
			File myFile = new File(OSIdentifier.getMyDataFolder("nationalcipher"), "savedata.txt");
			String input = StringTransformer.joinWith(FileReader.compileTextFromFile(myFile), "");
				
			HashMap<String, Object> map = gson.fromJson(input, new TypeToken<HashMap<String, Object>>(){}.getType());
			
			if(map == null) return;
			//READ
			if(map.containsKey("language")) {
				String langKey = (String)map.get("language");
				for(ILanguage language : Languages.languages) {
					if(language.getName().equalsIgnoreCase(langKey)) {
						this.language = language;
						break;
					}
				}
			}
			
			if(map.containsKey("keyword_creation")) {
				int keywordCreation = (int)(double)map.get("keyword_creation");
				this.keywordCreation = keywordCreation;  
			}
			
			if(map.containsKey("simulated_annealing")) {
				List<Double> simulatedAnnealing = ((List<Double>)map.get("simulated_annealing"));
				this.simulatedAnnealing = simulatedAnnealing;
			}
			
			for(LoadElement loadElement : this.loadElements)
				loadElement.read(map);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void addLoadElement(LoadElement loadElement) {
		this.loadElements.add(loadElement);
	}
	
}
