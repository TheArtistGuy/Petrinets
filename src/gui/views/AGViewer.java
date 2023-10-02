package gui.views;

import java.util.LinkedList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import interfaces.IAGViewerAccess;
import interfaces.IViewer;
import model.DataModel;
import model.elements.*;

/**
 * Viewer zur Darstellung von Erreichbarkeitsgraphen.
 *
 */

public class AGViewer extends MultiGraph implements IViewer {

	/** URL-Angabe zur css-Datei, in der das Layout des Graphen angegeben ist. */
	private static final String CSS_FILE = "url(" + PetrinetGraph.class.getResource("/accgraph.css") + ")";

	private IAGViewerAccess accessabilityGraph;

	/**
	 * Konstruktor
	 * 
	 * @param accessabilityGraph der darzustellende Erreichbarkeitsgraph
	 */
	public AGViewer(IAGViewerAccess accessabilityGraph) {
		super("AccessabilityGraph");
		this.accessabilityGraph = accessabilityGraph;
		initialiseGraphicSettings();
		revalidateGraph();
	}

	private void initialiseGraphicSettings() {
		addAttribute("ui.quality");
		addAttribute("ui.antialias");
		this.addAttribute("ui.stylesheet", CSS_FILE);
	}


	@Override
	public void refreshView() {
		// falls nur 1 Element im Graphen zurücksetzen, sonst entstehen Grafikfehler
		// beim hinzufügen/abziehen von Marken in dem der Knoten aus dem Bild wandert.
		if (accessabilityGraph.getListOfMarkingsInGraph().size() == 1
				&& accessabilityGraph.getListOfEdgesOfMarkings().isEmpty()) {
			this.clear();
			initialiseGraphicSettings();
			revalidateGraph();
		} else {
			revalidateGraph();
		}
	}

	private void revalidateGraph() {
		revalidateNodes();
		revalidateEdges();
		drawInvalidPath();
		drawLastAddedEdge();
		deleteUnnecessaryClassesFromEdges();
	}

	private void drawLastAddedEdge() {
		Edge lastAddedEdge = getEdge(accessabilityGraph.getMetaDataViewerAccess().getLastAddedEdgeId());
		if (lastAddedEdge != null) {
			lastAddedEdge.addAttribute("ui.class", "last_activated_edge");
		}
	}

	private void deleteUnnecessaryClassesFromEdges() {
		for (PAccEdge e : accessabilityGraph.getListOfEdgesOfMarkings()) {
			// Falls weder im Pfad zwischen M und M' noch angezeigte letzte Kante
			if (!accessabilityGraph.getMetaDataViewerAccess().getInvalidPath().contains(e)
					&& (!accessabilityGraph.getMetaDataViewerAccess().getLastAddedEdgeId().equals(e.getId()))) {
				Edge edge = getEdge(e.getId());
				edge.removeAttribute("ui.class");
			}
		}
	}

	private void revalidateNodes() {
		for (PMarking m : accessabilityGraph.getListOfMarkingsInGraph()) {
			addNodeIfNotAllreadyinGraph(m);

			// Farblich hervorheben, falls ein besonderer Knoten, bzw Klasse löschen falls
			// hervorheben nicht mehr nötig
			drawOrRemoveActualNodeClass(m);
		}
		removeNodesThatAreNoLongerInModel();
	}

	private void addNodeIfNotAllreadyinGraph(PMarking m) {
		if (getNode(m.getId()) == null && !m.getId().equals("()")) {
			// Markierung hinzufügen, falls noch nicht im Graphen und falls sie Stellen
			// enthält
			Node node = addNode(m.getId());
			node.addAttribute("ui.label", m.getLabel());
		}
	}

