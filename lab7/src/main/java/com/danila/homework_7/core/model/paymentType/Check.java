package com.danila.homework_7.core.model.paymentType;

import com.danila.homework_7.core.model.Payment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "check_payments")
@Getter
@Setter
public class Check extends Payment {
    private String name;
    private String bankID;
}