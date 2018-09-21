package com.everythingCauseWeLazy;


/**
 * 
 * @author Maiko Bergman, Guy Puts, TODO: flikker hier je naam bij.
 *
 *
 *	The main class for calculations, contains static methods for performing the various calculation functions.
 *	Every method uses the ParsedInputData and ParsedOutputData classes for standardization reasons.
 *
 */
public class BigIntegerCalculator {

	
	// The addition and multiplication counts, which are incremented by their respective methods.
	public static int ADD_COUNT = 0;
	public static int MUL_COUNT = 0;
	
	public BigIntegerCalculator() {
		
	}
	
	/*
	 * The addition function, performs an addition of two numbers of any length of a specified radix.
	 * Outputs the result in the ParsedOutputData format for ease of use. 
	 * Throws an exception of a digit of a larger radix than the specified one is found.
	 * 
	 */
	public static ParsedOutputData add(ParsedInputData pd) throws Exception{
		
		String n1 = pd.getNumberOne();
		String n2 = pd.getNumberTwo();
		
		boolean poNeg = false;
		boolean negNeg = false;
		
		// If one number is negative and the other is positive, remove the signs and treat
		// this addition as a subtraction of two positive numbers.
		if((n1.indexOf("-") != -1 && n2.indexOf("-") == -1) || (n1.indexOf("-") == -1 && n2.indexOf("-") != -1)) {
			poNeg = true;
		}
		// If both numbers are negative, remove the signs and treat this as an addition of two positive numbers,
		// adding the sign at the end.
		else if(n1.indexOf("-") != -1 && n2.indexOf("-") != -1) {
			negNeg = true;
		}
		
		if(poNeg) {
			n1 = n1.replaceAll("-", "");
			n2 = n2.replaceAll("-", "");
			
			ParsedInputData tPd = new ParsedInputData(pd.getRadix(), pd.getModulus(), n1, n2);
			
			System.out.println("PoNEG!!!");
			
			return BigIntegerCalculator.subtract(tPd);
		}
		else {
		
			if(negNeg) {
				n1 = n1.replaceAll("-", "");
				n2 = n2.replaceAll("-", "");
				
				System.out.println("NEGNEG!!!");
			}
			
			// Reverse the input numbers, as we want to start at the least significant digit for primary school addition
			String rn1 = new StringBuilder(n1).reverse().toString();
			String rn2 = new StringBuilder(n2).reverse().toString();
			
			System.out.println("De reversed input strings zijn: " + rn1 + " + " + rn2);
			
			int carry = 0;
			
			String output = "";
			
			int radix = pd.getRadix();
			
			int biggestLength = Math.max(n1.length(), n2.length());
			int smallestLength = Math.min(n1.length(), n2.length());
			
			// Loop over all the digits, performing primary school addition for each digit digit pair.
			for(int i = 0; i < biggestLength; i++) {
				
				int n1Char = 0;
				int n2Char = 0;
				
				// If one number has more digits than the other, treat all the digits that are absent from one
				// number as zeroes in the addition.
				
				if(i >= smallestLength && smallestLength != biggestLength) {
					if(n1.length() == smallestLength) {
						// Convert the char to an int
						n2Char = rn2.charAt(i) - '0';
					}
					else {
						n1Char = rn1.charAt(i) - '0';
					}
				}
				else {
					n1Char = rn1.charAt(i) - '0';
					n2Char = rn2.charAt(i) - '0';
				}
				
				// If the value of the digits are ten or greater (AKA they are represented as A through F in our number system)
				// we convert these digits to their corresponding numeric value for the addition, afterwards they are converted back.
				if(n1Char >= 10) {
					n1Char = hexaToInt(rn1.charAt(i));
				}
				if(n2Char >= 10) {
					n2Char = hexaToInt(rn2.charAt(i));
				}
				
				// If one of the digits is bigger than the specified radix, throw an exception.
				if(n1Char >= radix || n2Char >= radix) {
					throw new Exception();
				}
				
				// Add the digits, taking into account the carry digit
				int tInt = n1Char + n2Char + carry;
				
				System.out.println("Doe " + n1Char + " + " + n2Char + " + " + carry);
				System.out.println("Krijg: " + tInt);
				carry = 0;
				
				// If the output digit is bigger than the radix, subtract the radix and set the carry to one, as per the primary school addition
				// algorithm.
				if(tInt >= radix) {
					carry = 1;
					tInt = tInt - radix;
				}
				
				
				// Convert any digits whose value is bigger than 10 back to A through F, such that they are only one actual digit again.
				if(tInt >= 10) {
					output += intToHexa(tInt);
				}
				else {
					output += tInt;
				}
			}
			
			// Reverse the output string again, because it's calculated in reverse.
			String properReversedOutput = new StringBuilder(output).reverse().toString();
			
			
			
			// If both input numbers were negative, add the negative sign again as the result is negative.
			if(negNeg) {
				StringBuilder stBuilder = new StringBuilder(properReversedOutput);
				stBuilder.insert(0, "-");
				properReversedOutput = stBuilder.toString();
			}
			
			// Convert the result data into a ParsedOutputData object for ease of use.
			
			ParsedOutputData d = new ParsedOutputData();
			
			d.setAnswer(properReversedOutput);
			
			System.out.println("De output van: " + n1 + " + " + n2 + " = " + properReversedOutput);
			
			// Increment the addition counter as one full addition has finished.
			BigIntegerCalculator.ADD_COUNT++;
			
			return d;
		}
	}
	
