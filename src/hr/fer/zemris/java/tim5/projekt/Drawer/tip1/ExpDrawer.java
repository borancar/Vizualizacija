package hr.fer.zemris.java.tim5.projekt.Drawer.tip1;

import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.Drawer.Drawer;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Apstrakni razred koji nasljeđuju crtači koji logičku
 * funkciju prikazuju u obliku izraza (crtači tipa 1)
 */
public abstract class ExpDrawer extends Drawer {

	/** Standardna vrijednost inseta generirane slike */
	protected final int DEFALUT_NOT_HEIGHT = 5;

	/** Standardna vrijednost veličine simbola */
	protected final int DEFALUT_SYMBOL_SIZE = 30;

	/** Širina slike */
	protected int picWidth;

	/** Visina dijela slike za NOT operatore; tu se iscrtavaju crte kod tipa 1b */
	protected int notHeight;

	/**
	 * Lista sa svim čvorovima poredanim onim redom kojim se trebaju iscrtati u
	 * sliku
	 */
	protected LinkedList<ParserNode> nodes;

	/** Trenutna pozicija po X osi na kojoj se crtač nalazi */
	protected int nowX;

	/** Grafika na koju se iscrtava sve što se treba iscrtati */
	protected Graphics g;

	/** flag koji označava trebaju li se micati zagrade s pocetka i kraja izraza */
	protected boolean removePars;

	/**
	 * Getter za privatnu varijablu picWidth.
	 * 
	 * @return Širinu izgenerirane slike.
	 */
	public int getImageWidth() {
		return picWidth;
	}

	/**
	 * Vraća visinu izgenerirane slike, ona je uvijek jednaka veličini simbola +
	 * gornji razmak od ruba + donji razmak od ruba + veličina dijela za crtanje
	 * not operatora (taj je dio kod prikaza 1a jednak 0)
	 * 
	 * @return Visinu izgenerirane slike.
	 */
	public int getImageHeight() {
		return symbolSize + notHeight + 2 * insets;
	}

	/**
	 * Metoda je zapravo binary search koji traži veličinu fonta takva da je za
	 * tu veličinu getAscent jednak zadanom ascentu. Metoda je potrebna budući da
	 * se preko nje izračunava veličina fonta takva da je veličina simbola u 
	 * prikazu jednaka symbolSize.
	 * 
	 * @param 	ascent	zadani ascent za koji se traži veličina fonta
	 * @param 	minSize	donja granica veličine fonta
	 * @param 	maxSize	gornja granica veličine fonta
	 * @return 	veličinu fonta.
	 */
	protected int getFontSize(int ascent, int minSize, int maxSize) {
		while (minSize < maxSize) {
			int currSize = (minSize + maxSize) / 2;

			g.setFont(new Font("Dialog", Font.PLAIN, currSize));
			FontMetrics fm = g.getFontMetrics();
			int currAscent = fm.getAscent();

			if (currAscent == ascent)
				return currSize;
			else if (currAscent < ascent)
				minSize = currSize;
			else
				maxSize = currSize;
		}
		return minSize;
	}

	/**
	 * Metoda koja iscrtava tekst, dodana je kako bi se olakšao ispis imena
	 * varijabli.
	 * 
	 * @param 	name	Ime varijable.
	 * @param 	x	X koordinata početka teksta.
	 */
	protected void drawText(String name, int x) {
		FontMetrics metrics = g.getFontMetrics();
		int startY = symbolSize + insets + notHeight;
		g.drawString(name, x, startY);
		nowX += metrics.stringWidth(name) + symbolSize / 5;
	}

	/**
	 * Metoda miče prvu i zadnju zagradu iz liste čvorova koje treba iscrtati
	 */
	protected void removePars() {
		for (ParserNode p : nodes) {
			if (p.getName().equals("(")) {
				nodes.remove(p);
				break;
			}
		}
		if (nodes.getLast().getName().equals(")")) {
			nodes.removeLast();
		}

	}
	
	/**
	 * Metoda stvara listu čvorova koji će se redom iscrtati na slici
	 */
	protected void createDrawList() {
		nodes = new LinkedList<ParserNode>();
		goPreorder(this.tree);
		if (removePars) {
//			Obriši prvu i zadnju zagradu ako je potrebno
			removePars();
		}
	}

	abstract public BufferedImage drawFunction();

	abstract public String getType();

	/**
	 * Metoda koja računa širinu slike (znači treba zbrojiti širinu svih
	 * operatora i varijabli).
	 * 
	 * @param 	p	korijenski čvor
	 */
	abstract protected void calcWidth(ParserNode p);

	/**
	 * Metoda koja obavlja preorder obilazak stabla i baca u listu čvorova sve
	 * čvorove onim redom kojim se trebaju nalaziti u finalnom ispisu
	 * 
	 * @param 	p	korijenski čvor
	 */
	abstract protected void goPreorder(ParserNode p);

}
