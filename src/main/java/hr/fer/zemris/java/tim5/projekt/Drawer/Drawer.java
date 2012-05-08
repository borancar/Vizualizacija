package hr.fer.zemris.java.tim5.projekt.Drawer;

import hr.fer.zemris.java.tim5.projekt.ParserNode;

import java.awt.image.BufferedImage;

/**
 * Klasa koju nasljeđuje svaki razred zadužen za crtanje Booleove funkcije
 */
public abstract class Drawer {
	
	/** Korijen parsiranog stabla funkcije koju treba nacrtati */
	protected ParserNode tree;

	/** Razmaci do ruba svake strane slike */
	protected int insets;
	
	/** Duljina stranice simbola logičkog sklopa */
	protected int symbolSize;

	/**
	 * Pozivom ove funkcije iscrtava se Booleova funkcija
	 * 
	 * @return BufferedImage - sliku Booleove funkcije
	 */
	abstract public BufferedImage drawFunction();

	/**
	 * Metoda koja vraća o kojem tipu crtača se radi (metoda je potrebna
	 * servletu).
	 * 
	 * @return Naziv tipa prikaza.
	 */
	abstract public String getType();

	/**
	 * Setter korijena stabla.
	 * 
	 * @param tree
	 *            Korijen stabla.
	 */
	public void setTreeRoot(ParserNode tree) {
		if (tree == null) {
			throw new NullPointerException("Stablo nije zadano!");
		}
		this.tree = tree;
	}

	/**
	 * Postavljanje veličine simbola.
	 * 
	 * @param symbolSize
	 *            Veličina simbola.
	 */
	public void setSymbolSize(int symbolSize) {
		if (symbolSize < 0) {
			throw new IllegalArgumentException(
					"Parametri slike ne mogu biti negativni");
		}
		this.symbolSize = symbolSize;
	}
	
	public int getSymbol() {
		return this.symbolSize;
	}

}
