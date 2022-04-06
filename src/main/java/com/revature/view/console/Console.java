package com.revature.view.console;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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

  private void loginWithEmailAndPassword(String email, String password)
          throws UnsupportedEncodingException, IOException {
    System.out.println("Logging with email: " + email);
    // TODO
    // HttpClient POST http://localhost:8080/login
    // https://hc.apache.org/httpcomponents-client-5.1.x/index.html
    CloseableHttpClient client = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost("http://localhost:8080/login");
    List<NameValuePair> params = new ArrayList<>();
    params.add((new BasicNameValuePair("email",email)));
    params.add((new BasicNameValuePair("password",password)));
    httpPost.setEntity(new UrlEncodedFormEntity(params));
    CloseableHttpResponse response = client.execute(httpPost);
    if(response.getStatusLine().getStatusCode()==200){
      System.out.println("_______________________________");
      System.out.println("|           Welcome           |");
      System.out.println("|          User Menu          |");
      System.out.println("|_____________________________|");
    }else if(response.getStatusLine().getStatusCode()==403){
      System.out.println("The email and password was wrong, please try again.");
    }
  }

}
