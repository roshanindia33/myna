package com.myna.myna.userdata;

public class WithdrawalRequest {
    private String amount;
    private String status;
    private Long timestamp; // Firebase ServerValue.TIMESTAMP will be Long

    public WithdrawalRequest() {
        // Default constructor required for calls to DataSnapshot.getValue(WithdrawalRequest.class)
    }

    public WithdrawalRequest(String amount, String status, Long timestamp) {
        this.amount = amount;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters
    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    // Setters (if needed, but for Firebase read usually only getters are sufficient)
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}