package com.ShaikZubair.PaymentService.entity;


import com.ShaikZubair.PaymentService.model.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "TRANSACTION_DETAILS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long orderId;
    private PaymentMethod paymentMethod;
    private Instant paymentDate;
    private String paymentStatus;
    private long amount;
}
