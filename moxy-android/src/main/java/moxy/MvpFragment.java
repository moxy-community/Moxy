package moxy;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;

@SuppressWarnings("ConstantConditions")
public class MvpFragment extends Fragment implements MvpDelegateHolder {

    private static final int ANDROID_OS_JELLY_BEAN = 17;

    private boolean isStateSaved;

    private MvpDelegate<? extends MvpFragment> mvpDelegate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMvpDelegate().onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        isStateSaved = false;

        getMvpDelegate().onAttach();
    }

    public void onResume() {
        super.onResume();

        isStateSaved = false;

        getMvpDelegate().onAttach();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        isStateSaved = true;

        getMvpDelegate().onSaveInstanceState(outState);
        getMvpDelegate().onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();

        getMvpDelegate().onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getMvpDelegate().onDetach();
        getMvpDelegate().onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //We leave the screen and respectively all fragments will be destroyed
        if (getActivity().isFinishing()) {
            getMvpDelegate().onDestroy();
            onClose();
            return;
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (isStateSaved) {
            isStateSaved = false;
            return;
        }

        boolean anyParentIsRemoving = false;

        if (Build.VERSION.SDK_INT >= ANDROID_OS_JELLY_BEAN) {
            Fragment parent = getParentFragment();
            while (!anyParentIsRemoving && parent != null) {
                anyParentIsRemoving = parent.isRemoving();
                parent = parent.getParentFragment();
            }
        }

        if (isRemoving() || anyParentIsRemoving) {
            getMvpDelegate().onDestroy();
            onClose();
        }
    }

    /**
     * Called right after presenters, associated with current activity, have been destroyed.
     */
    public void onClose() {

    }

    /**
     * @return The {@link MvpDelegate} being used by this Fragment.
     */
    @Override
    public MvpDelegate getMvpDelegate() {
        if (mvpDelegate == null) {
            mvpDelegate = new MvpDelegate<>(this);
        }

        return mvpDelegate;
    }
}
