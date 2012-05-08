package hr.fer.zemris.java.tim5.projekt.Drawer.tip1a;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.List;

import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.Drawer.tip1.ExpDrawer;
import hr.fer.zemris.java.tim5.projekt.nodes.AndNode;
import hr.fer.zemris.java.tim5.projekt.nodes.NotNode;
import hr.fer.zemris.java.tim5.projekt.nodes.OrNode;
import hr.fer.zemris.java.tim5.projekt.nodes.VariableNode;
import hr.fer.zemris.java.tim5.projekt.nodes.XnorNode;
import hr.fer.zemris.java.tim5.projekt.nodes.XorNode;

/**
 * Implementacija crtača koji crta Booleovu funkciju. Prikazuje se izraz pomoću
 * standardnih oznaka za logičke operacije (prikaz tipa 1a). Ime razreda je
 * kratica od "Expression Drawer type A". Naziv ovog prikaza je
 * "std-log-notacija".
 */
public class ExpDrawerA extends ExpDrawer {

	/**
	 * Konstruktor.
	 * 
	 * @param tree
	 *            Booleova funkcija.
	 * @param symbolSize
	 *            dimenzija simbola operacije.
	 */
	public ExpDrawerA(ParserNode tree, int symbolSize) {
		this.tree = tree;
		this.symbolSize = symbolSize;
		this.insets = symbolSize / 3;
		this.notHeight = 0;
		this.picWidth = 0;
	}

	/**
	 * Konstruktor bez parametara.
	 */
	public ExpDrawerA() {
		this.tree = null;
		this.symbolSize = DEFALUT_SYMBOL_SIZE;
		this.insets = this.symbolSize / 3;
		this.notHeight = DEFALUT_NOT_HEIGHT;
		this.picWidth = 0;
	}
	
	@Override
	public void setSymbolSize(int symbolSize) {
		super.setSymbolSize(symbolSize);
		this.insets = symbolSize/3;
		this.notHeight = 0;
		this.picWidth = 0;
	}

	protected void calcWidth(ParserNode p) {
		FontMetrics fm = g.getFontMetrics();

		if (p.isVar()) {
			// Ako je cvor varijabla, na sirinu slike se dodaje sirina varijable
			// i razmak do sljedeceg znaka.
			picWidth += fm.stringWidth(p.getName()) + symbolSize / 5;

			// Ako je roditelj varijable bio Not, onda se od sirine mora oduzeti
			// sirina dviju zagrada koje su za varijablu nepotrebne
			if (p.getParent() != null && p.getParent() instanceof NotNode) {
				picWidth -= 2 * fm.stringWidth("(") + 2 * symbolSize / 5;
			}
		}
		if (p.isOp()) {
			// Ako je cvor operator, na sirinu se dodaje sirina simbola i razmak
			// do
			// sljedeceg znaka
			picWidth += symbolSize + symbolSize / 5;

			// Flag replace označava treba li se čvor zamijeniti sa NOT +
			// ekvivalent
			// Flag giveMePar označava treba li staviti zagrade oko izraza
			boolean replace = (p.isNot() && p.getChildren().size() > 2);
			boolean giveMePar = (p instanceof NotNode || !(p.getParent() != null && p
					.getParent() instanceof NotNode));

			// XNOR je širi pa mu treba dodatni symbolSize
			if (!replace && p instanceof XnorNode)
				picWidth += symbolSize;
			if (replace) {
				// na širinu se dodaje širina operatora NOT i
				// razmak do sljedećeg
				picWidth += symbolSize + symbolSize / 5;
				giveMePar = true;
			}
			if (giveMePar) {
				// na širinu se dodaje širina zarada i razmak
				picWidth += 2 * fm.stringWidth("(") + 2 * symbolSize / 5;
			}
			for (int i = 0; i < p.getChildren().size(); i++) {
				// rekurzivno se nastavlja izračun
				calcWidth(p.getChildren().get(i));
				// Između svaka dva djeteta treba staviti operator i razmak
				if ((i != p.getChildren().size() - 1) && (i != 0)) {
					picWidth += symbolSize + symbolSize / 5;
				}
			}
		}
		if (p == tree && removePars) {
			// Treba maknuti prvu i zadnju zagradu iz izraza
			picWidth -= 2 * fm.stringWidth("(") + 2 * symbolSize / 5;
		}
	}

