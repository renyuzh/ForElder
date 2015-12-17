/**
 * 
 */
package org.heartwings.care.falldetect;

/**
 * @author Wave A fall detector based on an simple threshold-based algorithm
 * 
 */
public class ThresholdFallDetector extends FallDetector {
	LowPassFilter lpf;

	// Parameters
	private double maxStableCount;
	private double maxFreefallCount;
	private double freefallWindowSize;
	private int tempCount = 0;
	private double upperThresh;
	private double lowerThresh;
	private double deltaAngleThresh;
	private int stableToleranceTime;

	private int Status;
	private double deltaAngle;
	public double deltaAngleX;
	public double deltaAngleY;
	public double deltaAngleZ;
	private int stableCount = 0;
	private int freefallCount = 0;
	private int freefallStay = 0;
	private int unstableCount = 0;

	// Window size for recording gyroscope.
	private int windowSize;

	public static final int Collecting = 1;
	public static final int Freefall = 2;
	public static final int Uncertain = 3;
	public static final int Impact = 4;
	public static final int Stable = 5;
	public static final int Confirmed = 6;

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public double getDeltaAngle() {
		return deltaAngle;
	}

	/*
	 * Gyroscope Record in last one second.
	 */
	// private double[] windowGyroscopeX, windowGyroscopeY, windowGyroscopeZ;
	public ThresholdFallDetectorGyroscope thresholdFallDetectorGyroscope = new ThresholdFallDetectorGyroscope(
			80, 150, 2000);

	public static String statusToString(int status) {
		switch (status) {
		case Collecting:
			return "Collecting";
		case Freefall:
			return "Freefall";
		case Uncertain:
			return "Uncertain";
		case Impact:
			return "Impact";
		case Stable:
			return "Stable";
		case Confirmed:
			return "Confirmed";
		}
		return null;
	}

	/**
	 * @param sampleRate
	 *            Period of sampling (ms).
	 * @param upperThresh
	 *            Acceleration determining impact (m/s^2)
	 * @param lowerThresh
	 *            Acceleration determining free-fall (m/s^2)
	 * @param stableTime
	 *            Time required for being stable right before confirming that
	 *            user has fallen down.
	 * @param freefallTime
	 *            Time required for staying free-fall to determine that user is
	 *            indeed falling down.
	 * @param freefallWindowTime
	 *            The time that free fall state will wait for maximum impact
	 *            (200 ms is recommended)
	 * @param deltaAngleThresh
	 *            Minimal amount of angle offset in 0.5s to determine that user
	 *            has fallen down.
	 * @param stableToleranceCount
	 *            How many unstable data stable state may tolerate, if exceed
	 *            such threshold, means unstable and this may consider not a
	 *            fall down
	 */
	public ThresholdFallDetector(double sampleRate, double upperThresh,
			double lowerThresh, double stableTime, double freefallTime,
			double freefallWindowTime, double deltaAngleThresh,
			int stableToleranceCount) {
		super(sampleRate);
		lpf = new LowPassFilter(2 * sampleRate, sampleRate);
		this.upperThresh = upperThresh;
		this.lowerThresh = lowerThresh;
		this.deltaAngleThresh = deltaAngleThresh;
		this.maxStableCount = stableTime / sampleRate;
		this.maxFreefallCount = freefallTime / sampleRate;
		this.freefallWindowSize = freefallWindowTime / sampleRate;
		this.stableToleranceTime = stableToleranceCount;

		Status = 1;
	}

	// private double util_accumulate(double[] array) {
	// double res = 0.0;
	// for (double elem : array) {
	// res += elem;
	// }
	// return res;
	// }

	// private double util_magnitude(double X, double Y, double Z) {
	// return Math.sqrt(X * X + Y * Y + Z * Z);
	// }

