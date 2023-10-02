package interfaces;

import model.DataModel;

/**
 * Das Interface stellt Methoden bereit um mit Views zu kommunizieren.
 *
 */
public interface IViewer {
	/**
	 * gleiche die Darstellung mit dem Betrachteten Model ab
	 */
    void refreshView();

	/**
	 * ändere das Betrachtete Model zu dem übergebenen DataModel
	 * 
	 * @param model das übergebene DataModel
	 */
    void changeViewedModelTo(DataModel model);
}