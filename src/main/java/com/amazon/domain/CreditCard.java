package com.amazon.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "credit_cards")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Card holder's name can't be null or empty!")
    private String cardNumber;
    @FutureOrPresent(message = "Credit card has expired!")
    private LocalDate expiringDate;
    @NotBlank(message = "Card holder's name can't be null or empty!")
    private String cardholderName;
    @NotNull(message = "Balance of credit card can't be null!") @PositiveOrZero(message = "Balance of credit card can't be negative!")
    private Double balance;

    public CreditCard(String cardNumber, LocalDate expiringDate, String cardholderName, Double balance) {
        this.cardNumber = cardNumber;
        this.expiringDate = expiringDate;
        this.cardholderName = cardholderName;
        this.balance = balance;
    }
}
