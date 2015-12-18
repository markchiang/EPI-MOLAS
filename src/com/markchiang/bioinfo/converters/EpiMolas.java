/**
 * 
 */
package com.markchiang.bioinfo.converters;

import java.io.IOException;

import com.markchiang.bioinfo.datatypes.Gene;
import com.markchiang.bioinfo.files.BismarkMethylationExtractorReader;
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
		int upstream = 1000;
		int downstream = 0;
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
			System.out.println(String.format("%s\t%.6f\t%.6f\t%.6f\t%.6f\t%.6f\t%.6f", 
					gene.toString(),
					cgmap.mean("CG",gene.getChr(), gene.getPromoterStart(upstream,downstream), gene.getPromoterEnd(upstream,downstream)),
					cgmap.mean("CG",gene.getChr(), gene.getStart(), gene.getEnd()),
					cgmap.mean("CHG",gene.getChr(), gene.getPromoterStart(upstream,downstream), gene.getPromoterEnd(upstream,downstream)),
					cgmap.mean("CHG",gene.getChr(), gene.getStart(), gene.getEnd()),
					cgmap.mean("CHH",gene.getChr(), gene.getPromoterStart(upstream,downstream), gene.getPromoterEnd(upstream,downstream)),
					cgmap.mean("CHH",gene.getChr(), gene.getStart(), gene.getEnd())
					));
		}

		// test human data
		String bmerfilename = "example/sample.fastq_bismark_bt2.CX_report.txt";
		String humangtffilename = "example/sample.GRCh37.75.head.gtf";
		threshold = 0;
		
		BismarkMethylationExtractorReader bmer = null;
		try {	
		    bmer = new BismarkMethylationExtractorReader(bmerfilename,threshold);
		} catch (IOException e) {
			System.err.println("Error reading Bismark file "+bmerfilename);
			e.printStackTrace();
		}
		
		try {
			gtf = new GtfReader(humangtffilename);
		} catch (NumberFormatException e) {
			System.err.println("GTF format error, start end columns should be integer.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading GTF file "+gtffilename);
			e.printStackTrace();
		}

		for (Gene gene : gtf.getGenes()) {
			System.out.println(String.format("%s\t%.6f\t%.6f\t%.6f\t%.6f\t%.6f\t%.6f", 
					gene.toString(),
					bmer.mean("CG",gene.getChr(), gene.getPromoterStart(upstream,downstream), gene.getPromoterEnd(upstream,downstream)),
					bmer.mean("CG",gene.getChr(), gene.getStart(), gene.getEnd()),
					bmer.mean("CHG",gene.getChr(), gene.getPromoterStart(upstream,downstream), gene.getPromoterEnd(upstream,downstream)),
					bmer.mean("CHG",gene.getChr(), gene.getStart(), gene.getEnd()),
					bmer.mean("CHH",gene.getChr(), gene.getPromoterStart(upstream,downstream), gene.getPromoterEnd(upstream,downstream)),
					bmer.mean("CHH",gene.getChr(), gene.getStart(), gene.getEnd())
					));
		}
	}

}
