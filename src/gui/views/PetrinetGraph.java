package gui.views;

import java.util.LinkedList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import interfaces.IPetrinetViewerAccess;
import interfaces.IViewer;
import model.DataModel;
import model.elements.*;

/**
 * View zur Darstellung eines Petrinetzes durch die GraphStream Bibliothek. 
 * 
 */
public class PetrinetGraph extends MultiGraph implements IViewer {

	/** URL-Angabe zur css-Datei, in der das Layout des Graphen angegeben ist. */
	private static String CSS_FILE = "url(" + PetrinetGraph.class.getResource("/petrinetgraph.css") + ")";

	private IPetrinetViewerAccess petrinet;

	/**
	 * Konstruktor
	 * 
	 * @param petrinet Das darzustellende Petrinetz
	 */
	public PetrinetGraph(IPetrinetViewerAccess petrinet) {
		super("PetriNetz");

		initializeGraph(petrinet);
	}

	private void initializeGraph(IPetrinetViewerAccess p) {
		this.petrinet = p;
		initialiseLayoutSettings();
		initialiseGraphElements();
	}

	private void initialiseLayoutSettings() {
		addAttribute("ui.quality");
		addAttribute("ui.antialias");
		addAttribute("ui.stylesheet", CSS_FILE);
	}

	private void initialiseGraphElements() {
		initializeFields();
		initialiizeTransitions();
		initializeEdges();
	}

	// fügt alle Kanten des Petrinetzes dem Graphen hinzu
	private void initializeEdges() {
		for (PEdge e : petrinet.getEdgeList()) {
			
			Edge edgeAllreadyInGraph = getEdge(e.getId());
			if (edgeAllreadyInGraph == null) {
				addEdgeToGraph(e);
			}
		}
	}

	private void addEdgeToGraph(PEdge e) {
		Edge edge = this.addEdge(e.getId(), e.getFrom().getId(), e.getTo().getId(), true);
		edge.addAttribute("ui.label", e.getLabel());
	}

	// fügt alle Stellen des Petrinetzes dem Graphen hinzu
	private void initializeFields() {
		for (PField f : petrinet.getFieldList()) {
			Node nodeAlreadyInGraph = getNode(f.getId());
			if (nodeAlreadyInGraph == null) {
				addFieldToGraph(f);

			}
		}
	}

	private void addFieldToGraph(PField f) {
		Node node = this.addNode(f.getId());
		node.addAttribute("ui.label", f.getLabel());
		node.addAttribute("xy", f.getPosition().getX(), f.getPosition().getY());
		node.addAttribute("ui.class", evalFieldPicture(f));
	}

	// fügt alle Transitionen des Petrinetzes dem Graphen hinzu
	private void initialiizeTransitions() {
		for (PTransition t : petrinet.getTransitionList()) {
			Node nodeAlreadyInGraph = getNode(t.getId());
			if (nodeAlreadyInGraph == null) {
				addTransitionToGraph(t);
			}
		}
	}

	private void addTransitionToGraph(PTransition t) {
		Node node = this.addNode(t.getId());
		node.addAttribute("ui.label", t.getLabel());
		node.addAttribute("xy", t.getPosition().getX(), t.getPosition().getY());
		node.addAttribute("ui.class", transitionPictureSelector(t));
	}

	// weist einer Stelle die Grafik zu die ihrem Zustand entspricht
	private String evalFieldPicture(PField field) {
		if (petrinet.getSelectedNode() != null) {
			return field.getId().equals(petrinet.getSelectedNode().getId()) ? markedFieldPictureSelector(field)
					: fieldPictureSelector(field);
		} else {
			return fieldPictureSelector(field);
		}
	}

	// Bestimmt die zuzuweisende Grafik für eine Transition bzgl. ihrer Aktivität
	private String transitionPictureSelector(PTransition t) {
		if (t.isReadyToShift()) {
			return "transition_active";
		} else {
			return "transition_inactive";
		}
	}

