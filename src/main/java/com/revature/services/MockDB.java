package com.revature.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MockDB {
  public static void main(String[] args) {

    JSONParser jsonParser = new JSONParser();
    try (FileReader reader = new FileReader("src/main/resources/user.json")) {
      Object obj = jsonParser.parse(reader);
      JSONArray userList = (JSONArray) obj;
      userList.forEach(user -> parseUserObject((JSONObject) user));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  private static void parseUserObject(JSONObject user) {
    String name = (String) user.get("name");
    String firstname = name.split(" ")[0];
    String lastname = name.split(" ")[1];
    String username = (String) user.get("username");
    String email = ((String) user.get("email"));
    System.out.println(email);

  }

}
