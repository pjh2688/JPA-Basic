package com.valueTypeComparison.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Embeddable;

@Embeddable
public class Period {

	private String startDate;
	private String endDate;
	
	public Period() {
		this.startDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	
}
