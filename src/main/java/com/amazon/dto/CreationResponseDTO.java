package com.amazon.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreationResponseDTO extends ResponseDTO {


	private Long id;

	public CreationResponseDTO(Integer statusCode, String message, Long id) {
		super(statusCode, message);
		this.id = id;
	}

}
