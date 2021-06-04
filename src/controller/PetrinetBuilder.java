package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import enums.EBuilderModes;
import interfaces.*;
import model.DataModel;
import model.elements.*;

/**
 * Ein Controller um einen PetrinetGraphen zu bearbeiten. Knoten / Kanten
 * hinzuzufügen und zu löschen. Dieser wird an einen eigentlichen
 * {@link Controller} als zusätzlichen {@link IModelChanger} angemeldet und über
 * den {@link Controller} den Zugriff auf das Model zu regeln, da die
 * entsprechende Klasse überschrieben wurde.
 *
 */

public class PetrinetBuilder extends Controller implements IPBuilderToolbarListener, IPBuilderMouseAdapter {

	private EBuilderModes mode;
	private String selectedGraphElementId;

	/**
	 * Konstruktor
	 * 
	 * @param model das zu bearbeitende DataModel
	 */
	public PetrinetBuilder(IModel model) {
		super(model);
		this.mode = EBuilderModes.SELECT_MODE;
	}

	/**
	 * Wenn im ADD_FIELD_MODE wird eine Stelle an der übergebenen Position
	 * hinzugefügt, wenn im ADD_TRANSITION_MODE eine Transition
	 */
	@Override
	public void ClickOccurred(int x, int y) {

		if (mode == EBuilderModes.ADD_FIELD_MODE) {
			String updateMessage = addFieldAtPosition(x, y);
			pushTextToTextListeners(updateMessage);
			updateViews();
		}
		if (mode == EBuilderModes.ADD_TRANSITION_MODE) {
			String updateMessage = addTransitionAtPosition(x, y);
			pushTextToTextListeners(updateMessage);
			updateViews();
		}
	}

	private String addTransitionAtPosition(int x, int y) {
		PTransition t = new PTransition(transitionIdCreator());
		t.setPosition(new Point(x, -y));
		model.addTransition(t);
		model.setInitialMarkingToActualState();
		return "Transition " + t.getId() + " wurde hinzugefügt\n";
	}

	private String addFieldAtPosition(int x, int y) {
		PField f = new PField(fieldIdCreator());
		f.setPosition(new Point(x, -y));
		model.addField(f);
		return "Stelle " + f.getId() + " wurde hinzugefügt\n";
	}

	// Generiert eine passende Id für Transitionen
	private String transitionIdCreator() {
		int counter = model.getTransitionList().size() + 1;
		while (true) {
			String transitionId = ("t" + counter);
			boolean hasDoublette = false;
			for (PTransition t : model.getTransitionList()) {
				if (t.getId().equals(transitionId)) {
					hasDoublette = true;
				}
			}
			if (!hasDoublette) {
				return transitionId;
			} else {
				counter += 1;
			}
		}
	}

	// Generiert eine passende ID für Stellen
	private String fieldIdCreator() {
		int counter = model.getFieldList().size() + 1;
		while (true) {
			String fieldId = ("p" + counter);
			boolean hasDoublette = false;
			for (PField f : model.getFieldList()) {
				if (f.getId().equals(fieldId)) {
					hasDoublette = true;
				}
			}
			if (!hasDoublette) {
				return fieldId;
			} else {
				counter += 1;
			}
		}
	}

	// Generiert eine passende ID für Kanten
	private String edgeIdCreator() {
		int counter = model.getEdgeList().size() + 1;
		while (true) {
			String edgeId = ("e" + counter);
			boolean hasDoublette = false;
			for (PEdge e : model.getEdgeList()) {
				if (e.getId().equals(edgeId)) {
					hasDoublette = true;
				}
			}
			if (!hasDoublette) {
				return edgeId;
			} else {
				counter += 1;
			}
		}
	}

