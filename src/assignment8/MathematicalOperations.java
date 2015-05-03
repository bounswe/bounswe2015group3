/**
 * 
 */
package assignment8;

/**
 * @author Bunyamin, MrNanonA ,Umut
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
        
}
