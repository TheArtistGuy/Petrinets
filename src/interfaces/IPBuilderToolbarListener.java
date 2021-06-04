package interfaces;

import enums.EBuilderModes;

/**
 * Stellt Funktionen bereit um ein Petrinetz zu bearbeiten, die Über eine
 * Toolbar angesprochen werden.
 *
 */
public interface IPBuilderToolbarListener {
	/**
	 * Wechselt in den angegebenen Modus
	 * 
	 * @param mode der Modus
	 */
	public void changeModeTo(EBuilderModes mode);

	/**
	 * Setzt das Label des angewählten Knotens auf des übergebene Label
	 * 
	 * @param label das Label
	 */
	public void setLabelOfSelectedGraphElementTo(String label);

	/**
	 * vermindert die Marken auf der gewählten Stelle um 1
	 */
	public void decraseMarksOfSelectedFieldByOne();

	/**
	 * erhöht die Marken auf der gewählten Stelle um 1
	 */
	public void increaseMarksOfSelectedFieldByOne();

	/**
	 * löscht den gewählten Knoten
	 */
	public void deleteSelectedNode();
}
