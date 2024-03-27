package com.javarush;

import com.javarush.controller.Controller;
import com.javarush.controller.MyController;
import com.javarush.view.*;

public class Main {
    public static void main(String[] args) {
        String[] params = new String[7];
        Controller controller = new MyController(params);
        View view = new TextView(controller, params);
        view.handler();
    }
}