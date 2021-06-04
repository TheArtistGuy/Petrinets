package interfaces;

/**
 * Das Interface stellt Methoden bereit um einen Listener über MouseEvents zu
 * informieren. In diesem Fall ob ein Knoten ausgewählt wurde.
 *
 *
 */
public interface IMouseInputListener {

	/**
	 * Wählt den Knoten mit der übergebenen Id an
	 * 
	 * @param id die Id des anzuwählenden Knotens
	 */
	public void selectLabeledNode(String id);
}