	// A function that converts a character from A through F, not case sensitive, to its corresponding numeric value
	// and returns this.
	// Required for addition of base larger than 10.
	private static int hexaToInt(char c) {
		
		if((int)("" + c).toUpperCase().charAt(0) >= 65 && (int)("" + c).toUpperCase().charAt(0) <= 70) {
			return (10 + ((int) c - 65));
		}
		
		return 0;
	}
	
	// A function that converts an integer from 10 through 15 to its corresponding hexadecimal character, always in uppercase.
	// Required for addition of base larger than 10.
	private static char intToHexa(int i) {
		return (char) (65 + (i - 10));
	}


	public static ParsedOutputData subtract(ParsedInputData pd) {
		
		// TODO Auto-generated method stub
		return null;
	}


	public static ParsedOutputData multiply(ParsedInputData pd) {
		/**
		 * 
		 * @author Guy Puts, with code parts used from Maiko Bergman.
		 *	Multiplication algorithm was based on the multiplication algorithm found in the course material
		 *	Some code parts from Maiko's code were used.
		 *	The class for multiplying integers in certain radixes.
		 *
		 */
		String n1 = pd.getNumberOne();
		String n2 = pd.getNumberTwo();
		
		boolean poNeg = false;
		boolean negNeg = false;
		
		// If one number is negative and the other is positive, remove the signs and treat
		// this addition as a subtraction of two positive numbers.
		if((n1.indexOf("-") != -1 && n2.indexOf("-") == -1) || (n1.indexOf("-") == -1 && n2.indexOf("-") != -1)) {
			poNeg = true;
		}
		// If both numbers are negative, remove the signs and treat this as an addition of two positive numbers,
		// adding the sign at the end.
		else if(n1.indexOf("-") != -1 && n2.indexOf("-") != -1) {
			negNeg = true;
		}
		
		if(poNeg) {
			n1 = n1.replaceAll("-", "");
			n2 = n2.replaceAll("-", "");
			
			System.out.println("PONEG!!!");
		}
		else {
		
			if(negNeg) {
				n1 = n1.replaceAll("-", "");
				n2 = n2.replaceAll("-", "");
				
				System.out.println("NEGNEG!!!");
			}
		}
		
		// Reverse the input numbers, as we want to start at the least significant digit for primary school addition
		String rn1 = new StringBuilder(n1).reverse().toString();
		String rn2 = new StringBuilder(n2).reverse().toString();
		System.out.println("De reversed input strings zijn: " + rn1 + " + " + rn2);
		int radix = pd.getRadix();
		
		String output = "";
		int loopLength = rn1.length() + rn2.length();
		for(int leadingZero = rn2.length(); leadingZero < loopLength - 1; leadingZero++) {
			output="0"+output;
		}
		for(int i = 0; i < loopLength; i++) {
			int carry = 0;
			for(int j = 0; j < rn2.length(); j++) {
				int t = output.charAt(i+j)+(rn1.charAt(i)*rn2.charAt(j))+carry;
				carry = (int)Math.floor(t/radix);
				output.replace(Integer.toString(i+j),  Integer.toString(t - carry*radix));
			}
			output.replace(Integer.toString(i+rn2.length()),  Integer.toString(carry));
		}
		while(output.charAt(loopLength - 1) == 0) {
			output = output.substring(0, loopLength - 2);
		}
		
		String properReversedOutput = new StringBuilder(output).reverse().toString();
		
		if(poNeg) {
			StringBuilder stBuilder = new StringBuilder(properReversedOutput);
			stBuilder.insert(0, "-");
			properReversedOutput = stBuilder.toString();
		}
		
		ParsedOutputData d = new ParsedOutputData();
		
		d.setAnswer(properReversedOutput);
		
		System.out.println("De output van: " + n1 + " * " + n2 + " = " + properReversedOutput);
		
		BigIntegerCalculator.MUL_COUNT++;
		
		return d;
	}


	public static ParsedOutputData karatsuba(ParsedInputData pd) {
		//@author Guy Puts
		String n1 = pd.getNumberOne();
		String n2 = pd.getNumberTwo();
		
		boolean poNeg = false;
		boolean negNeg = false;
		
		// If one number is negative and the other is positive, remove the signs and treat
		// this addition as a subtraction of two positive numbers.
		if((n1.indexOf("-") != -1 && n2.indexOf("-") == -1) || (n1.indexOf("-") == -1 && n2.indexOf("-") != -1)) {
			poNeg = true;
		}
		// If both numbers are negative, remove the signs and treat this as an addition of two positive numbers,
		// adding the sign at the end.
		else if(n1.indexOf("-") != -1 && n2.indexOf("-") != -1) {
			negNeg = true;
		}
		
		if(poNeg) {
			n1 = n1.replaceAll("-", "");
			n2 = n2.replaceAll("-", "");
			
			System.out.println("PONEG!!!");
		}
		else {
		
			if(negNeg) {
				n1 = n1.replaceAll("-", "");
				n2 = n2.replaceAll("-", "");
				
				System.out.println("NEGNEG!!!");
			}
		}
		/*public static String Karatsuba(n1, n2) {
			return null;
		}
		*/
		return null;
	}


	public static ParsedOutputData reduce(ParsedInputData pd) {
		// TODO Auto-generated method stub
		return null;
	}


	public static ParsedOutputData inverse(ParsedInputData pd) {
		// TODO Auto-generated method stub
		return null;
	}


	public static ParsedOutputData euclid(ParsedInputData pd) {
		// TODO Auto-generated method stub
		return null;
	}
}
