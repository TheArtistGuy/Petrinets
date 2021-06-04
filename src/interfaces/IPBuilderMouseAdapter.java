package interfaces;

/**
 * Interface für die Funktionen eines {@link controller.PetrinetBuilder}s, die über einen Maus Adapter gesteuert werden.
 *
 */
public interface IPBuilderMouseAdapter{
	/**
	 * Ein Click wurde in x, y registriert
	 * @param x Koordinate x
	 * @param y Koordinate y
	 */
	public void ClickOccurred(int x, int y);
}
