package hr.fer.zemris.java.tim5.projekt.parser;

import hr.fer.zemris.java.tim5.projekt.ParserNode;

/**
 * Klasa predstavlja općeniti token
 */
public class Token {
	
	/**
	 * Enumeracija tipova tokena
	 */
	public enum Type {
		OPERATOR("*"), VARIABLE("*"), BLOCK_BEGIN("("), BLOCK_END(")"),
		WHITESPACE_SEPARATOR(" "), COMMA_SEPARATOR(","), END("\n"), FORCED_END("");
		
		/**
		 * String reprezentacija tokena
		 */
		private final String value;
		
		/**
		 * Privatni konstruktor za inicijaliziranje tokena enumeracije
		 * @param value string reprezentacija tipa tokena
		 */
		private Type(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	/**
	 * O kojem se općenitom tokenu radi
	 */
	protected Type type;
	
	/**
	 * Konstruktor stvara novi općeniti token na osnovu tipa
	 * @param type tip općenitog tokena
	 */
	public Token(Type type) {
		this.type = type;
	}
	
	/**
	 * Parsira općeniti token iz tekstualnog zapisa
	 * @param text tekstualni zapis
	 * @return općeniti token
	 */
	public static Token parseToken(String text) {
		char ch = text.charAt(0);
		
		switch(ch) {
		case '(':
			return new Token(Type.BLOCK_BEGIN);
			
		case ')':
			return new Token(Type.BLOCK_END);
			
		case ' ':
			return new Token(Type.WHITESPACE_SEPARATOR);
			
		case ',':
			return new Token(Type.COMMA_SEPARATOR);
		
		case '\n':
			return new Token(Type.END);
		
		default:
			try {
				return OperatorToken.parseToken(text);
			} catch(IllegalArgumentException e) {
				return VariableToken.parseToken(text);
			}
		}			
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Token) {
			return ((Token) other).type == this.getType();
		} else {
			return false;
		}
	}
	
	/**
	 * Vraća općeniti tip tokena
	 * @return tip tokena
	 */
	public Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return type.toString();
	}
	
	/**
	 * Vraća prikladan objekt za jezično stablo stvoren od tokena
	 * @return objekt stvoren iz tokena
	 */
	public ParserNode toProperObject() {
		return null;
	}
}
