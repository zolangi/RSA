package project2;


import java.security.*;

public class MD {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		String S1 = new String("Here is the original string!  Cool!");
		String S2 = new String("Here is the original string!  Cool!");
		String S3 = new String("Here is che original string!  Cool!");

		MessageDigest m1 = MessageDigest.getInstance("MD5");
		MessageDigest m2 = MessageDigest.getInstance("MD5");
		MessageDigest m3 = MessageDigest.getInstance("MD5");

		byte [] b1 = S1.getBytes();
		byte [] b2 = S2.getBytes();
		byte [] b3 = S3.getBytes();

		m1.update(b1);
		m2.update(b2);
		m3.update(b3);

		byte [] digest1 = m1.digest();
		byte [] digest2 = m2.digest();
		byte [] digest3 = m3.digest();
		
		print(digest1);
		print(digest2);
		print(digest3);

		System.out.println(digest1.length);

		if (MessageDigest.isEqual(digest1, digest2))
			System.out.println("Equal");
		else
			System.out.println("Not equal");
		
		if (MessageDigest.isEqual(digest2, digest3))
			System.out.println("Equal");
		else
			System.out.println("Not equal");
		
		if (MessageDigest.isEqual(digest1, digest3))
			System.out.println("Equal");
		else
			System.out.println("Not equal");
	}

	private static void print(byte[] digest) {
		// TODO Auto-generated method stub
		
		for (int i = 0; i < digest.length; i++) {
			System.out.print(digest[i]);
		}
		System.out.println();
	}

}