package gui.views.debug;

import interfaces.IViewer;
import model.DataModel;
import model.elements.PField;

/**
 * Viewer für das Petrinetz zu Debug zwecken
 *
 */

public class ConsoleView implements IViewer {
	private DataModel model;

	/**
	 * Konstruktor
	 * 
	 * @param model das DataModel
	 */
	public ConsoleView(DataModel model) {
		this.model = model;
	}

	@Override
	public void refreshView() {
		for (PField f : model.getFieldList()) {
			System.out.print(" | " + f.getId() + "-" + f.getTokens());
		}
		System.out.println();
	}

	@Override
	public void changeViewedModelTo(DataModel model) {
		this.model = model;
	}

}
