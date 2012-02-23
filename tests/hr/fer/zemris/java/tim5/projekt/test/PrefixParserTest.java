package hr.fer.zemris.java.tim5.projekt.test;


import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.text.ParseException;

import junit.framework.JUnit4TestAdapter;
import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.nodes.AndNode;
import hr.fer.zemris.java.tim5.projekt.nodes.OrNode;
import hr.fer.zemris.java.tim5.projekt.nodes.VariableNode;
import hr.fer.zemris.java.tim5.projekt.parser.PrefixParser;
import hr.fer.zemris.java.tim5.projekt.parser.Tokenizer;

import org.junit.Test;

/**
 * JUnit test koji testira prefiksni parser. 
 */
public class PrefixParserTest {
	private static final int NUMBERS_OF_TESTS = 10;
	
	/*
	 * Metoda testira parse metodu parsera. Radi na temelju testnog generatora stabla, koje uspoređuje sa
	 * stablom dobivenim slanjem istog tog stablo u obliku stringa 
	 * infiksnom parseru, tj. njegovoj parse() metodi.
	 */
	@Test
	public void parse_GeneratorNode1()throws ParseException{
		for(int i = 0; i < NUMBERS_OF_TESTS; i++) {		
			TestTreeGenerator generator = new TestTreeGenerator();
			
			String preorder = generator.printPreorder();
						
			ParserNode testNode = PrefixParser.parse(
					new Tokenizer(new StringReader(
							preorder
							)
					));
			
			assertEquals(testNode,generator.getTree());
		}	
	}
	
	/*
	 * U sljedećim metodama se direktno stvoreno stablo uspoređuje sa 
	 * stablom dobivenim parserom
	 */
	@Test
	public void parse_CustomNode1()throws ParseException{
						
			AndNode compareNode = new AndNode();
			compareNode.addChild(new VariableNode("a"));
			compareNode.addChild(new VariableNode("b"));
			
			ParserNode parsedNode = PrefixParser.parse(
					new Tokenizer(new StringReader(
							"and (a,b)"
							)
					));
			
			assertEquals(compareNode,parsedNode);
		
	}
	
	@Test
	public void parse_CustomNode2()throws ParseException{
						
		OrNode compareNode = new OrNode();
		AndNode and = new AndNode();
		and.addChild(new VariableNode("a"));
		and.addChild(new VariableNode("b"));
		compareNode.addChild(and);
		compareNode.addChild(new VariableNode("c"));
			
			ParserNode parsedNode = PrefixParser.parse(
					new Tokenizer(new StringReader(
							"or(and(a,b),c)"
							)
					));
			
			assertEquals(compareNode,parsedNode);
		
	}
	
	
	/*
	 * Sljdeće Metode provjeravaju baca li parser ispravno exceptione 
	 * prilikom nevaljano zadanog stringa 
	 */
	@Test (expected = ParseException.class)
	public void parse_Exceptions1() throws ParseException{
		
		@SuppressWarnings("unused")
		ParserNode testNode = PrefixParser.parse(
				new Tokenizer(new StringReader("AND(NOT(")
				));
		
	}
	
	@Test
	public void parse_Exceptions2() {
		String errorMessage = null;
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = PrefixParser.parse(
					new Tokenizer(new StringReader("A AND")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Višak: AND!",errorMessage);
			
	}
	
	@Test
	public void parse_Exceptions3() {
		String errorMessage = null;
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = PrefixParser.parse(
					new Tokenizer(new StringReader("AND(A,(B)")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Treba stajati operator ili varijabla!",errorMessage);
			
	}
	
	@Test
	public void parse_Exceptions4() {
		String errorMessage = null;
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = PrefixParser.parse(
					new Tokenizer(new StringReader("AND A,B")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Nema ( nakon operatora!",errorMessage);
			
	}
	
	@Test
	public void parse_Exceptions5() {
		String errorMessage = null;
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = PrefixParser.parse(
					new Tokenizer(new StringReader("+")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Neispravno ime varijable: +!",errorMessage);
			
	}
	
	@Test
	public void parse_Exceptions6() {
		String errorMessage = null;
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = PrefixParser.parse(
					new Tokenizer(new StringReader("AND(a)")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Operator AND mora imati minimalno 2 operanda!",errorMessage);
			
	}
	
	@Test
	public void parse_Exceptions7() {
		String errorMessage = null;
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = PrefixParser.parse(
					new Tokenizer(new StringReader("and(a,b,or(c,d)")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Parametri moraju biti odvojeni , ili završeni )!",errorMessage);
			
	}
	
	@Test
	public void parse_Exceptions8() {
		String errorMessage = null;
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = PrefixParser.parse(
					new Tokenizer(new StringReader(")")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Treba stajati operator ili varijabla!",errorMessage);
			
	}
	
	/*
	 * Metoda potrebna za pozivanje testa iz ANTa
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(PrefixParserTest.class);
    }
}
