package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import controller.Controller;
import controller.PetrinetBuilder;
import controller.mousseinputs.*;
import gui.views.PetrinetGraph;
import model.DataModel;

/**
 * Ein Panel zur Anzeige eines Petrinetzes durch ein {@link PetrinetGraph}.
 * Kann einen Schalter zurückgeben mit dem zwischen verschiedenen
 * Eingabe-Modi umgeschaltet werden kann.
 *
 */

class PetrinetGraphPanel extends JPanel {
	/**
	 * Default Serial Version Id
	 */
	private static final long serialVersionUID = 1L;
	private MouseInputSwitch mousseInputSwitch;
	private ViewPanel viewPanel;

	/**
	 * Konstruktor 
	 * @param model das Model welches die Informationen zur Anzeige des Graphen hält
	 * @param controller Controller zum Anmelden an das Panel
	 * @param builder Builder zum anmelden an das Panel
	 * @param size gewünschte Grösse des Panels
	 */
	PetrinetGraphPanel(DataModel model, Controller controller, PetrinetBuilder builder, Dimension size) {
		super(new BorderLayout());
		// Erzeuge Viewer mit passendem Threading-Model für Zusammenspiel mit
		// Swing
		PetrinetGraph petGraph = new PetrinetGraph(model);
		controller.addView(petGraph);
		builder.addView(petGraph);
		Viewer viewer = new Viewer(petGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		// ausschalten des AutoLayout
		viewer.disableAutoLayout();
		// viewer auf Panel abbilden ohne neues Fenster zu erzeugen
		viewPanel = viewer.addDefaultView(false);
		viewPanel.setPreferredSize(size);
		
		// Einbetten des ViewPanels in ein JPanel um Darstellungsprobleme zu vermeiden.
		this.add(BorderLayout.CENTER, viewPanel);
		this.setPreferredSize(size);

		ViewerPipe viewerPipe = viewer.newViewerPipe();
		this.mousseInputSwitch = initialiseMousseListeners(controller, builder, viewerPipe);

	}

	private MouseInputSwitch initialiseMousseListeners(Controller controller, PetrinetBuilder builder,
			ViewerPipe viewerPipe) {
		ToggleableGraphClickListener viewModeClickListener = new ToggleableGraphClickListener(true, controller);
		ToggleableGraphClickListener buildModeClickListener = new ToggleableGraphClickListener(false, builder);

		// clickListener als ViewerListener bei der viewerPipe anmelden
		viewerPipe.addViewerListener(viewModeClickListener);
		viewerPipe.addViewerListener(buildModeClickListener);

		ToggleableMouseAdapter viewModeMousseAdapter = new ToggleableMouseAdapter(viewerPipe, true);
		ToggleableMouseAdapterBuildMode buildModeMousseAdapter = new ToggleableMouseAdapterBuildMode(builder,
				viewerPipe, false);

		// Neuen MouseListener beim viewPanel anmelden.
		viewPanel.addMouseListener(viewModeMousseAdapter);
		viewPanel.addMouseListener(buildModeMousseAdapter);

		return new MouseInputSwitch(viewModeMousseAdapter, buildModeMousseAdapter, viewModeClickListener,
				buildModeClickListener);
	}

	/**
	 * Getter Methode für den inputSwitch des ViewPanels. Mit diesem können die
	 * MausEingaben zwischen dem Controller und dem Builder umgeschaltet werden.
	 * 
	 * @return der MousseInputSwitch
	 */
	public MouseInputSwitch getMousseInputSwitch() {
		return this.mousseInputSwitch;
	}

	/**
	 * Getter Methode für das ViewPanel, das in dem Petrinet Panel dargestellt wird
	 * 
	 * @return das ViewPanel
	 */
	public ViewPanel getViewPanel() {
		return this.viewPanel;
	}
}
