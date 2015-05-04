/**
 * 
 */
package assignment8;

/**
 * @author Bunyamin, MrNanonA ,Umut , BurakYilmaz92 , akifcorduk
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
	
    public boolean negation(boolean x){
		if(x == true){
			return false;
		}
		else{
			return true;
		}
	}

	public int remainder(int x, int y) {

		return x % y;
	}
    
    public int subtraction(int num1, int num2)
    {
        return num1 - num2;
    }
    
    public int division(int num1, int num2) {
		return num1 / num2 ;
	}
    
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
    }
}
