package com.revature.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.revature.models.Reimbursement;
import com.revature.repositories.ReimbursementDao;

public class ReimbursementService {
    ReimbursementDao reimbursementDao;

    public ReimbursementService(Connection connection) {
        this.reimbursementDao = new ReimbursementDao(connection);
    }

    public Reimbursement add(Reimbursement reimbursement) throws SQLException {
        return reimbursementDao.add(reimbursement);
    }

    public Reimbursement getReimbursementById(int id) throws SQLException, IllegalArgumentException {
        return reimbursementDao.get(id);
    }

    public void update(Reimbursement reimbursementTobeUpdated) throws SQLException {
        reimbursementDao.update(reimbursementTobeUpdated);

    }

    public void delete(int reimbursementId) throws SQLException {
        reimbursementDao.delete(reimbursementId);
    }

    public List<Reimbursement> getAllReimbursements() throws SQLException {
        return reimbursementDao.getAll();

    }

    public List<Reimbursement> getAllByUserId(int userId) throws SQLException {
        return reimbursementDao.getAllByUserId(userId);
    }

    public List<Reimbursement> getAllReimbursementsByUserId(int userId) throws SQLException {
        return reimbursementDao.getAllReimbursementsByUserId(userId);
    }

}
