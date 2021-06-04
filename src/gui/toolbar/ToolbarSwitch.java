package gui.toolbar;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JToolBar;

import interfaces.IModeManager;

/**
 * Ein Manager um in einem Frame die angezeigten Toolbars umzuschalten, Für
 * jeden implementierten Betrachtungsmodus. In der aktuellen implementierung
 * schaltet er zwischen {@link PetrinetToolbar} und {@link BuildModeToolbar}.
 *
 */

public class ToolbarSwitch implements IModeManager {
	private JFrame superFrame;
	private JToolBar viewModeToolbar;
	private JToolBar buildModeToolbar;
	// Kontrollvariable sollten weitere Modi hinzugefügt werden kann diese durch ein
	// int oder enum ersetzt werden.
	private boolean isInViewMode;

	/**
	 * Konstruktor Fügt dem übergebenen Frame Standardmässig eine PetrinetToolbar im
	 * View Modus hinzu.
	 * 
	 * @param superFrame       Der Frame auf dem die Toolabrs angezeigt werden hinzu
	 * @param viewModeToolbar  Die Toolbar für den View-Mode
	 * @param buildModeToolbar Die Toolbar für den Build-Mode
	 */
	public ToolbarSwitch(JFrame superFrame, JToolBar viewModeToolbar, JToolBar buildModeToolbar) {
		this.superFrame = superFrame;
		this.viewModeToolbar = viewModeToolbar;
		this.buildModeToolbar = buildModeToolbar;
		superFrame.add(viewModeToolbar, BorderLayout.NORTH);
		isInViewMode = true;
	}

	@Override
	public void changeToBuildMode() {
		if (isInViewMode) {
			superFrame.remove(viewModeToolbar);
			superFrame.revalidate();
			superFrame.add(buildModeToolbar, BorderLayout.NORTH);
			superFrame.revalidate();
			isInViewMode = false;
		}
	}

	@Override
	public void changeToViewMode() {
		if (!isInViewMode) {
			superFrame.remove(buildModeToolbar);
			superFrame.revalidate();
			superFrame.add(viewModeToolbar, BorderLayout.NORTH);
			superFrame.revalidate();
			isInViewMode = true;
		}
	}

}
