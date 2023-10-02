package gui.toolbar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * Der PetrinetToolbarButton realisiert einen {@link JButton} mit den Standard
 * Style-Einstellungen der Toolbar des Programmes.
 *
 */

class PetrinetToolbarButton extends JButton {

	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private static final Color TOOLBARCOLOR = new Color(75, 75, 75);
	private static final Color TEXTCOLOR = new Color(190, 190, 190);
	private static final Color BORDERCOLOR = new Color(100, 100, 100);

	/**
	 * Konstruktor
	 * 
	 * @param name der Name des JButtons
	 */
	protected PetrinetToolbarButton(String name) {
		super(name);
		setBorderPainted(true);
		setBorder(new Border() {

			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
				g.setColor(BORDERCOLOR);
				g.drawRect(x - 1, y, width, height);
			}

			@Override
			public boolean isBorderOpaque() {
				return true;
			}

			@Override
			public Insets getBorderInsets(Component c) {
				return new Insets(5, 10, 5, 10);
			}
		});
		setBackground(TOOLBARCOLOR);
		setForeground(TEXTCOLOR);
		// setFocusable(false);
	}

	/**
	 * Konstruktor
	 * 
	 * @param icon das Bild des Buttons
	 */
	protected PetrinetToolbarButton(ImageIcon icon) {
		super(icon);
		setBorderPainted(true);
		setBorder(new Border() {

			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
				// g.setColor(BORDERCOLORCOLOR);
				// g.drawRect(x-1, y, width, height);
			}

			@Override
			public boolean isBorderOpaque() {
				return true;
			}

			@Override
			public Insets getBorderInsets(Component c) {
				return new Insets(0, 20, 0, 20);
			}
		});
		setBackground(TOOLBARCOLOR);
		setForeground(TEXTCOLOR);
	}

}
