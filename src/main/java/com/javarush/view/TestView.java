package com.javarush.view;

import com.javarush.controller.Controller;
import com.javarush.controller.Operation;

import java.util.Scanner;

import static com.javarush.controller.Operation.*;

public class TestView implements View {

    private static final String DELIMITER = "======================";
    private String[] params;
    private Controller controller;

    public TestView(Controller controller, String[] params) {
        this.params = params;
        this.controller = controller;
    }

    @Override
    public void handler() {
        params[1] = String.valueOf("3");
        controller.handler(1);
        controller.handler(1);
        controller.handler(1);
        controller.handler(1);
        controller.handler(1);
        controller.handler(1);
        controller.handler(0);


        while(true) {
            this.printMenu();
            int choiceOperation = this.chooseOperation();
            System.out.println(String.format("Your choice: %s", choiceOperation));
            if       (choiceOperation == CITY_FIND_ALL.ordinal()) {

            } else if(choiceOperation == CITY_FIND_BY_ID.ordinal()) {
                findCityById();
            } else if(choiceOperation == CITY_FIND_BY_NAME.ordinal()) {

            } else if(choiceOperation == CITY_SAVE.ordinal()) {

            } else if(choiceOperation == CITY_UPDATE.ordinal()) {

            } else if(choiceOperation == CITY_DELETE_BY_ID.ordinal()) {

            }  else if(choiceOperation == EXIT.ordinal()) {
                System.out.println("Good luck! It was a pleasure doing business with you! Thank you!");
                return;
            }
            controller.handler(choiceOperation);
        }
    }

    private int chooseOperation() {
        int choose = 0;
        while(true) {
            System.out.println("Choose operation to perform: ");
            choose = enterNonNegativeNumber();
            if(choose >= 0 && choose <= values().length) {
                return choose;
            } else {
                System.out.println("You entered invalid data. Please, do it again!");
            }
        }
    }

    private int enterNonNegativeNumber() {
        int choose = 0;
        while(true) {
            choose = Integer.valueOf(enterChoice());
            if(choose >= 0) {
                return choose;
            } else {
                System.out.println("We are very sorry, but the entered data is invalid. Please, enter non-negative number!");
            }
        }
    }

    private String enterChoice() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    private void findCityById() {
        System.out.println("Please, enter city id: ");
        params[1] = String.valueOf(enterNonNegativeNumber());
        System.out.println(params[1]);
     }

    private void printMenu() {
        StringBuilder menu = new StringBuilder();
        menu.append(DELIMITER)
                .append(System.lineSeparator())
                .append("List of available operations:")
                .append(System.lineSeparator())
                .append(DELIMITER)
                .append(System.lineSeparator())
                .append(CITY_FIND_ALL.ordinal())
                .append(": ")
                .append(CITY_FIND_ALL.getTitle())
                .append(System.lineSeparator())

                .append(CITY_FIND_BY_ID.ordinal())
                .append(": ")
                .append(CITY_FIND_BY_ID.getTitle())
                .append(System.lineSeparator())

                .append(CITY_FIND_BY_NAME.ordinal())
                .append(": ")
                .append(CITY_FIND_BY_NAME.getTitle())
                .append(System.lineSeparator())

                .append(CITY_SAVE.ordinal())
                .append(": ")
                .append(CITY_SAVE.getTitle())
                .append(System.lineSeparator())

                .append(CITY_UPDATE.ordinal())
                .append(": ")
                .append(CITY_UPDATE.getTitle())
                .append(System.lineSeparator())

                .append(CITY_DELETE_BY_ID.ordinal())
                .append(": ")
                .append(CITY_DELETE_BY_ID.getTitle())
                .append(System.lineSeparator())

                .append(EXIT.ordinal())
                .append(": ")
                .append(EXIT)
                .append(System.lineSeparator())

                .append(DELIMITER)
                .append(System.lineSeparator());
        System.out.println(menu.toString());
    }
}
