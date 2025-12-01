package com.bill.billing.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "single_swap_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SingleSwapPayment extends BasePayment {

    private String stationId;

    private Long packageId;
}
