package utils;

public class ConsoleUtils {

    public static void printProgress(int percentage) {
        float progress = percentage / 2.5f;
        System.out.print("\r" + "[" + Thread.currentThread().getName() + "] " + percentage + "%    [");
        for (int i = 0; i < 40; i++) {
            if (progress > i) {
                System.out.print("=");
            } else {
                System.out.print(" ");
            }
        }
        System.out.print("]");
    }

}
