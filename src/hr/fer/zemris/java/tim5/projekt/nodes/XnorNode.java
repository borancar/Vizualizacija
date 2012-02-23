package hr.fer.zemris.java.tim5.projekt.nodes;

import hr.fer.zemris.java.tim5.projekt.ParserNode;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Implementacija operatora XNOR
 */
public class XnorNode extends XorNode {
	
	@Override
	public String getName() {
		return "XNOR";
	}
	
	@Override
	public boolean isNot() {
		return true;
	}
	
	@Override
	public void drawTip1a(Graphics g, int x, int y, int symbolSize) {
		Font font = g.getFont();
		String sign = "XNOR"; 
		g.setColor(Color.BLACK);
		int minSize = 0;
		int maxSize = font.getSize();
		int currSize = 0;
		while (minSize < maxSize) {
			currSize = (minSize + maxSize)/2;
			
			g.setFont(new Font(font.getName(), font.getStyle(), currSize));
			FontMetrics fm = g.getFontMetrics();
			int currWidth = fm.stringWidth(sign);
			
			if (currWidth == symbolSize*2) break;
			else if (currWidth < symbolSize*2) minSize = currSize + 1;
			else maxSize = currSize - 1  ;
		}
		g.setFont(new Font(font.getName(), font.getStyle(), currSize));
		g.drawString(sign, x, y - (symbolSize - g.getFontMetrics().getAscent())/2);
		g.setFont(font);
	}
	
	@Override
	public void drawTip2(Graphics g, int x, int y, int symbolSize) {
		int circleRadius = (int) (symbolSize/6.25);
		
		super.drawTip2(g, x, y, symbolSize);
		g.setColor(Color.WHITE);
		g.fillArc(x+symbolSize, y+(symbolSize-circleRadius)/2, circleRadius, circleRadius, 0, 360);
		g.setColor(Color.BLACK);
		g.drawArc(x+symbolSize, y+(symbolSize-circleRadius)/2, circleRadius, circleRadius, 0, 360);
	}
	
	@Override
	public boolean calculate(boolean[] operands) {
		return !super.calculate(operands);
	}
	
	@Override
	public boolean equals(Object obj){
		ParserNode node = null;
		try {
			node = (OperatorNode)obj;
		} catch (ClassCastException nijeDobarObjekt) {
			return false;
		}
		return node.getName().equals("XNOR");
	}
}
