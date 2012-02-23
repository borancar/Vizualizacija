package hr.fer.zemris.java.tim5.projekt.parser;

import java.text.ParseException;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.parser.OperatorToken.Operator;

/**
 * Klasa koja sadržava parsera infix notacije logičkih izraza
 */
public class InfixParser {
	
	/**
	 * Metoda koja parsira ulaz koji dobiva od tokenizatora, vraća jezično stablo
	 * @param tokenizer tokenizator
	 * @return jezično stablo izraza
	 */
	public static ParserNode parse(Tokenizer tokenizer) throws ParseException {
		ParserNode root = null;

		Queue<Token> rpn = convertRPN(tokenizer);
		Stack<ParserNode> operands = new Stack<ParserNode>();
		
		while(!rpn.isEmpty()) {
			Token token = rpn.remove();

			if(token instanceof VariableToken) {
				operands.add(token.toProperObject());
			} else if(token instanceof OperatorToken) {
				try {
					OperatorToken operator = (OperatorToken) token;
					ParserNode a;
					ParserNode b;
	
					root = operator.toProperObject();
					
					switch(operator.operator) {
					case NOT:
						a = operands.pop();
						root.addChild(a);
						operands.add(root);
						break;
					default:
						b = operands.pop();
						a = operands.pop();
						
						root.addChild(a);
						root.addChild(b);
						
						operands.add(root);
					}
				} catch (EmptyStackException e) {
					throw new ParseException("Mora stajati varijabla uz operator!", -1);
				}
			} else {
				throw new ParseException("Mora stajati varijabla ili operator!", -1);
			}
		}
		
		root = operands.pop();
		
		if(!operands.empty()) {
			throw new ParseException("Višak: " + operands.pop().getName() + "!", tokenizer.getPosition());
		}
		
		return root;
	}

	/**
	 * Metoda koja infiks pretvara u postfiks i time olakšava parsiranje
	 * @param tokenizer tokenizator
	 * @return postfiks zapis prikladan za dalju obradu
	 */
	private static Queue<Token> convertRPN(Tokenizer tokenizer) throws ParseException {
		Stack<Token> stack = new Stack<Token>();
		Queue<Token> queue = new LinkedList<Token>();
		Token token = null;
		
		try {
			while((token = tokenizer.getToken()).type != Token.Type.END) {
				switch(token.type) {
				case OPERATOR:
					OperatorToken operator = (OperatorToken) token;
					
					if(operator.operator == Operator.XNOR || operator.operator == Operator.NAND ||
							operator.operator == Operator.NOR) {
						throw new ParseException("Zabranjeno korištenje ne-tranzitivnih operatora u infiksnom prikazu!", tokenizer.getPosition());
					}
					
					while(!stack.empty() && (stack.peek() instanceof OperatorToken)) {
						if(operator.compareTo((OperatorToken) stack.peek()) <= 0) {
							queue.add(stack.pop());
						} else {
							break;
						}
					}
					
					if(queue.isEmpty() && operator.operator != OperatorToken.Operator.NOT) {
						throw new ParseException("Ne postoji lijevi operand ili je višak s desne strane za " + operator + "!", tokenizer.getPosition());
					}
					
					stack.add(operator);
					break;
					
				case VARIABLE:
					queue.add(token);
					break;
				
				case BLOCK_BEGIN:
					stack.push(token);
					break;
					
				case BLOCK_END:
					try {
						while(stack.peek().type != Token.Type.BLOCK_BEGIN) {
							queue.add(stack.pop());
						}

						stack.pop();
					} catch (EmptyStackException e) {
						throw new ParseException("Zatvoren blok koji uopće nije otvoren! ) viška!", tokenizer.getPosition());
					}
					break;
					
				default:
					throw new ParseException("Token ne pripada infiksnom zapisu: " + token, tokenizer.getPosition());
				}
			}
		} catch(IllegalArgumentException e) {
			throw new ParseException(e.getMessage(), tokenizer.getPosition());
		}
			
		while(!stack.empty()) {
			if(stack.peek().type == Token.Type.BLOCK_BEGIN) {
				throw new ParseException("Nije zatvoren blok! Nedostaje )!", tokenizer.getPosition());
			}
			
			queue.add(stack.pop());
		}
		
		return queue;
	}
}
