package com.example;

public enum CustomerType {
	BLUE(5, 1),
	RED(2, 2),
	YELLOW(2, 5);


	private double alpha;
	private double beta;

	CustomerType(double alpha, double beta) {
		this.alpha = alpha;
		this.beta = beta;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}
}
