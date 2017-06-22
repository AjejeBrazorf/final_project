package rest;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.javasampleapproach.security.model.BusLine;

public class LinesResource extends ResourceSupport{
	private List<BusLine> lines;

	public LinesResource() {
		
	}
	
	public List<BusLine> getLines() {
		return lines;
	}

	public void setLines(List<BusLine> lines) {
		this.lines = lines;
	}
}
