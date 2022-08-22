package com.smart.base;

import java.util.ArrayList;
import java.util.List;

public class PageView<T> {

	private int total;

	private String amount;

	private List<T> data = new ArrayList<>();

	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
