package learning.demo;

public class Utils {
	public static int transmogrify(int src){
		if(Character.isLetter(src)){
			return src ^ ' ';
		} else {
			return src;
		}
	}
	
	public static void log(String format, Object... args) {
		System.out.println(String.format(format, args));
	}
}