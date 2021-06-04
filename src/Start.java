import gui.PetrinetMainFrame;

/**
 * Die Main Methode des Programms. Es wird es gestartet in dem es eine neue Instanz des PetrinetMainFrame erzeugt.
 *
 */
public class Start {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new PetrinetMainFrame();
			}
		});
	}
}
