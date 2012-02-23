package hr.fer.zemris.java.tim5.projekt.test;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.text.ParseException;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.nodes.AndNode;
import hr.fer.zemris.java.tim5.projekt.nodes.OrNode;
import hr.fer.zemris.java.tim5.projekt.nodes.VariableNode;
import hr.fer.zemris.java.tim5.projekt.parser.InfixParser;
import hr.fer.zemris.java.tim5.projekt.parser.Tokenizer;

/**
 * JUnit test koji testira infiksni parser. 
 */
public class InfixParserTest {
	/**
	 * Broj testova za parser
	 */
	private static final int NUMBERS_OF_TESTS = 10;
	
	/*
	 * Metoda testira parse metodu parsera. Radi na temelju testnog generatora stabla, koje uspoređuje sa
	 * stablom dobivenim slanjem istog tog stablo u obliku stringa 
	 * infiksnom parseru, tj. njegovoj parse() metodi.
	 */
	@Test (expected = ParseException.class)
	public void parse_GeneratorNode1()throws ParseException{
		for(int i = 0; i < NUMBERS_OF_TESTS; i++) {
			TestTreeGenerator generator = new TestTreeGenerator();
			
			String inorder = generator.printInorder();
			
			System.out.println(inorder);
			
			ParserNode testNode = InfixParser.parse(
					new Tokenizer(new StringReader(
							inorder
							)
					));
			
			assertEquals(generator.getTree(),testNode);
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
			
			ParserNode parsedNode = InfixParser.parse(
					new Tokenizer(new StringReader(
							"a and b"
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
			
			ParserNode parsedNode = InfixParser.parse(
					new Tokenizer(new StringReader(
							"a and b or c"
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
		ParserNode testNode = InfixParser.parse(
				new Tokenizer(new StringReader("nevaljani unos")
				));
			
	}
	
	@Test
	public void parse_Exceptions2() {
		String errorMessage = null;
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = InfixParser.parse(
					new Tokenizer(new StringReader("A AND (")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Nije zatvoren blok! Nedostaje )!",errorMessage);
			
	}
	
	@Test
	public void parse_Exceptions3() {
		String errorMessage = null;
		
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = InfixParser.parse(
					new Tokenizer(new StringReader("+")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Neispravno ime varijable: +!",errorMessage);
			
	}
	
	@Test
	public void parse_Exceptions4() {
		String errorMessage = null;
		
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = InfixParser.parse(
					new Tokenizer(new StringReader("and b")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Ne postoji lijevi operand ili je višak s desne strane za AND!",
				errorMessage);
			
	}
	
	@Test
	public void parse_Exceptions5() {
		String errorMessage = null;
		
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = InfixParser.parse(
					new Tokenizer(new StringReader("a and b)")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Zatvoren blok koji uopće nije otvoren! ) viška!",errorMessage);
			
	}
	
	@Test
	public void parse_Exceptions7() {
		String errorMessage = null;
		
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = InfixParser.parse(
					new Tokenizer(new StringReader("a b")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Višak: a!",errorMessage);
			
	}
	
	@Test
	public void parse_Exceptions9() {
		String errorMessage = null;
		
		try {
			@SuppressWarnings("unused")
			ParserNode testNode = InfixParser.parse(
					new Tokenizer(new StringReader("a and and")
					));
		} catch (ParseException e) {
			errorMessage = e.getMessage();
		}
		
		assertEquals("Mora stajati varijabla uz operator!",errorMessage);
			
	}
	
	/*
	 * Metoda potrebna za pozivanje testa iz ANTa
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(InfixParserTest.class);
    }
}
