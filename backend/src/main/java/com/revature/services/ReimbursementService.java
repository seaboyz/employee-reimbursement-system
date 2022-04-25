package com.revature.services;

import java.sql.Connection;
import java.sql.SQLException;

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

}
