import java.time.LocalTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        LocalTime time = LocalTime.parse(input);
        System.out.println(time.withSecond(0));
      ;
//        System.out.println( LocalTime.of(1, 30).plusMinutes(690).getHour());
    }
}