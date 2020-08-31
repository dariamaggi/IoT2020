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
    public static final String[] options = 
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
                                        System.out.println("");
                                        System.out.println("Thermostats");
                                        printResource(thermostats);
                                        System.out.println("");
                                        System.out.println("Cooling Systems:");
                                        printResource(coolers);
                                        break;

                                    case "show temperature sensors":
                                        System.out.println("Thermostats");
                                        printResource(thermostats);
                                        break;
                                    

                                    case "show cooling":
                                        System.out.println("Cooling Systems:");
                                        printResource(coolers);
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
            for(int i = 0; i < options.lenght(); i ++)
                System.out.println(commands[i]);
            System.out.println("");
	}
        

        public static void listTemperatureSensors(){
            for (int i = 0; i < thermostats.size(); i++) {
                TemperatureSensor sensor = thermostats.get(i);
                System.out.println(i + "\t\tTemperature Sensor: " + sensor.getAddress() + " " + sensor.getPath());
            }

        }
        
        public printResource(ArrayList<String> list){
            CommandLineTable st = new CommandLineTable();            
            st.setShowVerticalLines(true);
            st.setHeaders("Resource", "Address", "Path", "Status");//
            for(int i = 0; i < list.size(); i++){
                String elem = list.get(i);
                st.addRow(elem.getAddress() , elem.getPath(), elem.checkActive()?"active":"idle");
            }
            st.print();
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

    class CommandLineTable {
            private static final String HORIZONTAL_SEP = "-";
            private String verticalSep;
            private String joinSep;
            private String[] headers;
            private List<String[]> rows = new ArrayList<>();
            private boolean rightAlign;

            public CommandLineTable() {
                setShowVerticalLines(false);
            }

            public void setRightAlign(boolean rightAlign) {
                this.rightAlign = rightAlign;
            }

            public void setShowVerticalLines(boolean showVerticalLines) {
                verticalSep = showVerticalLines ? "|" : "";
                joinSep = showVerticalLines ? "." : " ";
            }

            public void setHeaders(String... headers) {
                this.headers = headers;
            }

            public void addRow(String... cells) {
                rows.add(cells);
            }

            public void print() {
                int[] maxWidths = headers != null ?
                        Arrays.stream(headers).mapToInt(String::length).toArray() : null;

                for (String[] cells : rows) {
                    if (maxWidths == null) {
                        maxWidths = new int[cells.length];
                    }
                    if (cells.length != maxWidths.length) {
                        throw new IllegalArgumentException("Number of row-cells and headers should be consistent");
                    }
                    for (int i = 0; i < cells.length; i++) {
                        maxWidths[i] = Math.max(maxWidths[i], cells[i].length());
                    }
                }

                if (headers != null) {
                    printLine(maxWidths);
                    printRow(headers, maxWidths);
                    printLine(maxWidths);
                }
                for (String[] cells : rows) {
                    printRow(cells, maxWidths);
                }
                if (headers != null) {
                    printLine(maxWidths);
                }
            }

            private void printLine(int[] columnWidths) {
                for (int i = 0; i < columnWidths.length; i++) {
                    String line = String.join("", Collections.nCopies(columnWidths[i] +
                            verticalSep.length() + 1, HORIZONTAL_SEP));
                    System.out.print(joinSep + line + (i == columnWidths.length - 1 ? joinSep : ""));
                }
                System.out.println();
            }

            private void printRow(String[] cells, int[] maxWidths) {
                for (int i = 0; i < cells.length; i++) {
                    String s = cells[i];
                    String verStrTemp = i == cells.length - 1 ? verticalSep : "";
                    if (rightAlign) {
                        System.out.printf("%s %" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp);
                        } else {
                            System.out.printf("%s %-" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp);
                        }
                    }
                System.out.println();
                }
            }
                
        }
