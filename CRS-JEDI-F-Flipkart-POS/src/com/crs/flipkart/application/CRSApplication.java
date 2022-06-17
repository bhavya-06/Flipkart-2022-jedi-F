/**
 * 
 */
package com.crs.flipkart.application;

import java.time.*;
import java.time.format.*;
import java.util.*;

import com.crs.flipkart.bean.*;
import com.crs.flipkart.business.StudentInterface;
import com.crs.flipkart.business.StudentService;
import com.crs.flipkart.business.UserInterface;
import com.crs.flipkart.business.UserService;
import com.crs.flipkart.exceptions.EmailAlreadyInUseException;
import com.crs.flipkart.exceptions.UserNotFoundException;
import com.crs.flipkart.validators.StudentValidator;
import com.crs.flipkart.constants.Roles;

public class CRSApplication {

	static boolean loggedIn = false;
	UserInterface userService = new UserService();
	StudentInterface studentService = new StudentService();

	/**
     * Main function. Program starts execution from here. Displays initial menu
     */
	
	public static void main(String[] args) {
		// Main Menu
		// Main screen will have 4 things

		Scanner sc = new Scanner(System.in);
		CRSApplication crsApplication = new CRSApplication();
		int choice;
		do {
			//System.out.println("\n\n_____________________________________________________________________________");
			//System.out.println("");
			System.out.println("----------------WELCOME TO COURSE REGISTRATION SYSTEM----------------");
			System.out.println("                                                                     ");
			System.out.println("----------------Student Menu----------------");
			System.out.println("                                                                     ");
			System.out.print("\nEnter 1 for Login\nEnter 2 to register yourself as a student \nEnter 3 to Update Password\nEnter 4 to exit CRS \nOption : ");
			choice = sc.nextInt();
			switch (choice) {
			case 1: // Login
				crsApplication.login();
				break;
			case 2: // Student registration
				crsApplication.registerStudent();
				break;
			case 3: // update password
				crsApplication.updatePassword();
				break;
			case 4: // exit
				break;
			default:
				System.out.print("Invalid option\n");
			}
		} while (choice != 4);
	}

	/**
     * method for all users to login
     */
	public void login() {
		Scanner sc = new Scanner(System.in);

		System.out.println("\n\n\n");
		System.out.println("");
		System.out.println("                                    Login for all users                              ");
		System.out.println("\n\n");
		String email, password;
		System.out.print("Please enter the Email address you used for registration:");
		email = sc.next();
		System.out.print("Enter correct Password:");
		password = sc.next();
		try {			
			
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
			 
			 LocalDateTime myDateObj = LocalDateTime.now();
			   
			 String formattedDate = myDateObj.format(myFormatObj);  
			
			User user = userService.login(email, password);
			
			loggedIn=true;
			
			Roles userRole = Roles.stringToName(user.getRole());
			
			switch (userRole) {
			
			case ADMIN:

				System.out.println("\n\nAdmin successfully logged in at " + formattedDate  +"You can now access all functions. ");
				CRSAdmin admin=new CRSAdmin ();
				admin.create_menu();
				break;
			case PROFESSOR:
				
				System.out.println("\n\n" + user.getName() +" successfully logged in at " + formattedDate );
				String professorId=user.getUserId();
				CRSProfessor professor = new CRSProfessor();
				professor.create_menu(professorId);
				break;
			case STUDENT:
				String studentId = user.getUserId();
				boolean isApproved = StudentValidator.isApproved(studentId);
				if (isApproved) {
					System.out.println("\n\n" + user.getName() +" successfully logged in at " + formattedDate );
					CRSStudent student = new CRSStudent();
					student.create_menu(studentId);

				} else {
					System.err.print("\n Failed to login, Please contact admin \n ");
					loggedIn = false;
				}
				break;
			}
		}
		catch(UserNotFoundException e) {
			System.err.println("Invalid Credentials!, \n, Please try again");
		}			

	}
	
	/**
     * method for registration of new students
     */
	public void registerStudent() {
		Scanner sc = new Scanner(System.in);

		String email, name, password, branchName, batch, contactNumber;
		// input all the student details
		System.out.println("\n\n");
		System.out.println("");
		System.out.println("                           STUDENT REGISTRATION                              ");
		System.out.println("\n\n");
		System.out.print("Enter the Name:");
		name = sc.nextLine();
		System.out.print("Enter Contact: ");
		contactNumber = sc.nextLine();
		System.out.print("Enter Email:");
		email = sc.next();
		System.out.print("Choose a strong Password:");
		password = sc.next();
		sc.nextLine();
		System.out.print("Enter your Branch:");
		branchName = sc.nextLine();
		System.out.print("Enter your Batch:");
		batch = sc.nextLine();

		try {
			System.out.println(userService.registerStudent(name,contactNumber,email, password, branchName, batch));
		}
		catch(EmailAlreadyInUseException e) {
			System.err.println("Error : " + e.getMessage());
		}

	}
	
	/**
     * method for updating the existing password
     */
	public void updatePassword()
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("\n\n");
		System.out.println("");
		System.out.println("                             UPDATE PASSWORD                                 ");
		System.out.println("\n\n");

		String email, oldPassword, newPassword;
		System.out.print("Enter the Email:");
		email = sc.next();
		System.out.print("Old Password:");
		oldPassword = sc.next();
		System.out.print("New Password:");
		newPassword = sc.next();
		
		try {			
			System.out.println(userService.updatePassword(email, oldPassword,newPassword));
		}
		catch(UserNotFoundException e) {
			System.err.println("Email or password is incorrect!");
		}
	}

}
