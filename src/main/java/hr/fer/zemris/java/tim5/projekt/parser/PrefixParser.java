package hr.fer.zemris.java.tim5.projekt.parser;

import java.text.ParseException;

import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.nodes.AndNode;
import hr.fer.zemris.java.tim5.projekt.nodes.OrNode;
import hr.fer.zemris.java.tim5.projekt.nodes.XorNode;

/**
 * Klasa koja sadržava parsera prefix notacije logičkih izraza.
 */
public class PrefixParser {
	
	/**
	 * Parsira tokene dobivene od tokenizera.
	 * @param tokenizer tokenizer koji šalje izraze parseru
	 * @return jezično stablo napisanog izraza
	 * @throws ParseException u slučaju greške pri parsiranju
	 */
	public static ParserNode parse(Tokenizer tokenizer) throws ParseException {
		ParserNode root = recursiveParse(tokenizer);
		Token token;
		
		if((token = tokenizer.getToken()).type != Token.Type.END) {
			throw new ParseException("Višak: " + token + "!", tokenizer.getPosition());
		}
		
		return root;
	}
	
	/**
	 * Pomoćna funkcija za parsiranje. Služi glavnoj da isparsira zadani tekst, ostavljajući
	 * višak.
	 * @param tokenizer tokenizer koji šalje izraze parseru
	 * @return jezično stablo napisanog izraza
	 * @throws ParseException u slučaju greške pri parsiranju
	 */
	private static ParserNode recursiveParse(Tokenizer tokenizer) throws ParseException {
		ParserNode root = null;
		Token token = null;
		
		try {
			token = tokenizer.getToken();
		
			switch(token.type) {
			case OPERATOR:
				root = token.toProperObject();
				
				token = tokenizer.getToken();
				
				if(token.type != Token.Type.BLOCK_BEGIN) {
					throw new ParseException("Nema ( nakon operatora!", tokenizer.getPosition());
				}
				
				while(token.type != Token.Type.BLOCK_END) {
					root.addChild(recursiveParse(tokenizer));
					
					token = tokenizer.getToken();
					if(token.type != Token.Type.COMMA_SEPARATOR && token.type != Token.Type.BLOCK_END) {
						throw new ParseException("Parametri moraju biti odvojeni , ili završeni )!", tokenizer.getPosition());
					}
				}
				
				if(root instanceof AndNode || root instanceof OrNode || root instanceof XorNode) {
					if(root.getChildren().size() < 2) {
						throw new ParseException("Operator " + root.getName() + " mora imati minimalno 2 operanda!", tokenizer.getPosition());
					}
				}
				
				break;
			
			case VARIABLE:
				root = token.toProperObject();
				break;
				
			default:
				throw new ParseException("Treba stajati operator ili varijabla!", tokenizer.getPosition());
			}
		} catch (IllegalArgumentException e) {
			throw new ParseException(e.getMessage(), tokenizer.getPosition());
		} catch (UnsupportedOperationException e) {
			throw new ParseException(e.getMessage(), tokenizer.getPosition());			
		}
		
		return root;
	}
}
