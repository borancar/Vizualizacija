package hr.fer.zemris.java.tim5.projekt.test;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.Drawer.tip2.SquareDrawer;
import hr.fer.zemris.java.tim5.projekt.nodes.*;

/**
 * Generator testnog primjerka stabla. Služi i junit testovima, a i služi za testiranje
 * prikaza, zato je izdvojen posebno.
 */
public class TestTreeGenerator {
	
	/** Maksimalni broj ulaza koji će se generirati */
	private final int MAX_BROJ_ULAZA = 4;
	
	/** Maksimalni broj varijabli koji će se generirati */
	private final int MAX_BROJ_VARIJABLI = 3;

	/** Korijenski čvor preko kojeg se dolazi do ostalih */
	private ParserNode TestTree;

	/** ime varijable koje se nasumično izvuče iz liste s imenima */
	private String name;

	/** Lista mogućih imena varijabli */
	private List<String> names;

	/** String koji će vratiti Preorder ispis */
	private String printPre;

	/** String kojeg će vratiti Inorder ispis */
	private String printIn;

	/** Broj logičkih razina koje se trebaju generirati */
	private int brRaz;

	/**
	 * Generira random broj od 0 - n
	 * 
	 * @param n
	 * @return random broj
	 */
	private int random(int n) {
		int r = (int) (n * Math.random());
		return r;
	}

	/**
	 * Generira and, or ili not čvor ovisno o random broju
	 * 
	 * @return čvor
	 */
	private ParserNode generate() {
		ParserNode tmpNode = null;
		int tmp = random(8);
		switch (tmp) {
		case 0:
			tmpNode = new AndNode();
			break;
		case 1:
			tmpNode = new OrNode();
			break;
		case 2: 
			tmpNode = new NotNode();
			break;
		case 3:
			tmpNode = new NorNode();
			break;
		case 4:
			tmpNode = new NandNode();
			break;		
		case 5:
			tmpNode = new XorNode();
			break;
		case 6:
			tmpNode = new XnorNode();
			break;	
		case 7:
			name = names.get(random(MAX_BROJ_VARIJABLI));
			tmpNode = new VariableNode(name);
			break;
		}
		return tmpNode;
	}

	/**
	 * Metoda koja vraća broj djece čvora p
	 * 
	 * @param node
	 *            čvor
	 * @return maks. broj djece
	 */
	public int numChildren(ParserNode node) {
		int maxBrUlaza = MAX_BROJ_ULAZA - 2;
		if (node.getName().equals("NOT"))
			return 1;
		else
			return random(maxBrUlaza) + 2;
	}

	/**
	 * Metoda koja stvara djecu zadanom čvoru
	 * 
	 * @param parent
	 *            novopečeni roditelj
	 */
	public void giveBirth(ParserNode parent) {
		ParserNode newNode = null;
		for (int i = 0; i < numChildren(parent); i++) {
			newNode = generate();
			parent.addChild(newNode);
			newNode.setParent(parent);
		}
	}

	/**
	 * Metoda koja dodjeljuje varijable kao djecu zadanom čvoru
	 * 
	 * @param parent
	 *            novopečeni roditelj
	 */
	public void giveBirthVar(ParserNode parent) {
		ParserNode newNode = null;
		for (int i = 0; i < numChildren(parent); i++) {
			name = names.get(random(MAX_BROJ_VARIJABLI));
			newNode = new VariableNode(name);
			parent.addChild(newNode);
			newNode.setParent(parent);
		}
	}

	/**
	 * Javni konstruktor koji obavlja generiranje stabla
	 */
	public TestTreeGenerator() {
		printPre = "";
		printIn = "";
		names = genNames(26);

		TestTree = generate();

		if (TestTree.isOp()) {
			giveBirth(TestTree);
			brRaz = 2 + random(5);
		}
		genTree(TestTree, brRaz);
	}
	/**
	 * Metoda koja generira imena za varijable.
	 * @param numOfVars 
	 * 					Broj varijabli.
	 * @return Lista sa izgeneriranim imenima.
	 */
	private List<String> genNames(int numOfVars) {
		List<String> names = new ArrayList<String>();
		char c = 'A';
		for (int i = 0; i < numOfVars; i++) {
			names.add(new String("" + c));
			c++;
		}
		return names;
	}

