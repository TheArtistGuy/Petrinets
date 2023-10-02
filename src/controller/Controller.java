package controller;

import java.util.ArrayList;
import java.util.List;

import interfaces.*;
import model.DataModel;
import model.elements.*;

/**
 * Der Controller koordiniert alle Aktionen. Nimmt Eingaben entgegen, verändert
 * wenn nötig das {@link DataModel}, benachrichtigt angemeldete {@link IViewer}
 * und sendet Nachrichten an angemeldete {@link ITextWindow}s.
 *
 */

public class Controller implements IModelChanger, IMouseInputListener, IPetrinetToolBarListener {
	protected IModel model;
	private final List<IViewer> views;
	private final List<ITextWindow> textWindows;
	private AGBuilder acessabilityGraphBuilder;
	private final List<IModelChanger> modelChangers;

	/**
	 * Konstruktor
	 * 
	 * @param model das Model das der Controller kontrolliert
	 */
	public Controller(IModel model) {
		this.model = model;
		this.acessabilityGraphBuilder = new AGBuilder(model);
		this.views = new ArrayList<>(2);
		this.textWindows = new ArrayList<>(1);
		this.modelChangers = new ArrayList<>(1);
	}

	/**
	 * Anmelden eines IViewer an den Controller
	 * 
	 * @param view Der IViewer
	 */
	public void addView(IViewer view) {
		views.add(view);
	}

	/**
	 * Fügt der Liste der IModelChanger ein Element hinzu. Diese werden informiert,
	 * falls das vom Controller betrachtete Model geändert wird.
	 * 
	 * @param modelChanger der IModelChanger
	 */
	public void addModelChangers(IModelChanger modelChanger) {
		modelChangers.add(modelChanger);
	}

	/**
	 * Anmelden eines ITextWindow als Listener an den Controller um dort Text
	 * auszugeben.
	 * 
	 * @param window das ITextWindow
	 */
	public void addTextWindowListener(ITextWindow window) {
		textWindows.add(window);
	}

	/**
	 * Benachrichtigt die angemeldeten Views, dass eine Änderung eingetreten ist und
	 * sie sich Updaten müssen.
	 */

	protected void updateViews() {
		for (IViewer v : views) {
			v.refreshView();
		}
	}

	/**
	 * Baut einen Erreichberkeitsgraphen und bestimmt ob dieser Beschränkt ist.
	 * 
	 * @param showInTextWindows      soll das Ergebnis in den angemeldeten Text
	 *                               Windows angegeben werden?
	 * 
	 * @param mergeWithExistingGraph sollen die bereits dargestellten Elemente des
	 *                               EG erhalten bleiben?
	 * 
	 * @return ist das Petrinetz unbeschränkt?
	 */
	@Override
	public boolean validateAccessabilityGraph(boolean showInTextWindows, boolean mergeWithExistingGraph) {

		acessabilityGraphBuilder.buildFullAccesabilityGraph(mergeWithExistingGraph);
		if (showInTextWindows) {
			writeResultOfLimitationTestToTextWindow();
		}
		updateViews();
		return model.getAccessabilityGraph().getIsValid();
	}

	private void writeResultOfLimitationTestToTextWindow() {
		pushTextToTextListeners("Analysiere Petrinetz :");
		if (model.getAccessabilityGraph().getIsValid()) {
			pushTextToTextListeners("Der Graph ist beschränkt mit (Knoten/Kanten) : "
					+ model.getAccessabilityGraph().getValidityRepresentation() + "\n");

		} else {
			pushTextToTextListeners("Der Graph ist unbeschränkt (Pfad, m, m') : "
					+ model.getAccessabilityGraph().getValidityRepresentation() + "\n");

		}
	}

	@Override
	public void changeDataModelTo(DataModel newModel) {
		this.model = newModel;
		newModel.setInitialMarkingToActualState();
		this.acessabilityGraphBuilder = new AGBuilder(newModel);
		for (IViewer view : views) {
			view.changeViewedModelTo(newModel);
		}
		for (IModelChanger mc : modelChangers) {
			mc.changeDataModelTo(newModel);
		}
	}

