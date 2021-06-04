package gui.toolbar;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import enums.EBuilderModes;
import interfaces.IPBuilderToolbarListener;

/**
 * Die Toolbar des Programmes für die Funktionen des Bearbeitungsmodus.
 * Implementierte Funktionen: inkrementieren von Marken, dekrementieren von
 * Marken, Auswahlmodus, Stellen hinzufügen, Transitionen hinzufügen, Kanten
 * hinzufügen, Knoten löschen, Labels bearbeiten.
 */
public class BuildModeToolbar extends JToolBar {
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;
	private List<IPBuilderToolbarListener> builders;
	private JFrame superFrame;

	private static Color TOOLBARCOLOR = new Color(75, 75, 75);

	private final static URL BUTTON_MINUS_FILE = PetrinetToolbar.class.getResource("/buttonimages/button_minus.png");
	private final static URL BUTTON_PLUS_FILE = PetrinetToolbar.class.getResource("/buttonimages/button_plus.png");
	private final static URL SELECT_FILE = PetrinetToolbar.class.getResource("/buttonimages/select.png");
	private final static URL ADD_FIELD_FILE = PetrinetToolbar.class.getResource("/buttonimages/add_field.png");
	private final static URL ADD_TRANSITION_FILE = PetrinetToolbar.class
			.getResource("/buttonimages/add_transition.png");
	private final static URL ADD_EDGE_FILE = PetrinetToolbar.class.getResource("/buttonimages/add_edge.png");
	private final static URL ADD_LABEL_FILE = PetrinetToolbar.class.getResource("/buttonimages/addLabel.png");
	private final static URL DELETE_NODE_FILE = PetrinetToolbar.class.getResource("/buttonimages/deleteNode.png");

	/**
	 * Konstruktor
	 * 
	 * @param superFrame der FRame zu dem PopUp Fenster ausgerichtet werden sollen.
	 */
	public BuildModeToolbar(JFrame superFrame) {
		super();
		this.superFrame = superFrame;
		this.builders = new ArrayList<>();
		this.setBackground(TOOLBARCOLOR);
		initialisePlusButton();
		initialiseMinusButton();
		initialiseSelectModeButton();
		initialiseAddFieldButton();
		initialiseAddTransitionButton();
		initialiseAddEdgeButton();
		initialiseDeleteNodeButton();
		initialiseSetLabelButton();
		
		this.setFloatable(false);
	}

	private void initialiseSetLabelButton() {
		JButton labelButton = new PetrinetToolbarButton(new ImageIcon(ADD_LABEL_FILE));
		labelButton.setToolTipText(
				"Label setzen : Ändert das Label des ausgewälten Knoten auf den Text des Eingabe-Fensters");
		labelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String label = JOptionPane.showInputDialog(superFrame,
						"Neues Label für selektierten Knoten eingeben: ");
				for (IPBuilderToolbarListener builder : builders) {
					builder.setLabelOfSelectedGraphElementTo(label);
				}

			}
		});
		this.add(labelButton);
	}

	private void initialiseDeleteNodeButton() {
		JButton deleteNodeButton = new PetrinetToolbarButton(new ImageIcon(DELETE_NODE_FILE));
		deleteNodeButton.setToolTipText("Knoten löschen : löscht den ausgewählten Knoten");
		deleteNodeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPBuilderToolbarListener builder : builders) {
					builder.deleteSelectedNode();
				}
			}
		});
		this.add(deleteNodeButton);
	}

	private void initialiseAddEdgeButton() {
		JButton addEdgeButton = new PetrinetToolbarButton(new ImageIcon(ADD_EDGE_FILE));
		addEdgeButton.setToolTipText(
				"Kante hinzufügen : durch Klick auf einen Knoten wird eine Kante zwischen dem ausgewählten Knoten und diesem erzeugt.");
		addEdgeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPBuilderToolbarListener builder : builders) {
					builder.changeModeTo(EBuilderModes.ADD_EDGE_MODE);
				}

			}
		});
		this.add(addEdgeButton);
	}

	private void initialiseAddTransitionButton() {
		JButton addTransitionButton = new PetrinetToolbarButton(new ImageIcon(ADD_TRANSITION_FILE));
		addTransitionButton.setToolTipText(
				"Transition hinzufügen : In diesem Modus können mit Doppelklick Transitionen erzeugt werden");
		addTransitionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPBuilderToolbarListener builder : builders) {
					builder.changeModeTo(EBuilderModes.ADD_TRANSITION_MODE);
					;
				}
			}
		});
		this.add(addTransitionButton);
	}

	private void initialiseAddFieldButton() {
		JButton addFieldButton = new PetrinetToolbarButton(new ImageIcon(ADD_FIELD_FILE));
		addFieldButton
				.setToolTipText("Stellen hinzufügen : In diesem Modus können mit Doppelklick Stellen erzeugt werden");
		addFieldButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPBuilderToolbarListener builder : builders) {
					builder.changeModeTo(EBuilderModes.ADD_FIELD_MODE);
					;
				}
			}
		});
		this.add(addFieldButton);
	}

	private void initialiseSelectModeButton() {
		JButton selectModeButton = new PetrinetToolbarButton(new ImageIcon(SELECT_FILE));
		selectModeButton.setToolTipText("Auswahl : in diesem Modus können Knoten selektiert werden.");
		selectModeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPBuilderToolbarListener builder : builders) {
					builder.changeModeTo(EBuilderModes.SELECT_MODE);
					;
				}
			}
		});
		this.add(selectModeButton);
	}

	private void initialiseMinusButton() {
		JButton minusButton = new PetrinetToolbarButton(new ImageIcon(BUTTON_MINUS_FILE));
		minusButton.setToolTipText("Vermindert die Marken des ausgewählten Feldes um 1");
		minusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPBuilderToolbarListener b : builders) {
					b.decraseMarksOfSelectedFieldByOne();
				}
			}
		});
		add(minusButton);
	}

	private void initialisePlusButton() {
		JButton plusButton = new PetrinetToolbarButton(new ImageIcon(BUTTON_PLUS_FILE));
		plusButton.setToolTipText("Erhöht die Marken des ausgewählten Feldes um 1");
		plusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPBuilderToolbarListener b : builders) {
					b.increaseMarksOfSelectedFieldByOne();
				}

			}
		});
		add(plusButton);
	}
	
	/**
	 * Meldet einen PetrinetBuilder als Listener an die Toolbar an.
	 * 
	 * @param builder der anzumeldende PetrinetBuilder
	 */
	public void addBuilderAsListener(IPBuilderToolbarListener builder) {
		builders.add(builder);
	}

}
