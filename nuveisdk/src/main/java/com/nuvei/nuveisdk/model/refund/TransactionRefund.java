package com.nuvei.nuveisdk.model.refund;

import androidx.annotation.Nullable;

public class TransactionRefund {
    private String id;

    @Nullable
    private Long reference_label;

    public TransactionRefund(String id, @Nullable Long reference_label) {
        this.id = id;
        this.reference_label = reference_label;
    }
}
