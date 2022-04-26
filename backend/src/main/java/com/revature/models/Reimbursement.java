package com.revature.models;

import java.sql.Blob;
import java.sql.Timestamp;

public class Reimbursement {
    private int id;
    private double amount;
    private Timestamp submitted;
    private Timestamp resolved;
    private String description;
    private Blob receipt;
    private int authorId;
    private int resolverId;
    private int statusId;
    private int reimbursementTypeId;

    public Reimbursement() {
        super();
    }

    public Reimbursement(int id, double amount, Timestamp submitted, Timestamp resolved, String description,
            Blob receipt, int authorId, int resolverId, int statusId, int reimbursementTypeId) {
        super();
        this.id = id;
        this.amount = amount;
        this.submitted = submitted;
        this.resolved = resolved;
        this.description = description;
        this.receipt = receipt;
        this.authorId = authorId;
        this.resolverId = resolverId;
        this.statusId = statusId;
        this.reimbursementTypeId = reimbursementTypeId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getSubmitted() {
        return this.submitted;
    }

    public void setSubmitted(Timestamp submitted) {
        this.submitted = submitted;
    }

    public Timestamp getResolved() {
        return this.resolved;
    }

    public void setResolved(Timestamp resolved) {
        this.resolved = resolved;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Blob getReceipt() {
        return this.receipt;
    }

    public void setReceipt(Blob receipt) {
        this.receipt = receipt;
    }

    public int getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getResolverId() {
        return this.resolverId;
    }

    public void setResolverId(int resolverId) {
        this.resolverId = resolverId;
    }

    public int getStatusId() {
        return this.statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getReimbursementTypeId() {
        return this.reimbursementTypeId;
    }

    public void setReimbursementTypeId(int reimbursementTypeId) {
        this.reimbursementTypeId = reimbursementTypeId;
    }

}