package main;

import java.util.List;
import java.util.Scanner;

import models.TheaterLayout;
import models.TheaterRequest;
import services.TheaterSeatingService;
import services.TheaterSeatingServiceImpl;


public class TheatreSeating {
    
    public static void main(String[] args) {
        
        String line;
        StringBuilder layout = new StringBuilder();
        StringBuilder ticketRequests = new StringBuilder();
        boolean isLayoutFinished = false;
        
        System.out.println("Please enter Theater Layout and Ticket requests separated by a line and then enter 'done'.\n");
        
        Scanner in = new Scanner(System.in);

        while((line = in.nextLine()) != null && !line.equals("done")){
            
            if(line.length() == 0){
                
                isLayoutFinished = true;
                continue;
                
            }
            
            if(!isLayoutFinished){
                
                layout.append(line + System.lineSeparator());
                
            }else{
                
                ticketRequests.append(line + System.lineSeparator());
                
            }
            
        }
        
        in.close();
        
        TheaterSeatingService service = new TheaterSeatingServiceImpl();
        
            TheaterLayout theaterLayout = service.getTheaterLayout(layout.toString());
            if(theaterLayout.getSections()==null){
            	System.out.println("Invalid layout input, Please try again with valid input layout.");
            }
            else{
            List<TheaterRequest> requests = service.getTicketRequests(ticketRequests.toString());
            if(requests.size()==0){
            	System.out.println("Invalid ticket requests, Please try again with valid input requests.");
            }
            else{
            	service.processTicketRequests(theaterLayout, requests);
            } 
            }

        
        
    }

}
