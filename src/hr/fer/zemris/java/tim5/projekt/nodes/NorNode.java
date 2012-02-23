package hr.fer.zemris.java.tim5.projekt.nodes;

import hr.fer.zemris.java.tim5.projekt.ParserNode;

import java.awt.Color;
import java.awt.Graphics;

/** Implementacija NOR operatora */
public class NorNode extends OrNode {
	
	@Override
	public String getName() {
		return "NOR";
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
	public void drawTip1a(Graphics g, int x, int y, int symbolSize) {
		String sign = "\u22BD"; 
		g.setColor(Color.BLACK);
		int width = g.getFontMetrics().stringWidth(sign);
		g.drawString(sign, x + (symbolSize-width)/2, y);
	}
	
	@Override
	public boolean isNot(){
		return true;
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
		return node.getName().equals("NOR");
	}
}
