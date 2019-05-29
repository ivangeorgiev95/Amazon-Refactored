package com.amazon.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
public class ResponseDTO {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private String time;
	private Integer statusCode;
	private String message;
	
	public ResponseDTO(Integer statusCode, String message) {
		this.time = LocalDateTime.now().format(FORMATTER);
		this.statusCode = statusCode;
		this.message = message;
	}
	
	

}
