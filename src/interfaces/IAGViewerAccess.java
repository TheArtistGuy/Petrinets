package interfaces;

import java.util.List;

import model.elements.*;

/**
 * Das Interface IAGViewerAccess gewährt Zugriff auf einen Erreichbarkeitsgrafen
 * und stellt Methoden bereit um auf diesen lesend zuzugreifen.
 *
 */

public interface IAGViewerAccess {

	/**
	 * Übergibt die Liste der Markierungen aus dem Erreichbarkeitsgraphen
	 * 
	 * @return die Liste der Markierungen
	 */

	public List<PMarking> getListOfMarkingsInGraph();

	/**
	 * Übergibt die Liste der Kanten aus dem Erreichbarkeitsgraphen
	 * 
	 * @return die Liste der Kanten
	 */

	public List<PAccEdge> getListOfEdgesOfMarkings();

	/**
	 * Übergibt die Anfangsmarkierung des Graphen
	 * 
	 * @return die Anfangsmarkierung
	 */
	public PMarking getInitialMarking();

	/**
	 * Übergibt die Id der Makierung die den aktuellen Zustand des Petrinetzes
	 * beschreibt
	 * 
	 * @return die Id der Markierung
	 */
	public String getCurrentMarkingId();

	/**
	 * Gibt Lesezugriff auf die MetaDaten des Erreichbarkeitsgraphen. diese bestehen
	 * aus M, M' dem Pfad zwischen beiden, sowie die Kante die durch die zuletzt
	 * geschaltete Transition erzeugt wurde.
	 * 
	 * @return Die MetaDaten des ErreichbarkeitsGraphen
	 */
	public IAGMetaDataViewerAcces getMetaDataViewerAccess();
}
