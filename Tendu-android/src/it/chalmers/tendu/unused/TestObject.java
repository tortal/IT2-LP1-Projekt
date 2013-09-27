package it.chalmers.tendu;

import java.io.Serializable;

public class TestObject implements Serializable{

	private int one, two, three;
	private double test;
	String test1;
	
	public TestObject(){
		one = 1;
		two = 2;
		three = 3;
		test = 666.666;
		test1 = "balubas";
	}

	public int getOne() {
		return one;
	}

	public void setOne(int one) {
		this.one = one;
	}

	public int getTwo() {
		return two;
	}

	public void setTwo(int two) {
		this.two = two;
	}

	public int getThree() {
		return three;
	}

	public void setThree(int three) {
		this.three = three;
	}

	public double getTest() {
		return test;
	}

	public void setTest(double test) {
		this.test = test;
	}

	public String getTest1() {
		return test1;
	}

	public void setTest1(String test1) {
		this.test1 = test1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + one;
		long temp;
		temp = Double.doubleToLongBits(test);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((test1 == null) ? 0 : test1.hashCode());
		result = prime * result + three;
		result = prime * result + two;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestObject other = (TestObject) obj;
		if (one != other.one)
			return false;
		if (Double.doubleToLongBits(test) != Double
				.doubleToLongBits(other.test))
			return false;
		if (test1 == null) {
			if (other.test1 != null)
				return false;
		} else if (!test1.equals(other.test1))
			return false;
		if (three != other.three)
			return false;
		if (two != other.two)
			return false;
		return true;
	}
	
	
}
