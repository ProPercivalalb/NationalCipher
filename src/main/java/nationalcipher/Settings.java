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
import nationalcipher.cipher.interfaces.ILoadElement;

public class Settings {

	private ILanguage language;
	public ButtonGroup languageGroup;
	private int keywordCreation;
	public ButtonGroup keywordCreationGroup;
	public boolean checkShift;
	public boolean checkReverse;
	public boolean checkRoutes;
	public boolean useParallel;
	private List<Double> simulatedAnnealing;
	public boolean updateProgressBars;
	public boolean collectSolutions;
	
	private List<ILoadElement> loadElements;
	private Gson gson;
	
	public Settings() {
		//Default
		this.language = Languages.ENGLISH;
		this.keywordCreation = 0;
		this.simulatedAnnealing = Arrays.asList(20.0D, 0.1D, 500.0D);
		this.checkShift = true;
		this.checkReverse = true;
		this.checkRoutes = true;
		this.updateProgressBars = false;
		this.collectSolutions = false;
		this.useParallel = false;
		
		this.loadElements = new ArrayList<ILoadElement>();
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
	
	//
	public String getKeywordFiller() {
		return new String[]{"ABCDEFGHIJKLMNOPQRSTUVWXYZ", "NOPQRSTUVWXYZABCDEFGHIJKLM", "ZYXWVUTSRQPONMLKJIHGFEDCBA"}[this.getKeywordCreationId()];
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
	
	//Iteration counter
	public int getSAIteration() {
		return (int)(this.getSATempStart() / this.getSATempStep()) * this.getSACount();
	}
	
	public List<Double> getSA() {
		return this.simulatedAnnealing;
	}
	
	public boolean checkShift() {
		return this.checkShift;
	}
	
	public boolean checkReverse() {
		return this.checkReverse;
	}
	
	public boolean checkRoutes() {
		return this.checkRoutes;
	}
	
	public boolean updateProgress() {
		return this.updateProgressBars;
	}
	
	public boolean collectSolutions() {
		return this.collectSolutions;
	}
	
	public boolean useParallel() {
        return this.useParallel;
    }
	
	public void setLanguage(ILanguage language) {
		this.language = language;
	}
	
	public void setKeywordCreationId(int id) {
		this.keywordCreation = id;
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
			map.put("check_shift", this.checkShift);
			map.put("check_reverse", this.checkReverse);
			map.put("check_routes", this.checkRoutes);
			map.put("update_progress_bars", this.updateProgressBars);
			map.put("collect_solutions", this.collectSolutions);
			map.put("use_parallel", this.useParallel);
			
			for(ILoadElement loadElement : this.loadElements)
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
			
			this.checkShift = SettingsUtil.getSetting("check_shift", map, Boolean.TYPE, true);
			this.checkReverse = SettingsUtil.getSetting("check_reverse", map, Boolean.TYPE, true);
			this.checkRoutes = SettingsUtil.getSetting("check_routes", map, Boolean.TYPE, true);
			this.updateProgressBars = SettingsUtil.getSetting("update_progress_bars", map, Boolean.TYPE, false);
			this.collectSolutions = SettingsUtil.getSetting("collect_solutions", map, Boolean.TYPE, false);
			this.useParallel = SettingsUtil.getSetting("use_parallel", map, Boolean.TYPE, false);
			
			for(ILoadElement loadElement : this.loadElements)
				loadElement.read(map);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void addLoadElement(ILoadElement loadElement) {
		this.loadElements.add(loadElement);
	}
	
}
