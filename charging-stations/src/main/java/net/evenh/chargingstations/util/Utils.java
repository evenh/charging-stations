package net.evenh.chargingstations.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * A simple collection of small utility methods
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class Utils {
	private static final String TAG = "Utils";

	/**
	 * Gets coordinates from a String
	 *
	 * @param coordinates Format "(1.2343245,2.33435435)"
	 * @return A double array containing the coordinates
	 * @since 1.0.0
	 */
	public static double[] getCoordinatesFromString(String coordinates){
		double[] returnCoordinates = new double[2];

		coordinates = coordinates.replace("(", "");
		coordinates = coordinates.replace(")", "");

		String c[] = coordinates.split(",", 2);

		returnCoordinates[0] = Float.parseFloat(c[0]);
		returnCoordinates[1] = Float.parseFloat(c[1]);

		return returnCoordinates;
	}

	/**
	 * Determines if the app is running inside an emulated environment
	 *
	 * @return True if running inside emulator, false otherwise
	 * @since 1.0.0
	 */
	public static boolean isAndroidEmulator() {
		String product = Build.PRODUCT;
		boolean isEmulator = false;
		if (product != null) {
			isEmulator = product.equals("sdk") || product.contains("_sdk") || product.contains("sdk_");
		}
		return isEmulator;
	}

	/**
	 * Determines whether an internet connection exists
	 *
	 * @param context The calling class
	 * @return True if internet connectivity exists, false otherwise
	 * @since 1.0.0
	 */
	public static boolean hasInternet(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnected();
	}
}
