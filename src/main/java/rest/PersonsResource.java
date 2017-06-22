package rest;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.javasampleapproach.security.model.AnonymousUser;

public class PersonsResource extends ResourceSupport{

	private List<AnonymousUser> users;
	
	public PersonsResource(List<AnonymousUser> users) {
		this.users = users;
	}

	public List<AnonymousUser> getUsers() {
		return users;
	}

	public void setUsers(List<AnonymousUser> users) {
		this.users = users;
	}

}
