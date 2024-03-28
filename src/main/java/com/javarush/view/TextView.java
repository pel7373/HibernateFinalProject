package com.javarush.view;

import com.javarush.controller.Controller;

import java.util.Scanner;

import static com.javarush.controller.Operation.*;

public class TextView implements View {

    private static final String DELIMITER = "======================";
    private String[] params;
    private Controller controller;

    public TextView(Controller controller, String[] params) {
        this.params = params;
        this.controller = controller;
    }

    @Override
    public void handler() {
        while(true) {
            this.printMenu();
            int choiceOperation = chooseOperation();
            System.out.println(String.format("Your choice: %s", choiceOperation));

            if       (choiceOperation == GET_SOME_CITIES_FROM_DB_AND_CACHE.ordinal()) {

            } else if(choiceOperation == CITY_FIND_ALL.ordinal()) {


            } else if(choiceOperation == CITY_FIND_BY_ID.ordinal()) {
                findCityById();
            } else if(choiceOperation == CITY_FIND_BY_NAME.ordinal()) {
                findCityByName();
            } else if(choiceOperation == CITY_SAVE.ordinal()) {
                saveCity();
            } else if(choiceOperation == CITY_UPDATE.ordinal()) {
                updateCity();
            } else if(choiceOperation == CITY_DELETE_BY_ID.ordinal()) {
                deleteCityById();
            } else if(choiceOperation == RUN_LIST_OF_OPERATIONS.ordinal()) {
                runListOfOperations();
            } else if(choiceOperation == EXIT.ordinal()) {
                System.out.println("Good luck! It was a pleasure doing business with you! Thank you!");
                controller.shutdown();
                return;
            }
            controller.handler(choiceOperation);
            System.out.println(params[0]);
        }
    }

    private void findCityById() {
        System.out.println("Please, enter city id: ");
        params[1] = String.valueOf(enterNonNegativeNumber());
        System.out.println(params[1]);
    }

    private void findCityByName() {
        System.out.println("Please, enter city name: ");
        params[1] = enterString();
        System.out.println(params[1]);
    }

    private void saveCity() {
        System.out.println("Please, enter city name: ");
        params[1] = enterString();
        System.out.println("Please, enter city population: ");
        params[2] = String.valueOf(enterNonNegativeNumber());
        System.out.println("Please, enter country id: ");
        params[3] = String.valueOf(enterNonNegativeNumber());
        System.out.printf("You entered city's name: %s, population: %s and country's id: %s\t", params[1], params[2], params[3]);
    }

    private void updateCity() {
        System.out.println("Please, enter id of the city, you want to update: ");
        System.out.println("Please, enter city new name: ");
        params[1] = enterString();
        System.out.println("Please, enter city new population: ");
        params[2] = String.valueOf(enterNonNegativeNumber());
        System.out.printf("You entered %s and %s\t", params[1], params[2]);
    }

    private void deleteCityById() {
        System.out.println("Please, enter city id: ");
        params[1] = String.valueOf(enterNonNegativeNumber());
        System.out.println(params[1]);
    }

    private void runListOfOperations() {
        params[1] = "4";
        controller.handler(CITY_FIND_BY_ID.ordinal()); //
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();
        controller.handler(CITY_FIND_BY_ID.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();
        controller.handler(CITY_FIND_BY_ID.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();
        controller.handler(CITY_FIND_BY_ID.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();
        System.out.println("!!!Run get the same city 5-th time!!!");
        controller.handler(CITY_FIND_BY_ID.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();
        System.out.println("!!!Run get the same city 6-th time!!!");
        controller.handler(CITY_FIND_BY_ID.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();

        params[1] = "Amsterdam";
        controller.handler(CITY_FIND_BY_NAME.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();

        params[1] = "Some city66";
        params[2] = "40000";
        params[3] = "5";
        controller.handler(CITY_SAVE.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();

        params[1] = "Some city66";
        controller.handler(CITY_FIND_BY_NAME.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();

        params[1] = "496";
        controller.handler(CITY_UPDATE.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();

        params[1] = "496";
        controller.handler(CITY_FIND_BY_ID.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();

        params[1] = "501";
        controller.handler(CITY_FIND_BY_ID.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();

        params[1] = "4116";
        controller.handler(CITY_DELETE_BY_ID.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();

        params[1] = "4";
        controller.handler(CITY_FIND_BY_ID.ordinal());
        System.out.println(params[0]);
        System.out.println(DELIMITER);
        System.out.println();
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
            try {
                choose = Integer.valueOf(enterString());
            } catch (NumberFormatException e) {
                System.out.println("We are very sorry, but the entered data is invalid. Please, enter non-negative number!");
                continue;
            }

            if(choose >= 0) {
                return choose;
            } else {
                System.out.println("We are very sorry, but the entered data is invalid. Please, enter non-negative number!");
            }
        }
    }

    private String enterString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
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

                .append(RUN_LIST_OF_OPERATIONS.ordinal())
                .append(": ")
                .append(RUN_LIST_OF_OPERATIONS.getTitle())
                .append(System.lineSeparator())

                .append(GET_SOME_CITIES_FROM_DB_AND_CACHE.ordinal())
                .append(": ")
                .append(GET_SOME_CITIES_FROM_DB_AND_CACHE.getTitle())
                .append(System.lineSeparator())

                .append(GET_ALL_CITIES_FROM_DB_AND_CACHE.ordinal())
                .append(": ")
                .append(GET_ALL_CITIES_FROM_DB_AND_CACHE.getTitle())
                .append(System.lineSeparator())


                .append(EXIT.ordinal())
                .append(": ")
                .append(EXIT)
                .append(System.lineSeparator())

                .append(DELIMITER)
                .append(System.lineSeparator());
        System.out.println(menu);
    }
}