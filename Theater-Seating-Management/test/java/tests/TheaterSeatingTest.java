package tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import models.TheaterLayout;
import models.TheaterRequest;
import services.TheaterSeatingService;
import services.TheaterSeatingServiceImpl;

public class TheaterSeatingTest {

	@Test
	public void requesttest1() {
		TheaterSeatingService service = new TheaterSeatingServiceImpl();
		List<TheaterRequest> requests = new ArrayList<>();
		StringBuilder ticketRequests = new StringBuilder();
				ticketRequests.append("john 2"+System.lineSeparator());
				ticketRequests.append("smith 4"+System.lineSeparator());
				ticketRequests.append("brad brad"+System.lineSeparator());
				ticketRequests.append("yash 8"+System.lineSeparator());
		requests = service.getTicketRequests(ticketRequests.toString());
		for(TheaterRequest r: requests){
			System.out.println(r.getName());
		}
		
		assertEquals(0,requests.size());
	}
	
	@Test
	public void requesttest2() {
		TheaterSeatingService service = new TheaterSeatingServiceImpl();
		List<TheaterRequest> requests = new ArrayList<>();
		StringBuilder ticketRequests = new StringBuilder();
		ticketRequests.append("john 2"+System.lineSeparator());
		ticketRequests.append("smith 4"+System.lineSeparator());
		ticketRequests.append("brad 6"+System.lineSeparator());
		ticketRequests.append("yash 8"+System.lineSeparator());
requests = service.getTicketRequests(ticketRequests.toString());
		
		assertEquals(4,requests.size());
	}
	
	@Test
	public void requesttest3() {
		TheaterSeatingService service = new TheaterSeatingServiceImpl();
		List<TheaterRequest> requests = new ArrayList<>();
		StringBuilder ticketRequests = new StringBuilder();
				ticketRequests.append("john 2.6"+System.lineSeparator());
				ticketRequests.append("smith 4.5"+System.lineSeparator());
				ticketRequests.append("brad brad"+System.lineSeparator());
				ticketRequests.append("yash 8"+System.lineSeparator());
		requests = service.getTicketRequests(ticketRequests.toString());
		for(TheaterRequest r: requests){
			System.out.println(r.getName());
		}
		
		assertEquals(0,requests.size());
	}
	
	@Test
	public void requesttest4() {
		TheaterSeatingService service = new TheaterSeatingServiceImpl();
		List<TheaterRequest> requests = new ArrayList<>();
		StringBuilder ticketRequests = new StringBuilder();
				ticketRequests.append("john01 2"+System.lineSeparator());
				ticketRequests.append("smith 4"+System.lineSeparator());
				ticketRequests.append("brad 6"+System.lineSeparator());
				ticketRequests.append("yash 8"+System.lineSeparator());
		requests = service.getTicketRequests(ticketRequests.toString());
		for(TheaterRequest r: requests){
			System.out.println(r.getName());
		}
		
		assertEquals(0,requests.size());
	}
	
	@Test
	public void layouttest1() {
		TheaterSeatingService service = new TheaterSeatingServiceImpl();
		TheaterLayout layout = new TheaterLayout();
		StringBuilder rawLayout = new StringBuilder();
		rawLayout.append("2 4 4 2"+System.lineSeparator());
		rawLayout.append("6 6"+System.lineSeparator());
		rawLayout.append("5 5"+System.lineSeparator());
		rawLayout.append("3 3 3 3"+System.lineSeparator());
layout = service.getTheaterLayout(rawLayout.toString());
		
		assertEquals(12, layout.getSections().size());
	}
	
	@Test
	public void layouttest2() {
		TheaterSeatingService service = new TheaterSeatingServiceImpl();
		TheaterLayout layout = new TheaterLayout();
		StringBuilder rawLayout = new StringBuilder();
		rawLayout.append("2 4 4 2"+System.lineSeparator());
		rawLayout.append("6 ana"+System.lineSeparator());
		rawLayout.append("5 5"+System.lineSeparator());
		rawLayout.append("3 3 3 3"+System.lineSeparator());
layout = service.getTheaterLayout(rawLayout.toString());
		
		assertEquals(null, layout.getSections());
	}
	
	@Test
	public void layouttest3() {
		TheaterSeatingService service = new TheaterSeatingServiceImpl();
		TheaterLayout layout = new TheaterLayout();
		StringBuilder rawLayout = new StringBuilder();
		rawLayout.append("2 4 4.5 2"+System.lineSeparator());
		rawLayout.append("6 6"+System.lineSeparator());
		rawLayout.append("5 5.5"+System.lineSeparator());
		rawLayout.append("3 3 3 3"+System.lineSeparator());
layout = service.getTheaterLayout(rawLayout.toString());
		
		assertEquals(null, layout.getSections());
	}
	

}
