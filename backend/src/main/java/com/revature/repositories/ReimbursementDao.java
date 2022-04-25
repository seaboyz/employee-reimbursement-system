package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Reimbursement;

public class ReimbursementDao {
  private Connection connection;

  public ReimbursementDao(Connection connection) {
    this.connection = connection;
  }

  public Reimbursement add(Reimbursement reimbursement) throws SQLException {
    String query = "INSERT INTO ERS_REIMBURSEMENT (REIMB_AMOUNT, REIMB_DESCRIPTION, REIMB_AUTHOR, REIMB_TYPE_ID) VALUES (?, ?, ?, ?)";

    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    ps.setDouble(1, reimbursement.getAmount());
    ps.setString(2, reimbursement.getDescription());
    ps.setInt(3, reimbursement.getAuthorId());
    ps.setInt(4, reimbursement.getReimbursementTypeId());

    ps.executeUpdate();
    // get auto generated key back
    ResultSet keys = ps.getGeneratedKeys();
    if (keys.next()) {
      int id = keys.getInt(1);
      reimbursement.setId(id);
      return reimbursement;
    } else {
      throw new SQLException("Fail save new reimbursement to database");
    }
  }

  public Reimbursement get(int id) throws SQLException {
    String query = "SELECT * FROM ers_reimbursement WHERE reimb_id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setInt(1, id);
    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
      Reimbursement reimbursement = new Reimbursement();
      reimbursement.setId(rs.getInt("reimb_id"));
      reimbursement.setAmount(rs.getDouble("reimb_amount"));
      reimbursement.setDescription(rs.getString("reimb_description"));
      reimbursement.setAuthorId(rs.getInt("reimb_author"));
      reimbursement.setReimbursementTypeId(rs.getInt("reimb_type_id"));
      reimbursement.setStatusId(rs.getInt("reimb_status_id"));
      return reimbursement;
    }
    return null;
  }

  public Reimbursement update(Reimbursement reimbursementTobeUpdated) throws SQLException {
    String query = "UPDATE ers_reimbursement SET reimb_amount = ?, reimb_description = ?, reimb_type_id = ? WHERE reimb_id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setDouble(1, reimbursementTobeUpdated.getAmount());
    ps.setString(2, reimbursementTobeUpdated.getDescription());
    ps.setInt(3, reimbursementTobeUpdated.getReimbursementTypeId());
    ps.setInt(4, reimbursementTobeUpdated.getId());
    if (ps.executeUpdate() == 1) {
      return reimbursementTobeUpdated;
    }

    return null;
  }

  public void delete(int reimbursementId) throws SQLException {
    String query = "DELETE FROM ers_reimbursement WHERE reimb_id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setInt(1, reimbursementId);
    if (ps.executeUpdate() == 1) {
      return;
    } else {
      throw new SQLException("Failed to delete reimbursement");
    }

  }

  public List<Reimbursement> getAll() throws SQLException {
    String query = "SELECT * FROM ers_reimbursement";

    List<Reimbursement> reimbursements = new ArrayList<>();

    PreparedStatement ps = connection.prepareStatement(query);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Reimbursement reimbursement = new Reimbursement();
      reimbursement.setId(rs.getInt("reimb_id"));
      reimbursement.setAmount(rs.getDouble("reimb_amount"));
      reimbursement.setDescription(rs.getString("reimb_description"));
      reimbursement.setAuthorId(rs.getInt("reimb_author"));
      reimbursement.setReimbursementTypeId(rs.getInt("reimb_type_id"));
      reimbursement.setStatusId(rs.getInt("reimb_status_id"));
      reimbursements.add(reimbursement);
    }
    return reimbursements;
  }

  public List<Reimbursement> getAllByUserId(int userId) throws SQLException {
    String query = "SELECT * FROM ers_reimbursement WHERE reimb_author = ?";
    List<Reimbursement> reimbursements = new ArrayList<>();

    PreparedStatement ps = connection.prepareStatement(query);
    ps.setInt(1, userId);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Reimbursement reimbursement = new Reimbursement();
      reimbursement.setId(rs.getInt("reimb_id"));
      reimbursement.setAmount(rs.getDouble("reimb_amount"));
      reimbursement.setDescription(rs.getString("reimb_description"));
      reimbursement.setAuthorId(rs.getInt("reimb_author"));
      reimbursement.setReimbursementTypeId(rs.getInt("reimb_type_id"));
      reimbursement.setStatusId(rs.getInt("reimb_status_id"));
      reimbursements.add(reimbursement);
    }
    return reimbursements;
  }
}
