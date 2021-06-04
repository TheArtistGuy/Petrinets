package model.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse zur implementierung einer Markierung in einem Erreichbarkeitsgraphen.
 *
 */

public class PMarking extends PLabeledNode {
	private int[] markingDistribution;
	private List<PAccEdge> adjacencyListOfMarkingsFollowing;

	/**
	 * Konstruktor
	 * 
	 * @param fieldList Die Liste der Felder, deren Zustand der Markierung
	 *                  entspricht.
	 */
	public PMarking(List<PField> fieldList) {
		super(createStringRepresentation(fieldList));
		initialiseMarkingDistribution(fieldList);
		adjacencyListOfMarkingsFollowing = new ArrayList<PAccEdge>();
	}

	private void initialiseMarkingDistribution(List<PField> fieldList) {
		if (fieldList != null) {
			this.markingDistribution = new int[fieldList.size()];
			for (int i = 0; i < fieldList.size(); i++) {
				markingDistribution[i] = (int) fieldList.get(i).getTokens();
			}
		}
	}

	/**
	 * Getter Methode für die Liste der ausgehenden Kanten.
	 * 
	 * @return Die Liste der ausgehenden KAnten
	 */
	public List<PAccEdge> getListOfOutgoingEdges() {
		return adjacencyListOfMarkingsFollowing;
	}

	/**
	 * Methode um der Liste der ausgehenden Kanten eine Kante hinzuzufügen.
	 * 
	 * @param m Die hinzuzufügende Kante
	 */
	public void addEdgeToListOfOutgoingEdges(PAccEdge m) {
		adjacencyListOfMarkingsFollowing.add(m);
	}

	/**
	 * Methode um die Marken der einzelnen Felder dieser Markierung zu erhalten
	 * 
	 * @return ein Array mit den Marken der Felder
	 */
	public int[] getTokenDistribution() {
		return markingDistribution;
	}

	/**
	 * Methode um zu bestimmen ob alle Felder dieser und einer anderen Markierung
	 * genau gleich viele Marken haben. Diese also gleich sind.
	 * 
	 * @param marking Die Markierung mit der verglichen werden soll
	 * @return Sind die Markierungen gleich?
	 */
	public boolean equalsInDistributionOfMarks(PMarking marking) {
		// System.out.println("Abfrage : " + this.getId() + " == " + marking.getId());
		return this.getId().equals(marking.getId());
	}

	/**
	 * Bestimmt ob in einer Stelle der Markierung m' jeder Stelle genauso viele
	 * Marken zugewiesen wurde und mindestens einer Stelle mehr Marken zugewiesen
	 * wurden als in dieser Markierung m. Und somit die Regel der Beschränktheit
	 * verletzt wird.
	 * 
	 * @param marking m'
	 * @return wurde in m' mindestens einer Stelle eine Marke mehr zugewiesen.
	 */

	public boolean violatesRuleOfLimitation(PMarking marking) {
		boolean allFieldsHaveEqualOrMoreMarks = true;
		for (int i = 0; i < markingDistribution.length; i++) {
			if (markingDistribution[i] < marking.getTokenDistribution()[i]) {
				allFieldsHaveEqualOrMoreMarks = false;
			}
		}
		if (allFieldsHaveEqualOrMoreMarks) {
			for (int i = 0; i < markingDistribution.length; i++) {
				if (marking.getTokenDistribution()[i] > markingDistribution[i]) {
					return true;
				}
			}
		}
		return false;
	}

	private static String createStringRepresentation(List<PField> fieldList) {
		if (fieldList != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			for (int i = 0; i < fieldList.size(); i++) {
				sb.append(fieldList.get(i).getTokens());
				if (i != fieldList.size() - 1) {
					sb.append("|");
				}
			}
			sb.append(")");
			return sb.toString();
		}
		return "";
	}

}
