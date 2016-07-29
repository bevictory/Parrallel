
package util;

import java.math.BigDecimal;

public class test {
	public static void main(String []args){
		double a = 1.0 / 3;
		System.out.println(a+1.0/7);
		BigDecimal bigDecimal = new BigDecimal(((Double)(a+1.0/7)).toString());
		
		System.out.println(bigDecimal.doubleValue());
	}

}
