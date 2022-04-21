package com.revature.cli;

public class App {

  private static Controller controller;

  public static void main(String[] args) {
    init();
    View view = new View(controller);
    view.init();
    ;
  }

  private static void init() {
    controller = new Controller();
    try {
      controller = new Controller();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
