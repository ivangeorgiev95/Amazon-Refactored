package com.amazon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentDTO {

	@NotNull(message = "Credit card id can't be null!")@Positive(message = "Credit card id must be positive number!")
	private Long creditCardId;
}
