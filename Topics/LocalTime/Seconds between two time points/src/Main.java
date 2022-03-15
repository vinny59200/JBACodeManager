import java.time.Duration;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // write your code here
        String time1 = scanner.nextLine();
        String time2 = scanner.nextLine();
        LocalTime timeA = LocalTime.parse(time1);
        LocalTime timeB = LocalTime.parse(time2);
        if(timeA.isBefore(timeB)) {
            Duration duration = Duration.between(timeA, timeB);
            System.out.println(duration.toSeconds());
        } else {
            Duration duration = Duration.between(timeB, timeA);
            System.out.println(duration.toSeconds());
        }

    }
}