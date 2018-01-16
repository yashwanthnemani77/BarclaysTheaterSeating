package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.TheaterLayout;
import models.TheaterRequest;
import models.TheaterSection;

public class TheaterSeatingServiceImpl implements TheaterSeatingService {

	/*
	 * Parsing the raw layout string created from the input  and creating a TheaterLayout object.
	 */
    @Override
    public TheaterLayout getTheaterLayout(String rawLayout) {
        
        TheaterLayout theaterLayout = new TheaterLayout();
        TheaterSection section;
        List<TheaterSection> sectionsList = new ArrayList<TheaterSection>();
        int totalCapacity = 0, value;
        //Splitting the string using line separator to obtain the rows
        String[] rows = rawLayout.split(System.lineSeparator());
        String[] sections;
        
        for(int i=0 ; i<rows.length ; i++){
            //splitting each row to obtain sections
            sections = rows[i].split(" ");
            
            for(int j=0 ; j<sections.length ; j++){
            
                try{
                //verifying if the input value for section capacity is a valid Integer 
                    value = Integer.valueOf(sections[j]);
                    totalCapacity = totalCapacity + value;
                    
                    section = new TheaterSection();
                    section.setRowNumber(i + 1);
                    section.setSectionNumber(j + 1);
                    section.setCapacity(value);
                    section.setAvailableSeats(value);
                    
                    sectionsList.add(section);
                    
                }catch(NumberFormatException nfe){
                    System.out.println("'" + sections[j] + "'" + " is invalid type for section capacity. Please modify.");
                    //Returning an empty layout object in case of an invalid layout input
                    TheaterLayout invalidLayout = new TheaterLayout();
                    return invalidLayout;
                }
                
            }

        }
        
        theaterLayout.setTotalCapacity(totalCapacity);
        theaterLayout.setAvailableSeats(totalCapacity);
        theaterLayout.setSections(sectionsList);
        
        return theaterLayout;
        
    }

  /*
   * Parsing the ticket requests string created from input and creating a list of TheaterRequest objects.
   */
    @Override
    public List<TheaterRequest> getTicketRequests(String ticketRequests){
        
        List<TheaterRequest> requestsList = new ArrayList<TheaterRequest>();
        TheaterRequest request;
      //Splitting the string using line separator to obtain individual requests
        String[] requests = ticketRequests.split(System.lineSeparator());
        
        for(String req : requests){
            //separating each request to obtain the name and requested tickets
            String[] reqData = req.split(" ");
            
            request = new TheaterRequest();
            //Verifying if the name is a valid non numeric string 
            if(isAlpha(reqData[0])){
            	request.setName(reqData[0]);
            }
            else{
            	System.out.println("'" + reqData[0] + "'" + " is invalid ticket request name. Please correct it.");
            	
             //Returning an empty list of TheaterRequest objects if the requests input is invalid
             	 List<TheaterRequest> invalidList = new ArrayList<TheaterRequest>();
             	 return invalidList;
            }
            
            
            
            try{
            //Verifying if the requested tickets is a valid Integer
                request.setNoOfTickets(Integer.valueOf(reqData[1]));
                request.setCompleted(false);
                
                requestsList.add(request);
                
            }catch(NumberFormatException nfe){
            	System.out.println("'" + reqData[1] + "'" + " is invalid ticket request. Please correct it.");
            	//Returning an empty list of TheaterRequest objects if the requests input is invalid
            	 List<TheaterRequest> invalidList = new ArrayList<TheaterRequest>();
            	 return invalidList;
            }         
        }
        
        return requestsList;
        
    }
    
