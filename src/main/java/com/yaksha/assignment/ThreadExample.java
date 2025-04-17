package com.yaksha.assignment;

public class ThreadExample {

	public static void main(String[] args) {
		// Task 1: Implement Thread class by extending it
		MyThread thread1 = new MyThread(); // Create instance of custom thread class
		thread1.start(); // Start the thread

		// Task 2: Create another thread by extending Thread class
		MyThread thread2 = new MyThread(); // Create another instance of custom thread class
		thread2.start(); // Start the second thread
	}

	// Task 1: Custom Thread class by extending Thread class
	static class MyThread extends Thread {
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + " is running.");
		}
	}
}
