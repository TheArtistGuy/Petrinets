package interfaces;

/**
 * Das Interface stellt Methoden bereit um einen Listener über Eingaben der
 * Tollbar zu informieren.
 *
 */

public interface IPetrinetToolBarListener {
	/**
	 * vermindere die Marken auf dem ausgewählten Feld um 1
	 */
	public void decraseMarksOfSelectedFieldByOne();

	/**
	 * erhöhe die Marken auf dem ausgewählten Feld um 1
	 */
	public void increaseMarksOfSelectedFieldByOne();

	/**
	 * setzt den Zustand des Petrinetzes auf die Ausgangsmarkierung zurück
	 */
	public void resetToInitialMarking();

	/**
	 * löscht den Erreichbarkeitsgraphen und setzt das Petrinetz auf die
	 * Anfangsmarkierung zurück
	 */
	public void deleteAccessabilityGraphAndResetToInitialMarking();

	/**
	 * Baut einen Erreichberkeitsgraphen und bestimmt ob dieser Beschränkt ist.
	 * 
	 * @param showInTextWindows      soll das Ergebnis in den angemeldeten Text
	 *                               Windows angegeben werden?
	 * 
	 * @param mergeWithExistingGraph soll der durch den Algorithmus gebaute Graph
	 *                               mit dem manuell erstellten zusammengefügt
	 *                               werden?
	 * 
	 * @return ist das Petrinetz unbeschränkt?
	 */
	public boolean validateAccessabilityGraph(boolean showInTextWindows, boolean mergeWithExistingGraph);

	/**
	 * löscht den Text in angemeldeten iTextWindows
	 */
	public void resetTextFields();

	/**
	 * setzt die Informationen der Unbeschränktheitsprüfung zurück, also M, M', den
	 * Pfad dazwischen und die information der Unbeschränktheit.
	 */
	public void deleteUnlimitationInformation();
}
