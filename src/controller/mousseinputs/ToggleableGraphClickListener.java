package controller.mousseinputs;

import interfaces.IMouseInputListener;
import org.graphstream.ui.view.ViewerListener;

/**
 * Ein {@link ViewerListener} um Maus-Eingaben aus dem Petrinetz dargestellt mit
 * GraphStream zu verarbeiten und an genau einen {@link IMouseInputListener} weiterzuleiten.
 * Dieser ist an/ausschalbar um Eingaben zwischen mehreren {@link IMouseInputListener}
 * umzuschalten.
 *
 */
public class ToggleableGraphClickListener implements ViewerListener {
	private boolean isActive;
	private final IMouseInputListener listener;

	/**
	 * Konstruktor
	 * 
	 * @param isActiv  ist der GraphClickListener aktiviert?
	 * @param listener der angemeldete MouseInputListener
	 */
	public ToggleableGraphClickListener(boolean isActiv, IMouseInputListener listener) {
		this.isActive = isActiv;
		this.listener = listener;
	}

	/**
	 * Keine Action
	 */
	@Override
	public void viewClosed(String viewName) {

	}

	/**
	 * Bei Knopfdruck wird der Knoten mit der übergebenen id ausgewählt, falls der
	 * GraphClickListener angeschaltet ist.
	 */
	@Override
	public void buttonPushed(String id) {
		if (isActive) {
			listener.selectLabeledNode(id);
		}
	}

	/**
	 * Keine Aktion
	 */
	@Override
	public void buttonReleased(String id) {

	}

	/**
	 * Methode um den Click Listener aktiv zu schalten
	 * 
	 * @param b ist er aktiv?
	 */
	public void setIsActive(boolean b) {
		this.isActive = b;
	}

}
