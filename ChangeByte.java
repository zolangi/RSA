package project2;

import java.io.File;
import java.util.Scanner;

/**
 * @author Zolangi Ramirez
 * @author Phillip Gulegin
 */
public class ChangeByte {

	public static void main(String[] args) {
//		Prompt the user for the input file name
//		Open the file as a binary file
//		Prompt the user for which byte to change (i.e. you are indexing the file byte by byte)
//		Change the byte to a random value and close the file

		Scanner sc = new Scanner(System.in);
		System.out.println("Please input a file name: ");
		String filename = sc.next();
		File file = new File(filename);
		
		sc.close();
	}

}
