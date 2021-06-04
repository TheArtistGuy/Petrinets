package controller.mousseinputs;

import interfaces.IModeManager;

/**
 * Ein Schalter um die Eingaben der Maus, welche aus dem {@link gui.views.PetrinetGraph} kommen
 * zu koordinieren. So kann zwischen Eingaben für den {@link controller.Controller} und Eingaben
 * für den {@link controller.PetrinetBuilder} gewählt werden.
 *
 */

public class MouseInputSwitch implements IModeManager {
	private ToggleableMouseAdapter viewModeMousseAdapter;
	private ToggleableMouseAdapter buildModeMousseAdapter;
	private ToggleableGraphClickListener viewModeClickListener;
	private ToggleableGraphClickListener buildModeClickListener;
	private boolean isInViewMode;

	/**
	 * Konstruktor
	 * 
	 * @param viewModeMousseAdapter  der MousseAdapter für den View-Mode
	 * @param buildModeMousseAdapter der MousseAdapter für den Build-Mode
	 * @param viewModeClickListener  der ClickListener für den View-Mode
	 * @param buildModeClickListener der ClickListener für den Build-Mode
	 */

	public MouseInputSwitch(ToggleableMouseAdapter viewModeMousseAdapter, ToggleableMouseAdapter buildModeMousseAdapter,
			ToggleableGraphClickListener viewModeClickListener, ToggleableGraphClickListener buildModeClickListener) {
		super();
		this.viewModeMousseAdapter = viewModeMousseAdapter;
		this.buildModeMousseAdapter = buildModeMousseAdapter;
		this.viewModeClickListener = viewModeClickListener;
		this.buildModeClickListener = buildModeClickListener;

	}

	@Override
	public void changeToBuildMode() {
		if (isInViewMode) {
			viewModeClickListener.setIsActive(false);
			viewModeMousseAdapter.setIsActive(false);
			buildModeClickListener.setIsActive(true);
			buildModeMousseAdapter.setIsActive(true);
			isInViewMode = false;
		}
	}

	@Override
	public void changeToViewMode() {
		viewModeClickListener.setIsActive(true);
		viewModeMousseAdapter.setIsActive(true);
		buildModeClickListener.setIsActive(false);
		buildModeMousseAdapter.setIsActive(false);
		isInViewMode = true;
	}
}
