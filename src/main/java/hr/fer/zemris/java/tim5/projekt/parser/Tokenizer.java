package hr.fer.zemris.java.tim5.projekt.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

/**
 * Tokenizer
 */
public class Tokenizer {
	
	/**
	 * Ulaz koji se parsira za riječi
	 */
	private Reader input = null;
	
	/**
	 * Set terminalnih znakova nakon kojih ne treba čitati riječ dalje
	 */
	private Set<Character> terminals = new HashSet<Character>();
	
	/**
	 * Zadnji učitani znak, postavljen inicijalno na prazninu
	 */
	int lastChar = ' ';
	
	/**
	 * Pozicija unutar readera (na kojoj se lokaciji string reader nalazi).
	 */
	int position;
	
	/**
	 * Javni konstruktor tokenzirea, prima čitač ulaznih podataka.
	 * @param input čitač ulaznih podataka
	 */
	public Tokenizer(Reader input) {
		this.input = input;
		
		position = 0;
		
		terminals.add('(');
		terminals.add(')');
		terminals.add(',');
		terminals.add(' ');
		terminals.add('\t');
		terminals.add('\r');
		terminals.add('\n');
	}
	
	/**
	 * Vraća string tokena
	 * @return token
	 */
	public Token getToken() {		
		StringBuilder keywords = new StringBuilder();
		
		try {
			while(lastChar == ' ' || lastChar == '\t' || lastChar == '\n' || lastChar == '\r') {
				lastChar = input.read();
				position++;
			}

			if(lastChar == -1) return new Token(Token.Type.END);
			
			if(terminals.contains((char) lastChar)) {
				keywords.append((char) lastChar);
				
				Token newToken = Token.parseToken(keywords.toString());
				
				lastChar = ' ';
			
				return newToken;
			}
			
			while(!terminals.contains((char) lastChar)) {
				keywords.append((char) lastChar);
				lastChar = input.read();
				position++;
				if(lastChar == -1) break;
			}
			
			String keyword = keywords.toString();
			
			return Token.parseToken(keyword);
		} catch (IOException e) {
			return new Token(Token.Type.FORCED_END);
		}
	}
	
	/**
	 * Vraća trenutnu poziciju na kojoj se čitač nalazi.
	 * @return pozicija čitača
	 */
	public int getPosition() {
		return position;
	}
}