package net.evenh.chargingstations;

import android.os.Build;

/**
 * Created by evenh on 18/11/14.
 */
public class Utils {
	private static final String TAG = "Utils";

	public static double[] getCoordinatesFromString(String coordinates){
		double[] returnCoordinates = new double[2];

		coordinates = coordinates.replace("(", "");
		coordinates = coordinates.replace(")", "");

		String c[] = coordinates.split(",", 2);

		returnCoordinates[0] = Float.parseFloat(c[0]);
		returnCoordinates[1] = Float.parseFloat(c[1]);

		return returnCoordinates;
	}

	public static boolean isAndroidEmulator() {
		String product = Build.PRODUCT;
		boolean isEmulator = false;
		if (product != null) {
			isEmulator = product.equals("sdk") || product.contains("_sdk") || product.contains("sdk_");
		}
		return isEmulator;
	}
}
