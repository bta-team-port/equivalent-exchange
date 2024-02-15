package teamport.ee.miscallaneous.math;

public class MathHelper {

	// Wrapped math helper from BestSoft.
	public static int wrapDegrees(int degrees) {
		int i = degrees % 360;
		if (i < 0) {
			i += 360;
		}
		return i;
	}
}
