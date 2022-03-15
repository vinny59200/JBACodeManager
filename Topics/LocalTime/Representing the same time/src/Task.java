// You can experiment here, it won’t be checked

import java.time.LocalTime;

public class Task {
  public static void main(String[] args) {
    // put your code here
    LocalTime.of(23,50);
    LocalTime.of(11,50);
    LocalTime.of(23,50,30).withSecond(0);
    LocalTime.of(23,50,0).plusHours(24);
    LocalTime.of(23,30,50).minusSeconds(30);

  }
}