	@Override
	public IModel getCurrentModel() {
		return this.model;
	}

	@Override
	public void decraseMarksOfSelectedFieldByOne() {
		if (model.getSelectedNode() != null && model.getSelectedNode().getClass().isInstance(new PField(""))) {
			((PField) model.getSelectedNode()).decreaseMarksByOne();
			model.setInitialMarkingToActualState();
			updateViews();
		}
	}

	@Override
	public void increaseMarksOfSelectedFieldByOne() {
		if (model.getSelectedNode() != null && model.getSelectedNode().getClass().isInstance(new PField(" "))) {
			((PField) model.getSelectedNode()).increaseMarksByOne();
			model.setInitialMarkingToActualState();
			updateViews();
		}
	}


	/**
	 * Wählt den Knoten mit der übergebenen id an. Ist der Knoten eine Stelle wird
	 * sie nur angewählt. Ist er eine Transition wird diese geschaltet, falls sie
	 * aktiviert ist. Ist der Knoten eine Markierung wird das Petrinetz auf den
	 * Zustand der Markierung gesetzt.
	 * 
	 * @param id die id des Knotens
	 */
	@Override
	public void selectLabeledNode(String id) {
		PLabeledNode matchingNode = searchFieldTransitionAndMarkingListForMatch(id);
		if (matchingNode != null) {
			if (matchingNode.getClass().isInstance(new PTransition(""))) {
				shiftTransition((PTransition) matchingNode);
			} else if (matchingNode.getClass().isInstance(new PField(""))) {
				model.setSelectedNode(matchingNode);
				pushTextToTextListeners("Feld " + matchingNode.getId() + " ausgewählt.\n");
			} else if (matchingNode.getClass().isInstance(new PMarking(null))) {
				model.setFieldsToMarking((PMarking) matchingNode);
				pushTextToTextListeners("Petrinetz auf Markierung " + matchingNode.getId() + " gesetzt.\n");
			}

			updateViews();
		}

	}
	
	private PLabeledNode searchFieldTransitionAndMarkingListForMatch(String id) {
		
		for (PField f : model.getFieldList()) {
			if (f.getId().equals(id)) {
				return f;
			}
		}
		for (PTransition t : model.getTransitionList()) {
			if (t.getId().equals(id)) {
				return t;
			}
		}
		for (PMarking p : model.getAccessabilityGraph().getListOfMarkings()) {
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	private void shiftTransition(PTransition t) {
		
		if (t.isReadyToShift()) {
			pushTextToTextListeners("Transiton " + t.getId() + " geschaltet.\n");
			PMarking activeMarking = new PMarking(model.getFieldList());
			acessabilityGraphBuilder.shiftTransitionAndCreateNewMarking(activeMarking, t);
		}
		updateViews();
	}
	
	@Override
	public void resetToInitialMarking() {
		model.resetFieldsToInitialMarking();
		model.getAccessabilityGraphMetaData().setLastAddedEdgeId(null);
		updateViews();
	}

	/**
	 * Schicke den übergebenen Text zur Anzeige, an angemeldete TextListener
	 * 
	 * @param text der Text
	 */
	protected void pushTextToTextListeners(String text) {
		for (ITextWindow iTextWindow : textWindows) {
			iTextWindow.pushText(text);
		}
	}

	@Override
	public void deleteAccessabilityGraphAndResetToInitialMarking() {
		model.resetFieldsToInitialMarking();
		model.getAccessabilityGraph().resetAcessabilityGraph();
		model.getAccessabilityGraph().addMarkingToListOfMarkings(model.getInitialMarking());
		updateViews();

	}

	@Override
	public void resetTextFields() {
		for (ITextWindow iTextWindow : textWindows) {
			iTextWindow.clear();
		}

	}

	@Override
	public void deleteUnlimitationInformation() {
		model.getAccessabilityGraph().setIsLimited(true);
		model.getAccessabilityGraphMetaData().setInvalidMId(null);
		model.getAccessabilityGraphMetaData().setInvalidMStrokeId(null);
		model.getAccessabilityGraphMetaData().deleteInvalidPath();
		updateViews();
	}

}
