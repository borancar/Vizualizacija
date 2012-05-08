package hr.fer.zemris.java.tim5.projekt.Drawer.tip3;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.tim5.projekt.ParserNode;
import hr.fer.zemris.java.tim5.projekt.nodes.OperatorNode;

/**
 * Evaluator logičke funkcije
 */
public class Evaluator {
	
	/** Stablo logičke funkcije za evaluaciju */
	private ParserNode tree;
	
	/** Lista sa svim varijablama */
	private List<String> variables;
	
	/** Lista mapâ sa svim rezultatima funkcije */
	private List<Map<String,Boolean>> allResults;
	
	/**
	 * Konstruktor
	 * 
	 * @param	tree	Stablo logičke funkcije
	 */
	public Evaluator(ParserNode tree) {
		this.tree = tree;
		
		variables = new ArrayList<String>();
		findVariables(tree);
		Collections.sort(variables);
		variables.remove("1");
		variables.remove("0");
	}
	
	/**
	 * Metoda rekurzivno traži sve varijable u stablu i stavlja ih u listu
	 * variables
	 * 
	 * @param 	node	Stablo logičke funkcije.
	 */
	private void findVariables(ParserNode node) {
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
	 * Metoda vraća listu svih varijabli funkcije nad kojom je
	 * stvoren evaluator.
	 * 
	 * @return	listu svih varijabli.
	 */
	public List<String> getVariables() {
		return variables;
	}
	
	/**
	 * Metoda računa vrijednost funkcije zadane preko stabla. Također, zadane
	 * su i vrijednosti svih varijabli u toj funkciji.
	 * 
	 * @param 	node	Stablo Booleove funkcije.
	 * @param 	values	Mapa sa boolean vrijednostima svih varijabli funkcije 
	 * @return	boolean rezultat funkcije.
	 */
	private boolean evaluate(ParserNode node, Map <String,Boolean> values) {
		if (node.isVar()) {
			if (node.getName().equals("1")) {
				return true;
			} else if (node.getName().equals("0")) {
				return false;
			} else if (values.get(node.getName()) == null) {
				throw new IllegalArgumentException(
						"Nije zadana vrijednost varijable " + node.getName());
			} else {
				return values.get(node.getName());
			}
		} else {
			boolean[] results = new boolean[node.getChildren().size()];
			List<ParserNode> children = node.getChildren();
			for (int i=0; i<children.size(); i++) {
				results[i] = evaluate(children.get(i), values);
			}
			return ((OperatorNode) node).calculate(results);
		}
	}
	
	/**
	 * Metoda vraća listu rezultata za sve kombinacije vrijednosti varijabli.
	 * 
	 * @return listu rezultata.
	 */
	public List<Map<String,Boolean>> evaluateAll() {
		allResults = new ArrayList<Map<String,Boolean>>();
		buildResults(0, new HashMap<String,Boolean>(this.getVariables().size()));
		return allResults;
	}

	/**
	 * Rekurzivna metoda koja generira sve kombinacije vrijednosti 
	 * varijabli logičke funkcije, te ih zajedno sa rezultatom sprema u 
	 * mapu svih rezultata.
	 * 
	 * @param 	currentVar	redni broj trenutne varijable
	 * @param 	values	mapa sa trenutnim vrijednostima varijabli
	 */
	private void buildResults(int currentVar, Map<String, Boolean> values) {
		if (currentVar < this.getVariables().size()) {
			values.put(this.getVariables().get(currentVar), 
					Boolean.valueOf(false));
			buildResults(currentVar+1, values);
			
			values.put(this.getVariables().get(currentVar), 
					Boolean.valueOf(true));
			buildResults(currentVar+1, values);
		} else {
			values.put("_RESULT", evaluate(tree, values));
			allResults.add(new HashMap<String, Boolean>(values));
		}
	}

}
