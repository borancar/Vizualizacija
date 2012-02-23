package hr.fer.zemris.java.tim5.projekt.Drawer.tip1b;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.List;

import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.Drawer.tip1.ExpDrawer;
import hr.fer.zemris.java.tim5.projekt.nodes.NotNode;
import hr.fer.zemris.java.tim5.projekt.nodes.VariableNode;

/**
 * Implementacija crtača koji crta Booleovu funkciju. Prikazuje se izraz pomoću
 * matematičkih oznaka za logičke operacije (prikaz tipa 1b). Ime razreda je
 * kratica od "Expression Drawer type B". Naziv ovog prikaza je
 * "mat-log-notacija".
 */
public class ExpDrawerB extends ExpDrawer {

	/**
	 * Konstruktor.
	 * 
	 * @param 	tree	Booleova funkcija.
	 * @param 	symbolSize	dimenzija simbola operacije.
	 */
	public ExpDrawerB(ParserNode tree, int symbolSize) {
		this.tree = tree;
		this.symbolSize = symbolSize;
		this.insets = symbolSize / 3;
		this.notHeight = getNotDepth(tree) * symbolSize / 6;
		this.picWidth = 0;
	}

	/**
	 * Konstruktor bez parametara.
	 */
	public ExpDrawerB() {
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
		this.notHeight = getNotDepth(tree) * symbolSize / 6;
		this.picWidth = 0;
	}

	protected void calcWidth(ParserNode p) {
		FontMetrics fm = g.getFontMetrics();
		
		if (p.isVar()) {
//			Ako je cvor varijabla, na sirinu slike se dodaje sirina varijable
//			i razmak do sljedeceg znaka.
			picWidth += fm.getStringBounds(p.getName(), g).getWidth()
					+ symbolSize / 5;

		}
		if (p.isOp()) {
			if (p instanceof NotNode) {
//				Čvor Not nema svoju širinu niti zagrada pa se samo
//				rekurzivno nastavlja traženje širine za potomka
				calcWidth(p.getChildren().get(0));
			} else {
//				Ako je cvor operator (i nije Not), na sirinu se dodaje sirina simbola i razmak do
//				sljedeceg znaka
				picWidth += symbolSize + symbolSize / 5;
				
//				na širinu se još dodaje širina zarada i razmak
				picWidth += 2 * fm.stringWidth("(") + 2 * symbolSize / 5;
				
//				Čim u stablu postoji operator koji nije Not, to znači
//				da postoje zagrade na početku i na kraju koje treba maknuti
				removePars = true;
				
				for (int i = 0; i < p.getChildren().size(); i++) {
//					rekurzivno se nastavlja izračun
					calcWidth(p.getChildren().get(i));
//					Između svaka dva djeteta treba staviti operator i razmak
					if ((i != p.getChildren().size() - 1) && (i != 0)) {
						picWidth += symbolSize + symbolSize / 5;
					}
				}
			}
		}
		if (p == tree && removePars) {
//			Treba maknuti prvu i zadnju zagradu iz izraza
			picWidth -= 2 * fm.stringWidth("(") + 2 * symbolSize / 5;
		}
	}

