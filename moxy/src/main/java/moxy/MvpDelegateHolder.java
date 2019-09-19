package moxy;

/**
 * Defines class, that contains {@link moxy.MvpDelegate}.
 * Every call to {@link #getMvpDelegate()} must return same instance.
 */
public interface MvpDelegateHolder {
    /**
     * @return Instance of MvpDelegate
     */
    MvpDelegate getMvpDelegate();
}
