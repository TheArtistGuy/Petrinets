package gui.menubar;

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

/**
 * JDialog um abzufragen ob das Programm wirklich geschlossen werden soll.
 *
 */
class SystemExitDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor
	 */
	protected SystemExitDialog() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(150, 100));
		JLabel label = new JLabel("Programm beenden?");
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});
		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		panel.add(label);
		panel.add(okButton);
		panel.add(cancelButton);
		add(panel);
		pack();

	}

}
