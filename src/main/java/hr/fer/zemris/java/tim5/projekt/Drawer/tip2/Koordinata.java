package hr.fer.zemris.java.tim5.projekt.Drawer.tip2;

import java.awt.Point;

/**
 * Klasa container koordinate. (prilagođena Point klasa potrebama crtača tip2).
 * 
 * Obrati pažnju na metodu Point2D.distance().
 * 
 * @see Point
 */
public class Koordinata extends Point {
	private static final long serialVersionUID = 1L;

	/** Konstruktor preko koordinata x i y */
	public Koordinata(int x, int y) {
		super(x, y);
	}

	/** Konstruktor preko koordinate u objektu Point ili Koordinata */
	public Koordinata(Point p) {
		super(p);
	}

	/**
	 * Računanje vertikalne udaljenosti između 2 koordinate.
	 * 
	 * @param x
	 *            x vrijednost druge koordinate (zanemariva u ovom slučaju,
	 *            služi samo radi formata).
	 * @param y
	 *            y vrijednost druge koordinate.
	 * @return Vertikalna udaljenost između 2 koordinate.
	 */
	public int vertikalnaUdaljenost(int x, int y) {
		return this.y - y;
	}

	/**
	 * Računanje vertikalne udaljenosti između 2 koordinate.
	 * 
	 * @param y
	 *            y vrijednost druge koordinate.
	 * @return Vertikalna udaljenost između 2 koordinate.
	 */
	public int vertikalnaUdaljenost(int y) {
		return this.y - y;
	}

	/**
	 * Računanje vertikalne udaljenosti između 2 koordinate.
	 * 
	 * @param drugaKoordinata
	 *            Objekt (Point ili Koordinata) koji sadrži drugu koordinatu.
	 * @return Vertikalna udaljenost između 2 koordinate.
	 */
	public int vertikalnaUdaljenost(Point drugaKoordinata) {
		return this.y - drugaKoordinata.y;
	}

	/**
	 * Računanje vertikalne udaljenosti između 2 koordinate. Static metoda!
	 * 
	 * @param x1
	 *            x vrijednost prve koordinate (zanemariva u ovom slučaju, služi
	 *            samo radi formata).
	 * @param y1
	 *            y vrijednost prve koordinate.
	 * @param x2
	 *            x vrijednost druge koordinate (zanemariva u ovom slučaju,
	 *            služi samo radi formata).
	 * @param y2
	 *            y vrijednost druge koordinate.
	 * @return Vertikalna udaljenost između 2 koordinate.
	 */
	public static int vertikalnaUdaljenost(int x1, int y1, int x2, int y2) {
		return y1 - y2;
	}

	/**
	 * Računanje vertikalne udaljenosti između 2 koordinate. Static metoda!
	 * 
	 * @param koordinata1
	 *            Objekt (Point ili Koordinata) koji sadrži prvu koordinatu.
	 * @param koordinata2
	 *            Objekt (Point ili Koordinata) koji sadrži drugu koordinatu.
	 * @return Vertikalna udaljenost između 2 koordinate.
	 */
	public static int vertikalnaUdaljenost(Point koordinata1, Point koordinata2) {
		return koordinata1.y - koordinata2.y;
	}

	/**
	 * Računanje horizontalne udaljenosti između 2 koordinate.
	 * 
	 * @param x
	 *            x vrijednost druge koordinate.
	 * @param y
	 *            y vrijednost druge koordinate.
	 * @return Horizontalna udaljenost između 2 koordinate.
	 */
	public int horizontalnaUdaljenost(int x, int y) {
		return this.x - x;
	}

	/**
	 * Računanje horizontalne udaljenosti između 2 koordinate.
	 * 
	 * @param x
	 *            x vrijednost druge koordinate.
	 * @return Horizontalna udaljenost između 2 koordinate.
	 */
	public int horizontalnaUdaljenost(int x) {
		return this.x - x;
	}

	/**
	 * Računanje horizontalne udaljenosti između 2 koordinate.
	 * 
	 * @param drugaKoordinata
	 *            Objekt (Point ili Koordinata) koji sadrži drugu koordinatu.
	 * @return Horizontalna udaljenost između 2 koordinate.
	 */
	public int horizontalnaUdaljenost(Point drugaKoordinata) {
		return this.x - drugaKoordinata.x;
	}

	/**
	 * Računanje horizontalne udaljenosti između 2 koordinate. Static metoda!
	 * 
	 * @param x1
	 *            x vrijednost prve koordinate.
	 * @param y1
	 *            y vrijednost prve koordinate (zanemariva u ovom slučaju, služi
	 *            samo radi formata).
	 * @param x2
	 *            x vrijednost druge koordinate.
	 * @param y2
	 *            y vrijednost druge koordinate (zanemariva u ovom slučaju,
	 *            služi samo radi formata).
	 * @return Horizontalna udaljenost između 2 koordinate.
	 */
	public static int horizontalnaUdaljenost(int x1, int y1, int x2, int y2) {
		return x1 - x2;
	}

	/**
	 * Računanje horizontalne udaljenosti između 2 koordinate. Static metoda!
	 * 
	 * @param koordinata1
	 *            Objekt (Point ili Koordinata) koji sadrži prvu koordinatu.
	 * @param koordinata2
	 *            Objekt (Point ili Koordinata) koji sadrži drugu koordinatu.
	 * @return Horizontalna udaljenost između 2 koordinate.
	 */
	public static int horizontalnaUdaljenost(Point koordinata1,
			Point koordinata2) {
		return koordinata1.x - koordinata2.x;
	}

	/**
	 * Override metode toString(). Ispisuje koordinatu po formatu: (x,y)
	 */
	@Override
	public String toString() {
		return ("(" + this.x + "," + this.y + ")");
	}
}
