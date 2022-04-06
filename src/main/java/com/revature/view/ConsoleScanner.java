package com.revature.view;

import java.util.Scanner;

public class ConsoleScanner {
  private static Scanner s;

  public static Scanner getInstance(){
    if(s==null){
      s = new Scanner(System.in);
    }
    return s;
  }
}
