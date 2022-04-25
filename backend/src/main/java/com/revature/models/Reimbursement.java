package com.revature.models;

public class Reimbursement {
    private int id;
    private int authorId;
    private double amount;
    private String description;
    private int statusId;
    private String type;
    private String submitted;
    private String resolved;
    private int reimbursementTypeId;

    public Reimbursement(int id, int authorId, double amount, String description, int statusId, String type,
            String submitted, String resolved, int reimbursementTypeId) {
        this.id = id;
        this.authorId = authorId;
        this.amount = amount;
        this.description = description;
        this.statusId = statusId;
        this.type = type;
        this.submitted = submitted;
        this.resolved = resolved;
        this.reimbursementTypeId = reimbursementTypeId;
    }

    public Reimbursement() {
        super();
    }

    public Reimbursement(int authorId, int amount, String description, int reimbursementTypeId) {
        this.authorId = authorId;
        this.amount = amount;
        this.description = description;
        this.reimbursementTypeId = reimbursementTypeId;
    }

    public Reimbursement(int amount, String description, int reimbursementTypeId) {
        this.amount = amount;
        this.description = description;
        this.reimbursementTypeId = reimbursementTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    public String getResolved() {
        return resolved;
    }

    public void setResolved(String resolved) {
        this.resolved = resolved;
    }

    public int getReimbursementTypeId() {
        return reimbursementTypeId;
    }

    public void setReimbursementTypeId(int reimbursementTypeId) {
        this.reimbursementTypeId = reimbursementTypeId;
    }

}