	protected void goPreorder(ParserNode p) {
		// Flag giveMePar označava treba li staviti zagrade oko izraza
		boolean giveMePar;

		if (p != null) {
			List<ParserNode> ch = p.getChildren();
			if (p instanceof NotNode) {
				giveMePar = p.getChildren().get(0).isOp();

				// Ako je čvor NotNode, u listu za crtanje se dodaje
				// 1. čvor
				// 2. Otvorena zagrada (ako treba)
				// 3. Rekurzivno izračunati izraz
				// 4. Zatvorena zagrada (ako treba)
				nodes.add(p);
				if (giveMePar)
					nodes.add(new VariableNode("("));
				goPreorder(ch.get(0));
				if (giveMePar)
					nodes.add(new VariableNode(")"));
			} else if (p.isVar()) {
				nodes.add(p);
			} else if (p.isOp()) {
				giveMePar = !(p.getParent() != null && p.getParent() instanceof NotNode);
				// Flag replace označava treba li se čvor
				// zamijeniti sa NOT + ekvivalent
				boolean replace = (p.isNot() && p.getChildren().size() > 2);

				// tmp je čvor koji će se staviti u listu čvorova za ispisivanje
				ParserNode tmp = (replace) ? superNode(p) : p;

				if (replace) {
					// Ako treba zamjena, stavlja se dodatni NotNode
					nodes.add(new NotNode());
					giveMePar = true;
				}
				// U listu za crtanje se dodaje
				// 1. Otvorena zagrada (ako treba)
				// 2. Rekurzivno izračunati izrazi za svako dijete
				// 3. Čvor tmp (Poslije svakog djeteta osim zadnjeg)
				// 4. Zatvorena zagrada (ako treba)
				if (giveMePar)
					nodes.add(new VariableNode("("));
				for (int i = 0; i < ch.size(); i++) {
					goPreorder(ch.get(i));
					if (i != ch.size() - 1)
						nodes.add(tmp);
				}
				if (giveMePar)
					nodes.add(new VariableNode(")"));
			}
		}
	}

	/**
	 * Metoda vraća super tip za operatore Nor, Nand i Xnor.
	 * 
	 * @param name
	 *            operator
	 * @return Super tip operatora.
	 */
	private ParserNode superNode(ParserNode p) {
		Operator op = Operator.valueOf(p.getName().toUpperCase());
		switch (op) {
		case NOR:
			return new OrNode();
		case NAND:
			return new AndNode();
		case XNOR:
			return new XorNode();
		default:
			throw new IllegalArgumentException(
					"Metoda vraća super-tip samo za Nor, Nand i Xnor operatore");
		}
	}

	/**
	 * Enum za metodu superNode(). Definirani su operatori kojima se može
	 * vratiti super tip.
	 */
	private enum Operator {
		NAND, NOR, XNOR;
	}

	public BufferedImage drawFunction() {

		BufferedImage slika = prepareImage();

		createDrawList();

		// Redom se crtaju svi čvorovi
		for (ParserNode p : nodes) {
			if (p.isOp()) {
				p.drawTip1a(g, nowX, symbolSize + insets, symbolSize);
				nowX += symbolSize + symbolSize / 5;

				// Xnor je širok 2*symbolSize pa je potreban dodatni symbolSize
				if (p instanceof XnorNode)
					nowX += symbolSize;
			} else if (p.isVar()) {
				drawText(p.getName(), nowX);
			}
		}

		g.dispose();
		return slika;
	}

	/**
	 * Metoda priprema sliku (traži ispravnu veličinu, postavlja font, boju..)
	 * 
	 * @return pripremljeni BufferedImage
	 */
	private BufferedImage prepareImage() {
		BufferedImage slika = new BufferedImage(1, 1,
				BufferedImage.TYPE_3BYTE_BGR);
		g = slika.createGraphics();

		Font fNew = new Font("Dialog", Font.PLAIN, getFontSize(
				(int) (symbolSize * 1.381), 0, symbolSize * 10));
		g.setFont(fNew);

		removePars = !((tree.isNot() && tree.getChildren().size() > 2)
				|| (tree instanceof VariableNode) || tree instanceof NotNode);
		calcWidth(this.tree);
		picWidth += 2 * insets;

		slika.flush();
		g.dispose();

		slika = new BufferedImage(getImageWidth(), getImageHeight(),
				BufferedImage.TYPE_3BYTE_BGR);

		g = slika.createGraphics();
		g.setFont(fNew);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getImageWidth(), getImageHeight());
		g.setColor(Color.BLACK);
		nowX = insets;

		return slika;
	}

	public String getType() {
		return "std-log-notacija";
	}

}
