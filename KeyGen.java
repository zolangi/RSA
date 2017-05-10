package project2;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Zolangi Ramirez
 *
 */

public class KeyGen {
	static BigInteger p,q,n,totient;
	
	public static void main(String[] args) {
		Random rnd = new Random();						// random number
		int bitLength = 512;							// recommended bit length for p and q
		p = BigInteger.probablePrime(bitLength, rnd);  	// create p, as a random prime
		q = BigInteger.probablePrime(bitLength, rnd);	// create q, as a random prime
		n = p.multiply(q);								// the size n by p x q 
		totient = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)); // totient function calculated by (p-1)x(q-1) 
		BigInteger e = genE(totient, rnd, bitLength);
		
	}

	public static boolean gcdIsOne(BigInteger e, BigInteger totient){
		boolean isOne= false;
		
		BigInteger gcd = e.gcd(totient);
		
		if(gcd == BigInteger.ONE)
			isOne = true;
		
		return isOne;
	}
	
	public static BigInteger genE (BigInteger totient, Random rnd, int bitLength){
		BigInteger temp = BigInteger.probablePrime(bitLength, rnd);
		BigInteger e = null;
		boolean gcd = gcdIsOne(temp, totient);
		if(temp.compareTo(BigInteger.ONE) == -1 && temp.compareTo(totient) == -1 && gcd == true ){
			e = temp;
		}
		else{
			genE(totient, rnd, bitLength);
		}
		return e;
	}
}