	/**
	 * Wählt eine Stelle oder Transition aus, falls im ADD_EDGE_MODE wird eine Kante
	 * zwischen dem vorher selektierten Knoten und dem nun geklicken Knoten erzeugt.
	 */
	@Override
	public void selectLabeledNode(String id) {
		if (mode == EBuilderModes.SELECT_MODE) {
			selectGraphElementWithId(id);
		}
		if (mode == EBuilderModes.ADD_EDGE_MODE) {
			drawEdgeFromSelectedNodeToNodeWithId(id);
		}

	}

	private void drawEdgeFromSelectedNodeToNodeWithId(String id) {
		try {
			model.addEdgeAndRelink(edgeIdCreator(), selectedGraphElementId, id);
			updateViews();
		} catch (Exception e2) {
			pushTextToTextListeners("Kante konnte nicht hinzugefügt werden\n");
		}
	}

	private void selectGraphElementWithId(String id) {
		this.selectedGraphElementId = id;
		for (PField f : model.getFieldList()) {
			if (f.getId().equals(id)) {
				super.selectLabeledNode(id);
				pushTextToTextListeners(id + " wurde ausgewählt.\n");
			}
		}
		for (PTransition t : model.getTransitionList()) {
			if (t.getId().equals(id)) {
				pushTextToTextListeners(id + " wurde ausgewählt.\n");
			}
		}
	}

	@Override
	public void setLabelOfSelectedGraphElementTo(String label) {
		for (PField x : model.getFieldList()) {
			if (x.getId().equals(selectedGraphElementId)) {
				x.setName(label);
			}
		}
		for (PTransition x : model.getTransitionList()) {
			if (x.getId().equals(selectedGraphElementId)) {
				x.setName(label);
			}
		}
		updateViews();
	}

	@Override
	public void deleteSelectedNode() {
		if (selectedGraphElementId != null) {
			String deletedNodeId = null;
			deletedNodeId = attemptToDeleteSelectedField(deletedNodeId);
			deletedNodeId = attemptToDeleteSelectedTransition(deletedNodeId);
			if (deletedNodeId != null) {
				deleteEdgesToAndFromNodewithId(deletedNodeId);
			}
			model.setInitialMarkingToActualState();
			updateViews();
		}
	}

	private String attemptToDeleteSelectedField(String deletedNodeId) {
		List<PField> copyOfFieldList = new ArrayList<PField>(model.getFieldList());
		for (PField field : copyOfFieldList) {
			if (field.getId().equals(selectedGraphElementId)) {
				deletedNodeId = selectedGraphElementId;
				selectedGraphElementId = "";
				pushTextToTextListeners("Stelle " + field.getId() + " wurde gelöscht\n");
				model.getFieldList().remove(field);
			}
		}
		return deletedNodeId;
	}

	private String attemptToDeleteSelectedTransition(String deletedNodeId) {
		List<PTransition> copyOfTransitionList = new ArrayList<>(model.getTransitionList());
		for (PTransition t : copyOfTransitionList) {
			if (t.getId().equals(selectedGraphElementId)) {
				deletedNodeId = selectedGraphElementId;
				selectedGraphElementId = "";
				pushTextToTextListeners("Transition " + t.getId() + " wurde gelöscht\n");
				model.getTransitionList().remove(t);
			}
		}
		return deletedNodeId;
	}
	
	private void deleteEdgesToAndFromNodewithId(String deletedNodeId) {
		List<PEdge> copyOfEdgeList = new ArrayList<PEdge>(model.getEdgeList());
		for (PEdge e : copyOfEdgeList) {
			if (e.getFrom().getId().equals(deletedNodeId) || e.getTo().getId().equals(deletedNodeId)) {
				pushTextToTextListeners("Kante " + e.getId() + " wurde gelöscht\n");
				model.deleteEdgeSafely(e);
			}
		}
	}



	@Override
	public void changeDataModelTo(DataModel model1) {
		model = model1;
	}

	@Override
	public void changeModeTo(EBuilderModes m) {
		this.mode = m;

	}

}
