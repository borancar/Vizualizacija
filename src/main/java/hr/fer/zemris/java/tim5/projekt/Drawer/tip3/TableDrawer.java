package hr.fer.zemris.java.tim5.projekt.Drawer.tip3;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.Drawer.Drawer;

/**
 * Implementacija crtača koji crta Booleovu funkciju u obliku tablice (tip 3).
 * Naziv tipa 3 je "tablicni-prikaz".
 */
public class TableDrawer extends Drawer {

	/** Grafika za crtanje */
	private Graphics g;

	/** Konstante čelije tablice */
	private final int CELL_width = 30;

	private final int CELL_height = 25;

	/**
	 * Konstruktor
	 * 
	 * @param tree
	 *            parsirano stablo funkcije koju treba nacrtati
	 */
	public TableDrawer(ParserNode tree) {
		this.tree = tree;
	}

	/**
	 * Konstruktor bez parametara.
	 */
	public TableDrawer() {
	}

	public BufferedImage drawFunction() {

		Evaluator evaluator = new Evaluator(tree);
		List<Map<String, Boolean>> allResults = evaluator.evaluateAll();
		List<String> variables = evaluator.getVariables();

		int brojRedaka = (int) Math.pow(2, variables.size()) + 1;
		int brojStupaca = variables.size() + 1;

		BufferedImage image = new BufferedImage(brojStupaca * CELL_width,
				brojRedaka * CELL_height, BufferedImage.TYPE_3BYTE_BGR);
		g = image.createGraphics();

		int x = 0;
		int y = 0;

		// Ispis prvog retka
		for (String var : variables) {
			drawCell(Color.decode("0x003366"), Color.white, var, x, y);
			x += CELL_width;
		}
		drawCell(Color.decode("0x003366"), Color.white, "f", x, y);

		// Ispis vrijednosti varijabli i rezultata funkcije (svi ostali retci)
		for (Map<String, Boolean> result : allResults) {

			y += CELL_height;
			x = 0;

			for (String var : variables) {
				String binarnaVrijednost = String.valueOf(result.get(var) ? 1
						: 0);
				drawCell(Color.decode("0xFFFFCC"), Color.BLACK,
						binarnaVrijednost, x, y);
				x += CELL_width;
			}

			String binarnaVrijednost = String.valueOf(result.get("_RESULT") ? 1
					: 0);
			drawCell(Color.decode("0x99CCFF"), Color.BLACK, binarnaVrijednost,
					x, y);
		}
		
		g.drawLine(brojStupaca * CELL_width-1, 0, 
				brojStupaca * CELL_width-1, brojRedaka * CELL_height-1);
		g.drawLine(0, brojRedaka * CELL_height-1, 
				brojStupaca * CELL_width-1, brojRedaka * CELL_height-1);
		g.dispose();
		return image;
	}

	/**
	 * Metoda koja crta polje tablice ispunjeno zadanom stavkom
	 * 
	 * @param background
	 *            boja pozadine
	 * @param foreground
	 *            boja slova stavke
	 * @param stavka
	 *            stavka koju upisujemo
	 * @param x
	 *            x koordinata na kojoj počinje polje
	 * @param y
	 *            y koordinata na kojoj počinje polje
	 */
	private void drawCell(Color background, Color foreground, String stavka,
			int x, int y) {
		g.setColor(Color.black);
		g.drawRect(x, y, x + CELL_width, y + CELL_height);
		g.setColor(background);
		g.fillRect(x + 1, y + 1, x + CELL_width - 1, y + CELL_height - 1);
		g.setColor(foreground);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(stavka, x + CELL_width / 2 - fm.stringWidth(stavka) / 2, y
				+ CELL_height / 2 + fm.getAscent() / 2);
	}

	public String getType() {
		return "tablicni-prikaz";
	}

}
