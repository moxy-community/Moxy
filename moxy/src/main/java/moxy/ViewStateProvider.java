package moxy;

import moxy.viewstate.MvpViewState;

public abstract class ViewStateProvider {

  /**
   * <p>Presenter creates view state object by calling this method.</p>
   *
   * @return view state class name
   */
  public abstract MvpViewState getViewState();
}
