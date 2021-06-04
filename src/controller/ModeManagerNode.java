package controller;

import java.util.ArrayList;
import java.util.List;

import interfaces.IModeManager;

/**
 * Klasse die {@link IModeManager} zusammenfasst um sie gemeinsam anzusprechen
 * und so die Modi des Programms umzustellen. Dies ist nötig, da verschiedene
 * Mangaer ({@link controller.mousseinputs.MouseInputSwitch},
 * {@link gui.toolbar.ToolbarSwitch} unterschiedliche Teile des Programms
 * koordinieren.
 *
 */
public class ModeManagerNode implements IModeManager {
	private List<IModeManager> modeManagers;

	/**
	 * Konstruktor
	 */
	public ModeManagerNode() {
		this.modeManagers = new ArrayList<>();
	}

	/**
	 * Meldet einen IModeManager als Listener an die ManagerNode an.
	 * 
	 * @param manager Der angemeldete IModeMAnager
	 */
	public void addModeManager(IModeManager manager) {
		modeManagers.add(manager);
	}

	@Override
	public void changeToBuildMode() {
		for (IModeManager iModeManager : modeManagers) {
			iModeManager.changeToBuildMode();
		}
	}

	@Override
	public void changeToViewMode() {
		for (IModeManager iModeManager : modeManagers) {
			iModeManager.changeToViewMode();
		}
	}

	/**
	 * Schaltet einmal durch die Modi, nachdem alle modeManager angemeldet wurde..
	 * Dies behebt einen Bug bei dem die modeManger im Build Mode beim ersten mal
	 * umschalten nicht richtig geschaltet werden.
	 */
	public void init() {
		changeToBuildMode();
		changeToViewMode();
	}

}
