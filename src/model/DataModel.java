package model;

import java.util.*;

import exceptions.InvalidEdgeException;
import interfaces.*;
import model.elements.*;

/**
 * Das Model des Petrinetzes. Enthält das Petrinetz, den Erreichbarkeitsgraphen
 * ({@link AccessabilityGraph})sowie dessen Metadaten ({@link AccGraphMetaData})
 * wie die Anfangsmarkierung, die Markierung die den aktuellen Zustand des
 * Petrinetzes beschreibt, die Kante der Markierung welche beschreibt wodurch
 * dieser Zustand erreicht wurde, sowie falls das Petrinetz Unbeschränt ist
 * unser M und M' sowie den Pfad zwischen diesen.
 * 
 * Auch stellt es Interfaces bereit über welche {@link IViewer} des Petrinetzes und des
 * Erreichbarkeitsgraphen die für sie benötigten Informationen abrufen können.
 *
 */
public class DataModel implements IAGViewerAccess, IPetrinetViewerAccess, IModel {
	private final List<PField> fieldList;
	private final List<PTransition> transitionList;
	private final List<PEdge> edgeList;
	private PMarking initialMarking;
	private final AccessabilityGraph accessabilityGraph;
	private PLabeledNode selectedNode;

	/**
	 * Konstruktor
	 */
	public DataModel() {
		this.fieldList = new ArrayList<PField>();
		this.transitionList = new ArrayList<PTransition>();
		this.edgeList = new ArrayList<PEdge>();
		this.initialMarking = new PMarking(fieldList);
		this.accessabilityGraph = new AccessabilityGraph();
		accessabilityGraph.addMarkingToListOfMarkings(initialMarking);
	}

	@Override
	public void setInitialMarkingToActualState() {
		initialMarking = new PMarking(fieldList);
		accessabilityGraph.resetAcessabilityGraph();
		accessabilityGraph.addMarkingToListOfMarkings(initialMarking);
	}

	@Override
	public void resetFieldsToInitialMarking() {
		setFieldsToMarking(initialMarking);
	}

	@Override
	public void setFieldsToMarking(PMarking m) {
		for (int i = 0; i < fieldList.size(); i++) {
			fieldList.get(i).setMarks(m.getTokenDistribution()[i]);
		}
		accessabilityGraph.getMetaData().setLastAddedEdgeId(null);
	}

	@Override
	public PMarking getInitialMarking() {
		return initialMarking;
	}

	@Override
	public void addTransition(PTransition t) {
		transitionList.add(t);
	}

	@Override
	public void addField(PField f) {
		fieldList.add(f);
		Collections.sort(fieldList);
		setInitialMarkingToActualState();
	}

	private void addEdge(PEdge edge) {
		if (!isEdgeAllreadyInList(edge)) {
			edgeList.add(edge);

			addEdgeToAdjacencyListOfAdjacentTransition(edge);
		}
	}

	@Override
	public void deleteEdgeSafely(PEdge e) {
		for (PTransition t : transitionList) {
			deleteEdgeFromList(e, t.getIncomingEdges());
			deleteEdgeFromList(e, t.getOutgoingEdges());
		}
		edgeList.remove(e);
	}

	private void deleteEdgeFromList(PEdge e, List<PEdge> list) {
		List<PEdge> listCopy = new ArrayList<>(list);
		List<PEdge> edgesToDelete = new ArrayList<>();
		for (PEdge e2 : listCopy) {
			if (e2.equals(e)) {
				edgesToDelete.add(e);
			}
		}
		for (PEdge edge : edgesToDelete) {
			list.remove(edge);
		}
	}

	private void addEdgeToAdjacencyListOfAdjacentTransition(PEdge edge) {
		for (PTransition t : transitionList) {
			if (edge.getFrom().equalsInId(t)) {
				t.addOutgoingEdge(edge);
			}
			if (edge.getTo().equalsInId(t)) {
				t.addIncomingEdge(edge);
			}
		}
	}

	@Override
	public void addEdgeAndRelink(String id, String from, String to) throws InvalidEdgeException {
		PEdge edge = new PEdge(id, from, to);
		if (!isEdgeAllreadyInList(edge)) {
			relinkEdgeToActualKnots(edge);
			addEdge(edge);
		}
	}

