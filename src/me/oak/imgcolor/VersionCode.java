
package me.oak.imgcolor;

public final class VersionCode {

    public static final int BUILD_NUMBER = 406;
    public static final String VERSION = "1.0.0";

    public static String getCode() {
	return VERSION + "-b" + BUILD_NUMBER;
    }
}
