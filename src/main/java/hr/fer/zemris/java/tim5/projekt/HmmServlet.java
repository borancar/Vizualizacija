package hr.fer.zemris.java.tim5.projekt;

import hr.fer.zemris.java.tim5.projekt.Drawer.Drawer;
import hr.fer.zemris.java.tim5.projekt.Drawer.tip1a.ExpDrawerA;
import hr.fer.zemris.java.tim5.projekt.Drawer.tip1b.ExpDrawerB;
import hr.fer.zemris.java.tim5.projekt.Drawer.tip2.SquareDrawer;
import hr.fer.zemris.java.tim5.projekt.Drawer.tip3.TableDrawer;
import hr.fer.zemris.java.tim5.projekt.parser.InfixParser;
import hr.fer.zemris.java.tim5.projekt.parser.PrefixParser;
import hr.fer.zemris.java.tim5.projekt.parser.Tokenizer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet za obradu logičkog izraza.
 */
public class HmmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** Mapa crtača. Ključ je naziv crtača. */
	private Map<String, Drawer> crtaci;

	/** Mapa veličine simbola koje su postavljene u web.xml-u. */
	private Map<String, Integer> parametriServleta;

	/**
	 * Metoda za primanje zahtjeva i parametara preko GET metode. Samo
	 * prosljeđuje zahtjev i parametre metodi obradi zahtjev.
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		obradiZahtjev(request, response);
	}

	/**
	 * Metoda za primanje zahtjeva i parametara preko POST metode. Samo
	 * prosljeđuje zahtjev i parametre metodi obradi zahtjev.
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		obradiZahtjev(request, response);
	}

	/**
	 * Obrada parametara i ispisivanje slike.
	 * 
	 * @param request
	 *            HTTP Request sa parametrima.
	 * @param response
	 *            HTTP Response za prosljeđivanje zahtjeva.
	 * @throws IOException
	 *             Ukoliko dođe do greške prilikom pisanja u ServletOutpuStream
	 */
	private void obradiZahtjev(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// Output stream servleta za iscrtavanje slike
		ServletOutputStream out = response.getOutputStream();

		response.setContentType("image/png");

		Parametri parametri = new Parametri(request);

		// Provjera parametara
		if (parametri.losiParametri(out)) {
			return;
		}

		Drawer crtac = this.crtaci.get(parametri.tipCrtaca);

		try {
			List<String> variables = new LinkedList<String>();
			ParserNode root = postaviKorijen(parametri, out);
			findVariables(root, variables);
			int maxVarijabli = this.parametriServleta.get("maxVarijabli")
					.intValue();
			if ((maxVarijabli != 0) && (variables.size() > maxVarijabli)) {
				throw new IllegalArgumentException(
						"Broj varijabli ne smije biti veći od " + maxVarijabli
								+ "!");
			}
			crtac.setTreeRoot(root);
		} catch (Exception e) {
			generirajSlikuGreske(e.getMessage(), out);
			return;
		}

		// Postavljanje veličine simbola (ukoliko su zadani preko
		// web.xml-a).
		postaviSymbolSize(crtac, parametri);

		BufferedImage slika = crtac.drawFunction();
		ImageIO.write(slika, "png", out);
		out.flush();
		out.close();
	}

	/**
	 * Postavljanje symbolSize-a crtaču iz web.xml-a. Ako je u web.xml-u
	 * postavljen parametar "nazivCrtaca-SymbolSize". Ukoliko se taj parametar
	 * nije nalazio u web.xml-u, ostavlja se defaultna vrijednost.
	 * 
	 * @param crtac
	 *            Crtač kojem se postavlja symbolSize.
	 * @param parametri
	 *            Objekt sa parametrima potrebnim za rad servleta.
	 */
	private void postaviSymbolSize(Drawer crtac, Parametri parametri) {
		if (this.parametriServleta.containsKey(parametri.tipCrtaca
				+ "-symbolSize")) {
			crtac.setSymbolSize(this.parametriServleta.get(
					parametri.tipCrtaca + "-symbolSize").intValue());
		}
	}

	/**
	 * Parsiranje izraza i postavljanje korijena stabla izraza.
	 * 
	 * @param parametri
	 *            Objekt sa parametrima potrebnim za rad servleta.
	 * @param out
	 *            Output za ispis slike koju generira servlet.
	 * @return Korijen parsiranog stabla izraza.
	 * @throws ParseException
	 *             Ukoliko dođe do greške prilikom parsiranja.
	 */
	private ParserNode postaviKorijen(Parametri parametri,
			ServletOutputStream out) throws ParseException {
		if (parametri.notacijaLogIzraza.toLowerCase().equals("infix")) {
			return InfixParser.parse(new Tokenizer(new StringReader(
					parametri.logickiIzraz)));
		} else if (parametri.notacijaLogIzraza.toLowerCase().equals("prefix")) {
			return PrefixParser.parse(new Tokenizer(new StringReader(
					parametri.logickiIzraz)));
		} else {
			// Do ovoga nikad neće doći.
			generirajSlikuGreske("Nepostojeći tip parsera", out);
			return null;
		}
	}

	/**
	 * Metoda rekurzivno traži sve varijable u stablu i pohranjuje ih u listu
	 * variables.
	 * 
	 * @param node
	 *            Parse stablo Booleove funkcije.
	 * @param variables
	 *            Lista u koju će se pohraniti sve varijable izraza.
	 */
	private void findVariables(ParserNode node, List<String> variables) {
		if (node.isVar()) {
			if (!variables.contains(node.getName())) {
				variables.add(node.getName());
			}
		} else {
			for (ParserNode child : node.getChildren()) {
				findVariables(child, variables);
			}
		}
	}

	/**
	 * Generiranje slike s opisom greške.
	 * 
	 * @param msg
	 *            Opis greške.
	 * @param out
	 *            Output za ispis slike koju generira servlet.
	 */
	private void generirajSlikuGreske(String msg, ServletOutputStream out) {
		msg = "Greška: " + msg;

		BufferedImage image = new BufferedImage(1, 1,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image.createGraphics();
		g.setFont(new Font("Dialog", Font.PLAIN, 16));
		FontMetrics fm = g.getFontMetrics();
		int sirinaSlike = fm.stringWidth(msg) + 100;
		int visinaSlike = 50;

		image = new BufferedImage(sirinaSlike, visinaSlike,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics gGreska = image.createGraphics();
		gGreska.setColor(Color.WHITE);
		gGreska.setFont(new Font("Dialog", Font.PLAIN, 16));
		gGreska.fillRect(0, 0, sirinaSlike, visinaSlike);

		int xPoz = (sirinaSlike - fm.stringWidth(msg)) / 2;
		int yPoz = (visinaSlike + fm.getAscent()) / 2;

		gGreska.setColor(Color.BLACK);
		gGreska.drawString(msg, xPoz, yPoz);
		gGreska.dispose();

		try {
			ImageIO.write(image, "png", out);
		} catch (IOException zanemarivo) {
		}
	}

	/**
	 * Inicijalizacija servleta. Punjenje potrebnih kolekcija i čitanje
	 * parametara iz web.xml-a.
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		napuniMapuCrtaca();
		napuniMapuParametara(config);

		// 
	}

	/**
	 * Inicijaliziranje mape K:"NazivSklopa-symbolSize", V:SymbolSize (Integer).
	 */
	private void napuniMapuParametara(ServletConfig config) {
		this.parametriServleta = new HashMap<String, Integer>();

		// Čitanje veličina simbola
		Set<String> naziviCrtaca = this.crtaci.keySet();

		String velicinaSimbola;
		for (String crtac : naziviCrtaca) {
			velicinaSimbola = config.getInitParameter(crtac + "-symbolSize");
			try {
				this.parametriServleta.put(crtac + "-symbolSize", new Integer(
						velicinaSimbola));
			} catch (NumberFormatException preskacemo) {
			} catch (NullPointerException preskacemo) {
			}
		}

		// Čitanje maksimalnog broja varijabli (0 = neograničen broj varijabli!)
		try {
			Integer maxBrVariabli = new Integer(config
					.getInitParameter("maxVarijabli"));

			this.parametriServleta.put("maxVarijabli", maxBrVariabli);
		} catch (NumberFormatException preskacemo) {
			this.parametriServleta.put("maxVarijabli", new Integer(0));
		} catch (NullPointerException preskacemo) {
			this.parametriServleta.put("maxVarijabli", new Integer(0));
		}
	}

	/**
	 * Inicijaliziranje mape nazivCrtača-Crtač (npr.
	 * "logicki-sklopovi"-Drawer(od tipa 2)).
	 */
	private void napuniMapuCrtaca() {
		this.crtaci = new HashMap<String, Drawer>();

		// Hardkodirano punjenje mape.
		this.crtaci.put("std-log-notacija", new ExpDrawerA());
		this.crtaci.put("mat-log-notacija", new ExpDrawerB());
		this.crtaci.put("logicki-sklopovi", new SquareDrawer());
		this.crtaci.put("tablicni-prikaz", new TableDrawer());

		/*
		 * Alternativa je učitati sve crtače iz jednog direktorija. (K:
		 * getType()).
		 */
	}

	/**
	 * Klasa za čuvanje svih parametara potrebnih za rad servleta.
	 */
	private class Parametri {
		/** Naziv tipa crtača čiji prikaz želite */
		public String tipCrtaca;

		/** Logički izraz */
		public String logickiIzraz;

		/** Notacija logičkog izraza. (prefix/infix) */
		public String notacijaLogIzraza;

		/**
		 * Konstruktor.
		 * 
		 * @param request
		 *            HTTP Request koji sadrži parametre.
		 * @throws UnsupportedEncodingException
		 *             Ako se pri enkodiranju izraza preda nepostojeći
		 *             encodeing.
		 */
		public Parametri(HttpServletRequest request)
				throws UnsupportedEncodingException {
			this.notacijaLogIzraza = (String) request.getParameter("notacija");
			this.tipCrtaca = (String) request.getParameter("tip");
			this.logickiIzraz = (String) request.getParameter("izraz");

			// Sređivanje izraza
			this.logickiIzraz = URLDecoder.decode(this.logickiIzraz, "UTF-8");
			this.logickiIzraz = this.logickiIzraz.toUpperCase();
		}

		/**
		 * Provjera valjanosti parametara.
		 * 
		 * @param out
		 *            Output za ispis slike koju generira servlet.
		 * @return True ako su parametri loši, inače false.
		 */
		private boolean losiParametri(ServletOutputStream out) {
			// Osnovna provjera parametara
			if ((this.logickiIzraz == null) || this.logickiIzraz.equals("")
					|| (this.notacijaLogIzraza == null)
					|| this.notacijaLogIzraza.equals("")
					|| (this.tipCrtaca == null) || this.tipCrtaca.equals("")) {
				generirajSlikuGreske("Niste zadali sve parametre!", out);
				return true;
			}

			// Provjera crtaca
			if (!crtaci.containsKey(this.tipCrtaca)) {
				generirajSlikuGreske("Nepodržan tip crtača!", out);
				return true;
			}

			// Provjera notacije
			if (!this.notacijaLogIzraza.equals("infix")
					&& !this.notacijaLogIzraza.equals("prefix")) {
				generirajSlikuGreske("Nepodržan tip notacije!", out);
				return true;
			}

			return false;
		}
	}
}
