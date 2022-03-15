import java.time.LocalTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        LocalTime time = LocalTime.of(0, 0).plusSeconds(n);
        System.out.println(time.toString());
    }
}