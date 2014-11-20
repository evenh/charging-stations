package net.evenh.chargingstations.util;

/**
 * A simple singleton to determine if the app has been initially refreshed
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class RefreshState {
	boolean refreshed;

    private static RefreshState ourInstance = new RefreshState();

    public static RefreshState getInstance() {
        return ourInstance;
    }

    private RefreshState() {
		this.refreshed = false;
    }

	public boolean isRefreshed() {
		return refreshed;
	}

	public void setRefreshed(boolean refreshed) {
		this.refreshed = refreshed;
	}
}
