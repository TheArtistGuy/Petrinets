package controller;

import java.util.ArrayList;
import java.util.List;

import interfaces.IModel;
import model.elements.*;

/**
 * Stellt die Methoden bereit um eine Erreichbarkeitsgraphen aus einem Petrinetz
 * zu erstellen. Sowie die Methoden um die Beschränktheit zu testen.
 *
 */

class AGBuilder {

	private IModel model;

	/**
	 * Konstruktor
	 * 
	 * @param model das Model des Petrinetzes aus dem ein Erreichbarkeitsgraph
	 *              erstellt werden soll
	 */

	protected AGBuilder(IModel model) {
		this.model = model;
		model.setInitialMarkingToActualState();

	}

	// Interne Klasse um Funktionen zum verschmelzen von Listen aufzunehmen
	private class ListMerger<T extends PGraphElement> {

		// Vereint die Listen, so dass list alle Elemente von sich selbst und list Copy
		// enthält
		private void mergeLists(List<T> list, List<T> listCopy) {
			List<T> elementsToAdd = new ArrayList<>();
			// Abgleich ob copyList Kanten enthält die list nicht enthält
			for (T element : listCopy) {
				boolean found = isThereAMatchingGraphElementInList(list, element);
				if (!found) {
					// merken der nicht enthaltenen Kanten
					elementsToAdd.add(element);
				}
			}
			// hinzufügen der Kanten
			for (T edge : elementsToAdd) {
				list.add(edge);
			}
		}

		private boolean isThereAMatchingGraphElementInList(List<T> list, T elementToCompare) {
			boolean found = false;
			for (T e : list) {
				if (elementToCompare.equalsInId(e)) {
					found = true;
				}
			}
			return found;
		}

	}

	/**
	 * Baut einen vollen Erreichbarkeitsgraphen von der Anfangsmarkierung aus und
	 * bestimmt ob das Petrinetz zulässig ist.
	 * 
	 * @param mergeWithAllreadyBuildedGraph Sollen der neu erstellte und der bereits
	 *                                      aufgebaute Graph zusammengeführt werden?
	 */
	protected void buildFullAccesabilityGraph(boolean mergeWithAllreadyBuildedGraph) {

		// Initalisieren von Listen falls der aktuell manuell gebaute EG erhalten
		// bleiben soll.
		List<PMarking> markingListCopy = new ArrayList<>(model.getAccessabilityGraph().getListOfMarkings());
		List<PAccEdge> edgeListCopy = new ArrayList<>(model.getListOfEdgesOfMarkings());

		// Petrinetz Auf Anfangszustand setzen
		resetModelToInitialState();

		// Kontrollvarialble, wird true, wenn alle Markierungen besucht und getestet
		// wurden.
		boolean buildFinished = false;
		List<PMarking> markingsVisited = new ArrayList<>();
		while (model.getAccessabilityGraph().getIsValid() && !buildFinished) {
			// Baue Breitensuchbaum
			for (int i = 0; i < model.getAccessabilityGraph().getListOfMarkings().size(); i++) {
				// Abfrage ob der Graph immernoch möglicherweise beschränkt ist
				if (model.getAccessabilityGraph().getIsValid()) {
					PMarking activeMarking = model.getAccessabilityGraph().getListOfMarkings().get(i);
					// Überspringen falls schon besucht
					if (!markingsVisited.contains(activeMarking)) {
						// Model auf den Zustand der Markierung setzen und alle aktiven Transitionen
						// nacheinender schalten
						model.setFieldsToMarking(activeMarking);
						List<PTransition> listOfActiveTransitions = listActiveTransitionsUnderMarking();
						for (PTransition transition : listOfActiveTransitions) {
							model.setFieldsToMarking(activeMarking);
							PMarking nextMarking = shiftTransitionAndCreateNewMarking(activeMarking, transition);
							if (nextMarking != null) {
								// ist nextMarking ein mögliches M' und gibt es einen Weg von M
								testIfViolatesRuleOfLimitationAndThereIsAPath(nextMarking);
							}
						}
						// Aktuelle Markierung zur Liste der bereits besuchten Markierungen hinzufügen
						markingsVisited.add(activeMarking);
					}
				}
			}
			// Wenn alle Markierungen bearbeitet wurden, breche ab
			if (markingsVisited.size() == model.getAccessabilityGraph().getListOfMarkings().size()) {
				buildFinished = true;
			}
		}
		// Zusammenführen der Graphen
		if (mergeWithAllreadyBuildedGraph) {
			mergeListsWithListsInModel(markingListCopy, edgeListCopy);
		}
		model.resetFieldsToInitialMarking();
	}

