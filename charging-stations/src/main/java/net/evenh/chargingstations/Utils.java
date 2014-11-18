package net.evenh.chargingstations;

/**
 * Created by evenh on 18/11/14.
 */
public class Utils {
	public static double[] getCoordinatesFromString(String coordinates){
		double[] returnCoordinates = new double[2];

		coordinates = coordinates.replace("(", "");
		coordinates = coordinates.replace(")", "");

		String c[] = coordinates.split(",", 2);

		returnCoordinates[0] = Float.parseFloat(c[0]);
		returnCoordinates[1] = Float.parseFloat(c[1]);

		return returnCoordinates;
	}
}
