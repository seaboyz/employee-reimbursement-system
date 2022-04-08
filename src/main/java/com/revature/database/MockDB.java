package com.revature.database;

import com.revature.models.Role;
import com.revature.models.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MockDB implements Database {
  private JSONParser jsonParser = new JSONParser();
  private JSONArray users;
  private static MockDB db;

  public static MockDB getInstance() {
    if (db == null) {
      db = new MockDB();
    }
    return db;
  }

  @Override
  public ArrayList<User> findAllUsers() {
    loadUserData();
    ArrayList<User> userArrayList = new ArrayList<>();

    for (Object user : users) {
      JSONObject userJsonObject = ((JSONObject) user);
      int id = ((Long) userJsonObject.get("id")).intValue();
      String username = userJsonObject.get("username").toString();
      String password = userJsonObject.get("password").toString();
      Role role = Role.valueOf((userJsonObject.get("role").toString().toUpperCase()));
      userArrayList.add(new User(id, username, password, role));
    }
    return userArrayList;
  }


  @Override
  public User getUserByUsername(String username) {
    ArrayList<User> users = findAllUsers();
    return users.stream().filter(user -> user.getUsername().equals(username)).findAny().orElse(null);
  }

  private void loadUserData() {
    try (FileReader reader = new FileReader("src/main/resources/user.json")) {
      Object obj = jsonParser.parse(reader);
      users = (JSONArray) obj;

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void parseUserObject(JSONObject user) {
    String name = (String) user.get("name");
    String firstname = name.split(" ")[0];
    String lastname = name.split(" ")[1];
    String username = (String) user.get("username");
    String email = ((String) user.get("email"));


  }

}
