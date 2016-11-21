package nationalcipher.wip;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javalibrary.algebra.SimultaneousEquations;
import javalibrary.exception.MatrixNoInverse;
import javalibrary.fitness.NGramData;
import javalibrary.fitness.TextFitness;
import javalibrary.lib.Timer;
import javalibrary.math.matrics.Matrix;
import javalibrary.streams.FileReader;
import javalibrary.string.StringTransformer;
import javalibrary.util.ArrayUtil;
import javalibrary.util.MapHelper;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeyIterator.SquareMatrixKey;

public class CalculateAllValidMatrices {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		Timer timer = new Timer();
/**
		HashMap<String, Double> mapping = new HashMap<String, Double>();
		List<String> trigram = new ArrayList<String>();
		double floor = 0.0D;
		double total = 0.0D;
		double fitnessPerChar = 0.0D;
		
		PrintWriter writer = new PrintWriter("commontrigrampairings.txt", "UTF-8");
		List<String> list = FileReader.compileTextFromResource("/javalibrary/fitness/english_trigrams.txt");
		int length = -1;
		
		for(String line : list) {
			String[] str = line.split(" ");
					
			if(str.length < 2) continue;
					
			int count = Integer.valueOf(str[1]);
			total += count;
			mapping.put(str[0], (double)count);
			length = str[0].length();
			trigram.add(str[0]);
		}
		
		NGramData data = TextFitness.loadFile("/javalibrary/fitness/english_trigrams.txt");
		int[][] pickPattern = generatePickPattern(128);
		List<String> contains = new ArrayList<String>();
		for(int i = 0; i < pickPattern.length; i++) {
			int[] pattern = pickPattern[i];
			int[] matrixData = new int[length * length];
			for(int l = 0; l < length; l++) 
				for(int k = 0; k < length; k++)
					matrixData[l * length + k] = trigram.get(pattern[l]).charAt(k) - 'A';

	
			Matrix matrix = new Matrix(matrixData, length, length);
			try {
				matrix.inverseMod(26);
				//double value = data.getValue(trigram.get(pattern[0]).toCharArray(), 0) + data.getValue(trigram.get(pattern[1]).toCharArray(), 0) + data.getValue(trigram.get(pattern[2]).toCharArray(), 0);
				List<Integer> values = new ArrayList<Integer>();
				for(int l = 0; l < length; l++)
					values.add(converse(trigram.get(pattern[l])));

				Collections.sort(values);
				String s = "";
				for(int l = 0; l < length; l++) 
					s += values.get(l) + (l != length - 1 ? " " : "");
				
				if(!contains.contains(s)) {
					writer.println(trigram.get(pattern[0]) + trigram.get(pattern[1]) + trigram.get(pattern[2]) + " " + s);
					contains.add(s);
				}
					//mapping2.put(value, trigram.get(pattern[0]) + " " + trigram.get(pattern[1]) + " " + trigram.get(pattern[2]));
				//writer.println(trigram.get(pattern[0]) + " " + trigram.get(pattern[1]) + " " + trigram.get(pattern[2]) + " " + (value));
				
			}
			catch(MatrixNoInverse e) {
				//System.out.println("NO");
			}
		}
		System.out.println("None same one, " + contains.size());
		writer.close();**/
		List<String> list = FileReader.compileTextFromResource("/resources/commontrigrampairings.txt");
		NGramData data = TextFitness.loadFile("/javalibrary/fitness/english_trigrams.txt");
		HashMap<String, Double> mapping = new HashMap<String, Double>();
		for(String line : list) {
			String[] split = StringTransformer.splitInto(line, 3);
			mapping.put(split[0] + split[1] + split[2], data.getValue(split[0].toCharArray(), 0) + data.getValue(split[1].toCharArray(), 0) + data.getValue(split[2].toCharArray(), 0));
		}
		Map<String, Double> newMap = MapHelper.sortMapByValue(mapping, false);
		PrintWriter writer = new PrintWriter("commontrigrampairingsordered.txt", "UTF-8");
		for(String key : newMap.keySet()) {
			writer.println(key);
		}
		writer.close();
		    
	
		//HillTask task = new HillTask();
		//KeyIterator.iteratorSquareMatrixKey(task, 3);
		//System.out.println("" + task.i);
		timer.displayTime();
	}
	
	public static int converse(String str) {
		int intConversion = 0;
		for(int i = 0; i < str.length(); i++)
			intConversion += (str.charAt(i) - 'A') * (int)Math.pow(26, str.length() - 1 - i);
		return intConversion;
	}
	
	public static int[][] generatePickPattern(int times) { //MathUtil.factorial(times)
		int[][] patterns = new int[(int)Math.pow(times, 3)][3];

		int count = 0;

			for(int i = 0; i < times; i++)
				for(int j = 0; j < times; j++) {
					for(int k = 0; k < times; k++) {
						if(i == j || i == k || j == k) continue;
						patterns[count++] = new int[] {k, j, i};
					}
				}
			System.out.println("Count: " + count);
			return patterns;

	}
	
	public static class HillTask implements SquareMatrixKey {

		int i = 0;
		
		@Override
		public void onIteration(Matrix matrix) {
			try {
				Matrix inverse = matrix.inverseMod(26);
				BigInteger number = BigInteger.ZERO;
				for(int d = 0; d < matrix.data.length; d++) {
					number = number.add(BigInteger.valueOf(26).pow(matrix.data.length - d - 1).multiply(BigInteger.valueOf((long)matrix.data[d])));
				}
				System.out.println(number);
			}
			catch(MatrixNoInverse e) {
				i++;
			}

			
			i++;
		}
		
	}
}
