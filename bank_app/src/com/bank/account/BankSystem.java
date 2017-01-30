package com.bank.account;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankSystem {

    private List items;
    private String amount;

    /**
     * Init BankSystem object with transactions data
     */
    public BankSystem() {
        this.items = readHTMLFile(Constants.SYSTEM_TRANSACTIONS_FILE_NAME);
    }

    /**
     * Deposit operation
     */
    private void deposit() {
        saveTransactionIntoTheFile(Constants.SYSTEM_TRANSACTIONS_FILE_NAME, false);
    }

    /**
     * Return balance of current account
     *
     * @return Double
     */
    private Double balance() {
        Double sum = new Double(0);
        for (int i = 0; i < items.size(); i++) {
            Double item = (Double) items.get(i);
            if (!(item <= 0)) {
                sum += item;
            } else {
                sum -= Math.abs(item);
            }
        }
        System.out.println(Constants.ACCOUNT_BALANCE_MESSAGE + Math.round(sum));
        return sum;
    }

    /**
     * Withdraw operation
     */
    private void withdraw() {
        saveTransactionIntoTheFile(Constants.SYSTEM_TRANSACTIONS_FILE_NAME, true);
    }

    /**
     * Close current session
     */
    private void exit() {
        System.exit(0);
    }

    /**
     * Return message to the client if command was not recognized to re-enter
     *
     * @return String
     */
    private String unrecognizedCommand() {
        return Constants.SYSTEM_UNRECOGNIZED_COMMAND;
    }

    /**
     * Public method to execute operations based on the input
     *
     * @param operation
     */
    public void executeOperation(String operation) {
        if (null != operation && !operation.equalsIgnoreCase("")) {
            if (operation.equalsIgnoreCase(Constants.SYSTEM_DEPOSIT)) {
                System.out.println(Constants.DEPOSIT_MESSAGE);
                Scanner reader = new Scanner(System.in);
                String amount = reader.next();
                if(validateAmount(amount)) {
                    deposit();
                }
            } else if (operation.equalsIgnoreCase(Constants.SYSTEM_BALANCE)) {
                balance();
            } else if (operation.equalsIgnoreCase(Constants.SYSTEM_WITHDRAW)) {
                System.out.println(Constants.WITHDRAW_MESSAGE);
                Scanner reader = new Scanner(System.in);
                String amount = reader.next();
                if (validateAmount(amount)) {
                    withdraw();
                }
            } else if (operation.equalsIgnoreCase(Constants.SYSTEM_EXIT)) {
                exit();
            } else {
                System.out.println(unrecognizedCommand());
            }
        } else {
            System.out.println(unrecognizedCommand());
        }
    }

    /**
     * Validate user input
     *
     * @param amount
     * @return boolean
     */
    private boolean validateAmount(String amount) {
        try {
            if (Double.parseDouble(amount) > 0) {
                this.setAmount(amount);
                return true;
            } else {
                System.out.println(Constants.AMOUNT_POSITIVE_MESSAGE);
            }
        } catch (NumberFormatException e) {
            System.out.println(Constants.AMOUNT_INVALID_MESSAGE);
        }
        return false;
    }

    /**
     * Read transactions from HTML file and stores it into the list
     *
     * @param fileName
     * @return List
     */
    private static List readHTMLFile(String fileName) {
        File html = new File(fileName);
        Document doc = null;
        List items = new ArrayList();
        try {
            doc = Jsoup.parse(html, "UTF-8", "");
            Elements tableElements = doc.select("table");
            Elements tableHeaderEles = null;
            for (int i = 0; i < tableElements.size(); i++) {
                if (tableElements.get(i).attr("id").equalsIgnoreCase("transactions")) {
                    tableHeaderEles = tableElements.get(i).select("tbody tr td");
                }
            }
            for (int i = 0; i < tableHeaderEles.size(); i++) {
                items.add(Double.parseDouble(tableHeaderEles.get(i).text()));
            }
            return items;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save current transaction to the file
     * works with deposit/withdraw operations
     *
     * @param fileName
     * @param remove
     */
    private void saveTransactionIntoTheFile(String fileName, boolean remove) {
        File html = new File(fileName);
        Document doc = null;
        List items = new ArrayList();
        try {
            doc = Jsoup.parse(html, "UTF-8", "");
            Elements tableElements = doc.select("table");
            Elements tableHeaderEles = null;
            for (int i = 0; i < tableElements.size(); i++) {
                if (tableElements.get(i).attr("id").equalsIgnoreCase("transactions")) {
                    tableHeaderEles = tableElements.get(i).select("tbody");
                    if (remove) {
                        tableHeaderEles.append("<tr><td>-" + this.getAmount() + "</td></tr>");
                    } else {
                        tableHeaderEles.append("<tr><td>" + this.getAmount() + "</td></tr>");
                    }
                }
                Writer writer = new PrintWriter(Constants.SYSTEM_TRANSACTIONS_FILE_NAME, "UTF-8");
                writer.write(doc.html());
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter
     *
     * @return String
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Setter
     *
     * @param amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }
}
