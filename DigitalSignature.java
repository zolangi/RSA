package project2;

import java.io.File;
import java.io.FileInputStream;
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
	
	@SuppressWarnings("resource")
	public static boolean sendFile(String fileName) throws IOException, NoSuchAlgorithmException {
		
		// Read entire message file as a byte stream; convert into byte array
		// No need to verify if the file exists because that was taken care of in Main.java
		byte[] initialMessage = Files.readAllBytes(new File(fileName).toPath());
		
		// Create a new digest object
		MessageDigest initialMessageDigest = MessageDigest.getInstance("MD5");
		
		// Update the message digest object
		initialMessageDigest.update(initialMessage);
		
		// Create digest from the initial message
		byte [] initialDigest = initialMessageDigest.digest();

		// Generate BigInt D from Digest
		BigInteger initialBigIntDigest = new BigInteger(1, initialDigest);
		
		// Create file and object streams
		FileInputStream privateKeyFileInStream = new FileInputStream("privkey.rsa");
		ObjectInputStream privateKeyObjectInStream = new ObjectInputStream(privateKeyFileInStream);

		// Read keys from file
		BigInteger d = null;
		BigInteger n = null;
		try {
			d = new BigInteger((byte[]) privateKeyObjectInStream.readObject());
			n = new BigInteger((byte[]) privateKeyObjectInStream.readObject());
		} catch (ClassNotFoundException e) {
			
			// Informs the user that the keys could not be read
			System.out.println("Keys from 'privkey.rsa' could not be read.");
			
			// Returns false if the keys are not found.
			return false;
		}

		// Close streams
		privateKeyFileInStream.close();
		privateKeyObjectInStream.close();
		
		// Sign D with private keys
		BigInteger initialSignature = initialBigIntDigest.modPow(d, n);

		// Create file and object streams
		FileOutputStream encryptFileOutStream = new FileOutputStream(fileName + ".signed");
		ObjectOutputStream encryptObjectOutStream = new ObjectOutputStream(encryptFileOutStream);

		// Write out signature and message to file
		encryptObjectOutStream.writeObject(initialSignature.toByteArray());
		encryptObjectOutStream.writeObject(initialMessage);

		// Close streams
		encryptFileOutStream.close();
		encryptObjectOutStream.close();
		
		// Returns true if everything goes to plan
		return true;
	}

	public static boolean receiveFile(String fileName) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
		
		// Create file and object streams
		FileInputStream decryptFileInStream = null;
		ObjectInputStream decryptObjectInStream = null;
		try {
			decryptFileInStream = new FileInputStream(fileName);
			decryptObjectInStream = new ObjectInputStream(decryptFileInStream);
		} catch (Exception e) {
			
			// Return false if the message has been tampered with
			return false;
		}

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
		BigInteger n = new BigInteger((byte[]) publicKeyObjectInStream.readObject());

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
		if (equals(signatureDigest, messageDigest)) {
			
			// Returns true if the message has not been tampered with
			return true;
		}
		else {
			
			// Return false if the message has been tampered with
			return false;
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
	
	private static void print(byte[] digest) {
		// TODO Auto-generated method stub
		
		for (int i = 0; i < digest.length; i++) {
			System.out.print(digest[i]);
		}
		System.out.println();
	}
}
