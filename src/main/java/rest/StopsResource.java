package rest;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.javasampleapproach.security.model.BusLine;
import com.javasampleapproach.security.model.BusStop;
import com.javasampleapproach.security.model.StopForClient;

public class StopsResource extends ResourceSupport{
	private List<StopForClient> stops;

	public StopsResource() {
		
	}
	
	public List<StopForClient> getStops() {
		return stops;
	}

	public void setStops(List<StopForClient> stops) {
		this.stops = stops;
	}
}
