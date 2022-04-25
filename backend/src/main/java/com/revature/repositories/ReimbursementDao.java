package com.revature.repositories;

import com.revature.models.Reimbursement;

import java.sql.*;

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

}