	protected void goPreorder(ParserNode p) {
		if (p != null) {
			List<ParserNode> ch = p.getChildren();
			if (p instanceof NotNode) {
//				Ako je čvor NotNode, u listu za crtanje se dodaje 
//				1. čvor
//				2. Rekurzivno izračunati izraz
//				U ovom načinu prikaza, Not nikada nema zagrada oko sebe.
				nodes.add(p);
				goPreorder(ch.get(0));
			} else if (p.isVar()) {
				nodes.add(p);
			} else if (p.isOp()) {
				if (p.isNot()) {
//					Ubacivanje NOT čvora kod operacija NOR, NAND i XNOR.
//					Primijetite da je taj NOT root svojeg stabla, a čvor p mu
//					je jedini potomak. Taj NOT istodobno nije i root našeg,
//					zadanog stabla funkcije pa se ta činjenica koristi u
//					metodi getNotDepth(). Ovo rješenje jedino ne koristi 
//					dodavanje/brisanje čvorova djece, pa je nešto efikasnije.
					NotNode not = new NotNode();
					not.addChild(p);
					nodes.add(not);
				}
				
//				U listu za crtanje se dodaje 
//				1. Otvorena zagrada
//				2. Rekurzivno izračunati izrazi za svako dijete
//				3. Čvor tmp (Poslije svakog djeteta osim zadnjeg)
//				4. Zatvorena zagrada
				nodes.add(new VariableNode("("));
				for (int i = 0; i < ch.size(); i++) {
					goPreorder(ch.get(i));
					if (i != ch.size() - 1)
						nodes.add(p);
				}
				nodes.add(new VariableNode(")"));
			}
		}
	}
	
	public BufferedImage drawFunction() {
		
		BufferedImage slika = prepareImage();
		
		createDrawList();

//		Redom se crtaju svi čvorovi
		for (ParserNode p : nodes) {
			if (p.isOp()) {
				if (p instanceof NotNode) {
					// Za crtanje Not čvora potrebno je odrediti duljinu
					// linije negacije, te Y koordinatu linije.
					p.drawTip1b(g, nowX, 
							insets + notHeight - getNotDepth(p) * symbolSize / 6, 
							getNotLength(p));
				} else {
					p.drawTip1b(g, nowX, symbolSize + insets + notHeight,
							symbolSize);
					nowX += symbolSize + symbolSize / 5;
				}
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
	 * @return	pripremljeni BufferedImage
	 */
	private BufferedImage prepareImage() {
		BufferedImage slika = new BufferedImage(
				1, 1, BufferedImage.TYPE_3BYTE_BGR);
		g = slika.createGraphics();

		Font fNew = new Font("Dialog", Font.PLAIN, 
				getFontSize((int) (symbolSize * 1.381), 0, symbolSize * 10));
		g.setFont(fNew);

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

		picWidth -= 2 * insets;
		nowX = insets;
		return slika;
	}

	/**
	 * Metoda računa duljinu linije za simbol operatora NOT
	 * 
	 * @param 	p	Not čvor za koji se treaži duljina
	 * @return duljinu linije
	 */
	private int getNotLength(ParserNode p) {
		int oldPicWidth = picWidth;
		picWidth = 0;
		calcWidth(p);
		int len = picWidth;
		if (len > oldPicWidth)
			len = oldPicWidth;
		picWidth = oldPicWidth;
		return len - g.getFontMetrics().getMaxAdvance() / 10;
	}

	/**
	 * Metoda vraća koliko najviše ugniježđenih NOT operatora ima u nekom
	 * podstablu. Metoda je potrebna budući da se ugniježđeni NOT-ovi u načinu
	 * prikaza 1b crtaju jedan iznad drugog.
	 * 
	 * @param 	p	Zadano stablo
	 * @return najveći broj ugniježđenih NOT operatora.
	 */
	private int getNotDepth(ParserNode p) {
		if (p == null) return 0;
		int ret = 0;
		
//		Ovim se uvjetom razlikuju Not čvorovi ubačeni umjesto Nor, Nand i Xnor 
//		čvorova, od mogućeg Not čvora na vrhu tree-a. Za takve, ubačene Notove
//		ne smije se povećati dubina jer će se ona povećati kada se naiđe na
//		Nor, Nand ili Xnor
		if (p.isNot() && (p.getParent() != null || p == tree))
			ret++;
		
		List<ParserNode> children = p.getChildren();
		int bestNotDepth = 0;
		
		//Rekurzivno se najveća dubina kod djece
		for (ParserNode child : children) {
			int notDepth = getNotDepth(child);
			if (notDepth > bestNotDepth) bestNotDepth = notDepth;
		}
		
		return bestNotDepth + ret;
	}

	public String getType() {
		return "mat-log-notacija";
	}
}
