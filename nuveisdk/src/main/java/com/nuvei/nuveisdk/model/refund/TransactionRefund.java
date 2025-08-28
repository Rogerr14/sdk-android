package com.nuvei.nuveisdk.model.refund;

public class TransactionRefund {
    private String id;
    private Long reference_label;

    public TransactionRefund(String id, Long reference_label) {
        this.id = id;
        this.reference_label = reference_label;
    }
}
