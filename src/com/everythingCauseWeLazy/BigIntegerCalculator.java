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
			
			if(carry == 1) {
				output = output + "1";
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

        //@author Dustin Bessmes
        /*
	 * The subtraction function, performs a subtraction of two numbers of any length of a specified radix.
	 * Outputs the result in the ParsedOutputData format for ease of use. 
	 * Throws an exception when a digit of a larger radix than the specified one is found.
	 * 
	 */
	public static ParsedOutputData subtract(ParsedInputData pd) throws Exception{
            
		String n1 = pd.getNumberOne();
		String n2 = pd.getNumberTwo();
                
                boolean poPo = false;
		boolean negPo = false;
                boolean poNeg = false;
		boolean negNeg = false;
                
                //If both integers are positive, continue with normal subtraction
                if((n1.indexOf("-") == -1 && n2.indexOf("-") == -1)) {
                    poPo = true;
                }
                // If n1 is negative and n2 is positive, remove the sign and treat
		// this subtraction as an addition of two positive numbers, adding the sign at the end.
                else if((n1.indexOf("-") != -1 && n2.indexOf("-") == -1)) {
                    negPo = true;
		}
                //if n1 is positive and n2 is negative, remove the sign and treat
                //this subtraction as an addition.
                else if((n1.indexOf("-") == -1 && n2.indexOf("-") != -1)) {
                    poNeg = true;
                }
		// If both numbers are negative, remove the signs and treat this as a subtraction of two positive numbers,
		// adding the sign at the end (and checking whether there are no two signs).
		else if(n1.indexOf("-") != -1 && n2.indexOf("-") != -1) {
                    negNeg = true;
		}
                
                if(negPo) {
			n1 = n1.replaceAll("-", "");
			
			ParsedInputData tPd = new ParsedInputData(pd.getRadix(), pd.getModulus(), n1, n2);
			
			System.out.println("negPo!!!");
	
                        StringBuilder tResult = new StringBuilder(BigIntegerCalculator.add(tPd).getAnswer());
                        tResult.insert(0, "-");
                        
                        String result = tResult.toString();
                        
                        ParsedOutputData d = new ParsedOutputData();
			
			d.setAnswer(result.replaceAll("--", ""));
                        
                        return d;
		}
                else if(poNeg) {
                    n2 = n2.replaceAll("-", "");
                    
                    ParsedInputData tPd = new ParsedInputData(pd.getRadix(), pd.getModulus(), n1, n2);
                    
                    System.out.println("poNeg!!!");
                    
                    return BigIntegerCalculator.add(tPd);
                }
                else {
                    
                    if(negNeg) {
                        n1 = n1.replaceAll("-", "");
                        n2 = n2.replaceAll("-", "");

                        System.out.println("NEGNEG!!!");
                    }
                    
                    // Reverse the input numbers, as we want to start at the least significant digit for primary school subtraction
                    String rn1 = new StringBuilder(n1).reverse().toString();
                    String rn2 = new StringBuilder(n2).reverse().toString();

                    System.out.println("De reversed input strings zijn: " + rn1 + " + " + rn2);

                    int carry = 0;

                    String output = "";

                    int radix = pd.getRadix();

                    int biggestLength = Math.max(n1.length(), n2.length());
                    int smallestLength = Math.min(n1.length(), n2.length());
                    
                    //Check whether n1 or n2 is bigger, if n2 is bigger swap the 2 by a recursive call, and add sign at the end of calculation.
                    boolean swap = false;
                    if(n1.length() < n2.length()) {
                        swap = true;
                    }
                    else if(n1.length() == n2.length())  {
                        for(int i =0; i < n1.length(); i++) {
                            int n1Char = n1.charAt(i) - '0';
                            int n2Char = n2.charAt(i) - '0';
                            if(n1Char < n2Char) {
                                //n1 is smaller so swap the numbess by recursive call, and add sign after calculation
                                swap = true;
                                break;
                            }
                        }
                    }
                    if(swap == true) {
                        System.out.println("swap!");
                        ParsedInputData sPd = new ParsedInputData(pd.getRadix(), pd.getModulus(), n2, n1);
                        StringBuilder stBuilder = new StringBuilder(BigIntegerCalculator.subtract(sPd).getAnswer());
                        stBuilder.insert(0, "-");
                        String answer = stBuilder.toString();
                        
                        //if both numbers were negative we need to re-add the negative sign
                        if(negNeg) {
                            stBuilder = new StringBuilder (answer);
                            stBuilder.insert(0, "-");
                            answer = stBuilder.toString();
                        }
                        
                        //remove double negative signs
                        answer = answer.replaceAll("--", "");
                        
                        ParsedOutputData d = new ParsedOutputData();
			d.setAnswer(checkZeros(answer));
                        return d;
                    }

                    //n1 is bigger than n2
                    // Loop over all the digits, performing primary school subtraction for each digit digit pair.
                    for(int i = 0; i < biggestLength; i++) {

                            int n1Char = 0;
                            int n2Char = 0;

                            // If one number has more digits than the other, treat all the digits that are absent from one
                            // number as zeroes in the subtraction.

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
                            // we convert these digits to their corresponding numeric value for the subtraction, afterwards they are converted back.
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
                            int tInt = n1Char - n2Char - carry;

                            System.out.println("Doe " + n1Char + " - " + n2Char + " - " + carry);
                            System.out.println("Krijg: " + tInt);
                            carry = 0;

                            // If the output digit is negative, add the radix and set the carry to one, as per the primary school subtraction
                            // algorithm.
                            if(tInt < 0) {
                                    carry = 1;
                                    tInt = tInt + radix;
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

                    d.setAnswer(checkZeros(properReversedOutput.replaceAll("--", "")));

                    System.out.println("De output van: " + n1 + " - " + n2 + " = " + properReversedOutput);


                    return d;
                }
	}
        
        //@author Dustin Bessems
        //Removes all heading 0s of a number (redundant).
        private static String checkZeros(String s) {
            for(int i = 0; i < s.length(); i++) {
                if(s.charAt(i) != '0' && s.charAt(i) != '-') {
                    break;
                }
                else if(s.charAt(i) == '0') {
                    s = s.replaceFirst("0", "");
                }
            }
            return s;
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
		
		System.out.println("In multiply!");
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
		for(int leadingZero = 0; leadingZero < loopLength; leadingZero++) {
			output="0"+output;
		}
		System.out.println("Aangekomen bij de main loop, de output is nu: " + output);
		for(int i = 0; i < rn1.length(); i++) {
			int carry = 0;
			for(int j = 0; j < rn2.length(); j++) {
				
				int prevOutputVal = (hexaToInt(output.charAt(i+j)) == 0 ? output.charAt(i+j) - '0' : hexaToInt(output.charAt(i+j)));
				int n1Val = (hexaToInt(rn1.charAt(i)) == 0 ? rn1.charAt(i) - '0' : hexaToInt(rn1.charAt(i)));
				int n2Val = (hexaToInt(rn2.charAt(j)) == 0 ? rn2.charAt(j) - '0' : hexaToInt(rn2.charAt(j)));
				
				
				int t = prevOutputVal + (n1Val * n2Val) + carry;
				System.out.println("De prevOutputVal is: " + prevOutputVal + ", n1 is: " + n1Val + " en n2 is: " + n2Val  + " en de carry is: " + carry + "Dus t is: " + t);
				carry = (int)Math.floor(t/radix);
				System.out.println("En de nieuwe carry is: " + carry);
				StringBuilder tempOutputBuilder = new StringBuilder(output);
				int tempResult = t - carry * radix;
				char cResult = ("" + tempResult).charAt(0);
				if(tempResult >= radix) {
					cResult = intToHexa(t - carry * radix);
				}
				
				tempOutputBuilder.setCharAt(i+j,  cResult);
				
				output = tempOutputBuilder.toString();
				
				System.out.println("Dus de nieuwe output waarde wordt: " + cResult);
			}
			
			StringBuilder tempOutputBuilder = new StringBuilder(output);
			char newOutputVal = ("" + carry).charAt(0);
		//	if(carry >= 10) {
		//		newOutputVal = intToHexa(carry);
		//	}
			
			tempOutputBuilder.setCharAt(i+rn2.length(),  newOutputVal);		
			output = tempOutputBuilder.toString();
			
		}
		
		System.out.println("De output van: " + n1 + " * " + n2 + " base " + radix + " = " + output);
		int counter = output.length() - 1;
		StringBuilder tempOutputBuilder = new StringBuilder(output);
		
			
		
		while(output.charAt(counter) == '0') {
			tempOutputBuilder.deleteCharAt(counter);
			counter = tempOutputBuilder.toString().length() - 1;
		}
		output = tempOutputBuilder.toString();
		System.out.println("De output van: " + n1 + " * " + n2 + " base " + radix + " = " + output);
		//Own attempt at making multiplication algorithm:
		/*for(int i = 0; i < rn1.length(); i++) {
			String rn1Multiplier=String.valueOf(rn1.charAt(i));
			if(rn1Multiplier=="a" || rn1Multiplier=="A") {
				rn1Multiplier="10";
			}
			if(rn1Multiplier=="b" || rn1Multiplier=="B") {
				rn1Multiplier="11";
			}
			if(rn1Multiplier=="c" || rn1Multiplier=="C") {
				rn1Multiplier="12";
			}
			if(rn1Multiplier=="d" || rn1Multiplier=="D") {
				rn1Multiplier="13";
			}
			if(rn1Multiplier=="e" || rn1Multiplier=="E") {
				rn1Multiplier="14";
			}
			if(rn1Multiplier=="f" || rn1Multiplier=="F") {
				rn1Multiplier="15";
			}
			int rn1MultiplierInt = Integer.parseInt(rn1Multiplier);
			if(rn1MultiplierInt==0) {
				output="0"+output;
			}
			else {
				for(int n = 0; n < rn2.length(); n++) {
					for(int k = 0; k < rn1MultiplierInt; k++) {
						//add stuff
						
					}
				}
			}
		} */
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


	public static ParsedOutputData karatsuba(ParsedInputData pd) throws Exception {
		//@author Guy Puts
		//Some code used from Maiko Bergman's addition method
		//This method uses the multiplication algorithm of Karatsuba, described in the course material
		String n1 = pd.getNumberOne();
		String n2 = pd.getNumberTwo();
		
		boolean poNeg = false;
		boolean negNeg = false;
		
		int radix = pd.getRadix();
		String output;
		
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
		
		//append front of string with zeroes
		int length=Math.max(n1.length(), n2.length());
		while(n1.length() < length) {
			n1="0"+n1;
		}
		while(n2.length() < length) {
			n2="0"+n1;
		}
		//if strings are both length 1, compute the result manually
		if(length == 1) {
			int result = n1.charAt(0)*n2.charAt(0);
			if(result < radix) {
				output = String.valueOf(result);
			}
			else {
				output = String.valueOf(result / radix)+String.valueOf(result % radix);
			}
		}
		else {
			//split strings as described in the Karatsuba algorithm
			int leftSplit = (length + 1) / 2;
			int rightSplit = length / 2;
			
			String n1Lo = n1.substring(0, leftSplit);
			String n1Hi = n1.substring(leftSplit, length);
			String n2Lo = n2.substring(0, leftSplit);
			String n2Hi = n2.substring(leftSplit, length);
			
			//do the three necessary multiplications
			ParsedInputData tPd = new ParsedInputData(pd.getRadix(), pd.getModulus(), n1Lo, n1Hi);
			String leftRes=BigIntegerCalculator.karatsuba(tPd).toString();
			String midResAdder1=BigIntegerCalculator.add(tPd).toString();
			tPd = new ParsedInputData(pd.getRadix(), pd.getModulus(), n2Lo, n2Hi);
			String midResAdder2=BigIntegerCalculator.add(tPd).toString();
			tPd = new ParsedInputData(pd.getRadix(), pd.getModulus(), midResAdder1, midResAdder2);
			String midRes=BigIntegerCalculator.karatsuba(tPd).toString();
			tPd = new ParsedInputData(pd.getRadix(), pd.getModulus(), n2Lo, n2Hi);
			String rightRes=BigIntegerCalculator.karatsuba(tPd).toString();
			
			//prepare subtraction necessary for Karatsuba algorithm
			String subLeftRes = leftRes;
			tPd = new ParsedInputData(pd.getRadix(), pd.getModulus(), leftRes, rightRes);
			String workingRes=BigIntegerCalculator.add(tPd).toString();
			tPd = new ParsedInputData(pd.getRadix(), pd.getModulus(), midRes, workingRes);
			String subMidRes=BigIntegerCalculator.subtract(tPd).toString();
			String subRightRes=rightRes;
			
			//compute last step of algorithm, as described in URL
			String leftProd = subLeftRes;
			for(int n = 0; n < 2 * rightSplit; n++) {
				leftProd = leftProd + "0";
			}
			String midProd = subMidRes;
			for(int n = 0; n < rightSplit; n++) {
				midProd = midProd + "0";
			}
			String rightProd = subRightRes;
			
			//first add of last step
			tPd = new ParsedInputData(pd.getRadix(), pd.getModulus(), leftProd, midProd);
			String firstAdd=BigIntegerCalculator.add(tPd).toString();
			
			//second add of last step
			tPd = new ParsedInputData(pd.getRadix(), pd.getModulus(), firstAdd, rightProd);
			String secondAdd=BigIntegerCalculator.add(tPd).toString();
			output = secondAdd;
		}
		ParsedOutputData d = new ParsedOutputData();
		d.setAnswer(output);
		//increment counter three times, for Karatsuba requires three multiplications
		BigIntegerCalculator.MUL_COUNT++;
		BigIntegerCalculator.MUL_COUNT++;
		BigIntegerCalculator.MUL_COUNT++;
		
		return d;
	}


	public static ParsedOutputData reduce(ParsedInputData pd) throws Exception{
		
            String n1 = pd.getNumberOne();
            String mod = pd.getModulus();
            int radix = pd.getRadix();
            
            String n1Abs = n1.replaceAll("-", "");
            int k = String.valueOf(hexaToTen(n1, radix)).length();
            int n = String.valueOf(hexaToTen("" + mod, radix)).length();
            int n1Num = hexaToTen(n1Abs, radix);
            int modNum = hexaToTen(mod, radix);
            
            for (int i = k - n; i >= 0; i--) {
                while(n1Num >= modNum*Math.pow(10, i)) {
                    n1Num -= modNum*Math.pow(10, i);
                }
            }
            System.out.println(n1Num);
            
            if(n1.indexOf("-") != -1) {
                n1Num = modNum - n1Num;
            }
            ParsedOutputData d = new ParsedOutputData();
            d.setAnswer(tenToHexa(n1Num));
            return d;
	}
        
        //Function converts input of base radix to base 10 for 
        //@author Dustin Bessems
        private static int hexaToTen(String s, int radix) {
            //Reverse the string, then multiply digit i by radix^i to get base 10 number
            String sR = new StringBuilder(s).reverse().toString();
            int b10 = 0;
            for(int i = 0; i < s.length(); i++) {
                int sChar = sR.charAt(i) - '0';
                if(sChar >= 10) {
                    sChar = hexaToInt(sR.charAt(i));
                }
                b10 += sChar * Math.pow(radix,i);
            }
            System.out.println(s + " becomes " + b10);
            return b10;
        }
        
        //Fucntion that converts base 10 number to a hexadecimal string
        //@author Dustin Bessems
        private static String tenToHexa(int ten) {
            String s ="";
            while(ten != 0) {
                int rem = ten - (int) Math.floor(ten / 16) * 16;
                ten = (int) Math.floor(ten / 16);
                if(rem >= 10) {
                    rem = intToHexa(rem);
                }
                s += rem;
            }
            if(s == "") {
                s = "0";
            }
            //Reverse s to get the right hexa number
            return s;
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
