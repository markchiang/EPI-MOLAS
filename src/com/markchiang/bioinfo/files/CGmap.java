/**
 * 
 */
package com.markchiang.bioinfo.files;

import java.util.SortedMap;
import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author markchiang -Dosgi.requiredJavaVersion=1.8
 * @version 0.10
 */
public class CGmap {
	
	String filename = null;
	TreeMap<String,TreeMap<Integer,Number>> chrMapCG= new TreeMap<String,TreeMap<Integer,Number>>();
	TreeMap<String,TreeMap<Integer,Number>> chrMapCHG= new TreeMap<String,TreeMap<Integer,Number>>();
	TreeMap<String,TreeMap<Integer,Number>> chrMapCHH= new TreeMap<String,TreeMap<Integer,Number>>();
	//following treemap will be generated in the while loop for each chr
	//TreeMap<Integer,Number> treeMap= new TreeMap<Integer, Number>();	
	/**
	 * 
	 */

	public CGmap(String filename, int threshold) throws IOException{
		this.filename = filename;
		// construct a CGmap object from BSSeeker2 CGmap file
		BufferedReader br = new BufferedReader((new FileReader(filename)));	
		String line;
		while ((line=br.readLine())!=null){
			String fields[] = line.split("\t");
			String chr = UCSC2Ensembl(fields[0]);
			Integer pos = Integer.parseInt(fields[2]);
			String type = fields[3];
			Number value = Double.parseDouble(fields[5]);
			Integer coverage = Integer.parseInt(fields[7]);
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
	 * return the mean value of query region
	 * @param type CG/CHG/CHH
	 * @param chr
	 * @param start
	 * @param end
	 * @return
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

}