package org.lolobored.revolut.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Transaction {
	private Date date;
	private BigDecimal amount;
	private String reference;
}
