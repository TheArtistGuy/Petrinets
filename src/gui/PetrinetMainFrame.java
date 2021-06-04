package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import controller.Controller;
import controller.ModeManagerNode;
import controller.PetrinetBuilder;
import controller.mousseinputs.GraphClickListener;
import gui.menubar.PetrinetMenuBar;
import gui.toolbar.BuildModeToolbar;
import gui.toolbar.PetrinetToolbar;
import gui.toolbar.ToolbarSwitch;
import gui.views.AGViewer;
import model.DataModel;

/**
 * Der Zentrale Frame des Programms in dem alle Elemente dargestellt werden.
 * Desweiteren werden hier alle nötigen Klassen zum Programmstart initialisiert.
 *
 */
public class PetrinetMainFrame extends JFrame {

	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private PetrinetGraphPanel petrinetPanel;
	
	private ViewPanel acessabilityGraphViewPanel;
	private JPanel accessabilityGraphPanel;
	
	private TextPanel txtPanel;

	private JSplitPane horiziontalSplit;

	private Controller controller;
	private DataModel model;
	private PetrinetBuilder builder;
	private ModeManagerNode modeManagerNode;
	private ToolbarSwitch toolbarSwitch;

	public PetrinetMainFrame() {
		super("Kai Mueller - 3126463");

		// Renderer mit Unterstützung für Multigraphen und aller CSS Attribute
		// verwenden.
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.model = new DataModel();

		initialiseController();
		initialiseTextPanel();		
		initialiseToolbar();
		initialiseViews();
		initialiseModeManager();
		initialiseMenuBar();

		splitFrame();
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.validate();
	}

	private void splitFrame() {
		JSplitPane verticalSplit = initializeHorizontalAndVerticalSplit();
		this.add(verticalSplit);
	}

	private JSplitPane initializeHorizontalAndVerticalSplit() {
		horiziontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		horiziontalSplit.add(petrinetPanel);
		horiziontalSplit.add(accessabilityGraphPanel);
		horiziontalSplit.setResizeWeight(0.5);
		horiziontalSplit.setDividerSize(2);
		JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		verticalSplit.add(horiziontalSplit);
		verticalSplit.add(txtPanel);
		verticalSplit.setDividerSize(5);
		verticalSplit.setDividerLocation(450);
		verticalSplit.setResizeWeight(0.7);
		return verticalSplit;
	}
	
	private void initialiseMenuBar() {
		JMenuBar menuBar = new PetrinetMenuBar(controller, this, txtPanel, modeManagerNode);
		this.setJMenuBar(menuBar);
	}

	private void initialiseModeManager() {
		this.modeManagerNode = new ModeManagerNode();
		modeManagerNode.addModeManager(toolbarSwitch);
		modeManagerNode.addModeManager(petrinetPanel.getMousseInputSwitch());
		modeManagerNode.init();
	}

	private void initialiseViews() {
		// Erzeuge und initialisiere ein Panel zur Anzeige des Petrinetz-Graphen
		this.petrinetPanel = new PetrinetGraphPanel(model, controller, builder, new Dimension(500, 450));
		
		// Erzeuge und initialisiere ein Panel zur Anzeige des Erreichbarkeits-Graphen
		initalizeAccessabilityGraph();
	}

	private void initialiseToolbar() {
		// Toolbar für den Beobachtungsmodus
		PetrinetToolbar toolbar = new PetrinetToolbar(this);
		toolbar.addToolBarListener(controller);
		
		// Toolbar für den Bearbeitungsmodus
		BuildModeToolbar buildToolbar = new BuildModeToolbar(this);
		buildToolbar.addBuilderAsListener(builder);

		// ToolbarSwitch für beide regelt welche Toolbar im Frame angezeigt wird.
		this.toolbarSwitch = new ToolbarSwitch(this, toolbar, buildToolbar);
	}

	private void initialiseController() {
		this.controller = new Controller(model);
		this.builder = new PetrinetBuilder(model);
		// builder bei controller anmelden, dass er benachrichtigt werden will wenn sich
		// das Model ändert
		controller.addModelChangers(builder);
	}

	private void initialiseTextPanel() {
		txtPanel = new TextPanel();
		txtPanel.setPreferredSize(new Dimension(1000, 200));
		controller.addTextWindowListener(txtPanel);
		builder.addTextWindowListener(txtPanel);
	}

	

	private void initalizeAccessabilityGraph() {
		AGViewer acessabilityGraph = new AGViewer(model);
		controller.addView(acessabilityGraph);
		builder.addView(acessabilityGraph);
		// Viewer mit entsprechendem Threading Model erzeugen
		Viewer accViewer = new Viewer(acessabilityGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		// AutoLAyout einschalten
		accViewer.enableAutoLayout();
		// viewer auf Panel abbilden ohne neues Fenster zu erzeugen
		acessabilityGraphViewPanel = accViewer.addDefaultView(false);

		// Einbetten des ViewPanels in ein JPanel um Darstellungsprobleme zu vermeiden.
		accessabilityGraphPanel = new JPanel(new BorderLayout());
		accessabilityGraphPanel.add(BorderLayout.CENTER, acessabilityGraphViewPanel);
		accessabilityGraphPanel.setPreferredSize(new Dimension(500, 450));

		ViewerPipe viewerPipe = accViewer.newViewerPipe();
		GraphClickListener clickListener = new GraphClickListener(controller);

		// clickListener als ViewerListener bei der viewerPipe anmelden
		viewerPipe.addViewerListener(clickListener);

		// Neuen MouseListener beim viewPanel anmelden.
		acessabilityGraphViewPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {
				viewerPipe.pump();
			}

			@Override
			public void mouseReleased(MouseEvent me) {
				viewerPipe.pump();
			}
		});
	}

}
