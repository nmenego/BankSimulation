package com.example;

public class Customer {

	private static final double P = 100.0;
	private CustomerType customerType;
	private long startTime = 0;
	private long processingTime = 0;
	private long endTime = 0;
	public Customer(long startTime, CustomerType customerType) {
		this.customerType = customerType;
		this.startTime = startTime;
		this.processingTime = Math.round(computeTimeToProcess());
	}

	public long getProcessingTime() {
		return processingTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getWaitTime() {
		return endTime - startTime;
	}

	private double computeTimeToProcess() {
		return P *
				Math.pow(Math.random(), customerType.getAlpha() - 1.0) *
				Math.pow(1.0 - Math.random(), customerType.getBeta() - 1.0);
	}
}
