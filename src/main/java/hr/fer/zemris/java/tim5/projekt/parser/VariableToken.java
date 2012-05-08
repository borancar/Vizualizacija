package hr.fer.zemris.java.tim5.projekt.parser;

import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.nodes.VariableNode;

/**
 * Predstavlja token varijable
 */
public class VariableToken extends Token {
	/**
	 * Naziv varijable
	 */
	protected String variable;
	
	/**
	 * Stvara novi token varijable iz njenog naziva, varijabla smije biti ili samo 0 ili samo 1
	 * ili standardno imenovana varijabla koja može počinjati malim ili velikim slovom i
	 * sadržavati samo slova i brojke u svom nazivu
	 * @param variable naziv varijable
	 */
	public VariableToken(String variable) {		
		super(Type.VARIABLE);
		
		if(variable.equalsIgnoreCase("true")) variable = "1";
		if(variable.equalsIgnoreCase("false")) variable = "0";
		
		if(!variable.matches("[01]")) {
			if(!variable.matches("[a-zA-Z][a-zA-Z0-9]*")) {
				throw new IllegalArgumentException("Neispravno ime varijable: " + variable + "!");
			}
		}

		this.variable = variable;
	}
	
	/**
	 * Parsira token varijable iz tekstulanog zapisa
	 * @param text tekstualni zapis
	 * @return token varijable
	 */
	public static Token parseToken(String text) {
		return new VariableToken(text);
	}
	
	/**
	 * Vraća naziv varijable
	 * @return naziv varijable
	 */
	public String getVariable() {
		return variable;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof VariableToken)) {
			return false;
		}
		
		return super.equals(other) && ((VariableToken) other).variable.equals(this.variable);
	}
	
	@Override
	public String toString() {
		return variable;
	}
	
	@Override
	public ParserNode toProperObject() {
		return new VariableNode(variable);
	}
}