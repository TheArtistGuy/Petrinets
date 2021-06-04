package controller.mousseinputs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.graphstream.ui.view.ViewerPipe;

/**
 * An / Ausschaltbarer {@link MouseAdapter} der Ereignisse an eine ViewerPipe
 * weitergibt.
 */

public class ToggleableMouseAdapter extends MouseAdapter {
	protected ViewerPipe pipe;
	protected boolean isActive;

	/**
	 * Konstruktor
	 * 
	 * @param pipe     die ViewerPipe an die Ereignisse weitergegeben werden
	 * @param isActive ist dieser MouseAdapter aktiviert?
	 */
	public ToggleableMouseAdapter(ViewerPipe pipe, boolean isActive) {
		super();
		this.pipe = pipe;
		this.isActive = isActive;
	}
	
	/**
	 * Bei Maus-Klick wird das Ereignis an die ViewerPipe übergeben
	 */
	@Override
	public void mousePressed(MouseEvent me) {
		if (isActive) {
			pipe.pump();
		}
	}

	/**
	 * Bei Maus-Release wird das Ereignis an die ViewerPipe übergeben
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
		if (isActive) {
			pipe.pump();
		}
	}

	/**
	 * Setter Methode die den Mousseadapter auf An/Aus Schaltet
	 * 
	 * @param b Ist der MousseAdapter aktiviert?
	 */
	public void setIsActive(boolean b) {
		this.isActive = b;
	}

}
