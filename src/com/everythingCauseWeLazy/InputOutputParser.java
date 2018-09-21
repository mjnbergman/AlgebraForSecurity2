


package com.everythingCauseWeLazy;

import java.io.*;
import java.nio.file.Files;
import java.util.regex.Pattern;

/**
 * 
 * @author Maiko Bergman
 * 
 * A class that parses input and output. It can read a file in the specified format and pass the parsed data
 * to further classes. It also acts as an endpoint for the programme, after the calculations have finished
 * the output data is outputted by this class in the required format to an output file called 'output.txt'.
 *
 */

public class InputOutputParser {

	public InputOutputParser() {
		
	}
	
	// This function just reads a file with the specified name
	private BufferedReader openFileFromString(String file) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		return reader;
	}
	
	/*
	 * This function does the actual parsing of the input file
	 */
	public void parseFile(String file) {
		try {
			BufferedReader bf = openFileFromString(file);
			
			String line = null;
			
			int radix = -1;
			String modulus = "";
			String numberOne = "";
			String numberTwo = "";
			String operation = "";
			
			// Regex to match the required functions
			String operatorMatchPattern = "\\[(add|subtract|multiply|karatsuba|euclid|inverse|reduce)\\]";
			
		    while ((line = bf.readLine()) != null) {
     
		        // If this is the line containing the radix, the same logic is applied to further if statements
		        if(line.indexOf("[radix] ") != -1) {
		        	
		        	// Extract the radius from the string
		        	radix = Integer.parseInt(line.substring(line.indexOf("[radix] ") + "[radix] ".length()));
		        }
		        else if(line.indexOf("[x] ") != -1) {
		        	numberOne = line.substring(line.indexOf("[x] ") + "[x] ".length());
		        }
		        else if(line.indexOf("[y] ") != -1) {
		        	numberTwo = line.substring(line.indexOf("[y] ") + "[y] ".length());
		        }
		        else if(line.indexOf("[m] ") != -1) {
		        	modulus = line.substring(line.indexOf("[m] ") + "[m] ".length());
		        }
		        // If this line matches the regex, AKA it's the line specifying the function
		        else if(line.matches(operatorMatchPattern)) {
		        	System.out.println("Deze lijn is een operator!!!");
		        	operation = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
		        }
		        
		    }
		    
			System.out.println("De radix is: " + radix);
			System.out.println("Het eerste getal is: " + numberOne);
			System.out.println("Het tweede getal is: " + numberTwo);
			System.out.println("De modulus is: " + modulus);
			System.out.println("De operatie is: " + operation);
			
			// Compile the found data into a ParsedInputData object for ease of use.
			ParsedInputData pd = new ParsedInputData(radix, modulus, numberOne, numberTwo);
			
			boolean error = false;
			boolean poNeg = false;
			boolean negNeg = false;
			

			
			
			// Pre-define a ParsedOutputDataObject for further use
			ParsedOutputData pod = new ParsedOutputData();
			
			// Switch over the operation, calling the appropriate static function in the BigIntegerCalculator class
			// Also catches any exceptions caused by for instance a digit in one of the input number being
			// bigger than the radix.
			
			try {
				switch(operation) {
				case "add":
							pod = BigIntegerCalculator.add(pd);	
					break;
				case "subtract":
							pod = BigIntegerCalculator.subtract(pd);
					break;
				case "multiply":
							pod = BigIntegerCalculator.multiply(pd);
					break;
				case "karatsuba":
							pod = BigIntegerCalculator.karatsuba(pd);
					break;
				case "reduce":
							pod = BigIntegerCalculator.reduce(pd);
					break;
				case "inverse":
							pod = BigIntegerCalculator.inverse(pd);
					break;
				case "euclid":
							pod = BigIntegerCalculator.euclid(pd);
					break;
				}
			}catch(Exception ex) {
				System.out.println("There's a number bigger than the specified radix! Could not perform the desired calculation!");
				error = true;
			}	
			
			this.outputData(pod, error);
			
				
		} catch(IOException ex) {
			System.out.println("Could not open or read file!\nCheck if the filename is correct!");
		}


		
	}
	/*
	 * An output data function, outputs the data in the specified format
	 * TODO: ADD the ans-a, ans-b and ans-d output lines.
	 */
	public void outputData(ParsedOutputData pod, boolean error) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {

			String ansString = "[answer] " + (error ? "ERROR" : pod.getAnswer());
			String countAddString = "[count-add] " + BigIntegerCalculator.ADD_COUNT;
			String countMulString = "[count-mul] " + BigIntegerCalculator.MUL_COUNT;

			bw.write(ansString);
			bw.newLine();
			bw.write(countAddString);
			bw.newLine();
			bw.write(countMulString);
			
			// no need to close it.
			//bw.close();

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		}
	}
}
