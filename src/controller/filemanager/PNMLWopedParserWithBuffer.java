package controller.filemanager;

import java.awt.Point;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import exceptions.InvalidEdgeException;
import model.DataModel;
import model.elements.*;
import propra.pnml.PNMLWopedParser;

/**
 * Diese Klasse liest PNML-Datein und filtert die relevanten Informationen
 * heraus. Sie hat einen Buffer um diese Informationen aufzunehmen. Dieser
 * Zwischenschritt stellt sicher, dass die Reihenfolge der Informationen in der
 * Datei beliebig sein können, aber immer Kanten erst dem {@link DataModel} hinzugefügt
 * werden, nachdem es alle Knoten kennt.
 * 
 * Schritte um die informationen einer Datei in ein Data Model umzuwandeln:
 * initParser() parse() flushBufferAndCreateModel()
 *
 */

class PNMLWopedParserWithBuffer extends PNMLWopedParser {

	// Buffer zum Zwischenspeichern von Transitionen und Feldern
	private List<PLabeledNode> transitionAndFieldBuffer;
	// //Buffer zum Zwischenspeichern von Kanten
	private List<PEdge> edgeBuffer;

	/**
	 * Konstruktor
	 * 
	 * Liest nach der initialisierung eine pnml-Datei aus und filtert die für uns
	 * wichtigen Elemente heraus. Diese werden in die entsprechenden Buffer
	 * geschrieben und können in ein Data Model überführt werden.
	 * 
	 * 
	 * @param pnml die pnml-Datei
	 */

	protected PNMLWopedParserWithBuffer(File pnml) {
		super(pnml);
		transitionAndFieldBuffer = new LinkedList<>();
		edgeBuffer = new LinkedList<>();
	}

	/**
	 * Erzeugt eine neue Transition mit der Id des übergebenen Strings
	 * 
	 * @param id die Id
	 */
	@Override
	public void newTransition(String id) {
		PTransition t = new PTransition(id);
		transitionAndFieldBuffer.add(t);
	}

	/**
	 * Erzeugt ein neues Feld mit der Id des übergebenen Strings
	 * 
	 * @param id die Id
	 */
	@Override
	public void newPlace(String id) {
		PField f = new PField(id);
		transitionAndFieldBuffer.add(f);
	}

	/**
	 * Erzeugt eine neue Kante mit der Id des übergebenen Strings von dem Knoten
	 * source nach dem Knoten target
	 * 
	 * @param id die Id
	 * 
	 * @param source die id des Ausgangsknotens
	 * @param target die id des Zielknotens
	 */
	@Override
	public void newArc(String id, String source, String target) {
		PEdge e = new PEdge(id, source, target);
		edgeBuffer.add(e);
	}

	/**
	 * Setzt den Namen des Knotens mit der übergebenen Id auf den übergebenen Namen.
	 * 
	 * @param id die Id des gewählten Knotens
	 * 
	 * @param name der zu setzende Name
	 */

	@Override
	public void setName(String id, String name) {
		for (PLabeledNode k : transitionAndFieldBuffer) {
			if (k.getId().equals(id)) {
				k.setName(name);
			}
		}

	}

	/**
	 * Setzt die Position des Knotens mit der übergebenen Id auf die entsprechenden
	 * x und y Koordinaten. Die y Koordinate wird dabei invertiert.
	 * 
	 * @param id die Id des gewählten Knotens
	 * @param x die x Koordinate
	 * @param y die y Koordinate
	 */

	@Override
	public void setPosition(String id, String x, String y) {
		int iX = Integer.parseInt(x);
		int iY = Integer.parseInt(y);
		for (PLabeledNode k : transitionAndFieldBuffer) {
			if (k.getId().equals(id)) {
				k.setPosition(new Point(iX, -iY));
			}
		}
	}

	/**
	 * Setzt die Marken auf des Feldes mit der übergebenen Id auf die übergebene
	 * Anzahl Marken
	 * 
	 * @param id die Id
	 * 
	 * @param tokens die Anzahl an Marken
	 */
	@Override
	public void setTokens(String id, String tokens) {
		int iTokens = Integer.parseInt(tokens);
		for (PLabeledNode k : transitionAndFieldBuffer) {
			if (k.getId().equals(id)) {
				((PField) k).setMarks(iTokens);
			}

		}
	}

	/**
	 * Erzeugt ein neues DataModel aus den Knoten und Kanten welche in den Buffern
	 * gespeichert sind.
	 * 
	 * @return Das erzeugte DataModel
	 *
	 * @throws InvalidEdgeException Exception falls eine Kante nicht richtig
	 *                              verknüpft werden konnte
	 */
	protected DataModel flushBufferAndCreateModel() throws InvalidEdgeException {
		DataModel model = new DataModel();
		for (PLabeledNode knot : transitionAndFieldBuffer) {
			if (knot.getClass().isInstance(new PField(""))) {
				// Stellen in Model schreiben
				model.addField((PField) knot);
			} else if (knot.getClass().isInstance(new PTransition(""))) {
				// Transitionen in Model schreiben
				model.addTransition((PTransition) knot);
			}
		}
		for (PEdge edge : edgeBuffer) {
			// Kanten in Model schreiben
			model.addEdgeAndRelink(edge);
		}
		// Zurücksetzen des Buffers
		transitionAndFieldBuffer = new LinkedList<>();
		edgeBuffer = new LinkedList<PEdge>();
		return model;
	}

}
