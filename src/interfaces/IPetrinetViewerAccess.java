package interfaces;

import java.util.List;

import model.elements.PEdge;
import model.elements.PField;
import model.elements.PLabeledNode;
import model.elements.PTransition;

/**
 * Das Interface gewährt einem {@link IViewer} Zugriff auf ein Petrinetz und stellt
 * Methoden bereit um auf dieses lesend zuzugreifen.
 *
 */
public interface IPetrinetViewerAccess {
	/**
	 * Getter Methode für die Liste der Felder des Petrinetzes
	 * 
	 * @return die Liste der Felder
	 */
	public List<PField> getFieldList();

	/**
	 * Getter Methode für die Liste der Transitionen des Petrinetzes
	 * 
	 * @return die Liste der Transitionen
	 */
	public List<PTransition> getTransitionList();

	/**
	 * Getter Methode für die Liste der Kanten des Petrinetzes
	 * 
	 * @return die Liste der Kanten
	 */
	public List<PEdge> getEdgeList();

	/**
	 * Getter Methode für den ausgewählten Knoten des Petrinetzes
	 * 
	 * @return der ausgewählte Knoten / null wenn keiner gewählt
	 */
	public PLabeledNode getSelectedNode();
}
