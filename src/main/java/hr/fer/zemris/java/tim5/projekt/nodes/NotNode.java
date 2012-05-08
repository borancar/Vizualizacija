package hr.fer.zemris.java.tim5.projekt.nodes;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import hr.fer.zemris.java.tim5.projekt.ParserNode;

/** Implementacija NOT operatora */
public class NotNode extends OperatorNode {

	@Override
	public void addChild(ParserNode child) {
		if(children.size() == 0) {
			super.addChild(child);
		} else {
			throw new UnsupportedOperationException("NOT može imati samo jedan ulaz!");
		}
	}

	@Override
	public String getName() {
		return "NOT";
	}
	
	@Override
	public void drawTip2(Graphics g, int x, int y, int symbolSize) {
		super.drawTip2(g, x, y, symbolSize);
		
		String simbol = "1";

		FontMetrics fm = g.getFontMetrics();
		// X koordinata donjeg lijevog kuta teksta
		int simbolX = x + (symbolSize - fm.stringWidth(simbol)) / 2;
		// Y koordinata donjeg lijevog kuta teksta
		int simbolY = y + (symbolSize + fm.getAscent()) / 2;
		
		g.drawString(simbol, simbolX, simbolY);
		
		int circleRadius = (int) (symbolSize/6.25);
		
		g.setColor(Color.WHITE);
		g.fillArc(x+symbolSize, y+(symbolSize-circleRadius)/2, circleRadius, circleRadius, 0, 360);
		g.setColor(Color.BLACK);
		g.drawArc(x+symbolSize, y+(symbolSize-circleRadius)/2, circleRadius, circleRadius, 0, 360);
	}
	
	@Override
	public void drawTip1a(Graphics g, int x, int y, int symbolSize) {
		String sign = "\u00AC";
		
		g.setColor(Color.BLACK);
		int width = g.getFontMetrics().stringWidth(sign);
		g.drawString(sign, x + (symbolSize-width)/2, y);
	}
	
	@Override
	public void drawTip1b(Graphics g, int x, int y, int symbolSize) {
		g.setColor(Color.BLACK);
		g.drawLine(x, y, x+symbolSize, y);
	}
	
	@Override
	public boolean isNot(){
		return true;
	}
	
	@Override
	public boolean calculate(boolean[] operands) {
		if (operands.length != 1) {
			throw new IllegalArgumentException(
					"Operacija NOT očekuje točno 1 operand, a ima ih " 
					+ operands.length);
		}
		return !operands[0];
	}
	
	@Override
	public boolean equals(Object obj){
		ParserNode node = null;
		try {
			node = (OperatorNode)obj;
		} catch (ClassCastException nijeDobarObjekt) {
			return false;
		}
		return node.getName().equals("NOT");
	}
}
