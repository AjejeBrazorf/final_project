package rest;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import com.javasampleapproach.security.ChatRestController;
import com.javasampleapproach.security.model.User;

public class PersonAssembler extends ResourceAssemblerSupport<User, PersonsResource> {

	public PersonAssembler() {
		super(ChatRestController.class, PersonsResource.class);
	}

	@Override
	public PersonsResource toResource(User user) {
		PersonsResource pr = createResourceWithId(user.getNickname(), user);
		
		return pr;
	}

}