	// Setzt das Model auf den Anfangszustand zurück
	private void resetModelToInitialState() {
		// Erreichbarkeitsgraph zurücksetzen
		model.getAccessabilityGraph().resetAcessabilityGraph();
		// Die Stellen des Models auf den Zustand der Anfangsmarkierung setzen
		model.resetFieldsToInitialMarking();
		// setzt die Anfangsmarkierung auf den aktuellen Zustand. Dies fügt die
		// Startmarkierung dem Erreichbarkeitsgraphen hinzu
		model.setInitialMarkingToActualState();
	}

	// Erzeugt eine Liste von Markierungen, welche zu dem aktuellen Zutand des
	// Petrinetzes aktiviert sind.
	private List<PTransition> listActiveTransitionsUnderMarking() {
		List<PTransition> listOfTransitions = new ArrayList<PTransition>();
		for (PTransition t : model.getTransitionList()) {
			if (t.isReadyToShift()) {
				listOfTransitions.add(t);
			}
		}
		return listOfTransitions;
	}

	/**
	 * Schaltet eine Transition von einer gegebenen Markierung aus,erstellt eine
	 * neue Markierung inklusive der Kante.
	 * 
	 * @param activeMarking die gegebene Markierung
	 * @param transition    die zu schaltende Transition
	 * @return die Kante die neu erzeugt wurde, null falls keine neu erzeugt wurde
	 */
	protected PMarking shiftTransitionAndCreateNewMarking(PMarking activeMarking, PTransition transition) {
		transition.shift();
		PMarking nextMarking = new PMarking(model.getFieldList());
		PMarking doublettOfNextMarking = searchForDoublettsInActualModelState(nextMarking);
		if (doublettOfNextMarking == null) {
			model.getAccessabilityGraph().addMarkingToListOfMarkings(nextMarking);
			addNewEdgeToAccessabilityGraph(activeMarking, nextMarking, transition);
			return (nextMarking);
		} else {
			addNewEdgeToAccessabilityGraph(activeMarking, doublettOfNextMarking, transition);
			return null;
		}
	}

	// Checke ob Makierung bereits im Erreichbarkeitsgraphen, wenn ja gebe diese
	// Markierung zurück
	private PMarking searchForDoublettsInActualModelState(PMarking nextMarking) {

		for (PMarking m : model.getAccessabilityGraph().getListOfMarkings()) {
			if (nextMarking.equalsInDistributionOfMarks(m)) {
				return m;
			}
		}
		return null;
	}

	// Teste ob es ein M und M' gibt und ob einWeg zwischen diesen existiert.
	private void testIfViolatesRuleOfLimitationAndThereIsAPath(PMarking nextMarking) {
		for (PMarking m : model.getAccessabilityGraph().getListOfMarkings()) {
			// Abfrage um Doppelte Einträge zu vermeiden.
			if (model.getAccessabilityGraph().getIsValid()) {
				if (violatesRuleOfLimitation(m, nextMarking) && existsAPathFromTo(m, nextMarking)) {
					model.getAccessabilityGraphMetaData().setInvalidMId(m.getId());
					model.getAccessabilityGraphMetaData().setInvalidMStrokeId(nextMarking.getId());
					// Schreibe Weg von der Anfangsmarkierung nach M in den Weg
					existsAPathFromTo(model.getInitialMarking(), m);
					model.getAccessabilityGraph().setIsLimited(false);
				}
			}
		}
	}

	/**
	 * Bestimmt ob der Markierung m'an jeder Stelle genauso viele Marken zugewiesen
	 * wurde und mindestens einer Stelle mehr Marken zugewiesen wurden als der
	 * Markierung m.
	 * 
	 * @param m       m
	 * 
	 * @param mStrich m'
	 * 
	 * @return wurde in m' mindestens einer Stelle eine Marke mehr zugewiesen?
	 */

