package cc.tianxun.changeclient.util;

public class RGBAColors {
	public final static int WHITE = color(255,255,255);
	public final static int BLACK = color(0,0,0);
	public final static int RED = color(255,0,0);

	public static int color(int r, int g, int b, int a) {
		return (r << 24) | (g << 16) | (b << 8) | a;
	}
	public static int color(int r, int g, int b) {
		return color(r, g, b, 255);
	}
}