	// Gibt einer Kante im Erreichbarkeitsgraphen die Klasse zur Ordnungsgemässen
	// Darstellung oder entfernt die Klasse falls Darstellung nicht mehr aktuell
	private void drawOrRemoveActualNodeClass(PMarking m) {
		Node node = getNode(m.getId());
		if (node != null) {
			if (node.getId().equals(accessabilityGraph.getMetaDataViewerAccess().getInvalidMId())
					&& node.getId().equals(accessabilityGraph.getInitialMarking().getId())) {
				// Falls M und Startmarkierung
				node.addAttribute("ui.class", "invalidmarkerandstartmarker");
			} else if (node.getId().equals(accessabilityGraph.getCurrentMarkingId())
					&& node.getId().equals(accessabilityGraph.getInitialMarking().getId())) {
				// Falls aktuelle Markierung und Startmarkierung
				node.addAttribute("ui.class", "actualmarkingandstartmarker");
			} else if (node.getId().equals(accessabilityGraph.getMetaDataViewerAccess().getInvalidMId())
					&& node.getId().equals(accessabilityGraph.getCurrentMarkingId())) {
				// Falls M und aktuelle Markierung
				node.addAttribute("ui.class", "invalidmarkerandactualmarker");
			} else if (node.getId().equals(accessabilityGraph.getMetaDataViewerAccess().getInvalidMStrokeId())
					&& node.getId().equals(accessabilityGraph.getCurrentMarkingId())) {
				// Falls M' und aktuelle Markierung
				node.addAttribute("ui.class", "invalidmarkerstrokeandactualmarker");
			} else if (node.getId().equals(accessabilityGraph.getMetaDataViewerAccess().getInvalidMId())) {
				// Falls M
				node.addAttribute("ui.class", "invalidmarker");
			} else if (node.getId().equals(accessabilityGraph.getMetaDataViewerAccess().getInvalidMStrokeId())) {
				// Falls M'
				node.addAttribute("ui.class", "invalidmarkerstroke");
			} else if (node.getId().equals(accessabilityGraph.getCurrentMarkingId())) {
				// Falls aktuelle Markierung
				node.addAttribute("ui.class", "actualmarking");
			} else if (node.getId().equals(accessabilityGraph.getInitialMarking().getId())) {
				// Falls Anfangsmarkierung
				node.addAttribute("ui.class", "startmarking");
			} else if ((node.hasAttribute("ui.class"))) {
				// sonst
				node.removeAttribute("ui.class");
			}
		}
	}

	private void removeNodesThatAreNoLongerInModel() {

		if (accessabilityGraph.getListOfMarkingsInGraph().size() < getNodeCount()) {
			// Liste zum Zwischenspeichern der zu entfernenden Knoten, damit der Iterator
			// nicht abbricht.
			LinkedList<Node> removalList = new LinkedList<>();
			for (Node node : getEachNode()) {
				boolean copyOfNodeFound = false;
				for (PMarking m : accessabilityGraph.getListOfMarkingsInGraph()) {
					if (node.getId().equals(m.getId())) {
						copyOfNodeFound = true;
					}
				}
				if (!copyOfNodeFound) {
					removalList.add(node);
				}
			}
			for (Node nodeToRemove : removalList) {
				removeNode(nodeToRemove);
			}
		}
	}

	// Zeichnet Pfad zwischen M und M'
	private void drawInvalidPath() {
		if (!accessabilityGraph.getMetaDataViewerAccess().getInvalidPath().isEmpty()) {
			for (PAccEdge pathEdge : accessabilityGraph.getMetaDataViewerAccess().getInvalidPath()) {
				Edge e = getEdge(pathEdge.getId());
				e.addAttribute("ui.class", "in_invalid_path");
			}
		}
	}
	// Kontrolliert ob alle Kanten im Model auch im Graphen vorhenden sind,
	// ansonsten wird die kante hinzugefügt, oder gelöscht
	private void revalidateEdges() {
		addEdgesThatAreNotAllredyInView();
		removeEdgesThatAreNoLongerInModel();
	}

	private void addEdgesThatAreNotAllredyInView() {
		for (PEdge e : accessabilityGraph.getListOfEdgesOfMarkings()) {
			if (getEdge(e.getId()) == null) {
				Edge edge = addEdge(e.getId(), e.getFrom().getId(), e.getTo().getId(), true);
				edge.changeAttribute("ui.label", e.getLabel());
			}
		}
	}

	private void removeEdgesThatAreNoLongerInModel() {
		if (accessabilityGraph.getListOfEdgesOfMarkings().size() < getEdgeCount()) {
			// Liste zum Zwischenspeichern der zu löschenden Kanten, damit der Iterator
			// nicht abbricht
			LinkedList<Edge> removalList = new LinkedList<>();
			for (Edge edge : getEachEdge()) {
				boolean copyOfEdgeFound = false;
				for (PEdge e : accessabilityGraph.getListOfEdgesOfMarkings()) {
					if (edge.getId().equals(e.getId())) {
						copyOfEdgeFound = true;
					}
				}
				if (!copyOfEdgeFound) {
					removalList.add(edge);
				}
			}
			for (Edge edgeToRemove : removalList) {
				removeEdge(edgeToRemove);
			}
		}
	}

	@Override
	public void changeViewedModelTo(DataModel model) {
		this.accessabilityGraph = model;
		refreshView();
	}
}
