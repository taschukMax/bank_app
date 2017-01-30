import com.bank.account.BankSystem;
import com.bank.account.Constants;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        BankSystem bankSystem = new BankSystem();
        while (true) {
            System.out.println(Constants.SYSTEM_LAUNCH_MESSAGE);
            String command = reader.next();
            bankSystem.executeOperation(command);
        }
    }
}