    /*
     * verifying if given name for ticket request is valid
     */
    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }
    
    /*
     * We try to find from the subsequent requests if there are any complementing requests to avoid wasting seats.
     * 
     */
    private int findComplementRequest(List<TheaterRequest> requests, int complementSeats, int currentReqIndex){
        
        int requestNum = -1;

        for(int i=currentReqIndex+1 ; i<requests.size() ; i++){
            
            TheaterRequest request = requests.get(i);
            
            if(!request.isCompleted() && request.getNoOfTickets() == complementSeats){
                
                requestNum = i;
                break;
                
            }
            
        }
        
        return requestNum;
    }
    
    
    /*
     * Find section by it's available seats
     * 
     */
    private int findSectionByAvailableSeats(List<TheaterSection> sections, int availableSeats){
        
        int i=0;
        TheaterSection section = new TheaterSection();
        section.setAvailableSeats(availableSeats);
        
        Collections.sort(sections);
        
        Comparator<TheaterSection> byAvailableSeats = new Comparator<TheaterSection>() {
            
            @Override
            public int compare(TheaterSection o1, TheaterSection o2) {
                
                return o1.getAvailableSeats() - o2.getAvailableSeats();
                
            }
        };
        
        int sectionNum = Collections.binarySearch(sections, section, byAvailableSeats);
        
        if(sectionNum > 0){
            
            for(i=sectionNum-1 ; i>=0 ; i--){
                
                TheaterSection s = sections.get(i);
                
                if(s.getAvailableSeats() != availableSeats) break;
                
            }
            
            sectionNum = i + 1;
            
        }
        
        return sectionNum;
    }
    
    /*
     *  
     * Request Processing Explanation
     * 
     * 1) Iterate over all ticket requests
     * 2) For each request, 
     * 
     *      - If total available seats < requested seats, then 'we can't handle the party'.
     *      - Iterate over all theater sections starting from  row#1
     *      
     *          - If requested tickets and section's available seats match EXACTLY then assign it.
     *          
     *          - If requested tickets < section's available seats
     *              
     *              - Find complement request, if any (complement request = section's available seats - original requested tickets)
     *                  
     *                  - If FOUND, complete assignment of original and complement ticket requests
     *                  - If NOT found
     *                      
     *                      - Find EXACTLY matching section with requested no of tickets
     *                          
     *                          - If FOUND, assign it
     *                          - If NOT found, then assign the request to current section
     *                          
     *      - If request is INCOMPLETE, 'Call party to split.'
     * 
     */
    
    @Override
    public void processTicketRequests(TheaterLayout layout, List<TheaterRequest> requests) {
        
        for(int i=0 ; i<requests.size() ; i++){
            
            TheaterRequest request = requests.get(i);
            if(request.isCompleted())   continue;
            
            /*
             * -2 is an indicator when we can't handle the party.
             */
            if(request.getNoOfTickets() > layout.getAvailableSeats()){
                
                request.setRowNumber(-2);
                request.setSectionNumber(-2);
                continue;
                
            }
            
            List<TheaterSection> sections = layout.getSections();
            
            for(int j=0 ; j<sections.size() ; j++){
                
                TheaterSection section = sections.get(j);
                
                if(request.getNoOfTickets() == section.getAvailableSeats()){
                    
                    request.setRowNumber(section.getRowNumber());
                    request.setSectionNumber(section.getSectionNumber());
                    section.setAvailableSeats(section.getAvailableSeats() - request.getNoOfTickets());
                    layout.setAvailableSeats(layout.getAvailableSeats() - request.getNoOfTickets());
                    request.setCompleted(true);
                    break;
                    
                }else if(request.getNoOfTickets() < section.getAvailableSeats()){
                    
                    int requestNo = findComplementRequest(requests, section.getAvailableSeats() - request.getNoOfTickets(), i);
                    
                    if(requestNo != -1){
                        
                        request.setRowNumber(section.getRowNumber());
                        request.setSectionNumber(section.getSectionNumber());
                        section.setAvailableSeats(section.getAvailableSeats() - request.getNoOfTickets());
                        layout.setAvailableSeats(layout.getAvailableSeats() - request.getNoOfTickets());
                        request.setCompleted(true);
                        
                        TheaterRequest complementRequest = requests.get(requestNo);
                        
                        complementRequest.setRowNumber(section.getRowNumber());
                        complementRequest.setSectionNumber(section.getSectionNumber());
                        section.setAvailableSeats(section.getAvailableSeats() - complementRequest.getNoOfTickets());
                        layout.setAvailableSeats(layout.getAvailableSeats() - complementRequest.getNoOfTickets());
                        complementRequest.setCompleted(true);
                        
                        break;
                        
                    }else{
                        
                        int sectionNo = findSectionByAvailableSeats(sections, request.getNoOfTickets());
                        
                        if(sectionNo >= 0){
                            
                            TheaterSection perferctSection = sections.get(sectionNo);
                            
                            request.setRowNumber(perferctSection.getRowNumber());
                            request.setSectionNumber(perferctSection.getSectionNumber());
                            perferctSection.setAvailableSeats(perferctSection.getAvailableSeats() - request.getNoOfTickets());
                            layout.setAvailableSeats(layout.getAvailableSeats() - request.getNoOfTickets());
                            request.setCompleted(true);
                            break;
                            
                        }else{
                            
                            request.setRowNumber(section.getRowNumber());
                            request.setSectionNumber(section.getSectionNumber());
                            section.setAvailableSeats(section.getAvailableSeats() - request.getNoOfTickets());
                            layout.setAvailableSeats(layout.getAvailableSeats() - request.getNoOfTickets());
                            request.setCompleted(true);
                            break;
                            
                        }
                        
                    }
                    
                }
                
            }
            
            /*
             * -1 is an indicator when we need to split the party.
             */
            if(!request.isCompleted()){
                
                request.setRowNumber(-1);
                request.setSectionNumber(-1);
                
            }
            
        }
        
        System.out.println("Seats Distribution.\n");
        
        for(TheaterRequest request : requests){
            
            System.out.println(request.getStatus());
            
        }
        
    }

}
