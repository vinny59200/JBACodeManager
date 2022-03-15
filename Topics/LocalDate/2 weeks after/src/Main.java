import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        LocalDate date = LocalDate.parse(input);
        System.out.println(date.plusWeeks(2).toString());
    }
}