import gui.PetrinetMainFrame;

/**
 * Die Main Methode des Programms. Es wird es gestartet in dem es eine neue Instanz des PetrinetMainFrame erzeugt.
 *
 */
public class Main {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> new PetrinetMainFrame());
	}
}