	private boolean violatesRuleOfLimitation(PMarking m, PMarking mStrich) {

		for (int i = 0; i < m.getTokenDistribution().length; i++) {
			if (mStrich.getTokenDistribution()[i] < m.getTokenDistribution()[i]) {
				// Fall eine Stelle in M hat mehr Marken als die gleiche Stelle in M'
				return false;
			}
		}
		for (int i = 0; i < m.getTokenDistribution().length; i++) {
			if (mStrich.getTokenDistribution()[i] > m.getTokenDistribution()[i]) {
				// Fall alle Stellen mindestens gleich und eine Stelle in M' hat mehr Marken als
				// die gleiche Stelle in M
				return true;
			}
		}
		// Fall alle Stellen sind gleich
		return false;
	}

	/**
	 * Kontrolliert in einem rekursiven Algorithmus ob es einen Weg zwischen m und
	 * nextMarking gibt.
	 * 
	 * @param m           die Ausgangsmarkierung
	 * @param nextMarking die Zielmarkierung
	 * @return gibt es einen Weg zwischen den zwei Punkten ?
	 */
	private boolean existsAPathFromTo(PMarking m, PMarking nextMarking) {

		List<PAccEdge> visitedEdges = new ArrayList<>();
		return findPath(m, nextMarking, visitedEdges);

	}

	private boolean findPath(PMarking m, PMarking nextMarking, List<PAccEdge> visitedEdges) {
		for (PAccEdge adjacentEdge : m.getListOfOutgoingEdges()) {
			// Fall Markierung gefunden
			if (((PMarking) adjacentEdge.getTo()).equalsInDistributionOfMarks(nextMarking)) {
				model.getAccessabilityGraphMetaData().addEdgeToInvalidPath(adjacentEdge);
				return true;
			} else if (!visitedEdges.contains(adjacentEdge)) {
				// Fall weitersuchen
				visitedEdges.add(adjacentEdge);
				if (findPath((PMarking) adjacentEdge.getTo(), nextMarking, visitedEdges)) {
					model.getAccessabilityGraphMetaData().addEdgeToInvalidPath(adjacentEdge);
					return true;
				}
			}
		}
		return false;
	}

	// Erschaffe Kante von fromMarking nach toMarking welche durch schalten von
	// transition entstanden ist und füge sie der Liste der ausgehenden Kanten von
	// aktiveMarking, sowie der Liste der Kanten des Erreichbarkeitsgraphen hinzu.
	private void addNewEdgeToAccessabilityGraph(PMarking fromMarking, PMarking toMarking, PTransition transition) {

		PAccEdge edgeToAdd = new PAccEdge(transition.getId() + fromMarking.getId() + toMarking.getId(), fromMarking,
				toMarking, transition);
		model.getAccessabilityGraphMetaData().setLastAddedEdgeId(edgeToAdd.getId());
		if (!edgeIsAlreadyInEdgeList(edgeToAdd)) {
			model.getAccessabilityGraph().addEdgeToListOfEdges(edgeToAdd);
			fromMarking.addEdgeToListOfOutgoingEdges(edgeToAdd);
		}
	}

	// stellt fest ob eine Kante bereits existiert
	private boolean edgeIsAlreadyInEdgeList(PAccEdge edgeToAdd) {
		for (PAccEdge e : model.getAccessabilityGraph().getEdgeList()) {
			if (e.equalsInId(edgeToAdd)) {
				return true;
			}
		}
		return false;
	}

	private void mergeListsWithListsInModel(List<PMarking> markingListCopy, List<PAccEdge> edgeListCopy) {
		ListMerger<PMarking> markingsMerger = new ListMerger<>();
		markingsMerger.mergeLists(model.getListOfMarkingsInGraph(), markingListCopy);
		ListMerger<PAccEdge> edgeMerger = new ListMerger<>();
		edgeMerger.mergeLists(model.getListOfEdgesOfMarkings(), edgeListCopy);
	}

}
