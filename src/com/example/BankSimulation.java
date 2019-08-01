package com.example;

import java.util.LinkedList;

public class BankSimulation {

	// some defaults....
	private static final long TIME_LIMIT = 1000;
	private static final int TELLER_COUNT = 1; // TODO

	private LinkedList<Customer> customerQueue;
	private Customer[] customersBeingServed;

	private LinkedList<Customer> customersServed;
	private int maximumQueueLength = 0;
	private int runningQueueSum = 0;
	private long timeLimit;

	private CustomerType customerType;

	public BankSimulation() {
		this(TIME_LIMIT, CustomerType.YELLOW);
	}

	public BankSimulation(CustomerType customerType) {
		this(TIME_LIMIT, customerType);
	}

	public BankSimulation(long timeLimit, CustomerType customerType) {
		this.timeLimit = timeLimit;
		this.customerQueue = new LinkedList<>();
		this.customersBeingServed = new Customer[TELLER_COUNT];
		this.customersServed = new LinkedList<>();
		this.customerType = customerType;
	}

	public static void main(String[] args) {
		BankSimulation bankSimulationYellow = new BankSimulation(CustomerType.YELLOW);
		bankSimulationYellow.simulate();
		bankSimulationYellow.printResults();

		BankSimulation bankSimulationRed = new BankSimulation(CustomerType.RED);
		bankSimulationRed.simulate();
		bankSimulationRed.printResults();

		BankSimulation bankSimulationBlue = new BankSimulation(CustomerType.BLUE);
		bankSimulationBlue.simulate();
		bankSimulationBlue.printResults();
	}

	public void simulate() {

		for (long time = 0; time < timeLimit || customerQueue.size() > 0; time++) {

			// we only welcome customers if we are still in time
			if (customerArrived(time) && time < timeLimit) {
				customerQueue.add(new Customer(time, customerType));
			}

			// check if customer is done
			for (int i = 0; i < customersBeingServed.length; i++) {
				Customer customer = customersBeingServed[i];
				if (customer != null) {
					if (customer.getEndTime() < time) {
						customersServed.add(customersBeingServed[i]);
						customersBeingServed[i] = null;
						//System.out.println("A customer was served: Customer#" + customersServed.size());
					}
				}
			}

			if (!customerQueue.isEmpty()) {
				// check open tellers, then assign customer to them
				int indexOfFreeTeller = getIndexOfFreeTeller();
				if (indexOfFreeTeller != -1) {
					// get next customer, and assign them to one of the tellers
					Customer customer = customerQueue.removeFirst();
					long processingTime = customer.getProcessingTime();
					customer.setEndTime(time + processingTime);
					customersBeingServed[indexOfFreeTeller] = customer;
				}
			}

			// update running sum
			runningQueueSum += customerQueue.size();
			// update max queue length
			if (customerQueue.size() > maximumQueueLength) {
				maximumQueueLength = customerQueue.size();
			}
		}
	}

	private void printResults() {
		System.out.println();
		System.out.println("== RESULTS ==");
		System.out.printf("Running %s customers for time: %s.\n", customerType.name(), timeLimit);
		System.out.println("Remaining customers in queue: " + customerQueue.size());
		System.out.printf("Customers Served: %s\n", customersServed.size());

		double totalWaitingTime = getTotalWaitingTime();
		double maximumWaitingTime = getMaximumWaitingTime();
		double averageWaitingTime = getAverageWaitingTime();
		System.out.printf("Total waiting time: %.2f\n", totalWaitingTime);
		System.out.printf("Maximum waiting time per customer: %.2f\n", maximumWaitingTime);
		System.out.printf("Average waiting time per customer: %.2f\n", averageWaitingTime);
		System.out.printf("Difference between average and maximum: %.2f\n", maximumWaitingTime - averageWaitingTime);
		System.out.printf("Average queue length: %.2f\n", getAverageQueueLength());
		System.out.printf("Maximum queue length: %s\n", maximumQueueLength);
		System.out.println();

	}

	private double getAverageQueueLength() {
		return runningQueueSum / timeLimit;
	}

	private double getAverageWaitingTime() {
		return getTotalWaitingTime() / customersServed.size();
	}

	private double getMaximumWaitingTime() {
		return customersServed.stream().mapToDouble(Customer::getWaitTime).max().orElse(0);
	}

	private double getTotalWaitingTime() {
		return customersServed.stream().mapToLong(Customer::getWaitTime).sum();
	}

	// TODO randomise this
	private int getIndexOfFreeTeller() {
		int nextEmpty = -1;
		for (int i = 0; i < customersBeingServed.length; i++) {
			if (customersBeingServed[i] == null) {
				nextEmpty = i;
			}
		}
		return nextEmpty;
	}

	public static boolean customerArrived(long time) {
		double prob = 1 - Math.exp(-time / 100);
		return Math.random() <= prob;
	}
}
