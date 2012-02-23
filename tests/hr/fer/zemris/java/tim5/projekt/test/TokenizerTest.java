package hr.fer.zemris.java.tim5.projekt.test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import hr.fer.zemris.java.tim5.projekt.parser.OperatorToken;
import hr.fer.zemris.java.tim5.projekt.parser.Token;
import hr.fer.zemris.java.tim5.projekt.parser.Tokenizer;
import hr.fer.zemris.java.tim5.projekt.parser.VariableToken;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test tokenizatora.
 */
public class TokenizerTest {
	
	/**
	 * Broj testova tokenizatora.
	 */
	private static final int NUMBER_OF_TESTS = 10;

	/**
	 * Stvori se niz tipova tokena, zatim se ti tokeni prebace u string.
	 * String se tokenizira i onda se uspoređuju ta 2 niza tokena.
	 */
	@Test
	public void tokenize() {
		for(int i = 0; i < NUMBER_OF_TESTS; i++) {
			StringBuilder sb = new StringBuilder();
			List<Token.Type> tokens = new ArrayList<Token.Type>();
			
			tokens.add(Token.Type.VARIABLE);
			tokens.add(Token.Type.OPERATOR);
			tokens.add(Token.Type.BLOCK_BEGIN);
			tokens.add(Token.Type.VARIABLE);
			tokens.add(Token.Type.OPERATOR);
			tokens.add(Token.Type.VARIABLE);
			tokens.add(Token.Type.BLOCK_END);
			
			Collections.shuffle(tokens);
			
			tokens.add(Token.Type.END);
			
			for(Token.Type token : tokens) {
				if(token == Token.Type.OPERATOR) {
					sb.append(new OperatorToken(OperatorToken.Operator.AND) + " ");
				} else if(token == Token.Type.VARIABLE) {
					sb.append(new VariableToken("adbaASanadSAD980213") + " ");
				} else {
					sb.append(token.toString() + " ");
				}
			}
			
			List<Token.Type> tokenized = new ArrayList<Token.Type>();
			
			System.out.println(sb.toString());
			
			Tokenizer tokenizer = new Tokenizer(new StringReader(sb.toString()));
			Token token;
			
			do {
				token = tokenizer.getToken();
				tokenized.add(token.getType());
			} while(token.getType() != Token.Type.END);
		
			Assert.assertEquals(tokens, tokenized);
		}
	}

	/**
	 * Metoda potrebna za pozivanje testa iz ANTa
	 * @return test
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TokenizerTest.class);
    }
}
