package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

import interfaces.ITextWindow;

/**
 * TextPanel realisiert ein JPanel um darauf Text-Nachrichten oder
 * kompliziertere Textausgaben anzuzeigen.
 *
 */

public class TextPanel extends JScrollPane implements ITextWindow {
	/**
	 * default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private final JTextArea txtField;
	private final Color BACKGROUNDCOLOR = new Color (220,220,220);

	/**
	 * Konstruktor
	 * 
	 */
	public TextPanel() {
		this.txtField = new JTextArea();
		this.setViewportView(txtField);
		setPreferredSize(new Dimension(1000, 200));
		txtField.setEditable(false);
		txtField.setBackground(BACKGROUNDCOLOR);
		txtField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		setVisible(true);
		revalidate();
	}

	@Override
	public void setTextTo(String text) {
		txtField.setText(text);

	}

	@Override
	public void pushText(String text) {
		// String oldText = txtField.getText();
		// txtField.setText(oldText + text);
		txtField.append(text);
		this.revalidate();
	}

	@Override
	public void clear() {
		setTextTo("");

	}

}