	/**
	 * Metoda koja generira stablo do određene random razine
	 * 
	 * @param parent
	 *            roditelj čijoj djeci treba stvoriti djecu i zatim izgenerirati
	 *            njegove unuke i praunuke.
	 * @param trenRaz
	 *            trenutna razina koja se generira
	 */
	private void genTree(ParserNode parent, int trenRaz) {
		if (trenRaz == 1) {
			List<ParserNode> children = parent.getChildren();
			for (ParserNode child : children) {
				if (child.isOp())
					giveBirthVar(child);
			}
		} else {
			List<ParserNode> children = parent.getChildren();
			for (ParserNode child : children) {
				if (child.isOp()) {
					giveBirth(child);
					genTree(child, trenRaz - 1);
				}
			}
		}
	}

	/**
	 * Vraća korijenski čvor izgeneriranog drveta
	 * 
	 * @return piše gore
	 */
	public ParserNode getTree() {
		return TestTree;
	}

	/**
	 * Metoda koja obavlja inorder obilazak stabla
	 * @param p
	 *            korijenski čvor
	 * @Fixed
	 */
	private void goPreorder(ParserNode p) {
		if (p != null) {
			printPre += p.getName();
			List<ParserNode> ch = p.getChildren();
			if (ch.size() == 0 || ch == null)
				return;
			printPre += "(";
			for (int i = 0; i < ch.size(); i++) {
				goPreorder(ch.get(i));
				if (i != ch.size() - 1)
					printPre += ",";
			}
			printPre += ")";
		}
	}

	/**
	 * Metoda koja obavlja inorder obilazak stabla
	 * @param p
	 *            korijenski čvor
	 * @Fixed
	 */
	private void goInorder(ParserNode p) {
		if (p != null) {
			List<ParserNode> ch = p.getChildren();
			if (p.getName().equals("NOT")) {
				printIn += p.getName() + "(";
				if (ch.size() == 0 || ch == null)
					return;
				goInorder(ch.get(0));
				printIn += ")";
			} else {
				if (p.isOp())
					printIn += "(";
				if (p.isVar()) printIn += p.getName();
				dodajRazmak();
				for (int i=0; i<ch.size(); i++) {
					if (!(ch.size() == 0 || ch == null))
						goInorder(ch.get(i));
					dodajRazmak();
					if (i < ch.size() - 1) {
						printIn += p.getName();
						dodajRazmak();
					}
				}
				if (p.isOp())
					printIn += ")";
			}
		}
	}

	/**
	 * Metoda stavlja razmak na kraj stringa koji predstavlja
	 * inorder obilazak, naravno, samo ako to treba napraviti.
	 */
	private void dodajRazmak() {
		if (printIn.length() > 0
				&& (printIn.charAt(printIn.length() - 1) != ' ')
				&& (printIn.charAt(printIn.length() - 1) != '(')
				&& (printIn.charAt(printIn.length() - 1) != ')'))
			printIn += " ";
	}

	/**
	 * Metoda koja vraća Inorder ispis
	 * 
	 * @return gore piše
	 */
	public String printInorder() {
		goInorder(TestTree);
		return printIn;
	}

	/**
	 * Metoda koja vraća Preorder ispis
	 * 
	 * @return gore piše
	 */
	public String printPreorder() {
		goPreorder(TestTree);
		return printPre;
	}

	/**
	 * Main metoda u svrhu testiranja generatora koji služi za testiranje :))
	 * 
	 * @param args
	 *            nema potrebe za njima ali eclipse ne prepoznaje main metodu
	 *            bez njih...
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		TestTreeGenerator g = new TestTreeGenerator();
		System.out.println(g.printInorder());
		System.out.println(g.printPreorder());
		SquareDrawer crt = new SquareDrawer(g.getTree(),60);
		BufferedImage img = crt.drawFunction();
		File f = new File("C:\\img.png");
		ImageIO.write(img, "png", f);
		
	}
}
