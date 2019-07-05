package org.lolobored.revolut;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Statement {
	private String currency;
private List<Transaction> transactions = new ArrayList<>();

public void addTransaction(Transaction tx){
	transactions.add(tx);
}
}
