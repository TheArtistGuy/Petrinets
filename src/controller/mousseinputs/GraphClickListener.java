package controller.mousseinputs;

import interfaces.IMouseInputListener;
import org.graphstream.ui.view.ViewerListener;

/**
 * Ein Click Listener um Maus-Eingaben aus dem Petrinetz dargestellt mit
 * GraphStream zu verarbeiten.
 *
 */

public class GraphClickListener implements ViewerListener {
	private IMouseInputListener listener;

	/**
	 * Konstruktor
	 * 
	 * @param listener der an den ClickListener angemeldete Controller
	 */
	public GraphClickListener(IMouseInputListener listener) {
		this.listener = listener;
	}

	/**
	 * Keine Aktion
	 */
	@Override
	public void viewClosed(String viewName) {

	}

	/**
	 * Bei Knopfdruck wird der Knoten mit der Übergebenen id ausgewählt.
	 */
	@Override
	public void buttonPushed(String id) {
		listener.selectLabeledNode(id);

	}

	/**
	 * Keine Aktion
	 */
	@Override
	public void buttonReleased(String id) {

	}

}
