package utils;

public class Console {
    public static void log(Object o) {
        String s = "[" + Thread.currentThread().getName() + "] " + o.toString();
        System.out.println(s);
    }
}
