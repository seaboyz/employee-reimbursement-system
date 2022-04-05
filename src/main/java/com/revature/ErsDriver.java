package com.revature;

import java.util.Scanner;

public class ErsDriver {

  static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {

    System.out.println("Welcome to ERS!");
    boolean quit = false;

    while (!quit) {
      printOptions();
      int choice = scanner.nextInt();
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

  static void login() {
    System.out.println("Please Enter your email:");
    scanner.nextLine();
    String email = scanner.nextLine();
    System.out.println("Your email is: " + email);
    System.out.println("Please Enter your password: ");
    String password = scanner.nextLine();
    System.out.println("Your password is: " + password);
    loginWithEmailAndPassword(email, password);
  }

  static void printOptions() {
    System.out.println("Press 1 to login");
    System.out.println("press 0 to quit");
  }

  static void quit() {
    System.out.println("Thank for using ERS");
    System.out.println("Good Bye");
  }

  static void loginWithEmailAndPassword(String email, String password) {
    System.out.println("Logging with email: " + email);
    // TODO
    // Authentication
    System.out.println("....");
  }


}
