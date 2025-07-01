package com.myna.myna.userdata;

public class BankAccount {

    // Member variables
    private String accountHolderName;
    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String branch;
    private String mobileNumber;
    private String upiId;
    private String panNumber;
    private String aadhaarNumber;

    // Constructor to initialize the fields
    public BankAccount(String accountHolderName, String accountNumber, String ifscCode, String bankName, String branch,
                       String mobileNumber, String upiId, String panNumber, String aadhaarNumber) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.bankName = bankName;
        this.branch = branch;
        this.mobileNumber = mobileNumber;
        this.upiId = upiId;
        this.panNumber = panNumber;
        this.aadhaarNumber = aadhaarNumber;
    }

    // Getters and setters
    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }
}
