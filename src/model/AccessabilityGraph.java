package model;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import model.elements.PAccEdge;
import model.elements.PMarking;

/**
 * Die Klasse AccesabilityGraph implementiert das Model eines
 * Erreichbarkeitsgraphen. Enthält eine Liste der Markierungen und der Kanten
 * zwischen ihnen, sowie Metadaten, welche die aktuelle Markierung und
 * gegebenenfalls M, M' sowie den Pfad vom Anfansknoten nach M' beschreibt.
 *
 */

public class AccessabilityGraph {

	private List<PMarking> listOfMarkings;
	private List<PAccEdge> edgeList;

	private boolean isLimited;

	private AccGraphMetaData metaData;

	/**
	 * Konstruktor
	 */
	public AccessabilityGraph() {
		resetAcessabilityGraph();
	}

	/**
	 * Setzt den Erreichbarkeitsgraphen auf den Zustand der Initialisierung zurück
	 */
	public void resetAcessabilityGraph() {
		this.isLimited = true;
		this.listOfMarkings = new ArrayList<PMarking>();
		this.edgeList = new ArrayList<>();
		this.metaData = new AccGraphMetaData();
	}

	/**
	 * Setter Methode für isValid. Dieser Parameter bestimmt ob der
	 * Erreichbarkeitsgraph beschränkt ist.
	 * 
	 * @param b ist der Erreichbarkeitsgraph beschränkt?
	 */
	public void setIsLimited(boolean b) {
		isLimited = b;
	}

	/**
	 * Getter Methode für isValid. Dieser Parameter bestimmt ob der
	 * Erreichbarkeitsgraph beschränkt ist.
	 * 
	 * @return ist der Erreichbarkeitsgraph beschränkt?
	 */
	public boolean getIsValid() {
		return isLimited;
	}

	/**
	 * Getter Methode für die Liste der Markierungen des Erreichbarkeitsgraphen
	 * 
	 * @return die Liste der Markierungen
	 */
	public List<PMarking> getListOfMarkings() {
		return listOfMarkings;
	}

	/**
	 * Fügt der Liste der Markierungen eine Markierung hinzu
	 * 
	 * @param m die hinzuzufügende Markierung
	 */
	public void addMarkingToListOfMarkings(PMarking m) {
		listOfMarkings.add(m);
	}

	/**
	 * Getter Methode für die Liste der Kanten des Erreichbarkeitsgraphen
	 * 
	 * @return die Liste der Kanten
	 */
	public List<PAccEdge> getEdgeList() {
		return edgeList;
	}

	/**
	 * Fügt der Liste der Kanten eine Kante hinzu
	 * 
	 * @param e die hinzuzufügende Kante
	 */
	public void addEdgeToListOfEdges(PAccEdge e) {
		edgeList.add(e);
	}

	/**
	 * Stellt die standardisierte Textform der Information des
	 * Erreichbarkeitsgraphen bzgl. seiner Unbeschränktheit bereit. 'Knoten/Kanten',
	 * falls beschränkt. '(Länge des Pfades : (Pfad); M, M' ', falls unbeschränkt
	 * 
	 * @return die Textform der Informationen
	 */
	public String getValidityRepresentation() {
		if (isLimited) {
			return (listOfMarkings.size()) + " / " + (edgeList.size());
		} else {
			String invalidPathrepresentation = writeInvalidPathRepresentation();
			Formatter formatter = new Formatter(Locale.GERMANY);
			String s = formatter.format("%1$-20s %2$-17s %3$-15s", invalidPathrepresentation,
					(metaData.getInvalidMId() + ","), metaData.getInvalidMStrokeId()).toString();
			formatter.close();
			return s;
		}
	}

	private String writeInvalidPathRepresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append(metaData.getInvalidPath().size());
		sb.append(":(");
		for (int i = metaData.getInvalidPath().size() - 1; i >= 0; i--) {
			sb.append(metaData.getInvalidPath().get(i).getTransition().getId());
			if (i > 0) {
				sb.append(",");
			} else {
				sb.append("); ");
			}
		}
		return sb.toString();
	}

	/**
	 * Getter Methode Für die Meta Daten des Erreichbarkeits-Graphen
	 * 
	 * @return die Meta Daten
	 */
	protected AccGraphMetaData getMetaData() {
		return this.metaData;
	}

}
