package project2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Zolangi Ramirez
 * @author Phillip Gulegin
 */

public class DigitalSignature {

	public static void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException, ClassNotFoundException {
		
		// Read entire message file as a byte stream; convert into byte array
		byte[] initialMessage = Files.readAllBytes(new File("test.txt").toPath());
		
		// Create a new digest object
		MessageDigest initialMessageDigest = MessageDigest.getInstance("MD5");
		
		// Update the digest object
		initialMessageDigest.update(initialMessage);
		
		// Create digest from the initial message
		byte [] initialDigest = initialMessageDigest.digest();
		
		// Generate BigInt D from Digest
		BigInteger initialBigIntDigest = new BigInteger(1, initialDigest);
				
		// Create file and object streams
		FileInputStream privateKeyFileInStream = new FileInputStream("privkey.rsa");
		ObjectInputStream privateKeyObjectInStream = new ObjectInputStream(privateKeyFileInStream);
		
		// Read keys from file
		BigInteger d = new BigInteger((byte[]) privateKeyObjectInStream.readObject());
		BigInteger n = new BigInteger((byte[]) privateKeyObjectInStream.readObject());
		
		// Close streams
		privateKeyFileInStream.close();
		privateKeyObjectInStream.close();
		
		// Sign D with private keys
		BigInteger initialSignature = initialBigIntDigest.modPow(d, n);
				
		// Create file and object streams
		FileOutputStream encryptFileOutStream = new FileOutputStream("test.txt.signed");
		ObjectOutputStream encryptObjectOutStream = new ObjectOutputStream(encryptFileOutStream);
		
		// Write out signature and message to file
		encryptObjectOutStream.writeObject(initialSignature.toByteArray());
		encryptObjectOutStream.writeObject(initialMessage);
		
		// Close streams
		encryptFileOutStream.close();
		encryptObjectOutStream.close();
		
		// ------- This ends the sending part of the message
		
		
		// ------- Start of the receiving part of the message
		
		// Create file and object streams
		FileInputStream decryptFileInStream = new FileInputStream("test.txt.signed");
		ObjectInputStream decryptObjectInStream = new ObjectInputStream(decryptFileInStream);
		
		// Read signature and message from file
		BigInteger resultSignature = new BigInteger((byte[]) decryptObjectInStream.readObject());
		byte[] resultMessage = (byte[]) decryptObjectInStream.readObject();
		
		// Close the streams
		decryptFileInStream.close();
		decryptObjectInStream.close();
		
		// Create file and object streams
		FileInputStream publicKeyFileInStream = new FileInputStream("pubkey.rsa");
		ObjectInputStream publicKeyObjectInStream = new ObjectInputStream(publicKeyFileInStream);

		// Read signature and message from file
		BigInteger e = new BigInteger((byte[]) publicKeyObjectInStream.readObject());
		n = new BigInteger((byte[]) publicKeyObjectInStream.readObject());
		
		// Close the streams
		publicKeyFileInStream.close();
		publicKeyObjectInStream.close();
				
		// Convert the signature back to the digest (We will be comparing this digest to the message digest)
		BigInteger resultBigIntDigest = resultSignature.modPow(e, n);
		byte[] tempsignatureDigest = resultBigIntDigest.toByteArray();
		byte[] signatureDigest = new byte[tempsignatureDigest.length-1];
		System.arraycopy(tempsignatureDigest, 1, signatureDigest, 0, signatureDigest.length);
		
		// Create digest for the input message
		MessageDigest resultMessageDigest = MessageDigest.getInstance("MD5");
		resultMessageDigest.update(resultMessage);
		byte [] messageDigest = resultMessageDigest.digest();
		
		// Compare message digest to signature digest
		if (equals(messageDigest, messageDigest)) {
			System.out.println("Equal");
		}
		else {
			System.out.println("Not equal");
		}
	}

	private static boolean equals(byte[] initial, byte[] result) {
		
		// Returns false if the digests are not the same length
		if (initial.length != result.length) {
			return false;
		}
		
		// Checks the digests byte by byte
		for (int i = 0; i < initial.length; i++) {
			if (initial[i] != result[i]) {
				return false;
			}
		}
		
		return true;
	}

	/*private static void print(byte[] digest) {		
		for (int i = 0; i < digest.length; i++) {
			System.out.print(digest[i]);
		}
		System.out.println();
	}*/

}
