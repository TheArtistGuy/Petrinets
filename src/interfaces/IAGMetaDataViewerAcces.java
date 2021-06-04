package interfaces;

import java.util.List;

import model.elements.PAccEdge;


/**
 * Interface das Lesezugriff auf die MetaDaten des Erreichbarkeitsgraphen gewährt
 *
 */

public interface IAGMetaDataViewerAcces {
	
	
	/**
	 * Getter Methode für die Markierung M', das durch die
	 * Beschänktheitsprüfungermittelt wurde
	 * 
	 * @return die Markierung M'
	 */

	public String getInvalidMStrokeId();
	
	/**
	 * Getter Methode für die Markierung M, das durch die
	 * Beschänktheitsprüfungermittelt wurde
	 * 
	 * @return die Markierung M
	 */
	public String getInvalidMId();
	
	
	/**
	 * Getter Methode für die Liste welchen den Weg zwischen M und M' der
	 * Beschränktheitsanalyse beschreibt.
	 * 
	 * @return edge die hinzuzufügende Kante
	 */
	public List<PAccEdge> getInvalidPath();
	
	
	/**
	 * Übergibt die Id der Kante die durch die zuletzt geschaltete Transition zum
	 * aktuellen Zustand führt.
	 * 
	 * @return die Id der Kante
	 */
	public String getLastAddedEdgeId();
}
