package com.revature.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.revature.models.Reimbursement;
import com.revature.repositories.ReimbursementDao;

public class ReimbursementService {
    ReimbursementDao reimbursementDAO;

    public ReimbursementService(Connection connection) {
        this.reimbursementDAO = new ReimbursementDao(connection);
    }

    public Reimbursement add(Reimbursement reimbursement) throws SQLException {
        return reimbursementDAO.add(reimbursement);
    }

    public Reimbursement get(int id) throws SQLException {
        return reimbursementDAO.get(id);
    }

    public Reimbursement update(Reimbursement reimbursementTobeUpdated) throws SQLException {
        return reimbursementDAO.update(reimbursementTobeUpdated);

    }

    public void delete(int reimbursementId) throws SQLException {
        reimbursementDAO.delete(reimbursementId);
    }

    public List<Reimbursement> getAll() throws SQLException {
        return reimbursementDAO.getAll();

    }

    public List<Reimbursement> getAllByUserId(int userId) throws SQLException {
        return reimbursementDAO.getAllByUserId(userId);
    }

}
