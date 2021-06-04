package model.elements;

/**
 * Die Klasse PEdge implementiert eine Kante in einem Graphen.
 *
 */
public class PEdge extends PGraphElement {
	private PLabeledNode from;
	private PLabeledNode to;

	/**
	 * Konstruktor
	 * 
	 * @param id   id der neuen Kante
	 * @param from der Knoten von der die Kante ausgeht
	 * @param to   der Knoten zu der die Kante führt
	 */
	public PEdge(String id, PLabeledNode from, PLabeledNode to) {
		super(id);
		this.from = from;
		this.to = to;
	}

	/**
	 * Konstruktor
	 * 
	 * @param id     id der neuen Kante
	 * @param fromID Id des Knoten von der die Kante ausgeht
	 * @param toID   Id des Knoten zu der die Kante führt
	 */
	public PEdge(String id, String fromID, String toID) {
		super(id);
		this.from = new PLabeledNode(fromID);
		this.to = new PLabeledNode(toID);
	}

	/**
	 * Getter Methode für den Ausgangsknoten der Kante
	 * 
	 * @return der Ausgangsknoten
	 */
	public PLabeledNode getFrom() {
		return from;
	}

	/**
	 * Getter Methode für den Zielknoten der Kante
	 * 
	 * @return der Zielknoten
	 */
	public PLabeledNode getTo() {
		return to;
	}

	/**
	 * Setter Methode für den Ausgangsknoten der Kante
	 * 
	 * @param node der Ausgangsknoten
	 */
	public void setFrom(PLabeledNode node) {
		this.from = node;
	}

	/**
	 * Setter Methode für den Zielknoten der Kante
	 * 
	 * @param node der Zielknoten
	 */
	public void setTo(PLabeledNode node) {
		this.to = node;
	}

	/**
	 * Methode um das Label der Kante zur Darstellung im Graphen zu erhalten.
	 * 
	 * @return das Label
	 */
	@Override
	public String getLabel() {
		return "[" + super.getId() + "]";

	}

	/**
	 * Gibt eine Repräsentation der Kante zurück die zum debuggen verwendet wird.
	 * Diese zeigt Ausgangs und Zielknoten.
	 * 
	 * @return die Repräsentation der Kante
	 */
	@Override
	public String toString() {
		return "(" + from.getId() + ", " + to.getId() + ")";
	}
}
