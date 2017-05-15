package project2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Zolangi Ramirez
 * @author Phillip Gulegin
 */
public class ChangeByte {
	
	public static boolean tamperFile(String fileName, int index) throws IOException {
		
		// Get the file
		File file = new File(fileName);
		
		// Read entire message file as a byte stream; convert into byte array
		byte[] message = Files.readAllBytes(file.toPath());
		
		// Tamper with the message at the index of choice
		message[index] = 0;
		
		// Save the message back to file
		Files.write(file.toPath(), message);
		
		// Returns true if tampering was complete
		return true;
	}
}
