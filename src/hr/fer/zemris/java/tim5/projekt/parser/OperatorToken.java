package hr.fer.zemris.java.tim5.projekt.parser;

import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.nodes.AndNode;
import hr.fer.zemris.java.tim5.projekt.nodes.NandNode;
import hr.fer.zemris.java.tim5.projekt.nodes.NorNode;
import hr.fer.zemris.java.tim5.projekt.nodes.NotNode;
import hr.fer.zemris.java.tim5.projekt.nodes.OrNode;
import hr.fer.zemris.java.tim5.projekt.nodes.XnorNode;
import hr.fer.zemris.java.tim5.projekt.nodes.XorNode;

/**
 * Klasa predstavlja jedan operator. Implementira sučelje comparable da bi se
 * operatori mogli uspoređivati po prioritetima.
 */
public class OperatorToken extends Token implements Comparable<OperatorToken> {

	/**
	 * Enumeracija tipova operatora
	 */
	public enum Operator {
		AND(2), OR(1), NOT(3), NAND(-1), NOR(-1), XOR(1), XNOR(-1);
		
		/**
		 * Prioritet operatora, što je prioritet veći, operator je prioritetniji.
		 */
		private final int priority;
		
		/**
		 * Privatni konstruktor za enum, služi za postavljanje prioriteta operatorima
		 * @param priority prioritet operatora, veći broj znači prioritetniji operator
		 */
		private Operator(int priority) {
			this.priority = priority;
		}
		
		/**
		 * Getter za prioritet
		 * @return prioritet operatora, veći prioritet znači prioritetniji operator
		 */
		public int getPriority() {
			return this.priority;
		}
	}
	
	/**
	 * O kojem se operatoru radi
	 */
	protected Operator operator;
	
	/**
	 * Konstruktor, stvara novi primjerak operatora
	 * @param operator o kojem se operatoru radi
	 */
	public OperatorToken(Operator operator) {
		super(Type.OPERATOR);
		this.operator = operator;
	}
	
	/**
	 * Parsira operator iz tekstualnog zapisa
	 * @param text tekstulani zapis operatora
	 * @return token operatora
	 */
	public static Token parseToken(String text) {
		return new OperatorToken(Operator.valueOf(text.toUpperCase()));
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof OperatorToken)) {
			return false;
		}
		
		return super.equals(other) && ((OperatorToken) other).operator == this.operator;
	}
	
	/**
	 * Vraća o kojem se operatoru radi
	 * @return o kojem se operatoru radi
	 */
	public Operator getOperator() {
		return operator;
	}
	
	@Override
	public String toString() {
		return operator.toString();
	}
	
	@Override
	public ParserNode toProperObject() {
		switch(operator) {
		case AND: return new AndNode();
		case OR: return new OrNode();
		case NOT: return new NotNode();
		case XOR: return new XorNode();
		case NAND: return new NandNode();
		case NOR: return new NorNode();
		case XNOR: return new XnorNode();
		default: throw new IllegalArgumentException("Nepoznati operator!");
		}
	}
	
	public int compareTo(OperatorToken other) {
		if(other == null) {
			return -1;
		}
		
		return this.operator.getPriority() - other.operator.getPriority();
	}
}
