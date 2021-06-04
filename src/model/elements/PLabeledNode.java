package model.elements;

import java.awt.Point;

/**
 * Klasse zur Implementierung eines gelabelten Knotens in einem Graphen.
 *
 */
public class PLabeledNode extends PGraphElement implements Comparable<PLabeledNode> {
	private String name;
	private Point position;

	/**
	 * Konstruktor
	 * 
	 * @param id die Id des Knotens
	 */
	public PLabeledNode(String id) {
		super(id);
		this.name = "";
		this.position = new Point(0, 0);
	}

	/**
	 * Methode um das Label des Knotens zur Darstellung im Graphen zu erhalten.
	 * 
	 * @return das Label
	 */
	@Override
	public String getLabel() {
		return getId();
	}

	/**
	 * Setter Methode für den Namen des Knotens. Dieser entspricht nicht
	 * zwangsläufig der Id des Knotens
	 * 
	 * @param name der Name des Knotens
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter Methode für den Namen des Knotens. Dieser entspricht nicht
	 * der Id des Knotens
	 * 
	 * @return name der Name des Knotens
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter Methode für die Position des Knotens im Graphen zur graphischen
	 * Darstellung.
	 * 
	 * @param position Die Position des Knotens in der graphischen Darstellung.
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * Getter Methode für die Position des Knotens im Graphen zur graphischen
	 * Darstellung.
	 * 
	 * @return position Die Position des Knotens in der graphischen Darstellung.
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Vergleicht diesen Knoten mit einem anderen alphabetisch nach der Id
	 */
	@Override
	public int compareTo(PLabeledNode o) {
		return this.getId().compareTo(o.getId());
	}

}
