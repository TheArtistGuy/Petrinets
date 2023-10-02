package controller.filemanager;

import java.io.File;

import exceptions.InvalidEdgeException;
import interfaces.IModel;
import interfaces.IModelChanger;
import interfaces.ITextWindow;
import model.DataModel;
import controller.filemanager.woped.PNMLWopedWriter;

/**
 * Der FileHandler ist der Handler aller Übersetzungsprozesse von PNML Dateien
 * und dem DataModel. Es besteht kein direkter zugriff auf das DataModel, statt
 * dessen wird der Zugriff über einen {@link IModelChanger} geregelt. In unserem Fall
 * dem {@link controller.Controller}.
 *
 */

public class FileHandler {
	private IModelChanger modelChanger;
	private ITextWindow txtWindow;

	/**
	 * Konstruktor
	 * 
	 * @param fileChanger Interface zu dem Controller der Kontrolliert was das
	 *                    aktuell betrachtete Data Model ist.
	 * @param txtWindow   TextWindow zum Anzeigen von Nachrichten
	 */
	public FileHandler(IModelChanger fileChanger, ITextWindow txtWindow) {
		this.modelChanger = fileChanger;
		this.txtWindow = txtWindow;
	}

	/**
	 * Lädt eine Datei und erzeugt ein neues Data Model. Dieses ersetzt über den
	 * ModelChanger das entsprechende vorher betrachtete Model.
	 * 
	 * @param file die Datei
	 */

	public void loadFile(File file) {
		PNMLWopedParserWithBuffer parser = new PNMLWopedParserWithBuffer(file);
		txtWindow.pushText(file.getName() + " wird geladen\n");
		try {
			parser.initParser();
			parser.parse();

			try {
				DataModel model = parser.flushBufferAndCreateModel();
				modelChanger.changeDataModelTo(model);
			} catch (InvalidEdgeException e) {
				txtWindow.pushText(file.getName() + ": Datei ist kein Petrinetz");
			}
		} catch (Exception e) {
			txtWindow.pushText("Datei konnte nicht geladen werden.");
		}
	}

	/**
	 * Bearbeitet mehrere Dateien welche in einem Array übergeben werden. Errechnet
	 * ob diese beschränkt sind und gibt das Ergebnis über das angemeldete Text
	 * Window aus.
	 * 
	 * @param files Die zu bearbeiteten Dateien
	 */
	public void batchProcessing(File[] files) {
		MultipleFileAnalyser mfa = new MultipleFileAnalyser(files, txtWindow);
		mfa.run();
	}

	/**
	 * Speichert das Aktuelle Petrinetz in der angegebenen Datei
	 * @param file die Datei
	 */
	public void save (File file) {
		PNMLWopedWriter writer = new PNMLWopedWriter(file);
		IModel model = this.modelChanger.getCurrentModel();
		writer.startXMLDocument();
		for (var transition : model.getTransitionList()){
			writer.addTransition(
					transition.getId(),
					transition.getLabel(),
					String.valueOf(transition.getPosition().x),
					String.valueOf(transition.getPosition().y)
			);
		}
		for (var field : model.getFieldList()){
			writer.addPlace(
					field.getId(),
					field.getLabel(),
					String.valueOf(field.getPosition().x),
					String.valueOf(field.getPosition().y),
					String.valueOf(field.getTokens())
			);
		}
		for (var arc : model.getEdgeList()){
			writer.addArc(
					arc.getId(),
					arc.getFrom().getId(),
					arc.getTo().getId()
			);
		}
		writer.finishXMLDocument();
	}

	public void resetGraph() {
		this.modelChanger.changeDataModelTo(new DataModel());
	}
}
