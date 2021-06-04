package gui.toolbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Fenster um Benachrichtigen anzuzeigen. Diese müssen erst gesetzt werden.
 *
 */

class NotificationWindow extends JDialog {
	/**
	 * Defaukt serial Version ID
	 */
	private static final long serialVersionUID = 1L;
	JLabel notification;

	/**
	 * Konstruktor
	 * 
	 */
	protected NotificationWindow() {
		JPanel panel = new JPanel();
		this.notification = new JLabel();
		panel.add(notification);
		panel.setPreferredSize(new Dimension(200, 100));
		initialiseAndAddOkButton(panel);
		this.add(panel);
		pack();
		this.setVisible(false);
	}

	private void initialiseAndAddOkButton(JPanel panel) {
		JButton OKButton = new JButton("OK");
		OKButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		panel.add(OKButton);
	}

	/**
	 * Setzt die angezeigte Benachrichtigung auf den übergebenen Text
	 * 
	 * @param s der übergebene Text
	 */
	protected void setNotificationLabel(String s) {
		notification.setText(s);
		revalidate();
	}

}
