package hr.fer.zemris.java.tim5.projekt.Drawer.tip2;

import java.util.List;

import hr.fer.zemris.java.tim5.projekt.ParserNode;

/**
 * Klasa za računanje i obradu podataka potrebnih za prikaz tipa 2.
 */
public class GraphCalc {

	/**
	 * Metoda računa širinu stabla izraženu u pikselima. Širina stabla je zbroj
	 * širina svih razina. Jedna razina se sastoji od simbola, te od linija
	 * ulaza i izlaza. Simbol je širok symbolSize, a linija ulaza i izlaza svaka
	 * po symbolSize/2.
	 * 
	 * @param node
	 *            zadano sablo
	 * @param symbolSize
	 *            dimenzija simbola
	 * @return širinu stabla.
	 */
	public static int getTreeWidth(ParserNode node, int symbolSize) {
		return (node.getHeight() - 1) * (symbolSize * 2);
	}

	/**
	 * Metoda računa visinu stabla u pikselima. Visina stabla je zbroj visina
	 * njegovih podstabala, te razmaka između tih podstabala.
	 * 
	 * @param node
	 *            zadano sablo
	 * @param symbolSize
	 *            dimenzija simbola
	 * @return visinu stabla.
	 */
	public static int getTreeHeight(ParserNode node, int symbolSize) {
		if (node.isVar())
			return 0;

		int height = 0;

		List<ParserNode> children = node.getChildren();
		int childrenSize = children.size();
		if (childrenSize == 1) {
			// Ako čvor ima samo jedno dijete, stablo je visoko koliko je
			// visoko i podstablo tog djeteta
			height = getTreeHeight(node.getChildren().get(0), symbolSize);
		} else {
			// visina stabla jednaka je zbroju visina podstabala

			// gornja podstabla
			for (int i = 0; i < childrenSize / 2; i++) {
				height += getTreeHeight(children.get(i), symbolSize);
				// ako je i-to dijete operacija, onda ona ima svoje podstablo
				// pa je potrebno dodati još i razmak do sljedeceg podstabla
				if (children.get(i).isOp())
					height += symbolSize;
			}
			// gornja podstabla mogu biti barem symbolSize široka, zato jer je
			// sam sklop širok toliko
			if (height == 0)
				height = symbolSize;

			// flag koji pokazuje je li dodan jedan symbolSize previse
			boolean exceeds = false;
			// donja podstabla
			for (int i = childrenSize / 2; i < childrenSize; i++) {
				height += getTreeHeight(children.get(i), symbolSize);
				if (children.get(i).isOp()) {
					// ako je i-to dijete operacija, onda ona ima svoje
					// podstablo
					// pa je potrebno dodati još i razmak do sljedeceg podstabla
					// Prvo dodavanje symbolSizea je višak, pa se postavlja flag
					height += symbolSize;
					exceeds = true;
				}
			}
			if (exceeds)
				height -= symbolSize;
		}
		// podstablo operacije može biti visoko najmanje symbolSizes
		return symbolSize > height ? symbolSize : height;
	}

	/**
	 * Metoda racuna visinu lijevih podstabala zadanog stabla. Lijeva podstabla
	 * su ona podstabla koja se spajaju na ulaze s indexima od 0 do
	 * brojUlaza/2-1, a na slici se nalaze korijena.
	 * 
	 * @param node
	 *            zadano sablo
	 * @param symbolSize
	 *            dimenzija simbola
	 * @return visinu lijevog podstabla
	 */
	public static int getTreeHeightLeft(ParserNode node, int symbolSize) {
		// Lijeva visina varijable je 0, a jednog samostalnog sklopa
		// symbolSize/2
		if (node.getHeight() == 1)
			return 0;
		if (node.getHeight() == 2)
			return symbolSize / 2;

		List<ParserNode> children = node.getChildren();
		int childrenSize = children.size();

		if (childrenSize == 1) {
			// Lijeva visina čvora sa jednim djetetom jednaka je
			// lijevoj visini djeteta
			return getTreeHeightLeft(node.getChildren().get(0), symbolSize);
		}

		// Lijeva visina čvora sa više djece je zbroj visina lijevih čvorova
		int height = 0;
		for (int i = 0; i < childrenSize / 2; i++) {
			height += getTreeHeight(children.get(i), symbolSize);
			if (children.get(i).isOp())
				height += symbolSize;
		}
		// Dodan je jedan symbolSize previše, ali još treba dodati polovicu
		// symbolSizea (gornja polovica korijena)
		height -= symbolSize / 2;
		// Lijeva visina operacije je najmanje symbolSize/2
		return (height < symbolSize / 2) ? symbolSize / 2 : height;
	}

