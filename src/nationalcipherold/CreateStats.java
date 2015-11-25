package nationalcipherold;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javalibrary.exception.MatrixNoInverse;
import javalibrary.math.matrics.Matrix;
import javalibrary.string.NumberString;

public class CreateStats {
	public static PrintWriter writer;
	
	public static Map<List<Integer>, List<Integer>> map = new HashMap<List<Integer>, List<Integer>>();
	
	public static void main(String[] args) throws IOException {
		List<String> list = new ArrayList<String>();
		writer = new PrintWriter("3x3matrixInverse.txt", "UTF-8");
		for(int i = 0; i < 1000000; i++) {
			String change = "";
			for(char j : NumberString.convert(i).toCharArray()) {
				if(!change.contains("" + j))
					change += j;
			}
			for(char j : "ABCDEFGHIKLMNOPQRSTUVWXYZ".toCharArray()) {
				if(!change.contains("" + j))
					change += j;
			}

			writer.println(change);
			
		}
		
		
		
		writer.close();
		
		System.out.println("END");
		/**
		int j = 0;
		for(String type : StatCalculator.ctype) {
			System.out.println("List<StatisticRange> " + type.toLowerCase() + "List = new ArrayList<StatisticRange>();");
			System.out.println(type.toLowerCase() + "List.add(new StatisticRange(StatisticType.INDEX_OF_COINCIDENCE, " + StatCalculator.ave.get(0).get(j) + "D, " + StatCalculator.std.get(0).get(j) + "D));");
			System.out.println(type.toLowerCase() + "List.add(new StatisticRange(StatisticType.MAX_IOC, " + StatCalculator.ave.get(1).get(j) + "D, " + StatCalculator.std.get(1).get(j) + "D));");
			System.out.println(type.toLowerCase() + "List.add(new StatisticRange(StatisticType.MAX_KAPPA, " + StatCalculator.ave.get(2).get(j) + "D, " + StatCalculator.std.get(2).get(j) + "D));");
			System.out.println(type.toLowerCase() + "List.add(new StatisticRange(StatisticType.DIGRAPHIC_IOC, " + StatCalculator.ave.get(3).get(j) + "D, " + StatCalculator.std.get(3).get(j) + "D));");
			System.out.println(type.toLowerCase() + "List.add(new StatisticRange(StatisticType.EVEN_DIGRAPHIC_IOC, " + StatCalculator.ave.get(4).get(j) + "D, " + StatCalculator.std.get(4).get(j) + "D));");
			System.out.println(type.toLowerCase() + "List.add(new StatisticRange(StatisticType.LONG_REPEAT_3, " + StatCalculator.ave.get(5).get(j) + "D, " + StatCalculator.std.get(5).get(j) + "D));");
			System.out.println(type.toLowerCase() + "List.add(new StatisticRange(StatisticType.LONG_REPEAT_ODD, " + StatCalculator.ave.get(6).get(j) + "D, " + StatCalculator.std.get(6).get(j) + "D));");
			System.out.println(type.toLowerCase() + "List.add(new StatisticRange(StatisticType.LOG_DIGRAPH, " + StatCalculator.ave.get(7).get(j) + "D, " + StatCalculator.std.get(7).get(j) + "D));");
			System.out.println(type.toLowerCase() + "List.add(new StatisticRange(StatisticType.SINGLE_LETTER_DIGRAPH, " + StatCalculator.ave.get(8).get(j) + "D, " + StatCalculator.std.get(8).get(j) + "D));");
		
			System.out.println("map.put(\"" + type + "\", " + type.toLowerCase() + "List);");
			j += 1;
		}**/
		
		
		/**
		BufferedReader updateReader = new BufferedReader(new InputStreamReader(CreateStats.class.getResourceAsStream("/nationalcipher/plainText.txt")));
	    String line = "";
	    int totalLines = 0;
		double totalIOC = 0.0D;
	    List<Double> values = new ArrayList<Double>();
	    while((line = updateReader.readLine()) != null) {
			if(line.isEmpty() || line.startsWith("#")) continue;
			String plainText = line;
			
			for(int i = 0; i < 45; ++i) {
				int key = new Random().nextInt(15);
				String cipherText = Transposition.encode(plainText, KeySquareManipulation.generateRandKey().substring(0, 2 + key));
				double ioc = StatCalculator.calculateSDD(cipherText);
				totalIOC += ioc;
				totalLines += 1;
				values.add(ioc);
			}
		}
	    
	    double average = totalIOC / totalLines;
	    
	    double totalDiff = 0.0D;
	    for(double value : values) {
	    	double diff = average - value;
	    	totalDiff += Math.pow(diff, 2);
	    }
	    double standardDeviation = Math.sqrt(totalDiff / totalLines);
	    System.out.println(average + " " + standardDeviation);**/
	}
	
	public static void run(int range_low, int range_high, int no, int rows, int columns, int time, int[] array) {
		for(int i = range_low; i <= range_high; i++) {
			array[time] = i;
			
			if(time + 1 >= no) {
				Matrix matrix = new Matrix(array, rows, columns);
				try {
					Matrix inverse = matrix.inverseMod(26);
					
					map.put(toIntegerArray(array), toIntegerArray(inverse.toArray()));
				}
				catch(MatrixNoInverse e) {
					
				}
				finally {
					
				}
				
				continue;
			}
			
			run(range_low, range_high, no, rows, columns, time + 1, array);
		}
	}
	
	public static List<Integer> toIntegerArray(int[] oldArray) {
		List<Integer> newArray = new ArrayList<Integer>();
		int i = 0;
		for(int value : oldArray)
		    newArray.add(value);
		
		return newArray;
	}

}
