package rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

public class ArgumentsResource extends ResourceSupport{
	private List<String> arguments;

	public ArgumentsResource() {
		arguments = new ArrayList<String>();
		arguments.add("Bus&Metro");
		arguments.add("Traffico");
		arguments.add("Giro in bici");
	}
	
	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}


}
