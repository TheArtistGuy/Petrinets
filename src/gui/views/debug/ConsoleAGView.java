package gui.views.debug;

import interfaces.IViewer;
import model.DataModel;
import model.elements.PEdge;
import model.elements.PMarking;

/**
 * Viewer für den Erreichbarkeitsgraphen zu Debug Zwecken
 */

public class ConsoleAGView implements IViewer {
	private DataModel model;

	/**
	 * Konstruktor
	 * 
	 * @param model Das DataModel
	 */
	public ConsoleAGView(DataModel model) {
		this.model = model;
	}

	@Override
	public void refreshView() {
		if (!model.getAccessabilityGraph().getIsValid()) {
			System.out.println("Petrinetz ist nicht zulässig");
		} else {

			for (PMarking m : model.getAccessabilityGraph().getListOfMarkings()) {
				System.out.print(m.getId() + " : ");
				for (PEdge a : m.getListOfOutgoingEdges()) {
					System.out.print(a.getTo().getId() + " ");
				}
				System.out.println();
			}
		}
	}

	@Override
	public void changeViewedModelTo(DataModel model) {
		this.model = model;
	}

}
