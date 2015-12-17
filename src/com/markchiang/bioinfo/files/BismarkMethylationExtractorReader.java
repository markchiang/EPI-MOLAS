/**
 * 
 */
package com.markchiang.bioinfo.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import com.markchiang.bioinfo.datatypes.Gene;

/**
 * @author markchiang
 *
 */
public class BismarkMethylationExtractorReader {
	
	String filename = null;
	TreeMap<String,TreeMap<Integer,Number>> chrMapCG= new TreeMap<String,TreeMap<Integer,Number>>();
	TreeMap<String,TreeMap<Integer,Number>> chrMapCHG= new TreeMap<String,TreeMap<Integer,Number>>();
	TreeMap<String,TreeMap<Integer,Number>> chrMapCHH= new TreeMap<String,TreeMap<Integer,Number>>();
	//following treemap will be generated in the while loop for each chr
	//TreeMap<Integer,Number> treeMap= new TreeMap<Integer, Number>();	
	
	public BismarkMethylationExtractorReader(String filename, int threshold) throws IOException{
		this.filename = filename;
		// construct a CGmap object from BSSeeker2 CGmap file
		BufferedReader br = new BufferedReader((new FileReader(filename)));	
		String line;
		while ((line=br.readLine())!=null){
			String fields[] = line.split("\t");
			String chr = fields[0];
			Integer pos = Integer.parseInt(fields[1]);
			String strand = fields[2];
			Double methylated = Double.parseDouble(fields[3]);
			Double unmethylated = Double.parseDouble(fields[4]);

			String type = fields[5];
			Double coverage = methylated + unmethylated;
			Double value = methylated / coverage;
			if (coverage < threshold) continue;
			if (type.equals("CG")){
				if (!chrMapCG.containsKey(chr)){
				  chrMapCG.put(chr, new TreeMap<Integer,Number>());	
				}
				TreeMap<Integer,Number> treeMap = chrMapCG.get(chr);
				treeMap.put(pos,value);
			}
			if (type.equals("CHG")){
				if (!chrMapCHG.containsKey(chr)){
				  chrMapCHG.put(chr, new TreeMap<Integer,Number>());	
				}
				TreeMap<Integer,Number> treeMap = chrMapCHG.get(chr);
				treeMap.put(pos,value);
			}
			if (type.equals("CHH")){
				if (!chrMapCHH.containsKey(chr)){
				  chrMapCHH.put(chr, new TreeMap<Integer,Number>());	
				}
				TreeMap<Integer,Number> treeMap = chrMapCHH.get(chr);
				treeMap.put(pos,value);
			}
		}
		br.close();
	}

	
	/**
	 * @param args
	 */
	public double mean(String type, String chr, Integer start, Integer end){
		TreeMap<String,TreeMap<Integer,Number>> chrMap = null;
		if (type.equals("CG")) chrMap = chrMapCG;
		else if (type.equals("CHG")) chrMap = chrMapCHG;
		else if (type.equals("CHH")) chrMap = chrMapCHH;
		if (chrMap.get(chr)==null) 
			return Double.NaN;
		SortedMap<Integer,Number> subset = chrMap.get(chr).subMap(start, end);
		double sum=0.0;
		int count=0;
		for (Number values : subset.values()) {
			sum+=values.doubleValue();
			count++;
		}
		return sum/count;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String bmerfilename = "example/test_data.fastq_bismark_bt2.CX_report.txt";
		Integer threshold = 4;
		
		BismarkMethylationExtractorReader bmer = null;
		try {	
		    bmer = new BismarkMethylationExtractorReader(bmerfilename,1);
		} catch (IOException e) {
			System.err.println("Error reading Bismark file "+bmerfilename);
			e.printStackTrace();
		}
		
	}

}