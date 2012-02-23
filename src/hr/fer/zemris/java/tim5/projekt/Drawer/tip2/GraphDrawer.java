package hr.fer.zemris.java.tim5.projekt.Drawer.tip2;

import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.Drawer.Drawer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementacija crtača koji crta Booleovu funkciju. Način prikaza je u obliku
 * grafa (tip 2). Naziv tipa 2 je "logicki-sklopovi".
 */
public abstract class GraphDrawer extends Drawer {

	/** Standardna vrijednost inseta generirane slike */
	protected final int DEFALUT_INSETS = 60;

	/** Standardna vrijednost duljine stranice simbola logičkog sklopa */
	protected final int DEFALUT_SYMBOL_SIZE = 60;

	/** Lista sa svim varijablama */
	protected List<String> variables;

	/** Mapa sa X koordinatama svake varijable */
	protected Map<String, Integer> signalPosX;

	/**
	 * Konstruktor.
	 * 
	 * @param tree
	 *            Booleova funkcija.
	 * @param symbolSize
	 *            dimenzija simbola operacije.
	 * @param insets
	 *            razmak do ruba slike.
	 */
	public GraphDrawer(ParserNode tree, int symbolSize, int insets) {
		if (tree == null) {
			throw new NullPointerException("Stablo nije zadano!");
		}
		if (insets < 0 || symbolSize < 0) {
			throw new IllegalArgumentException(
					"Parametri slike ne mogu biti negativni");
		}

		this.tree = tree;
		this.symbolSize = symbolSize;
		this.insets = insets;

		variables = new ArrayList<String>();
		findVariables(tree);
		Collections.sort(variables);

		signalPosX = fillSignalPos();
	}

	/**
	 * Konstruktor bez inseta
	 * 
	 * @param tree
	 *            Booleova funkcija.
	 * @param symbolSize
	 *            dimenzija simbola operacije.
	 */
	public GraphDrawer(ParserNode tree, int symbolSize) {
		if (tree == null) {
			throw new NullPointerException("Stablo nije zadano!");
		}
		if (symbolSize < 0) {
			throw new IllegalArgumentException(
					"Parametri slike ne mogu biti negativni");
		}

		this.tree = tree;
		this.symbolSize = symbolSize;
		this.insets = symbolSize;

		variables = new ArrayList<String>();
		findVariables(tree);
		Collections.sort(variables);

		signalPosX = fillSignalPos();
	}

	/**
	 * Konstruktor bez parametara.
	 */
	public GraphDrawer() {
		this.tree = null;
		this.symbolSize = DEFALUT_SYMBOL_SIZE;
		this.insets = DEFALUT_INSETS;

		this.variables = null;
		this.signalPosX = null;
	}
	
	@Override
	public void setSymbolSize(int symbolSize) {
		super.setSymbolSize(symbolSize);
		this.insets = symbolSize;
	}
	
	@Override
	public void setTreeRoot(ParserNode tree) {
		if (tree == null) {
			throw new NullPointerException("Stablo nije zadano!");
		}
		this.tree = tree;
		
		this.insets = this.symbolSize;
		this.variables = new ArrayList<String>();
		findVariables(tree);
		Collections.sort(this.variables);

		this.signalPosX = fillSignalPos();
	}
	
	/**
	 * Metoda rekurzivno traži sve varijable u stablu i stavlja ih u listu
	 * variables
	 * 
	 * @param node
	 *            Parse stablo Booleove funkcije.
	 */
	protected void findVariables(ParserNode node) {
		if (node.isVar()) {
			if (!variables.contains(node.getName())) {
				variables.add(node.getName());
			}
		} else {
			for (ParserNode child : node.getChildren()) {
				findVariables(child);
			}
		}
	}

	/**
	 * Metoda stvara mapu u kojoj je ključ signal, a podatak je X koordinata tog
	 * signala na slici.
	 * 
	 * @return Mapu Signal - X koordinata signala.
	 */
	protected Map<String, Integer> fillSignalPos() {
		Map<String, Integer> pos = new HashMap<String, Integer>();
		Integer x = new Integer(insets);
		for (String var : variables) {
			pos.put(var, x);
			x = Integer.valueOf(x + symbolSize / 3);
		}
		return pos;
	}

	/**
	 * Metoda računa širinu slike izraženu u pikselima. Širina slike je zbroj
	 * udaljenosti do ruba slike sa lijeve i desne strane, širine samog stabla,
	 * širine sabirnice te razmaka između sabirnice i stabla.
	 * 
	 * @return širinu slike.
	 */
	protected int getImageWidth() {
		return insets * 2 + GraphCalc.getTreeWidth(this.tree, this.symbolSize)
				+ getSignalsWidth() + symbolSize / 2;
	}

