package gui.toolbar;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import interfaces.IPetrinetToolBarListener;

/**
 * Die Toolbar des Programmes mit den Funktionen des Ansichtsmodus.
 * Implementierte Funktionen: Marken eines ausgewählten Knotens inkrementieren,
 * ,Marken eines ausgewählten Knotens dekrementieren, Graphen auf
 * Ursprungsmarkierung zurücksetzen, Löschen eines Erreichbarkeitsgraphen,
 * Löschen der Darstellung des Weges zwischen M und M'
 */
public class PetrinetToolbar extends JToolBar {
	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private List<IPetrinetToolBarListener> listeners;
	private NotificationWindow notificationWindow;
	// Der übergeordnete Frame
	private JFrame superFrame;

	private static Color TOOLBARCOLOR = new Color(75, 75, 75);

	private final static URL BUTTON_MINUS_FILE = PetrinetToolbar.class.getResource("/buttonimages/button_minus.png");
	private final static URL BUTTON_PLUS_FILE = PetrinetToolbar.class.getResource("/buttonimages/button_plus.png");
	private final static URL TO_START_FILE = PetrinetToolbar.class.getResource("/buttonimages/toStart.png");
	private final static URL DELETE_AG_BUTTON = PetrinetToolbar.class.getResource("/buttonimages/deleteAG.png");
	private final static URL DELETE_TEXT_BUTTON = PetrinetToolbar.class.getResource("/buttonimages/deleteText.png");
	private final static URL IS_LIMITED_BUTTON = PetrinetToolbar.class.getResource("/buttonimages/limited2.png");
	private final static URL DELETE_PATH_BUTTON = PetrinetToolbar.class.getResource("/buttonimages/deletePath2.png");

	/**
	 * Konstruktor
	 * 
	 * @param superFrame der Frame zu dem die Toolbar gehört
	 */
	public PetrinetToolbar(JFrame superFrame) {
		this.superFrame = superFrame;
		this.listeners = new LinkedList<>();
		this.notificationWindow = new NotificationWindow();
		initializePlusButton();
		initializeMinusButton();
		initialiseSetToInitMarkingButton();
		initialiseDeleteAGButton();
		initialiseResetTextButton();
		initialiseValidateGraphButton();
		initialiseDeleteUnLimitedGraphics();

		this.setBackground(TOOLBARCOLOR);
		this.setFloatable(false);
		setVisible(true);
	}

	private void initialiseDeleteUnLimitedGraphics() {
		JButton deleteUnlimitedGraphicsButton = new PetrinetToolbarButton(new ImageIcon(DELETE_PATH_BUTTON));
		deleteUnlimitedGraphicsButton.setToolTipText("lösche die Darstellung von M, M' und des Pfades dazwischen.");
		deleteUnlimitedGraphicsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPetrinetToolBarListener iPetrinetToolBarListener : listeners) {
					iPetrinetToolBarListener.deleteUnlimitationInformation();
				}

			}
		});
		add(deleteUnlimitedGraphicsButton);
	}

	private void initialiseValidateGraphButton() {
		JButton validateGraphButton = new PetrinetToolbarButton(new ImageIcon(IS_LIMITED_BUTTON));
		validateGraphButton.setToolTipText("bestimme ob das Petrinetz beschränkt ist");
		validateGraphButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPetrinetToolBarListener toolBarListener : listeners) {
					boolean isValid = toolBarListener.validateAccessabilityGraph(true, true);
					if (isValid) {
						notificationWindow.setNotificationLabel("Das Petrinetz ist beschränkt");
					} else {
						notificationWindow.setNotificationLabel("Das Petrinetz ist unbeschränkt");
					}
					notificationWindow.setLocationRelativeTo(superFrame);
					notificationWindow.setVisible(true);
				}

			}
		});
		add(validateGraphButton);
	}

	private void initialiseResetTextButton() {
		JButton resetTextButton = new PetrinetToolbarButton(new ImageIcon(DELETE_TEXT_BUTTON));
		resetTextButton.setToolTipText("löscht den Text im Textfenster");
		resetTextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPetrinetToolBarListener toolBarListener : listeners) {
					toolBarListener.resetTextFields();
				}

			}
		});
		add(resetTextButton);
	}

	private void initialiseDeleteAGButton() {
		JButton deleteEGButton = new PetrinetToolbarButton(new ImageIcon(DELETE_AG_BUTTON));
		deleteEGButton.setToolTipText("Löscht den angezeigten Erreichbarkeitsgraphen");
		deleteEGButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPetrinetToolBarListener toolBarListener : listeners) {
					toolBarListener.deleteAccessabilityGraphAndResetToInitialMarking();
				}
			}
		});
		add(deleteEGButton);
	}

	private void initialiseSetToInitMarkingButton() {
		JButton setToInitMarkingButton = new PetrinetToolbarButton(new ImageIcon(TO_START_FILE));
		setToInitMarkingButton.setToolTipText(
				"Setzt das Petrinetz auf die Startmarkierung zurück ohne den Erreichbarkeitsgraph zu löschen");
		setToInitMarkingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPetrinetToolBarListener l : listeners) {
					l.resetToInitialMarking();
				}

			}
		});
		add(setToInitMarkingButton);
	}

	private void initializeMinusButton() {
		JButton minusButton = new PetrinetToolbarButton(new ImageIcon(BUTTON_MINUS_FILE));
		minusButton.setToolTipText("Vermindert die Marken des ausgewählten Feldes um 1");
		minusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPetrinetToolBarListener l : listeners) {
					l.decraseMarksOfSelectedFieldByOne();
				}
			}
		});
		add(minusButton);
	}

	private void initializePlusButton() {
		JButton plusButton = new PetrinetToolbarButton(new ImageIcon(BUTTON_PLUS_FILE));
		plusButton.setToolTipText("Erhöht die Marken des ausgewählten Feldes um 1");
		plusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (IPetrinetToolBarListener l : listeners) {
					l.increaseMarksOfSelectedFieldByOne();
				}

			}
		});
		add(plusButton);
	}

	/**
	 * Meldet einen IPetrinetToolBarListener an die Toolbar an.
	 * 
	 * @param listener der IPetrinetToolbarListener
	 */
	public void addToolBarListener(IPetrinetToolBarListener listener) {
		listeners.add(listener);
	}

}
