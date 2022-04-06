package com.revature.view;

import java.util.Scanner;

public class Console {
  private Scanner scan = ConsoleScanner.getInstance();

  public void init(){
    System.out.println("Welcome to ERS!");
    boolean quit = false;

    while (!quit) {
      printOptions();
      int choice = scan.nextInt();
      switch (choice) {
        case 1:
          login();
          break;
        case 0:
          quit = true;
          break;
        default:
          printOptions();
      }
    }

    quit();
  }

  private void login() {
    System.out.println("Please Enter your email:");
    scan.nextLine();
    String email = scan.nextLine();
    System.out.println("Your email is: " + email);
    System.out.println("Please Enter your password: ");
    String password = scan.nextLine();
    System.out.println("Your password is: " + password);
    loginWithEmailAndPassword(email, password);
  }

  private void printOptions() {
    System.out.println("Press 1 to login");
    System.out.println("press 0 to quit");
  }

  private void quit() {
    System.out.println("Thank for using ERS");
    System.out.println("Good Bye");
  }

  private void loginWithEmailAndPassword(String email, String password) {
    System.out.println("Logging with email: " + email);
    // TODO
    // Authentication
    System.out.println("....");
  }

}
