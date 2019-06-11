package moxy;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Date: 18-Dec-15
 * Time: 13:51
 * <p>
 * This class represents a delegate which you can use to extend Mvp's support to any class.
 * <p>
 * When using an {@link MvpDelegate}, lifecycle methods which should be proxied to the delegate:
 * <ul>
 * <li>{@link #onCreate(Bundle)}</li>
 * <li>{@link #onAttach()}: inside onStart() of Activity or Fragment</li>
 * <li>{@link #onSaveInstanceState(android.os.Bundle)}</li>
 * <li>{@link #onDetach()}: inside onDestroy() for Activity or onDestroyView() for Fragment</li>
 * <li>{@link #onDestroy()}</li>
 * </ul>
 * <p>
 * Every {@link Object} can only be linked with one {@link MvpDelegate} instance,
 * so the instance returned from {@link #MvpDelegate(Object)}} should be kept
 * until the Object is destroyed.
 */
public class MvpDelegate<Delegated> {

  public static final String MOXY_DELEGATE_TAGS_KEY = "MoxyDelegateBundle";
  private static final String KEY_TAG = "moxy.MvpDelegate.KEY_TAG";
  private final Delegated delegated;
  private String keyTag = KEY_TAG;
  private String delegateTag;
  private boolean isAttached;

  private MvpDelegate parentDelegate;

  private List<MvpPresenter<? super Delegated>> presenters = Collections.emptyList();

  private List<MvpDelegate> childDelegates;

  private Bundle bundle;

  public MvpDelegate(Delegated delegated) {
    this.delegated = delegated;
    childDelegates = new ArrayList<>();
  }

  public void setParentDelegate(MvpDelegate delegate, String childId) {
    if (bundle != null) {
      throw new IllegalStateException(
        "You should call setParentDelegate() before first onCreate()");
    }
    if (childDelegates != null && childDelegates.size() > 0) {
      throw new IllegalStateException(
        "You could not set parent delegate when there are already has child presenters");
    }

    parentDelegate = delegate;
    keyTag = parentDelegate.keyTag + "$" + childId;

    delegate.addChildDelegate(this);
  }

  private void addChildDelegate(MvpDelegate delegate) {
    childDelegates.add(delegate);
  }

  private void removeChildDelegate(MvpDelegate delegate) {
    childDelegates.remove(delegate);
  }

  /**
   * Free self link from children list (childDelegates) in parent delegate
   * property parentDelegate stay keep link to parent delegate for access to
   * parent bundle for save state in {@link #onSaveInstanceState()}
   */
  public void freeParentDelegate() {

    if (parentDelegate == null) {
      throw new IllegalStateException(
        "You should call freeParentDelegate() before first setParentDelegate()");
    }

    parentDelegate.removeChildDelegate(this);
  }

  public void removeAllChildDelegates() {
    // For avoiding ConcurrentModificationException when removing by removeChildDelegate()
    List<MvpDelegate> childDelegatesClone = new ArrayList<MvpDelegate>(childDelegates.size());
    childDelegatesClone.addAll(childDelegates);

    for (MvpDelegate childDelegate : childDelegatesClone) {
      childDelegate.freeParentDelegate();
    }

    childDelegates = new ArrayList<>();
  }

  /**
   * <p>Similar like {@link #onCreate(Bundle)}. But this method try to get saved
   * state from parent presenter before get presenters</p>
   */
  public void onCreate() {
    Bundle bundle = new Bundle();
    if (parentDelegate != null) {
      bundle = parentDelegate.bundle;
    }

    onCreate(bundle);
  }

  /**
   * <p>Get(or create if not exists) presenters for delegated object and bind
   * them to this object fields</p>
   *
   * @param bundle with saved state
   */
  public void onCreate(Bundle bundle) {
    if (parentDelegate == null && bundle != null) {
      bundle = bundle.getBundle(MOXY_DELEGATE_TAGS_KEY);
    }

    isAttached = false;
    this.bundle = bundle != null ? bundle : new Bundle();

    //get base tag for presenters
    if (bundle == null || !this.bundle.containsKey(keyTag)) {
      delegateTag = generateTag();
    } else {
      delegateTag = bundle.getString(keyTag);
    }

    //bind presenters to view
    presenters =
      MvpFacade.getInstance().getMvpProcessor().getMvpPresenters(delegated, delegateTag);

    for (MvpDelegate childDelegate : childDelegates) {
      childDelegate.onCreate(bundle);
    }
  }

  /**
   * <p>Attach delegated object as view to presenter fields of this object.
   * If delegate did not enter at {@link #onCreate(Bundle)}(or
   * {@link #onCreate()}) before this method, then view will not be attached to
   * presenters</p>
   */
  public void onAttach() {
    for (MvpPresenter<? super Delegated> presenter : presenters) {
      if (isAttached && presenter.getAttachedViews().contains(delegated)) {
        continue;
      }

      presenter.attachView(delegated);
    }

    for (MvpDelegate<?> childDelegate : childDelegates) {
      childDelegate.onAttach();
    }

    isAttached = true;
  }

  /**
   * <p>Detach delegated object from their presenters.</p>
   */
  public void onDetach() {
    for (MvpPresenter<? super Delegated> presenter : presenters) {
      if (!isAttached && !presenter.getAttachedViews().contains(delegated)) {
        continue;
      }

      presenter.detachView(delegated);
    }

    isAttached = false;

    for (MvpDelegate<?> childDelegate : childDelegates) {
      childDelegate.onDetach();
    }
  }

  /**
   * <p>View was being destroyed, but logical unit still alive</p>
   */
  public void onDestroyView() {
    for (MvpPresenter<? super Delegated> presenter : presenters) {
      presenter.destroyView(delegated);
    }

    // For avoiding ConcurrentModificationException when removing from childDelegates
    List<MvpDelegate> childDelegatesClone = new ArrayList<MvpDelegate>(childDelegates.size());
    childDelegatesClone.addAll(childDelegates);

    for (MvpDelegate childDelegate : childDelegatesClone) {
      childDelegate.onDestroyView();
    }

    if (parentDelegate != null) {
      freeParentDelegate();
    }
  }

  /**
   * <p>Destroy presenters.</p>
   */
  public void onDestroy() {
    PresentersCounter presentersCounter = MvpFacade.getInstance().getPresentersCounter();
    PresenterStore presenterStore = MvpFacade.getInstance().getPresenterStore();

    Set<MvpPresenter> allChildPresenters = presentersCounter.getAll(delegateTag);
    for (MvpPresenter presenter : allChildPresenters) {
      boolean isRejected = presentersCounter.rejectPresenter(presenter, delegateTag);
      if (isRejected) {
        presenterStore.remove(presenter.getTag());
        presenter.onDestroy();
      }
    }
  }

  /**
   * <p>Similar like {@link #onSaveInstanceState(Bundle)}. But this method try to save
   * state to parent presenter Bundle</p>
   */
  public void onSaveInstanceState() {
    Bundle bundle = new Bundle();
    if (parentDelegate != null && parentDelegate.bundle != null) {
      bundle = parentDelegate.bundle;
    }

    onSaveInstanceState(bundle);
  }

  /**
   * Save presenters tag prefix to save state for restore presenters at future after delegate
   * recreate
   *
   * @param outState out state from Android component
   */
  public void onSaveInstanceState(Bundle outState) {
    if (parentDelegate == null) {
      Bundle moxyDelegateBundle = new Bundle();
      outState.putBundle(MOXY_DELEGATE_TAGS_KEY, moxyDelegateBundle);
      outState = moxyDelegateBundle;
    }

    outState.putAll(bundle);
    outState.putString(keyTag, delegateTag);

    for (MvpDelegate childDelegate : childDelegates) {
      childDelegate.onSaveInstanceState(outState);
    }
  }

  public Bundle getChildrenSaveState() {
    return bundle;
  }

  /**
   * @return generated tag in format: &lt;parent_delegate_tag&gt; &lt;delegated_class_full_name&gt;$MvpDelegate@&lt;hashCode&gt;
   * <p>
   * example: moxy.sample.SampleFragment$MvpDelegate@32649b0
   */
  private String generateTag() {
    String tag = parentDelegate != null ? parentDelegate.delegateTag + " " : "";
    tag += delegated.getClass().getSimpleName() + "$" + getClass().getSimpleName() + toString()
      .replace(getClass().getName(), "");
    return tag;
  }
}
