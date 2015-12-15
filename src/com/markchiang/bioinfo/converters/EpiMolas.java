/**
 * 
 */
package com.markchiang.bioinfo.converters;

import java.io.IOException;

import com.markchiang.bioinfo.datatypes.Gene;
import com.markchiang.bioinfo.files.CGmap;
import com.markchiang.bioinfo.files.GtfReader;

/**
 * @author markchiang -Dosgi.requiredJavaVersion=1.8
 *
 */
public class EpiMolas {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String cgmapfilename = "example/head.CGmap";
		String gtffilename = "example/head.gtf";
		Integer threshold = 4;
		CGmap cgmap = null;
		GtfReader gtf = null;
		try {
			cgmap = new CGmap(cgmapfilename,threshold);
		} catch (IOException e) {
			System.err.println("Error reading CGmap file "+cgmapfilename);
			e.printStackTrace();
		}

		try {
			gtf = new GtfReader(gtffilename);
		} catch (NumberFormatException e) {
			System.err.println("GTF format error, start end columns should be integer.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading GTF file "+gtffilename);
			e.printStackTrace();
		}

		if ((cgmap==null)||(gtf==null)){
			System.err.println("We don't have enough information to continue...\nProgram stopped.");
			System.exit(1);
		}
		
		for (Gene gene : gtf.getGenes()) {
			System.out.println(String.format("%s\t%.6f\t%.6f\t%.6f", 
					gene.toString(),
					cgmap.mean("CG",gene.getChr(), gene.getStart(), gene.getEnd()),
					cgmap.mean("CHG",gene.getChr(), gene.getStart(), gene.getEnd()),
					cgmap.mean("CHH",gene.getChr(), gene.getStart(), gene.getEnd())
					));
		}
	}

}
