package hr.fer.zemris.java.tim5.projekt.nodes;

import java.awt.Color;
import java.awt.Graphics;

import hr.fer.zemris.java.tim5.projekt.ParserNode;

/** 
 * Implementacija čvora varijable
 */
public class VariableNode extends ParserNode {
	
	private String name;

	public VariableNode(String ime) {
		name = ime;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Override metode equals() radi korištenja contains() metode kolekcije
	 * nodova. Dva variable node-a su jednaka ako su im nazivi jednaki.
	 */
	@Override
	public boolean equals(Object obj) {
		ParserNode drugi = null;
		try {
			drugi = (ParserNode)obj;
		} catch (ClassCastException nijeDobarObjekt) {
			return false;
		}
		return this.name.equals(drugi.getName());
	}

	/**
	 * Override metode hashCode() radi korištenja contains() metode kolekcije
	 * nodova.
	 */
	@Override
	public int hashCode() {
		int code = this.name.hashCode();
		code = (code << 8) | 'v';
		return code;
	}
	
	@Override
	public void drawTip1a(Graphics g, int x, int y, int symbolSize) {
		g.setColor(Color.BLACK);
		int width = g.getFontMetrics().stringWidth(this.getName());
		g.drawString(this.getName(), x + (symbolSize-width)/2, y);
	}
	
	/**
	 * Override ParserNodeove metode, vraća true za instance ovog razreda
	 */
	@Override
	public boolean isVar() {
		return true;
	}

	@Override
	public boolean isOp() {
		return false;
	}

	@Override
	public boolean isNot() {
		return false;
	}

}
