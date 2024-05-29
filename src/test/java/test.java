import org.testng.annotations.Test;

import java.util.Scanner;

public class test extends Google_classroom{
    static Google_classroom google = new Google_classroom();
    public static void main(String[] args) throws InterruptedException {

        Scanner scanner = new Scanner(System.in);
        System.out.println(" Enter Email : ");
        String email = scanner.nextLine();
        System.out.println(" Enter password : ");
        String password = scanner.nextLine();
        System.out.println(" Enter class name : ");
        String cn = scanner.nextLine();
        google.Grades(email,password,cn);


    }


}
