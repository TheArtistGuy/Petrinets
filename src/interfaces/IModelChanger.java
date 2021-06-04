package interfaces;

import model.DataModel;

/**
 * Das Interface IModelChanger gewährt Zugriff auf eine Methode die es erlaubt
 * das {@link DataModel} einer anderen Klasse gegen ein anderes zu tauschen.
 *
 */

public interface IModelChanger {
	/**
	 * Tauscht des bekannte DataModel einer Klasse gegen das übergebene DataModel
	 * 
	 * @param model das übergebene DataModel
	 */
	public void changeDataModelTo(DataModel model);

}