	/**
	 * Metoda računa širinu sabirnice izraženu u pikselima. Razmak između dvije
	 * linije sabirnice je symbolSize/3, iz čega slijedi izravna formula.
	 * 
	 * @return širinu sabirnice.
	 */
	protected int getSignalsWidth() {
		if (variables.size() >= 1) {
			return (variables.size() - 1) * (symbolSize / 3);
		} else
			return 0;
	}

	/**
	 * Metoda računa visinu slike. Visina slike je zbroj visine stabla i razmaka
	 * do ruba slike sa gornje i donje strane.
	 * 
	 * @return visinu slike.
	 */
	protected int getImageHeight() {
		return GraphCalc.getTreeHeight(this.tree, this.symbolSize)
				+ this.insets * 2;
	}

	/**
	 * Metoda vraća koordinate korijena.
	 * 
	 * @return Koordinatu korijena stabla na slici
	 */
	protected Koordinata getRootPos() {
		int x = this.insets + getSignalsWidth() + this.symbolSize / 2
				+ GraphCalc.getTreeWidth(this.tree, this.symbolSize);
		int y = this.insets
				+ GraphCalc.getTreeHeightLeft(this.tree, this.symbolSize);
		return new Koordinata(x, y);
	}

	public BufferedImage drawFunction() {
		int width = getImageWidth();
		int height = getImageHeight();

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image.createGraphics();

		g.setFont(new Font("Times New Roman", Font.PLAIN,
				(int) (symbolSize / 2.77)));

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		g.setColor(Color.BLACK);
		drawSignals(g, insets, insets, height - insets);

		Koordinata rootPos = getRootPos();

		drawText(g, rootPos.x + symbolSize / 3, rootPos.y, "f");
		if (tree.isVar()) {
			g.drawLine(rootPos.x, rootPos.y, signalPosX.get(tree.getName()),
					rootPos.y);
		} else {
			buildFunction(g, tree, rootPos.x, rootPos.y);
		}

		g.dispose();
		return image;
	}

	/**
	 * Metoda crta signale na Graphicsu g.
	 * 
	 * @param g
	 *            Graphics na kojem se crta.
	 * @param x
	 *            Y koordinata prvog signala.
	 * @param upperY
	 *            Y koordinata gornjeg kraja signala
	 * @param lowerY
	 *            Y koordinata donjeg kraja signala
	 */
	protected void drawSignals(Graphics g, int x, int upperY, int lowerY) {
		for (String var : variables) {
			g.drawLine(x, upperY, x, lowerY);
			drawText(g, x, upperY - symbolSize / 3, var);
			x += symbolSize / 3;
		}
	}

	/**
	 * Metoda crta text. Od metode drawString razlikuje se u tome što su zadane
	 * koordinate sredine, a ne donjeg lijevog kuta teksta.
	 * 
	 * @param g
	 *            Graphics na kojem se crta.
	 * @param x
	 *            X koordinata sredine texta.
	 * @param y
	 *            Y koordinata sredine texta.
	 * @param text
	 *            text koji se ispisuje
	 */
	protected void drawText(Graphics g, int x, int y, String text) {
		FontMetrics metrics = g.getFontMetrics();
		int startX = x - metrics.stringWidth(text) / 2;
		int startY = y + metrics.getAscent() / 2;
		g.drawString(text, startX, startY);
	}

	/**
	 * Metoda rekurzivno "gradi" sliku funkcije.
	 * 
	 * @param g
	 *            Graphics na kojem se crta
	 * @param node
	 *            Funkcija koja se crta
	 * @param x
	 *            X koordinata korijena funkcije
	 * @param y
	 *            Y koordinata korijena funkcije
	 */
	abstract protected void buildFunction(Graphics g, ParserNode node, int x, int y);

	/**
	 * Metoda spaja sklop na signal. Drugim riječima, crta liniju zadane točke
	 * do odgovarajućeg signala u sabirnici. Osim toga, crta i crni kružić
	 * budući da se linija ulaza siječe sa sabirnicom. Promjer kružića je fiksno
	 * 8
	 * 
	 * @param g
	 *            Graphics na kojem se crta.
	 * @param x
	 *            X koordinata početka spojne linije.
	 * @param y
	 *            Y koordinata početka spojne linije
	 * @param var
	 *            ime varijable na koju se spaja.
	 */
	protected void connectToSignal(Graphics g, int x, int y, String var) {
		g.drawLine(x, y, signalPosX.get(var), y);
		g.fillArc(signalPosX.get(var) - 4, y - 4, 8, 8, 0, 360);
	}

	public String getType() {
		return "logicki-sklopovi";
	}
}
