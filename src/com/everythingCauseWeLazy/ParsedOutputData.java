package com.everythingCauseWeLazy;

/**
 * 
 * @author Maiko Bergman
 * 
 * 	A container class for the parsed output data, straightforward and for ease of use.
 *
 */
public class ParsedOutputData {
	
	private String answer;
	private String answa;
	private String answb;
	private String answd;
	
	public ParsedOutputData() {
		
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public void setAnswerA(String answer) {
		this.answa = answer;
	}
	public void setAnswerB(String answer) {
		this.answb = answer;
	}
	public void setAnswerD(String answer) {
		this.answd = answer;
	}
	
	public String getAnswer() {
		return this.answer;
	}
	

}
