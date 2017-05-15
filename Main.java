/**
 * 
 */
package project2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * @author Zolangi Ramirez
 * @author Phillip Gulegin
 */
public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub

		// Welcome message to the user
		System.out.println("Welcome to Project2!");
		
		// Generates the appropriate keys
		KeyGen.generateKeys();

		// Infinite menu prompt, until user quits the program
		boolean exit = false;
		while (!exit) {
			
			// Prompt the user for the options
			System.out.println("\nPlease select an option:");
			System.out.println("Press 1 to 'send' a file.");
			System.out.println("Press 2 to 'receive' a file.");
			System.out.println("Press 3 to 'tamper' with a signed file.");
			System.out.println("Press 4 to exit the program.");
			
			// Get the user's choice
			Scanner scanner = new Scanner(System.in);
			
			// Input validation
			int choice = 0;
			try {
				choice = scanner.nextInt();
				
				// Skips the newLine character that is still in the console
				scanner.nextLine();
				
			} catch (Exception e) {
				
				// Inform the user the selection was not valid
				System.out.println("Input is not valid. Try again.");
				continue;
			}
			
			// Switch statement to follow user's instruction
			switch (choice) {
			
			// Send file
			case 1:
				
				// Ask the user for a file name
				System.out.println("Input the file name to send: ");
				
				// Get the file name
				String sendFileName = scanner.nextLine();
				
				// Try to get the file, if it exists
				File sendFile = new File(sendFileName);
					
				// Executes if the file does not exist
				if(!sendFile.exists()) {
					
					// Inform the user if the file does not exist
					System.out.println("The file: " + sendFileName + " does not exist.");
				}
				// Executes if the file exists
				else {
					
					// 'Sends' the file
					// Executes if the message was sent correctly
					if (DigitalSignature.sendFile(sendFileName)) {
						
						// Informs the user the message was sent correctly
						System.out.println("The message was sent correctly.");
					}
					// Executes if the message was not sent correctly
					else {
						
						// Informs the user the message was not sent correctly
						System.out.println("The message was not sent correctly.");
					}
				}
				
				// Continues to the next iteration of the while loop
				break;
				
			// Receive file
			case 2:
				
				// Ask the user for a file name
				System.out.println("Input the file name to receive: ");
				
				// Get the file name
				String receiveFileName = scanner.nextLine();
				
				// Try to get the file, if it exists
				File receiveFile = new File(receiveFileName);
					
				// Executes if the file does not exist
				if(!receiveFile.exists()) {
					
					// Inform the user if the file does not exist
					System.out.println("The file: " + receiveFileName + " does not exist.");
				}
				// Executes if the file exists
				else {
					
					// 'Receives' the file
					// Executes if the message was not tampered with
					if (DigitalSignature.receiveFile(receiveFileName)) {
						
						// Informs the user the message was sent correctly
						System.out.println("The message was not tampered with.");
					}
					// Executes if the message was tampered with
					else {
						
						// Informs the user the message was tampered with
						System.out.println("The message was tampered with.");
					}
				} 
				
				// Continues to the next iteration of the while loop
				break;
			
			// Tamper with file
			case 3:
				
				// Asks the user for a file to tamper with
				System.out.println("Input the file name to tamper: ");
				
				// Get the file name
				String tamperFileName = scanner.nextLine();
				
				// Try to get the file, if it exists
				File tamperFile = new File(tamperFileName);
					
				// Executes if the file does not exist
				if(!tamperFile.exists()) {
					
					// Inform the user if the file does not exist
					System.out.println("The file: " + tamperFileName + " does not exist.");
				}
				// Executes if the file exists
				else {
					
					// Read entire message file as a byte stream; convert into byte array
					byte[] message = Files.readAllBytes(tamperFile.toPath());
					
					// Inform the user to select a byte index to manipulate
					System.out.println("This file has " + message.length + " bytes.");
					System.out.println("Please select a byte from 0 to " + (message.length-1) + " to tamper with.");

					// Input validation
					int index = 0;
					try {
						
						// Gets the user's choice for the byte to manipulate
						index = scanner.nextInt();

						// Skips the newLine character that is still in the console
						scanner.nextLine();

					} catch (Exception e) {

						// Inform the user the selection was not valid
						System.out.println("Input is not valid. Try again.");
					}
					
					// Executes if the user enters an index not in range
					if (index < 0 || index > message.length-1) {
						
						// Informs the user that the index selected was not correct
						System.out.println("The index selected to tamper is out of range.");
						
						// Continues to the next iteration of the while loop
						continue;
					}
					
					// 'Tamper' with the file
					// Executes if the message was not tampered with
					if (ChangeByte.tamperFile(tamperFileName, index)) {
						
						// Informs the user the message was sent correctly
						System.out.println("The file was tampered with successfully.");
					}
					// Executes if the message was tampered with
					else {
						
						// Informs the user the message was tampered with
						System.out.println("The file was tampered with unsuccessfully.");
					}
				} 
				
				// Continues to the next iteration of the while loop
				break;
			
			// Exit the program
			case 4:
				
				// Exit out of the loop
				exit = true;
				
				// Close the scanner
				scanner.close();
				
				// Break out of switch statement to then exit the loop
				break;
				
			default:
				
				// Informs the user that the input was invalid
				System.out.println("Input is not valid. Try again.\n");
				
				// Continues the next iteration of the while loop
				continue;
			}
		}
		
	}

}
