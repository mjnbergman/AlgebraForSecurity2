package com.everythingCauseWeLazy;

/**
 * 
 * @author Maiko Bergman
 *
 *	A container class for the parsed input data, straightforward and for ease of use.
 *
 */
public class ParsedInputData {

	private int radix;
	private String modulus;
	private String numberOne;
	private String numberTwo;
	
	public ParsedInputData() {
		numberOne = "";
		numberTwo = "";
	}
	
	public ParsedInputData(int radix, String modulus, String n1, String n2) {
		this.radix = radix;
		this.modulus = modulus;
		this.numberOne = n1;
		this.numberTwo = n2;
	}
	
	public String getNumberOne() {
		return this.numberOne;
	}
	public String getNumberTwo() {
		return this.numberTwo;
	}
	public int getRadix() {
		return this.radix;
	}
	public String getModulus() {
		return this.modulus;
	}
}