	// Bestimmt die zuzuweisende Grafik für eines Feldes bzgl. seiner Marken
	private String fieldPictureSelector(PField field) {
		switch (field.getTokens()) {
		case 0:
			return "field0";
		case 1:
			return "field1";
		case 2:
			return "field2";
		case 3:
			return "field3";
		case 4:
			return "field4";
		case 5:
			return "field5";
		case 6:
			return "field6";
		case 7:
			return "field7";
		case 8:
			return "field8";
		case 9:
			return "field9";
		default:
			break;
		}
		return "fieldgreater9";
	}

	// Bestimmt die zuzuweisende Grafik für eines Markierten Feldes bzgl. seiner
	// Marken
	private String markedFieldPictureSelector(PField field) {
		switch (field.getTokens()) {
		case 0:
			return "field0m";
		case 1:
			return "field1m";
		case 2:
			return "field2m";
		case 3:
			return "field3m";
		case 4:
			return "field4m";
		case 5:
			return "field5m";
		case 6:
			return "field6m";
		case 7:
			return "field7m";
		case 8:
			return "field8m";
		case 9:
			return "field9m";
		default:
			break;
		}
		return "fieldgreater9m";
	}

	@Override
	public void refreshView() {
		initialiseGraphElements();
		revalidateNodeClasses();
		removeUnnessacaryGraphElements();
	}

	private void removeUnnessacaryGraphElements() {
		removeUnnessacaryNodes();
		removeUnnessacaryEdges();

	}

	private void removeUnnessacaryNodes() {
		if (this.getNodeCount() > (petrinet.getFieldList().size() + petrinet.getTransitionList().size())) {
			LinkedList<Node> removalList = new LinkedList<>();
			for (Node n : getEachNode()) {
				boolean nodeFound = false;
				nodeFound = searchForNodeInFieldList(n, nodeFound);
				nodeFound = searchForNodeInTransitionList(n, nodeFound);
				if (!nodeFound) {
					removalList.add(n);
				}
			}
			for (Node node : removalList) {
				removeNode(node);
			}
		}
	}

	private boolean searchForNodeInTransitionList(Node n, boolean nodeFound) {
		for (PTransition t : petrinet.getTransitionList()) {
			if (t.getId().equals(n.getId())) {
				return true;
			}
		}
		return nodeFound;
	}

	private boolean searchForNodeInFieldList(Node n, boolean nodeFound) {
		for (PField f : petrinet.getFieldList()) {
			if (f.getId().equals(n.getId())) {
				return true;
			}
		}
		return nodeFound;
	}

	private void removeUnnessacaryEdges() {
		if (this.getEdgeCount() > petrinet.getEdgeList().size()) {
			LinkedList<Edge> removalList = new LinkedList<>();
			for (Edge e : getEachEdge()) {
				boolean edgeFound = searchForEdgeInEdgeList(e);
				if (!edgeFound) {
					removalList.add(e);
				}
			}
			for (Edge edge : removalList) {
				removeEdge(edge);
			}

		}
	}

	private boolean searchForEdgeInEdgeList(Edge e) {
		boolean edgeFound = false;
		for (PEdge edge : petrinet.getEdgeList()) {
			if (edge.getId().equals(e.getId())) {
				edgeFound = true;
			}
		}
		return edgeFound;
	}

	private void revalidateNodeClasses() {
		for (PTransition t : petrinet.getTransitionList()) {
			Node node = this.getNode(t.getId());
			node.addAttribute("ui.label", t.getLabel());
			node.setAttribute("ui.class", transitionPictureSelector(t));
		}
		for (PField f : petrinet.getFieldList()) {
			Node node = this.getNode(f.getId());
			node.addAttribute("ui.label", f.getLabel());
			node.setAttribute("ui.class", evalFieldPicture(f));
		}
	}

	@Override
	public void changeViewedModelTo(DataModel model) {
		this.clear();
		initializeGraph(model);
	}

}
