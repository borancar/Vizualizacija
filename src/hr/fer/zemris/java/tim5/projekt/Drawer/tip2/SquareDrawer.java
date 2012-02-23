package hr.fer.zemris.java.tim5.projekt.Drawer.tip2;

import hr.fer.zemris.java.tim5.projekt.ParserNode;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Implementacija crtača koji crta Booleovu funkciju kao stablo sa logičkim
 * sklopovima. Crtaju se standardni, kvadratni oblici logičkih sklopova. Naziv
 * tipa 2 je "logicki-sklopovi".
 */
public class SquareDrawer extends GraphDrawer {

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
	public SquareDrawer(ParserNode tree, int symbolSize, int insets) {
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
	 * Konstruktor bez inseta.
	 * 
	 * @param tree
	 *            Booleova funkcija.
	 * @param symbolSize
	 *            dimenzija simbola operacije.
	 */
	public SquareDrawer(ParserNode tree, int symbolSize) {
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
	public SquareDrawer() {
		this.tree = null;
		this.symbolSize = DEFALUT_SYMBOL_SIZE;
		this.insets = DEFALUT_INSETS;

		this.variables = null;
		this.signalPosX = null;
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
	protected void buildFunction(Graphics g, ParserNode node, int x, int y) {

		// x1 je x koordinata polovice lijeve stranice kvadrata
		int x1 = x - (3 * symbolSize / 2);
		// x2, kada će mu se pridodati odgovarajući offset (pomakOdUlaza) bit će
		// x koordinata točke od koje će se nastaviti rekurzija.
		int x2 = x - (2 * symbolSize);

		// Koordinata gornjeg lijevog kuta kvadrata
		Koordinata upperLeft = new Koordinata(x1, y - symbolSize / 2);

		// Crtanje linija izlaza iz sklopa (kvadrata)
		g.drawLine(x, y, x - symbolSize / 2, y);

		// crtanje samog sklopa (kvadrata)
		node.drawTip2(g, upperLeft.x, upperLeft.y, symbolSize);

		int childrenSize = node.getChildren().size();
		Koordinata[] childPos = GraphCalc.getUlazi(upperLeft, childrenSize,
				this.symbolSize);

		// Udaljenost između ulaza i pregiba linije (dodatak na osnovicu x2!)
		int pomakOdUlaza;
		// Pomak između dvije izlazne linije
		int lineOffset = 7;
		for (int i = 0; i < childrenSize; i++) {
			// y1 je y koordinata i-tog djeteta (x koordinata mu je x2)
			int y1 = childPos[i].y;

			// Udaljenost između izlaza i i-tog djeteta
			int verticalDist = GraphCalc.getVerticalDistance(node, i,
					symbolSize);

			// Linija koja čini horizontalni pomak od ulaza (na rubovima je
			// kraći, u sredini najveći)
			pomakOdUlaza = i < childrenSize / 2 ? i * lineOffset
					: (childrenSize % (i + 1)) * lineOffset;

			if (node.getChildren().get(i).isOp()) {
				// Ako je dijete operator, trebaju se nacrtati dvije linije.
				// 1. linija ulaza u trenutni sklop
				// 2. linija koja spaja ulaz u trenutni sklop sa izlazom iz
				// djeteta
				g.drawLine(x1, y1, x2 - pomakOdUlaza, y1);
				g.drawLine(x2 - pomakOdUlaza, y1, x2 - pomakOdUlaza, y1
						+ verticalDist);

				// nastavlja se izgradnja funkcije od i-tog djeteta
				buildFunction(g, node.getChildren().get(i), x2 - pomakOdUlaza,
						y1 + verticalDist);
			} else {
				connectToSignal(g, x1, y1, node.getChildren().get(i).getName());
			}
		}
	}

	public String getType() {
		return "logicki-sklopovi";
	}
}
