/**
 * 
 */
package com.markchiang.bioinfo.datatypes;

/**
 * @author markchiang -Dosgi.requiredJavaVersion=1.8
 *
 */
public class Gene{

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the chr
	 */
	public String getChr() {
		return chr;
	}

	/**
	 * @return the start
	 */
	public Integer getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public Integer getEnd() {
		return end;
	}

	/**
	 * @return the strand
	 */
	public String getStrand() {
		return strand;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	String id = null;
	String chr = null;
	Integer start = null;
	Integer end = null;
	String strand = null;
	String symbol = null;

	public Gene(String id, String chr, Integer start, Integer end, String strand, String symbol) {
		this.id = id;
		this.chr = chr;
		this.start = start;
		this.end = end;
		this.strand = strand;
		this.symbol = symbol;
	}
	
	@Override
	public String toString(){
//		return (String.format("%s\t%s\t%s\t%s",id,chr,start,end));	// for debug
		return (id);
	}
}
