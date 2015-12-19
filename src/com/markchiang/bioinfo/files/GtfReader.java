/**
 * 
 */
package com.markchiang.bioinfo.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.markchiang.bioinfo.datatypes.Gene;

/**
 * @author markchiang -Dosgi.requiredJavaVersion=1.8
 * @version 0.10
 */
public class GtfReader {

	String filename = null;
	
	//TODO: improve performance using TreeMap. 
	List<Gene> genes = new ArrayList<Gene>();
	
	/**
	 * @return the genes
	 */
	public List<Gene> getGenes() {
		return genes;
	}


	/**
	 * @param filename
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public GtfReader(String filename) throws NumberFormatException, IOException {
		this.filename = filename;
		BufferedReader br = new BufferedReader((new FileReader(filename)));	
		String line;
		while ((line=br.readLine())!=null){
			if (line.startsWith("#")) continue;
			String fields[] = line.split("\t");
			String seqname = fields[0];
			String feature = fields[2];
			if (!feature.equals("gene")) continue;
			Integer start = Integer.parseInt(fields[3]);
			Integer end = Integer.parseInt(fields[4]);
			String strand = fields[6];
			String gene_id = null;
			String gene_name = null;
			String attributefield = fields[8];
			String attributes[] = attributefield.split("; ");
			for (String attributestr : attributes) {
				String attribute[] = attributestr.split(" ");
				String entry = attribute[0];
				String value = attribute[1];
				if (entry.equals("gene_id")){
					gene_id = value.substring(1,value.length()-1);
				}else if(entry.equals("gene_name")){
					gene_name = value.substring(1,value.length()-1);
				} 
			}
			genes.add(new Gene(gene_id, seqname, start, end, strand, gene_name));
		}
		br.close();
	}


	/**
	 * main() just provided as example and unit test
	 * @param args
	 */
	public static void main(String[] args) {
		String gtffilename = "example/head.gtf";
		try {
			GtfReader gtf = new GtfReader(gtffilename);
			for (Gene gene : gtf.getGenes()) {
				System.out.println(gene);
			}
		} catch (NumberFormatException e) {
			System.err.println("GTF format error, start end columns should be integer.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading GTF file "+gtffilename);
			e.printStackTrace();
		}
	}
}
