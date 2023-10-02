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
    void changeModeTo(EBuilderModes mode);

	/**
	 * Setzt das Label des angewählten Knotens auf des übergebene Label
	 * 
	 * @param label das Label
	 */
    void setLabelOfSelectedGraphElementTo(String label);

	/**
	 * vermindert die Marken auf der gewählten Stelle um 1
	 */
    void decraseMarksOfSelectedFieldByOne();

	/**
	 * erhöht die Marken auf der gewählten Stelle um 1
	 */
    void increaseMarksOfSelectedFieldByOne();

	/**
	 * löscht den gewählten Knoten
	 */
    void deleteSelectedNode();
}
