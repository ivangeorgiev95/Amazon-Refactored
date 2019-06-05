package com.amazon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCreditCardDTO {

	@NotNull(message = "Balance of credit card can't be null!") @PositiveOrZero(message = "Balance of credit card can't be negative!")
	private Double balance;
	@CreditCardNumber(message = "This is not a valid credit card number!")
	private String creditCardNumber;
	@FutureOrPresent(message = "Credit card has expired!")
	private LocalDate expiringDate;
	@NotBlank(message = "Card holder's name can't be null or empty!")
	private String cardHolderName;
}
