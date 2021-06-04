package model;

import java.util.ArrayList;
import java.util.List;

import interfaces.IAGMetaDataViewerAcces;
import model.elements.PAccEdge;

/**
 * Klasse die MetaDaten des Erreichbarkeitsgraphen enthält. Diese sind Die
 * durch die Beschränktheitsprüfung ermittelten M und M', sowie der Pfad
 * zwischen ihnen und die zuletzt durch die Schaltung einer Transition erzeugten
 * Kante.
 * 
 */

public class AccGraphMetaData implements IAGMetaDataViewerAcces {

	private String invalidMId;
	private String invalidMStrokeId;

	private List<PAccEdge> invalidPath;
	private String lastAddedEdgeId;

	/**
	 * Konstruktor
	 */
	public AccGraphMetaData() {
		this.invalidPath = new ArrayList<>();
	}

	/**
	 * Getter Methode für die Markierung M, das durch die
	 * Beschänktheitsprüfungermittelt wurde
	 * 
	 * @return die Markierung M
	 */
	@Override
    public String getInvalidMId() {
		if (invalidMId == null) {
			return "";
		} else {
			return invalidMId;
		}
	}

	/**
	 * Setter Methode für die Markierung M, das durch die
	 * Beschänktheitsprüfungermittelt wurde
	 * 
	 * @param invalidMId die Id der Markierung M
	 */

	public void setInvalidMId(String invalidMId) {
		this.invalidMId = invalidMId;
	}

	/**
	 * Getter Methode für die Markierung M', das durch die
	 * Beschänktheitsprüfungermittelt wurde
	 * 
	 * @return die Markierung M'
	 */

	@Override
    public String getInvalidMStrokeId() {
		if (invalidMStrokeId == null) {
			return "";
		} else {
			return invalidMStrokeId;
		}
	}

	/**
	 * Setter Methode für die Markierung M', das durch die
	 * Beschänktheitsprüfungermittelt wurde
	 * 
	 * @param invalidMStrokeId die Markierung M'
	 */
	public void setInvalidMStrokeId(String invalidMStrokeId) {
		this.invalidMStrokeId = invalidMStrokeId;
	}

	/**
	 * Getter Methode für die Liste welchen den Weg zwischen M und M' der
	 * Beschränktheitsanalyse beschreibt.
	 * 
	 * @return edge die hinzuzufügende Kante
	 */
	@Override
    public List<PAccEdge> getInvalidPath() {
		return invalidPath;
	}

	/**
	 * Methode zur Löschung der Liste welchen den Weg zwischen M und M' der
	 * Beschränktheitsanalyse beschreibt.
	 * 
	 */
	public void deleteInvalidPath() {
		this.invalidPath = new ArrayList<>();
	}

	/**
	 * Methode um der Liste welchen den Weg zwischen M und M' der
	 * Beschränktheitsanalyse beschreibt eine Kante hinzuzufügen.
	 * 
	 * @param edge die hinzuzufügende Kante
	 */
	public void addEdgeToInvalidPath(PAccEdge edge) {
		this.invalidPath.add(edge);
	}

	/**
	 * Übergibt die Id der Kante die durch die zuletzt geschaltete Transition zum
	 * aktuellen Zustand führt.
	 * 
	 * @return die Id der Kante
	 */
	@Override
    public String getLastAddedEdgeId() {
		if (lastAddedEdgeId == null) {
			return "";
		} else {
			return lastAddedEdgeId;
		}
	}

	/**
	 * Setter Methode für die Id der zuletzt hinzugefügte Kante
	 * 
	 * @param lastAddedEdgeId die Id der Kante
	 */

	public void setLastAddedEdgeId(String lastAddedEdgeId) {
		this.lastAddedEdgeId = lastAddedEdgeId;
	}
	
	


}
