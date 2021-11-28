package DeafCook;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CookReader{

    public static void main(String args[]){
        Scanner scan = new Scanner(System.in);
        String strurl = "https://www.allrecipes.com/search/results/?search=";
        String LINK = "<a class=\"card__titleLink manual-link-behavior\"";
        int counter = 5;

        System.out.println("What are your ingredient preferencies: ");
        String prefs[] = scan.nextLine().split(" ", 10);
        while(prefs[0].equals("")){
            System.out.println("Oops, you seem to have written smth inappropriate. Try again");
            prefs = scan.nextLine().split(" ", 10);
        }
        for(String pref : prefs){
            strurl.concat(pref+"%20");
        }
        
        try{
            URL url = new URL(strurl);
			Socket my_socket = new Socket();
			my_socket.connect(new InetSocketAddress(url.getHost(), 80), 5000);
			System.out.println("Socket intact");
			

			PrintWriter wtr = new PrintWriter(my_socket.getOutputStream(), true);
			wtr.println("GET "+url.getPath()+" HTTP/1.1");
			
        		wtr.println("Host: "+url.getHost());
	        	wtr.println("Connection: close");
        		wtr.println("");
        		wtr.flush();
        		
        		BufferedReader bufRead = new BufferedReader(new InputStreamReader(my_socket.getInputStream()));
			    String line;
			    while((line = bufRead.readLine()) != null && counter!=0){
                    //System.out.println(line);
                    if (line.contains(LINK)){
                        counter = counter-1;
                        System.out.println("\n!!! Found something!!!");
                        String href = bufRead.readLine().strip().replace("\"", ""); String title = bufRead.readLine().strip().replace("\"", "").replace("href=", "");
                        System.out.println(title.replace("title=","Name: ")+"\n"+href.replace("href=",""));
                    }
				}
			my_socket.close();
		}
			catch(SocketTimeoutException e) {
				System.out.println(e.toString());
			}
            catch(IOException e){
                System.out.println(e.toString());
            }


    }
}