	/**
	 * Metoda racuna visinu desnog podstabla zadanog stabla. Desna podstabla su
	 * ona podstabla koja se spajaju na ulaze s indexima od brojUlaza/2 do
	 * brojUlaza, a na slici se nalaze ispod korijena.
	 * 
	 * @param node
	 *            zadano sablo
	 * @param symbolSize
	 *            dimenzija simbola
	 * @return visinu desnog podstabla
	 */
	public static int getTreeHeightRight(ParserNode node, int symbolSize) {
		// Desna visina varijable je 0, a jednog samostalnog sklopa symbolSize/2
		if (node.getHeight() == 1)
			return 0;
		if (node.getHeight() == 2)
			return symbolSize / 2;

		List<ParserNode> children = node.getChildren();
		int childrenSize = children.size();
		if (childrenSize == 1) {
			// Desna visina čvora sa jednim djetetom jednaka je
			// desnoj visini djeteta
			return getTreeHeightRight(node.getChildren().get(0), symbolSize);
		}
		// Desna visina čvora sa više djece je zbroj visina desnih čvorova
		int height = 0;
		for (int i = childrenSize / 2; i < childrenSize; i++) {
			height += getTreeHeight(children.get(i), symbolSize);
			if (children.get(i).isOp())
				height += symbolSize;
		}
		// Dodan je jedan symbolSize previše, ali još treba dodati polovicu
		// symbolSizea (gornja polovica korijena)
		height -= symbolSize / 2;
		// Desna visina operacije je najmanje symbolSize/2
		return (height < symbolSize / 2) ? symbolSize / 2 : height;
	}

	/**
	 * Metoda računa relativnu vertikalnu udaljenost između linije izlaza iz
	 * podstabla nekog djeteta zadanog čvora i linije ulaza tog djeteta u zadani
	 * čvor. Relativna udaljenost je broj (pozitivan ili negativan) koji trebamo
	 * dodati y koordinati ulaza u sklop da bi dobili y koordinatu izlaza iz
	 * čvora djeteta.
	 * 
	 * @param node
	 *            zadani čvor
	 * @param childIndex
	 *            indeks djeteta do kojeg se traži vertikalna udaljenost
	 * @return Relativna vertikala daljenost između izlaza djeteta i ulaza
	 *         roditelja na koji je taj izlaz spojen.
	 */
	public static int getVerticalDistance(ParserNode node, int childIndex,
			int symbolSize) {

		List<ParserNode> children = node.getChildren();
		int childrenSize = children.size();

		// vertikalna udaljenost najnižih sklopova i čvorova sa samo jednim
		// djetetom je 0
		if (node.getHeight() <= 2 || childrenSize == 1)
			return 0;

		int height = 0;
		if (childIndex < childrenSize / 2) {
			// dijete se nalazi u lijevim(gornjim) podstablima, pa je relativna
			// udaljenost negativna
			for (int i = childIndex + 1; i < childrenSize / 2; i++) {
				// na relativnu udaljenost se dodaju visine svih podstabala od
				// i+1 do srednjeg
				height -= getTreeHeight(children.get(i), symbolSize);
				// Ako je i-to dijete operacija, još je potrebno dodati razmak
				// između podstabala
				if (children.get(i).isOp())
					height -= symbolSize;
			}
			// na relativnu udaljenost se dodaje desna visina podstabla djeteta
			height -= getTreeHeightRight(children.get(childIndex), symbolSize);
			// na relativnu udaljenost se dodaje razmak od gornjeg ruba zadanog
			// čvora do linije ulaza zadanog djeteta
			height -= (childIndex + 1) * symbolSize / (childrenSize + 1);
		} else {
			// dijete se nalazi u desnim (donjim) podstablima, pa je relativna
			// udaljenost pozitivna
			for (int i = childIndex - 1; i >= childrenSize / 2; i--) {
				// na relativnu udaljenost se dodaju visine svih podstabala od
				// i-1 do srednjeg
				height += getTreeHeight(children.get(i), symbolSize);
				// Ako je i-to dijete operacija, još je potrebno dodati razmak
				// između podstabala
				if (children.get(i).isOp())
					height += symbolSize;
			}
			// na relativnu udaljenost se dodaje lijeva visina podstabla djeteta
			height += getTreeHeightLeft(children.get(childIndex), symbolSize);
			// na relativnu udaljenost se dodaje razmak od donjeg ruba zadanog
			// čvora do linije ulaza zadanog djeteta
			height += (childrenSize - childIndex) * symbolSize
					/ (childrenSize + 1);
		}
		return height;
	}

	/**
	 * Računanje koordinata svih ulaza sklopa.
	 * 
	 * @param upperLeft
	 *            Koordinatana gornjeg lijevog kuta sklopa.
	 * @param childrenSize
	 *            Broj ulaza sklopa.
	 * @param symbolSize
	 *            Visina simbola sklopa
	 * @return Koordinate ulaza sklopa.
	 */
	public static Koordinata[] getUlazi(Koordinata upperLeft, int childrenSize,
			int symbolSize) {
		Koordinata[] ret = new Koordinata[childrenSize];
		int diff = symbolSize / (childrenSize + 1);
		int y = upperLeft.y + diff;
		for (int i = 0; i < childrenSize; i++) {
			ret[i] = new Koordinata(upperLeft.x, y);
			y += diff;
		}
		return ret;
	}
}
