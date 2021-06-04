package interfaces;

/**
 * Interface zur Implementierung eines TextFeldes, das Nachrichten
 * entgegennehmen kann.
 *
 */

public interface ITextWindow {
	/**
	 * Hängt einen übergebenen Text an den bereits dargestellten Text an.
	 * 
	 * @param text der übergebene Text
	 */
	public void pushText(String text);

	/**
	 * Setzt den Text eines IText Window auf den übergebenen Text
	 * 
	 * @param text der übergebene Text
	 */
	public void setTextTo(String text);

	/**
	 * Löscht den bereits getzten Text
	 */
	public void clear();
}
