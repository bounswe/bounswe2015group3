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
    /**
     * Tests the functionality of the negation operation.
    */
	public void testNegation() {
		MathematicalOperations t1 = new MathematicalOperations();
		assertEquals("negation of true", false, t1.negation(true));

		MathematicalOperations t2 = new MathematicalOperations();
		assertEquals("negation of false", true, t2.negation(false));

	}
    /**
     * Tests the functionality of the remainder operation.
    */
	@Test
	public void testRemainder() {
		MathematicalOperations t1 = new MathematicalOperations();
		assertEquals("remainder of 24 by 8 must be 0", 0, t1.remainder(24, 8));
		MathematicalOperations t2 = new MathematicalOperations();
		assertEquals("remainder of 16 by 15 must be 1", 1, t2.remainder(16, 15));
	}
    
    @Test
    public void testSubtraction()
    {
        MathematicalOperations t1 = new MatematicalOperations();
        assertEquals("Subtraction of 5 from 10 must be 5", 5, t1.subtraction(10,5));
        MathematicalOperations t2 = new MatematicalOperations();
        assertEquals("Subtraction of -3 from 12 must be 15", 15 , t2.subtraction(12,-3));
    }
    /**
     * Tests the functionality of the multiplication operation. 
    */
    @Test
    public void testMultiplication()
    {
        MathematicalOperations t1 = new MatematicalOperations();
        assertEquals("Multiplication of 0 with 10 must be 0", 0, t1.multiplication(0,10));
        MathematicalOperations t2 = new MatematicalOperations();
        assertEquals("Multiplication of 7 with -6 must be -42", -42 , t2.multiplication(7,-6));
    }
    
    @Test
    public void testPower() {
    	MathematicalOperations t1 = new MathematicalOperations();
    	assertEquals("8 to the power 0 must be 1", 0, t1.power(8 , 0));
    	MathematicalOperations t2 = new MathematicalOperations();
    	assertEquals("3 to the power 4 must be 27", 27 , t2.power(3 , 4));
    }
    @Test
    public void testInverseDivision() {
        MathematicalOperations t1 = new MathematicalOperations();
        assertEquals("5 inverse divided by 12 must be 2", 2, t1.inverseDivision(5 , 12));
        MathematicalOperations t2 = new MathematicalOperations();
        assertEquals("32 inverse divided by 3 must be 0", 0 , t2.inverseDivision(32 , 3));
    }

    @Test
    public void testUnaryPlus() {
        MathematicalOperations t1 = new MathematicalOperations();
        assertEquals("unary plus on 4 gives 4", 4, t1.inverseDivision(4));
        MathematicalOperations t2 = new MathematicalOperations();
        assertEquals("unary plus on 8 gives 8", 8 , t2.inverseDivision(8));
    }
    @Test
    public void testUnaryMinus() {
        MathematicalOperations t1 = new MathematicalOperations();
        assertEquals("unary minus on 4 gives -4", -4, t1.inverseDivision(4));
        MathematicalOperations t2 = new MathematicalOperations();
        assertEquals("unary minus on -5 gives 5", 5 , t2.inverseDivision(-5));
    }

	@Test
	public void testGreaterThan() {
		MathematicalOperations t1 = new MathematicalOperations();
		assertEquals("It must be true that 5 is greater than 3", true, t1.greaterThan(5, 3));
		
		MathematicalOperations t1 = new MathematicalOperations();
		assertEquals("It must be false that 4 is greater than 4", false, t1.greaterThan(4, 4));

	}
}
