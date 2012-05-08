package hr.fer.zemris.java.tim5.projekt.nodes;

import java.awt.Graphics;

import hr.fer.zemris.java.tim5.projekt.ParserNode;

/**
 * Implementacija klase općenitog operatora
 */
public abstract class OperatorNode extends ParserNode {

	/**
	 * Override ParserNodeove metode, vraća true za svaki razred
	 * koji nasljeđuje OperatorNode
	 */
	@Override
	public boolean isOp(){
		return true;
	}

	/**
	 * Metoda izračunava rezultat operacije nad operandima
	 * u polju <i>operands</i>
	 * 
	 * @param 	operands	polje booleana - operandi
	 * @return	rezultat operacije
	 */
	abstract public boolean calculate(boolean[] operands);
	
	@Override
	public void drawTip2(Graphics g, int x, int y, int symbolSize) {
		g.drawRect(x, y, symbolSize, symbolSize);
	}
	
}
