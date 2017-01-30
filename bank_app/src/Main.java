import com.bank.account.BankSystem;
import com.bank.account.Constants;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        BankSystem bankSystem = new BankSystem();
        System.out.println(Constants.SYSTEM_LAUNCH_MESSAGE);
        String command = reader.next();
        bankSystem.executeOperation(command);
        while (!Constants.SYSTEM_EXIT.equalsIgnoreCase(reader.next())) {
            System.out.println(Constants.SYSTEM_LAUNCH_MESSAGE);
            command = reader.next();
            bankSystem.executeOperation(command);
        }
    }
}
