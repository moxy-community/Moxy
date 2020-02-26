package moxy;

public interface OnDestroyListener {
    OnDestroyListener EMPTY = () -> {
    };

    void onDestroy();
}
