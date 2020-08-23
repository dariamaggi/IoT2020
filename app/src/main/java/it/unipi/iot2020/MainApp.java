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
        	public static final String[] commands = {"show resources","help","exit","start dehumidifier", "stop dehumidifier", "start cooling", "stop cooling",
            "show humidity sensors", "show temperature sensors", "show dehumidifiers", "show cooling"};
        
	public static ArrayList<CoapObserverHumidity> coapObserverHumidity = new ArrayList<CoapObserverHumidity>();
	public static ArrayList<CoapObserverTemperature> coapObserverTemperature= new ArrayList<CoapObserverTemperature>();        
	
    public static ArrayList<HumiditySensor> humiditySensors = new ArrayList<HumiditySensor>();
	public static ArrayList<TemperatureSensor> temperatureSensors = new ArrayList<TemperatureSensor>();
    public static ArrayList<Dehumidifier> dehumidifiers= new ArrayList<Dehumidifier>();
    public static ArrayList<Cooling> coolers = new ArrayList<Cooling>();
        
	public static void main(String[] args) throws IOException, InterruptedException {
        help();
        Integer index;
		runServer();
		while (true) {
			try {
                Scanner myObj = new Scanner(System.in);  
                System.out.print("Enter command: ");
                String userName = myObj.nextLine().toLowerCase();  

				switch (userName) {
                                    case "show resources":
                                        System.out.println("Resources List:");
                                        System.out.println("\tHumidity detection:");

                                        listHumiditySensors();
                                        listTemperatureSensors();
                                        listCoolingSystems();
                                        listDehumidifiers();
                                        break;

                                    case "show humidity sensors":
                                        listHumiditySensors();
                                        break;    

                                    case "show temperature sensors":
                                        listTemperatureSensors();
                                        break;
                                    
                                    case "show dehumidifiers":
                                        listDehumidifiers();
                                        break;

                                    case "show cooling":
                                        listCoolingSystems();
                                        break;
                            
                                    case "start cooling":
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
                                        }
                                        break;
                                        
                                    case "stop cooling":
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
                                        }
                                        break;
                                    case "start dehumidifier":
                                        listDehumidifiers();
                                        index=new Scanner(System.in).nextInt();
                                        if (index>=0 && index <= dehumidifiers.size()){
                                            Boolean status = dehumidifiers.get(index).checkActive();
                                            if(!status){
                                                Boolean result = changeDehumidifierStatus(dehumidifiers.get(index),"active",true);
                                                System.out.println(result?"Cooling system activated":"Attention! Cooling system not activated");
                                            }
                                            else
                                                System.out.println("Sensor is already active!");
                                        }
                                        break;

                                    case "stop dehumidifier":
                                        listDehumidifiers();
                                        index = new Scanner(System.in).nextInt();
                                        if (index >= 0 && inde x<= dehumidifiers.size()){
                                            Boolean status=dehumidifiers.get(index).checkActive();
                                            if(!status){
                                                Boolean result=changeDehumidifierStatus(dehumidifiers.get(index),"non active",false);
                                                System.out.println(result?"Cooling system disactivated":"Attention! Cooling system not disactivated");
                                            }
                                            else
                                                System.out.println("Sensor is already non active!");
                                        }
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
        /*
        public static void selectResource(){
            while(true){
                try{
                    System.out.println("What type of resource do you want to select?");
                    availableResources();
                    Scanner myObj = new Scanner(System.in);  
                    System.out.print("Enter type: ");
                    String type = myObj.nextLine().toLowerCase();  
                    switch(type){
                        case "humidity":
                            listHumiditySensors();
                         
                            break;
                        case "temperature":
                            listTemperatureSensors();
                        default:
                            System.out.println("Command not found.\n");
                            availableResources();
                            break;
                    }

                }catch (Exception e) {
				System.out.println("Invalid input, please try again.\n");
				e.printStackTrace();
			}
            }
        }*/

        public static void help() {
            System.out.println(".........................\n");
            System.out.println("");
	    System.out.println("Type one of the following and press enter to continue:");
            System.out.println("");
            System.out.println("\t"+commands[0]+" - get resources present in the system");
            System.out.println("\t"+commands[7]+" - show humidity sensors present in the system");
            System.out.println("\t"+commands[8]+" - show temperature sensors present in the system");
            System.out.println("\t"+commands[9]+" - show dehumidifiers present in the system");
            System.out.println("\t"+commands[10]+" - show cooling actuators present in the system");
            System.out.println("\t"+commands[3]+" - start a dehumidifier (if not already active)");            
            System.out.println("\t"+commands[4]+" - stop a dehumidifier (if not already idle)");
            System.out.println("\t"+commands[5]+" - start a cooling system (if not already active)");            
            System.out.println("\t"+commands[6]+" - stop a cooling system (if not already idle)");
            System.out.println("\t"+commands[1]+" - show the available commands");
            System.out.println("\t"+commands[2]+" - exit the application");
            System.out.println("");
	}/*
        public static void availableResources(){
            System.out.println("");
            System.out.println("humidity");
            System.out.println("temperature");
            System.out.println("or enter 'exit' to go back to main menu");
          

        }*/
        public static void listHumiditySensors(){
            for (int i = 0; i < humiditySensors.size(); i++) {
                HumiditySensor sensor = humiditySensors.get(i);
                System.out.println(i + "\t\tHumidity Sensor: " + sensor.getAddress() + " " + sensor.getPath());
            }
        }
        public static void listTemperatureSensors(){
            for (int i = 0; i < temperatureSensors.size(); i++) {
                TemperatureSensor sensor = temperatureSensors.get(i);
                System.out.println(i + "\t\tTemperature Sensor: " + sensor.getAddress() + " " + sensor.getPath());
            }

        }
        public static void listDehumidifiers(){
            for (int i = 0; i < dehumidifiers.size(); i++) {
                Dehumidifier sensor = dehumidifiers.get(i);
                String status= sensor.checkActive()?"active":"not active";

                System.out.println(i + "\t\tDehumidifier: " + sensor.getAddress() + " " + sensor.getPath()+" "+ status +"\n");
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

        public static Boolean changeDehumidifierStatus(Dehumidifier sensor, String state, Boolean status){
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
