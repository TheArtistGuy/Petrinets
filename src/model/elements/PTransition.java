package model.elements;

import java.util.LinkedList;
import java.util.List;

/**
 * Klasse um eine Transition in einem Petrinetz zu implementieren. Dieses kann
 * die eingehenden und ausgehenden Kanten aufnehmen und enthält Methoden um eine
 * Schaltung auszuführen.
 *
 */

public class PTransition extends PLabeledNode {

	private final List<PEdge> incomingEdges;
	private final List<PEdge> outgoingEdges;

	/**
	 * Konstruktor
	 * 
	 * @param id Die Id der Transition
	 */
	public PTransition(String id) {
		super(id);
		incomingEdges = new LinkedList<>();
		outgoingEdges = new LinkedList<>();
	}

	/**
	 * Methode um eine Kante der Liste der eingehenden Kanten hinzuzufügen.
	 * 
	 * @param e die eingehende Kante
	 */
	public void addIncomingEdge(PEdge e) {
		incomingEdges.add(e);
	}

	/**
	 * Methode um eine Kante der Liste der ausgehenden Kanten hinzuzufügen
	 * 
	 * @param e die ausgehende Kante
	 */
	public void addOutgoingEdge(PEdge e) {
		outgoingEdges.add(e);
	}

	/**
	 * Gettter Methode für die Liste der eingehenden Kanten
	 * 
	 * @return die Liste der eingehenden Kanten
	 */
	public List<PEdge> getIncomingEdges() {
		return incomingEdges;
	}

	/**
	 * Getter Methode für die Liste der ausgehenden Kanten
	 * 
	 * @return die Liste der Ausgehenden Kanten
	 */
	public List<PEdge> getOutgoingEdges() {
		return outgoingEdges;
	}

	/**
	 * Ermittelt ob alle Stellen im Vorbereich der Transition eine Marke haben und
	 * die Transition somit geschaltet werden kann.
	 * 
	 * @return Haben alle Stellen im Vorbereich eine Marke?
	 */
	public boolean isReadyToShift() {
		int numberOfEdgesIncoming = incomingEdges.size();
		// Fall Transition hat keine eingehenden Kanten => ist immer aktiviert
		if (numberOfEdgesIncoming == 0) {
			return true;
		}
		// Fall Transition hat eingehende Kanten : ist aktiviert wenn alle Felder der
		// eingehenden Kanten Marken haben
		int numberOfIncomingFieldsWithMarks = 0;
		for (PEdge e : incomingEdges) {
			if (((PField) e.getFrom()).getTokens() > 0) {
				numberOfIncomingFieldsWithMarks++;
			}
		}
		return numberOfEdgesIncoming == numberOfIncomingFieldsWithMarks;

	}

	/**
	 * Falls die Vorgaben erfüllt sind wird eine Schaltung auf der Transition
	 * ausgeführt. Also von den Stellen im Vorbereich eine Marke entfernt und den
	 * Feldern im Nachbereich eine Marke gegeben.
	 */
	public void shift() {
		if (isReadyToShift()) {
			for (PEdge ie : incomingEdges) {
				((PField) ie.getFrom()).decreaseMarksByOne();
			}
			for (PEdge oe : outgoingEdges) {
				((PField) oe.getTo()).increaseMarksByOne();
			}
		}
	}

	@Override
	public String getLabel() {
		return "[" + super.getId() + "] " + getName();
	}
}
