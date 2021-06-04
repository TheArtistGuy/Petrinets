package gui.menubar;

import java.io.File;

import javax.swing.JFileChooser;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Die Klasse realisiert einen {@link JFileChooser} um PNML-Dateien zu laden.
 * Standardmässig ist es auf das Verzeichnis ../ProPra-WS20-Basis/Beispiele/
 * gesetzt.
 *
 */
class OpenPNMLDialog extends JFileChooser {
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Konstruktor
	 */
	protected OpenPNMLDialog() {
		setCurrentDirectory(new File("../ProPra-WS20-Basis/Beispiele/"));
		setFileSelectionMode(FILES_AND_DIRECTORIES);
		addChoosableFileFilter(new FileNameExtensionFilter("Petrinet File", "pnml"));
		setAcceptAllFileFilterUsed(false);
		setVisible(true);
	}

}
