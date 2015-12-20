/**
 * 
 */
package com.markchiang.bioinfo.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author markchiang
 * @version 0.10
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
			String chr = UCSC2Ensembl(fields[0]);
			Integer pos = Integer.parseInt(fields[1]);
			String strand = fields[2];
			int methylated = Integer.parseInt(fields[3]);
			int unmethylated = Integer.parseInt(fields[4]);

			String type = fields[5];
			Double coverage = (double)methylated + (double)unmethylated;
			Double value = methylated / coverage;
			if (coverage <= threshold) continue;
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
	 * Converting human chromosome number from UCSC style to Ensembl style
	 * @param chr
	 * @return
	 */
	private String UCSC2Ensembl(String chr){
		if (chr.startsWith("chr")||chr.startsWith("Chr")){
			String answer = chr.substring(3,chr.length());
			answer.replace("M", "Mt");
			return answer;
		}else{
			return chr;
		}
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
		SortedMap<Integer,Number> subset = chrMap.get(chr).subMap(start, end+1); //+1 is because toKey in subMap() is exclusive
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
		String bmerfilename = "example/head_data.fastq_bismark_bt2.CX_report.txt";
		Integer threshold = 0;
		
		BismarkMethylationExtractorReader bmer = null;
		try {	
		    bmer = new BismarkMethylationExtractorReader(bmerfilename,threshold);
		} catch (IOException e) {
			System.err.println("Error reading Bismark file "+bmerfilename);
			e.printStackTrace();
		}
		
	}

}
