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
      reimbursement.setSubmitted(rs.getTimestamp("reimb_submitted"));
      reimbursement.setResolved(rs.getTimestamp("reimb_resolved"));
      reimbursement.setDescription(rs.getString("reimb_description"));
      reimbursement.setReceipt(rs.getBlob("reimb_receipt"));
      reimbursement.setAuthorId(rs.getInt("reimb_author"));
      reimbursement.setResolverId(rs.getInt("reimb_resolver"));
      reimbursement.setStatusId(rs.getInt("reimb_status_id"));
      reimbursement.setReimbursementTypeId(rs.getInt("reimb_type_id"));

      return reimbursement;
    } else {
      return null;
    }

  }

  public void update(Reimbursement reimbursementTobeUpdated) throws SQLException {
    String query = "UPDATE ers_reimbursement SET reimb_amount = ?, reimb_description = ?, reimb_author = ?, reimb_type_id = ? WHERE reimb_id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setDouble(1, reimbursementTobeUpdated.getAmount());
    ps.setString(2, reimbursementTobeUpdated.getDescription());
    ps.setInt(3, reimbursementTobeUpdated.getAuthorId());
    ps.setInt(4, reimbursementTobeUpdated.getReimbursementTypeId());
    ps.setInt(5, reimbursementTobeUpdated.getId());

    if (ps.executeUpdate() == 0) {
      throw new SQLException("Reimbursement not found");
    }

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

  public List<Reimbursement> getAllByStatusId(int statusId) throws SQLException {
    String query = "SELECT * FROM ers_reimbursement WHERE reimb_status_id = ?";
    List<Reimbursement> reimbursements = new ArrayList<>();

    PreparedStatement ps = connection.prepareStatement(query);
    ps.setInt(1, statusId);
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

  // public void approve(int reimbursementId, int adminId) throws SQLException {
  // String query = "UPDATE ERS_REIMBURSEMENT SET REIMB_STATUS_ID = ?,
  // REIMB_RESOLVER = ?, REIMB_RESOLVED = NOW() WHERE REIMB_ID = ?;";
  // PreparedStatement ps = connection.prepareStatement(query);
  // ps.setInt(1, 2);
  // ps.setInt(2, adminId);
  // ps.setInt(3, reimbursementId);
  // if (ps.executeUpdate() != 1) {
  // throw new SQLException("Failed to update reimbursement");
  // }
  // }

  // public void deny(int reimbursementId, int adminId) throws SQLException {
  // String query = "UPDATE ERS_REIMBURSEMENT SET REIMB_STATUS_ID = ?,
  // REIMB_RESOLVER = ?, REIMB_RESOLVED = NOW() WHERE REIMB_ID = ?;";
  // PreparedStatement ps = connection.prepareStatement(query);
  // ps.setInt(1, 3);
  // ps.setInt(2, adminId);
  // ps.setInt(3, reimbursementId);
  // if (ps.executeUpdate() != 1) {
  // throw new SQLException("Failed to update reimbursement");
  // }
  // }

  public List<Reimbursement> getAllReimbursementsByUserId(int userId) throws SQLException {
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

  public List<Reimbursement> getAllReimbursementsByUserIdAndStatus(int userId, int statusTypeId) throws SQLException {
    String query = "SELECT * FROM ers_reimbursement WHERE reimb_author = ? AND reimb_status_id = ?";
    List<Reimbursement> reimbursements = new ArrayList<>();
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setInt(1, userId);
    ps.setInt(2, statusTypeId);
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

  public void updateStatus(int reimbursementId, int statusId, int resolverId) throws SQLException {
    String query = "UPDATE ers_reimbursement SET reimb_status_id = ?, reimb_resolver = ?, reimb_resolved = NOW() WHERE reimb_id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setInt(1, statusId);
    ps.setInt(2, resolverId);
    ps.setInt(3, reimbursementId);
    if (ps.executeUpdate() != 1) {
      throw new SQLException("Failed to update reimbursement");
    }
  }

}
