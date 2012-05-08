package hr.fer.zemris.java.tim5.projekt.nodes;

import hr.fer.zemris.java.tim5.projekt.ParserNode;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/** Implementacija AND operatora */
public class AndNode extends OperatorNode {
	
	@Override
	public String getName() {
		return "AND";
	}
	
	@Override
	public void drawTip2(Graphics g, int x, int y, int symbolSize) {
		super.drawTip2(g, x, y, symbolSize);
		
		String simbol = "&";
		
		FontMetrics fm = g.getFontMetrics();
		// X koordinata donjeg lijevog kuta teksta
		int simbolX = x + (symbolSize - fm.stringWidth(simbol)) / 2;
		// Y koordinata donjeg lijevog kuta teksta
		int simbolY = y + (symbolSize + fm.getAscent()) / 2;
		
		g.drawString(simbol, simbolX, simbolY);
	}
	
	@Override
	public void drawTip1a(Graphics g, int x, int y, int symbolSize) {
		String sign = "\u2227";
		g.setColor(Color.BLACK);
		int width = g.getFontMetrics().stringWidth(sign);
		g.drawString(sign, x + (symbolSize-width)/2, y);
	}
	
	@Override
	public void drawTip1b(Graphics g, int x, int y, int symbolSize) {
		String sign = "\u22C5";
		g.setColor(Color.BLACK);
		int width = g.getFontMetrics().stringWidth(sign);
		g.drawString(sign, x + (symbolSize-width)/2, y);
	}
	
	@Override
	public boolean calculate(boolean[] operands) {
		boolean result = operands[0];
		for (int i=1; i<operands.length; i++) {
			result = result && operands[i];
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj){
		ParserNode node = null;
		try {
			node = (OperatorNode)obj;
		} catch (ClassCastException nijeDobarObjekt) {
			return false;
		}
		return node.getName().equals("AND");
	}
}
