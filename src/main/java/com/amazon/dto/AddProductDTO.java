package com.amazon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@AllArgsConstructor
public class AddProductDTO {

	@NotBlank(message = "Product name can't be blank!")
	private String name;
	@NotNull(message = "Product description can't be null!")
	private String description;
	@NotNull(message = "Product price can't be null!")@PositiveOrZero(message = "Product price can't be negative number!")
	private Double price;
	@NotNull(message = "Product quantity can't be null!")@PositiveOrZero(message = "Product quantity can't be negative number!")
	private Integer quantity;
	@NotNull(message = "Product category id can't be null!")@PositiveOrZero(message = "Product category id can't be negative number!")
	private Long categoryId;
	@NotNull(message = "Product seller id can't be null!")@PositiveOrZero(message = "Product seller id can't be negative number!")
	private Long sellerId;
}