	@Override
	public boolean feedData(double timestampMills, double accX, double accY, double accZ) {
		double acc = Math.sqrt(accX * accX + accY * accY + accZ * accZ);
		switch (Status) {
		case Collecting:
			tempCount = 0;
			stableCount = 0;
			freefallCount = 0;
			freefallStay = 0;
			unstableCount = 0;
			if (acc < lowerThresh) {
				freefallCount++;
				Status = Freefall;
			}
			break;
		case Freefall:
			freefallStay++;
			if (acc < lowerThresh) {
				freefallCount++;
			}
			if (acc >= upperThresh) {
				Status = Impact;
			}
			if (freefallStay >= freefallWindowSize) {
				Status = Collecting;
			}
			break;
//		case Uncertain:
//			if (acc > upperThresh && freefallCount > maxFreefallCount) {
//				Status = Impact;
//			} else {
//				Status = Collecting;
//			}
//			break;
		case Impact:
			if (acc >= lowerThresh && acc <= upperThresh) {
				tempCount++;
				if (tempCount >= 10) {
					tempCount = 0;
					Status = Stable;
				}
			}
			break;
		case Stable:
			if (stableCount == 0) {
				if (!thresholdFallDetectorGyroscope.getStatus(timestampMills)) {
					System.out
							.println("Due to not enough angle variance, not a fall down");
					Status = Collecting;
					break;
				}
			}
			if (acc >= 7 && acc <= 13) {
				stableCount++;
				if (stableCount >= maxStableCount) {
					stableCount = 0;
					Status = Confirmed;
					return true;
				}
			} else {
				unstableCount++;
				if (unstableCount >= stableToleranceTime) {
					unstableCount = 0;
					Status = Collecting;
				}
			}
			break;
		}
		return false;
	}

	public void feedGyroscope(double timestampMills, double X, double Y, double Z) {
		thresholdFallDetectorGyroscope.feedGyroscope(timestampMills, X, Y, Z);
	}

	public boolean getGyroscopeStatus(double timestampMills) {
		return thresholdFallDetectorGyroscope.getStatus(timestampMills);
	}

	// public boolean feedDataWithGyroscope(double accX, double accY, double
	// accZ) {
	// double acc = Math.sqrt(accX * accX + accY * accY + accZ * accZ);
	// switch (Status) {
	// case Collecting:
	// tempCount = 0;
	// stableCount = 0;
	// freefallCount = 0;
	// if (acc < lowerThresh) {
	// Status = Freefall;
	// }
	// break;
	// case Freefall:
	// freefallCount++;
	// if (acc > upperThresh && freefallCount >= maxFreefallCount) {
	// Status = Impact;
	// }
	// if (freefallCount >= 3 * maxFreefallCount) {
	// Status = Collecting;
	// }
	// break;
	// case Uncertain:
	// if (acc > upperThresh && freefallCount > maxFreefallCount) {
	// Status = Impact;
	// } else {
	// Status = Collecting;
	// }
	// break;
	// case Impact:
	// if (deltaAngle < deltaAngleThresh) {
	// Status = Collecting;
	// break;
	// }
	// if (acc >= lowerThresh && acc <= upperThresh) {
	// tempCount++;
	// if (tempCount >= 10) {
	// tempCount = 0;
	// // double deltaPitch = util_accumulate(windowGyroscopeX);
	// // double deltaYaw = util_accumulate(windowGyroscopeY);
	// // double deltaRoll = util_accumulate(windowGyroscopeZ);
	// // double deltaAngle = util_magnitude(deltaPitch, deltaYaw,
	// // deltaRoll);
	// // this.deltaAngle = deltaAngle;
	// Status = Stable;
	// }
	// }
	// break;
	// case Stable:
	// if (acc >= lowerThresh && acc <= upperThresh) {
	// stableCount++;
	// if (stableCount >= maxStableCount) {
	// stableCount = 0;
	// Status = Confirmed;
	// return true;
	// }
	// } else {
	// unstableCount++;
	// if (unstableCount >= stableToleranceTime) {
	// unstableCount = 0;
	// Status = Collecting;
	// }
	// }
	// break;
	// }
	// return false;
	// }
}
