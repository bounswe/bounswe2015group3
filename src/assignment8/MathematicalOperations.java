/**
 * 
 */
package assignment8;

/**
 * @author Bunyamin, MrNanonA, Umut , BurakYilmaz92 , akifcorduk, Cem OZEN
 *
 */
public class MathematicalOperations {

	/**



	 * 2 number addition

	 * @param num1 is the first number

	 * @param num2 is the second number

	 * @return num1 + num2

	 */



	public int addition(int num1, int num2) {
		return num1 + num2 ;
	}
	/**
     * Finds the remainder of x when it is divided by y
     * @param num1 boolean value
     * @return negated boolean value
    */
    public boolean negation(boolean x){
		if(x == true){
			return false;
		}
		else{
			return true;
		}
	}
    /**
     * Finds the remainder of x when it is divided by y
     * @param num1 the numerator
     * @param num2 the denumerator
     * @return the remainder
    */
	public int remainder(int num1, int num2) {

		return x % y;
	}
    
    public int subtraction(int num1, int num2)
    {
        return num1 - num2;
    }
    
    public int division(int num1, int num2) {
		return num1 / num2 ;
	}
    /**
     
     * Inverse Division
     
     * @param num1 is the divisor
     
     * @param num2 is the dividend
     
     * @return num2 divided by num1
     
     */
    public int inverseDivision(int num1, int num2){
        return num2 / num1 ;
    }
    
    /**
     * Multiplies given two numbers and returns the result
     * @param num1 the first factor
     * @param num2 the second factor
     * @return the product of num1 and num2
    */
    public int multiplication(int num1, int num2) {
        return num1 * num2 ;
    }
    
	/**



	 * 2 number power

	 * @param num1 is the base

	 * @param num2 is the power

	 * @return base^power

	 */
    public int power(int num1, int num2) {
    	
    	return Math.pow(num1 , num2);
    }
 	/**
	 * Compare
	 * @param num1 and num2 are the numbers to be compared
	 * @return true if num1>num2
	 */
    public boolean greaterThan(int num1, int num2) {
    	if(num1>num2)
    		return true;
		else
			return false;
    }

    /**
     * unary plus returns the number
     * @param num the number
     * @return the number itself
     */ 
    public int unaryPlus(int num) { 
        return num;
    }
    /**
     * unary minus returns 0 minus the number
     * @param num the number
     * @return 0 minus the number
     */ 
    public int unaryMinus(int num) {
        return -num;
    }
}
