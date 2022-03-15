import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        LocalDateTime dateTime = LocalDateTime.parse(input);
        System.out.println(dateTime.plusHours(11).toLocalDate().toString());
    }
}