/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipi.iot2020;

import java.io.IOException;
import java.util.Scanner; 

/**
 *
 * @author dariamargheritamaggi
 */
public class MainApp {
        	public static final String[] commands = {"humidity","help","exit","start dehumidifier", "stop dehumidifier"};

	public static void main(String[] args) throws IOException, InterruptedException {
                help();
		//runServer();
		while (true) {
			try {
                                 Scanner myObj = new Scanner(System.in);  
                                 System.out.print("Enter command: ");

                                String userName = myObj.nextLine().toLowerCase();  

				switch (userName) {
                                    case "start dehumidifier":
                                        System.out.println("TODO: check if not already active\n");
                                        System.out.println("Dehumidifier started");
                                        break;
                                    case "stop dehumidifier":
                                        System.out.println("TODO: check if not already off");
                                        System.out.println("Dehumidifier stopped");
                                        System.out.println("TODO: get current humidity level");
                                        break;
                                    case "help":
                                        help();
                                        break;
                                    case "exit":
                                        System.exit(0);
                                        break;
                                    default:
                                         System.out.println("Command not found.\n");
                                         help();
                                         break;
				}

			} catch (Exception e) {
				System.out.println("Invalid input, please try again.\n");
				e.printStackTrace();
			}
		}
                
		}
        public static void help() {
            System.out.println(".........................\n");
            System.out.println("");
	    System.out.println("Type one of the following and press enter to continue:");
            System.out.println("");
            System.out.println(commands[0]+" - get current humidity value");
            System.out.println(commands[1]+" - see the available commands");
            System.out.println(commands[2]+" - exit the application");
            System.out.println(commands[3]+" - start the dehumidifier (if not already active)");            
            System.out.println(commands[4]+" - stop the dehumidifier (if not already idle)");
            System.out.println("");
	}
        
        
        
        	}