	/**
	 * Fügt eine Kante hinzu und verlinkt sie neu mit Knoten die bereits im
	 * DataModel gespeichert sind
	 * 
	 * @param edge Die Kante
	 * @throws InvalidEdgeException Die Kante ist nicht zulässig
	 */
	public void addEdgeAndRelink(PEdge edge) throws InvalidEdgeException {
		if (!isEdgeAllreadyInList(edge)) {
			relinkEdgeToActualKnots(edge);
			addEdge(edge);
		}
	}

	private boolean isEdgeAllreadyInList(PEdge edge) {
		for (PEdge existingEdge : edgeList) {
			if (existingEdge.getFrom().equalsInId(edge.getFrom()) && existingEdge.getTo().equalsInId(edge.getTo())) {
				return true;
			}
		}
		return false;
	}

	// Verlinkt eine Kante neu mit Knoten die in den Listen gespeichert sind.
	private void relinkEdgeToActualKnots(PEdge edge) throws InvalidEdgeException {
		int linkControlCounter = 0;
		linkControlCounter = linkEdgeToTransitions(edge, linkControlCounter);
		linkControlCounter = linkEdgeToFields(edge, linkControlCounter);
		// Kontrolle ob Zuweisung eindeutig ist
		if (linkControlCounter != 2) {
			throw new InvalidEdgeException("Kante konnte nicht eindeutig verlinkt werden");
		}
		if (isEdgeBetweenFieldAndTransition(edge)) {
			throw new InvalidEdgeException();
		}
	}

	// Kontrolle ob beide Seiten der Kante unterschiedlicher Klassen sind
	private boolean isEdgeBetweenFieldAndTransition(PEdge edge) {
		return (edge.getFrom().getClass().isInstance(new PField("")) && edge.getTo().getClass().isInstance(new PField("")))
				|| (edge.getFrom().getClass().isInstance(new PTransition(""))
						&& edge.getTo().getClass().isInstance(new PTransition("")));
	}

	private int linkEdgeToFields(PEdge edge, int linkControlCounter) {
		for (PField f : fieldList) {
			if (f.equalsInId(edge.getFrom())) {
				edge.setFrom(f);
				linkControlCounter++;
			}
			if (f.equalsInId(edge.getTo())) {
				edge.setTo(f);
				linkControlCounter++;
			}
		}
		return linkControlCounter;
	}

	private int linkEdgeToTransitions(PEdge edge, int linkControlCounter) {
		for (PTransition t : transitionList) {
			if (t.equalsInId(edge.getFrom())) {
				edge.setFrom(t);
				linkControlCounter++;
			}
			if (t.equalsInId(edge.getTo())) {
				edge.setTo(t);
				linkControlCounter++;
			}
		}
		return linkControlCounter;
	}

	@Override
	public List<PField> getFieldList() {
		return fieldList;
	}

	@Override
	public List<PTransition> getTransitionList() {
		return transitionList;
	}

	@Override
	public List<PEdge> getEdgeList() {
		return edgeList;
	}

	@Override
	public AccessabilityGraph getAccessabilityGraph() {
		return accessabilityGraph;
	}

	@Override
	public List<PMarking> getListOfMarkingsInGraph() {
		return accessabilityGraph.getListOfMarkings();
	}

	@Override
	public List<PAccEdge> getListOfEdgesOfMarkings() {
		return accessabilityGraph.getEdgeList();
	}

	@Override
	public void setSelectedNode(PLabeledNode node) {
		this.selectedNode = node;
	}

	@Override
	public PLabeledNode getSelectedNode() {
		return selectedNode;
	}

	@Override
	public String getCurrentMarkingId() {
		PMarking currentMarking = new PMarking(fieldList);
		return currentMarking.getId();
	}

	@Override
	public AccGraphMetaData getAccessabilityGraphMetaData() {
		return this.accessabilityGraph.getMetaData();
	}

	@Override
	public IAGMetaDataViewerAcces getMetaDataViewerAccess() {
		return this.accessabilityGraph.getMetaData();
	}

}
