package assignment8;

import static org.junit.Assert.*;

import org.junit.Test;

import assignment8.MathematicalOperations;

/**
 * 
 */

/**
 *Test for MathematicalOperations Class
 */
public class TestMathematicalOperations {
	

	
	/**

	* to test general attitude of addition

	*/

	@Test
	public void testAddition() {
		MathematicalOperations t1 = new MathematicalOperations();
		assertEquals("-1 + 6 must be 5", 5, t1.addition(-1, 6));

		MathematicalOperations t2 = new MathematicalOperations();
		assertEquals("30 + -8 must be 22", 22, t2.addition(30, -8));

	}
	public void testNegation() {
		MathematicalOperations t1 = new MathematicalOperations();
		assertEquals("negation of true", false, t1.negation(true));

		MathematicalOperations t2 = new MathematicalOperations();
		assertEquals("negation of false", true, t2.negation(false));

	}
	@Test
	public void testRemainder() {
		MathematicalOperations t1 = new MathematicalOperations();
		assertEquals("remainder of 24 by 8 must be 0", 0, t1.remainder(24, 8));
		MathematicalOperations t2 = new MathematicalOperations();
		assertEquals("remainder of 16 by 15 must be 1", 1, t2.remainder(16, 15));
	}
}