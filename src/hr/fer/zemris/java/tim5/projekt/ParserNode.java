package hr.fer.zemris.java.tim5.projekt;

import java.awt.Graphics;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasa koja predstavlja čvorove stabla.
 */
public abstract class ParserNode {

	/**
	 * Lista djece trenutnog čvora
	 */
	protected List<ParserNode> children = new LinkedList<ParserNode>();

	/**
	 * Roditelj trenutnog čvora
	 */
	protected ParserNode parent = null;

	/**
	 * Vraća listu djece zadanog čvora.
	 * 
	 * @return lista djece
	 */
	public List<ParserNode> getChildren() {
		return children;
	}

	/**
	 * Dodaje novo dijete čvoru.
	 * 
	 * @param child
	 *            novo dijete
	 */
	public void addChild(ParserNode child) {
		child.parent = this;
		children.add(child);
	}

	/**
	 * Vraća roditelja zadanog čvora.
	 * 
	 * @return roditelj čvora
	 */
	public ParserNode getParent() {
		return parent;
	}
	
	/**
	 * Postavlja roditelja zadanom čvoru.
	 * 
	 * @param parent roditelj čvora
	 */
	public void setParent(ParserNode parent) {
		this.parent = parent;
	}

	/**
	 * Vraća dubinu podstabla, tj. koliko najviše spojenih potomaka stablo ima
	 * ispod zadanog čvora.
	 * 
	 * @return dubina podstabla zadanog čvora
	 */
	public int getHeight() {
		int maxHeight = 0;

		for (ParserNode child : this.getChildren()) {
			int childHeight = child.getHeight();

			if (childHeight > maxHeight) {
				maxHeight = childHeight;
			}
		}

		return maxHeight + 1;
	}

	/**
	 * Vraća širinu podstabla, tj. broj listova zadanog podstabla
	 * 
	 * @return širina podstabla zadanog čvora
	 */
	public int getWidth() {

		if (this.getChildren().size() == 0) {
			return 1;
		}

		int width = 0;

		for (ParserNode child : this.getChildren()) {
			width += child.getWidth();
		}

		return width;
	}

	/**
	 * Metoda koja vraća ime čvora
	 * 
	 * @return ime
	 */
	public String getName() {
		return null;
	}

	/**
	 * Metoda koja iscrtava sklop u obliku za tip2 prikaz.
	 */
	public void drawTip2(Graphics g, int x, int y, int symbolSize) {
		return;
	}

	/**
	 * Metoda koja iscrtava sklop u obliku za tip1a prikaz.
	 */
	public void drawTip1a(Graphics g, int x, int y, int symbolSize) {
		return;
	}
	
	/**
	 * Metoda koja iscrtava sklop u obliku za tip1b prikaz.
	 */
	public void drawTip1b(Graphics g, int x, int y, int symbolSize) {
		return;
	}

	/**
	 * Vraća je li ili nije korišteni razred operator.
	 * 
	 * @return Boolean vrijednosti je li razred operator.
	 */
	public boolean isOp() {
		return false;
	}

	/**
	 * Vraća je li ili nije korišteni razred varijabla.
	 * 
	 * @return Boolean vrijednosti je li razred varijabla.
	 */
	public boolean isVar() {
		return false;
	}

	/**
	 * Vraća je li korišteni razred operator s negiranim ulazom.
	 * 
	 * @return True ako je node operator koji ima negirani izlaz.
	 */
	public boolean isNot() {
		return false;
	}

	/**
	 * Okreće stablo u odnosu na x-os.
	 */
	public void okreniStablo() {
		Collections.reverse(this.children);
	}
	
	/**
	 * Metoda provjerava jesu li dva stabla jednaka
	 * @param obj objekt s kojim se uspoređuje 
	 * @return true ako su stabla ista, inače false
	 */
	@Override
	public boolean equals(Object obj){
		ParserNode node = null;
		try {
			node = (ParserNode)obj;
		} catch (ClassCastException nijeDobarObjekt) {
			return false;
		}
		return matches(this,node);
	}

	/**
	 * Rekurzivna metoda koja provjerava jesu li dva stabla jednaka. 
	 * Prati sljedeči pseudokod:
	 * 
	 * matches(root1, root2)
	 * | false if root1 != root2
	 * | for each child1 of root1 and corresponding child2 of root2
	 * | | false if matches(child1,child2) is false
	 * | if no falses then true
	 * 
	 * @param node1 prvo stablo
	 * @param node2 drugo stablo
	 * @return true ako su stabla ista, inače false
	 */
	private boolean matches(ParserNode node1, ParserNode node2) {
		
		if (!node1.getName().equals(node2.getName())){
			return false;
		}
		
		List<ParserNode> ChildrenList1 = node1.getChildren();
		
		for (ParserNode child1 : ChildrenList1){
			
			List<ParserNode> ChildrenList2 = node2.getChildren();
			
			for (ParserNode child2 : ChildrenList2){
				if (!matches(child1,child2)){
					return false;
				}
			}
			
		}
		return true;
	}
}
