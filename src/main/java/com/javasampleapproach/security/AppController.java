package com.javasampleapproach.security;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javasampleapproach.security.model.FornitoreCarSharing;
import com.javasampleapproach.security.model.Gender;
import com.javasampleapproach.security.model.Istruzione;
import com.javasampleapproach.security.model.Occupazione;
import com.javasampleapproach.security.model.TipoCarburante;
import com.javasampleapproach.security.model.TipoViaggio;
import com.javasampleapproach.security.model.User;
import com.javasampleapproach.security.query.ActivationQuery;
import com.javasampleapproach.security.query.UserQuery;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class AppController {

	@Autowired
	private  UserQuery userService;

	@Autowired
	private JavaMailSender sender;

	@Autowired
	private Configuration freemarkerConfig;
	
	@Autowired
	private ActivationQuery aq;
	
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern pattern;
	private Matcher matcher;

	
	@PostMapping("/addUser")
	public String goToUserpage(Model model, HttpServletRequest request,
			@RequestParam(value="email", required=true) String email,
			@RequestParam(value="nickname", required=true) String nickname,
			@RequestParam(value="password", required=true) String password) {

		int count = userService.mailAlreadyPresent(email); 
		
		//controllo se la mail è univoca
		if(count > 0){
			model.addAttribute("error", "Sorry, the mail is already present");
			model.addAttribute("field", "email");	
			return "registration";
		}
		
		//controllo il formato della mail
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		if(!matcher.matches()){
			model.addAttribute("error", "Sorry, insert a valid email");
			model.addAttribute("field", "email");	
			return "registration";
		}
		
		//controllo che il nickname abbia almeno 1 carattere
		if(nickname.length() <1){
			model.addAttribute("error", "Sorry, insert a valid nickname");
			model.addAttribute("field", "nickname");	
			return "registration";
		}

		//controllo che la password abbia almeno 8 caratteri
		if(password.length() <8){
			model.addAttribute("error", "Sorry, the password must be at least 8 charachters");
			model.addAttribute("field", "password");	
			return "registration";
		}

		count = userService.nicknameAlreadyPresent(nickname); 
		
		//controllo che il nickname sia univoco
		if(count > 0){
			model.addAttribute("error", "Sorry, the nickname is already present");
			model.addAttribute("field", "nickname");	
			return "registration";
		}
		
		//send an email for confirmation
		try {
			//create OTP for the user
			String otp = generateRandomSequence();
			//generate a new one if already assigned
			while(aq.getUsernameByCode(otp) != null){
				otp = generateRandomSequence();
			}
			
			userService.insertNewUser(email, otp, nickname, password, false, "ROLE_USER");
			//aq.insertCode(email, otp);
			System.out.println("Url"+getURLBase(request));
			sendEmail(email, getURLBase(request), otp);
		} catch (Exception e) {
			model.addAttribute("error", "Sorry, unable to send the email for confirmation, please try again. ");
			model.addAttribute("field", "email");	
			return "registration";
		}

		//pgq.insertCredentials(email, password, false);
		//pgq.insertUser(nickname, email);

		//bisogna aggiungere anche il ruolo
		//pgq.insertRole(email, "ROLE_USER");

		return "login";

	}

	public String getURLBase(HttpServletRequest request) throws MalformedURLException {

	    URL requestURL = new URL(request.getRequestURL().toString());
	    String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
	    return requestURL.getProtocol() + "://" + requestURL.getHost() + port;

	}

	
	private void sendEmail(String emailAddress, String link, String otp) throws Exception{
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		Map<String, Object> model = new HashMap<>();
		model.put("email", emailAddress);
		model.put("link", link + "/signup_final?otp=" + otp);

		// set loading location to src/main/resources
		// You may want to use a subfolder such as /templates here
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");

		Template t = freemarkerConfig.getTemplate("welcome.ftl");
		String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

		helper.setTo(emailAddress);
		helper.setText(text, true); // set to html
		helper.setSubject("Confirmation CinqueTi");

		sender.send(message);
	}

	private String generateRandomSequence(){
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	@PostMapping("/changePassword")
	public String goToHomePage(Model model, @RequestParam(value="oldpsw", required=true) String oldPassword,
			@RequestParam(value="newpsw", required=true) String newPassword, Principal name) {


		String oldpsw = userService.getPassword(name.getName());
		//controllo che abbia messo la sua vecchia password
		if(!oldpsw.equals(oldPassword)){
			model.addAttribute("error", "Please, insert the old password");
			model.addAttribute("field", "oldpsw");	//ci dobbiamo metter d'accordo con loro
			return "userpage";
		}
		//controllo che la password abbia almeno 8 caratteri
		if(newPassword.length() < 8){
			model.addAttribute("error", "Sorry, the password must be at least 8 charachters");
			model.addAttribute("field", "newpsw");	
			return "userpage";
		}

		userService.updateUserCred(name.getName(), newPassword);

		return "index";
	}

	@PostMapping("/addInfo")
	public String goToHome(Model model, Principal name,
			@RequestParam(value="gender", required=true) String gender, @RequestParam(value="birthday", required=true) String eta,
			@RequestParam(value="instruction", required=true) String istruzione, @RequestParam(value="job", required=true) String occupazione,
			@RequestParam(value="hascar", required=true) String hasCar, @RequestParam(value="carRegistration", required=true) String annoImmatr,
			@RequestParam(value="fuel", required=true) String carburante, @RequestParam(value="usecarsharing", required=true) String useCarSharing,
			@RequestParam(value="CSVendor", required=true) String fcs, @RequestParam(value="typeOfBike", required=true) String useBike,
			@RequestParam(value="usebikesharing", required=true) String useBikeSharing, @RequestParam(value="usemezzipubblici", required=true) String useMezzi,
			@RequestParam(value="TypeOfTicket", required=true) String tipoviaggio,
			@RequestParam(value="photo", required = false) String photo){
		
		
		//controllo che il valore dell'età sia un intero positivo
		if(eta.equals("") || Integer.parseInt(eta) < 0){
			model.addAttribute("error", "Please, insert a correct value");
			model.addAttribute("field", "birthday");	
			return "userpage";
		}
		//controllo che l'anno di immatricolazione sia valido
		if(annoImmatr.equals("") || Integer.parseInt(annoImmatr) < 0){
			model.addAttribute("error", "Please, insert a correct value");
			model.addAttribute("field", "carRegistration");
			return "userpage";
		}
		//controllo che il valore di FornitoreCarSharing sia valido
		if(FornitoreCarSharing.valueOf(fcs) == null){
			model.addAttribute("error", "Please, insert a correct value of FornitoreCarSharing");
			model.addAttribute("field", "CSVendor");	
			return "userpage";
		}
		//controllo che il valore di Gender sia valido
		if(Gender.valueOf(gender) == null){
			model.addAttribute("error", "Please, insert a correct value of Gender");
			model.addAttribute("field", "gender");	
			return "userpage";
		}
		//controllo che il valore di Istruzione sia valido
		if(Istruzione.valueOf(istruzione) == null){
			model.addAttribute("error", "Please, insert a correct value of Instruction");
			model.addAttribute("field", "instruction");	
			return "userpage";
		}
		//controllo che il valore di Occupazione sia valido
		if(Occupazione.valueOf(occupazione) == null){
			model.addAttribute("error", "Please, insert a correct value of Job");
			model.addAttribute("field", "job");	
			return "userpage";
		}
		//controllo che il valore di TipoCarburante sia valido
		if(TipoCarburante.valueOf(carburante) == null){
			model.addAttribute("error", "Please, insert a correct value of Fuel");
			model.addAttribute("field", "fuel");	
			return "userpage";
		}
		//controllo che il valore di TipoViaggio sia valido
		if(TipoViaggio.valueOf(tipoviaggio) == null){
			model.addAttribute("error", "Please, insert a correct value of FornitoreCarSharing");
			model.addAttribute("field", "TypeOfTicket");	
			return "userpage";
		}
	
		userService.updateUser(name.getName(), Gender.valueOf(gender), Integer.parseInt(eta) , Istruzione.valueOf(istruzione), Occupazione.valueOf(occupazione), Boolean.valueOf(hasCar), Integer.parseInt(annoImmatr), TipoCarburante.valueOf(carburante), Boolean.valueOf(useCarSharing), FornitoreCarSharing.valueOf(fcs), Boolean.valueOf(useBike), Boolean.valueOf(useBikeSharing), Boolean.valueOf(useMezzi), TipoViaggio.valueOf(tipoviaggio), photo);
		model.addAttribute("image", userService.getImage(name.getName()));
		model.addAttribute("nickname", userService.getUsernameByMail(name.getName()));
		
		return "index";
		
	}

	@RequestMapping("/userpage")
	public String userpageController(Model model, Principal name){
		//System.out.println(name.getName());

		model.addAttribute("user",userService.getUserbyUsername(name.getName()));
		model.addAttribute("image",userService.getImage(name.getName()));
		model.addAttribute("nickname", userService.getUsernameByMail(name.getName()));
		return "userpage";
	}
	
	@RequestMapping("/logout")
	public String logout(Model model) {
		model.addAttribute("user", "");
		return "index";

	}




}
