import java.time.LocalTime;
import java.util.Locale;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String time1 = scanner.nextLine();
        String time2[] = scanner.nextLine().split(" ");
        LocalTime timeA = LocalTime.parse(time1);
        timeA=timeA.minusHours(Integer.parseInt(time2[0]));
        timeA=timeA.minusMinutes(Integer.parseInt(time2[1]));
        System.out.println(timeA.toString());
    }
}