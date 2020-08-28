/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipi.iot2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner; 

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

/**
 *
 * @author dariamargheritamaggi
 */
public class MainApp {
    public static final String[] commands = 
    {"show resources - get resources present in the system",
    "start cooling - start a cooling system (if not already active)", 
    "stop cooling - stop a cooling system (if not already idle)",
    "show temperature sensors - show temperature sensors present in the system", 
    "show cooling - show cooling actuators present in the system",
    "help - show the available commands",
    "exit - exit the application"};
        
	public static ArrayList<TemperatureObserver> coapObserverTemperature= new ArrayList<TemperatureObserver>();        
	public static ArrayList<Thermostat> thermostats = new ArrayList<Thermostat>();
    public static ArrayList<Cooling> coolers = new ArrayList<Cooling>();
        
	public static void main(String[] args) throws IOException, InterruptedException {
        help();
        Integer index;
		runServer();
		while (true) {
			try {
                Scanner myObj = new Scanner(System.in);  
                System.out.print("Enter command: ");
                String command = myObj.nextLine().toLowerCase();  

				switch (command) {
                                    case "show resources":
                                        System.out.println("Resources List:");
                                        System.out.println("\tTemperature detection:");
                                        listTemperatureSensors();
                                        listCoolingSystems();
                                        break;

                                    case "show temperature sensors":
                                        listTemperatureSensors();
                                        break;
                                    

                                    case "show cooling":
                                        listCoolingSystems();
                                        break;
                            
                                    case "start cooling":
                                        System.out.println("TODO: implement if time")
                                        /*
                                        listCoolingSystems();
                                        index=new Scanner(System.in).nextInt();
                                        if (index>=0 && index<=humiditySensors.size()){
                                            Boolean status=coolers.get(index).checkActive();
                                            if(!status){
                                                Boolean result=changeCoolerStatus(coolers.get(index),"active",true);
                                                System.out.println(result?"Cooling system activated":"Attention! Cooling system not activated");
                                            }
                                            else
                                                System.out.println("Sensor is already active!");
                                        }*/
                                        break;
                                        
                                    case "stop cooling":
                                        System.out.println("TODO: implement if time")
                                        /*
                                        listCoolingSystems();

                                        index=new Scanner(System.in).nextInt();
                                        if (index>=0 && index<=coolers.size()){
                                            Boolean status=coolers.get(index).checkActive();
                                            if(!status){
                                                Boolean result=changeCoolerStatus(coolers.get(index),"non active",false);
                                                System.out.println(result?"Cooling system disactivated":"Attention! Cooling system not disactivated");
                                            }
                                            else
                                                System.out.println("Sensor is already non active!");
                                        }*/
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
            for(int i = 0; i < commands.lenght(); i ++)
                System.out.println(commands[i]);
            System.out.println("");
	}
        

        public static void listTemperatureSensors(){
            for (int i = 0; i < thermostats.size(); i++) {
                TemperatureSensor sensor = thermostats.get(i);
                System.out.println(i + "\t\tTemperature Sensor: " + sensor.getAddress() + " " + sensor.getPath());
            }

        }
        

        public static void listCoolingSystems(){
            for (int i = 0; i < coolers.size(); i++) {
                Cooling sensor = coolers.get(i);
                String status = sensor.checkActive()?"active":"not active";
                System.out.println(i + "\t\tCooling System: " + sensor.getAddress() + " "
                                                + sensor.getPath() +" "+ status +"\n");

                }
        
        	}

        public static Boolean changeCoolerStatus(Cooling sensor, String state, Boolean status){
            CoapClient client = new CoapClient(sensor.getNodeURI());
            CoapResponse response = client.post("state=" + state, MediaTypeRegistry.TEXT_PLAIN);
            String code = response.getCode().toString();
            if (!code.startsWith("2")) {
    			System.out.println("Error: " + code);
    			return false;
		      }
		    sensor.setActive(status);
            return true;
        }

        
        public static void runServer() {
		new Thread() {
			public void run() {
				ServerResource server = new ServerResource();
				server.startServer();
			     }
		      }.start();
	       }
        
        }
