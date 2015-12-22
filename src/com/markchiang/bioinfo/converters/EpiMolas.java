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
 * @version 0.10
 */
public class EpiMolas {

	private static void unit_test(){
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
	
	private static void printUsage(){
		System.err.println("BS-Seeker usage: java -jar EpiMolas.jar input.CGmap input.gtf [1000 0 4]");
		System.err.println("Bismark   usage: java -jar EpiMolas.jar input.bismark_bt2.CX_report.txt input.gtf [1000 0 4]");
		System.err.println("Input format of .CGmap and .CX_report.txt will be determined by file extention.");
		System.err.println("upstream default value 1000");
		System.err.println("downstream default value 0");
		System.err.println("threshold default value 4");		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length<2){
			printUsage();
			System.exit(1);
		}
		String filename = args[0];
		String gtffilename = args[1];
		int upstream = 1000;
		int downstream = 0;
		Integer threshold = 4;

		if (args.length==5){	// optional arguments
			try{
				upstream = Integer.parseInt(args[2]);
				downstream = Integer.parseInt(args[3]);
				threshold = Integer.parseInt(args[4]);
			}catch (NumberFormatException nfe){
				System.err.println("Error: upsteam, downstream, threshold arguments should be integers");
				System.exit(1);
			}
		}

		GtfReader gtf = null;
		try {
			gtf = new GtfReader(gtffilename);
		} catch (NumberFormatException e) {
			System.err.println("GTF format error, start end columns should be integer.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading GTF file "+gtffilename);
			e.printStackTrace();
		}

		String cgmapfilename = null;
		String bmerfilename = null;
		if (filename.endsWith(".CGmap")){
			cgmapfilename = filename;
			CGmap cgmap = null;
			
			try {
				cgmap = new CGmap(cgmapfilename,threshold);
			} catch (IOException e) {
				System.err.println("Error reading CGmap file "+cgmapfilename);
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
		}else if (filename.endsWith(".CX_report.txt")){
			bmerfilename = filename;
			BismarkMethylationExtractorReader bmer = null;
			
			try {	
			    bmer = new BismarkMethylationExtractorReader(bmerfilename,threshold);
			} catch (IOException e) {
				System.err.println("Error reading Bismark file "+bmerfilename);
				e.printStackTrace();
			}
			
			if ((bmer==null)||(gtf==null)){
				System.err.println("We don't have enough information to continue...\nProgram stopped.");
				System.exit(1);
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
		}else{
			System.err.println("Error: File type unsupported. Currently we support *.CGmap and *.CX_report.txt files.");
		}
	}
}
