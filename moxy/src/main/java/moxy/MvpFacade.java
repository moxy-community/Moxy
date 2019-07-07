package moxy;

@SuppressWarnings({ "WeakerAccess", "unused" })
public final class MvpFacade {

    private static final Object lock = new Object();
    private static volatile MvpFacade instance;
    private PresenterStore presenterStore;
    private MvpProcessor mvpProcessor;
    private PresentersCounter presentersCounter;

    private MvpFacade() {
        presentersCounter = new PresentersCounter();
        presenterStore = new PresenterStore();
        mvpProcessor = new MvpProcessor();
    }

    public static MvpFacade getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new MvpFacade();
                }
            }
        }
        return instance;
    }

    public static void init() {
        getInstance();
    }

    public PresenterStore getPresenterStore() {
        return presenterStore;
    }

    public void setPresenterStore(PresenterStore presenterStore) {
        this.presenterStore = presenterStore;
    }

    public MvpProcessor getMvpProcessor() {
        return mvpProcessor;
    }

    public void setMvpProcessor(MvpProcessor mvpProcessor) {
        this.mvpProcessor = mvpProcessor;
    }

    public PresentersCounter getPresentersCounter() {
        return presentersCounter;
    }

    public void setPresentersCounter(PresentersCounter presentersCounter) {
        this.presentersCounter = presentersCounter;
    }
}
