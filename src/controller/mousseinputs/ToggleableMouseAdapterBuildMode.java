package controller.mousseinputs;

import java.awt.event.MouseEvent;

import org.graphstream.ui.view.ViewerPipe;

import interfaces.IPBuilderMouseAdapter;

/**
 * An/ Auschaltbarer MouseAdapter für {@link controller.PetrinetBuilder} der Ereignisse an eine
 * ViewerPipe weitergibt. Oder einen {@link controller.PetrinetBuilder} über Ereignisse informiert.
 */

public class ToggleableMouseAdapterBuildMode extends ToggleableMouseAdapter {
	private IPBuilderMouseAdapter builder;

	/**
	 * Konstruktor
	 * 
	 * @param builder  der angemeldete PetrinetBuilder
	 * @param pipe     Die ViewerPipe in die Ereignisse gepumpt werden sollen
	 * @param isActive ist der GraphClickListener aktiviert?
	 * 
	 */
	public ToggleableMouseAdapterBuildMode(IPBuilderMouseAdapter builder, ViewerPipe pipe, boolean isActive) {
		super(pipe, isActive);
		this.builder = builder;
	}

	/**
	 * Bei Maus-Klick wird das Ereignis an die ViewerPipe übergeben, bei Doppelklick
	 * der Builder über das Ereignis an den Koordianten informiert.
	 */
	
	@Override
	public void mousePressed(MouseEvent me) {
		if (isActive) {
			if (me.getClickCount() == 2) {
				builder.ClickOccurred(me.getX(), me.getY());
			} else {
				pipe.pump();
			}
		}
	}

	/**
	 * Keine Aktion
	 */
	@Override
	public void mouseReleased(MouseEvent me) {

	}
}
