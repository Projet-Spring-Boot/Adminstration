package com.spring.social.controller;

import java.security.Principal;
import java.util.*;

import com.spring.social.RandId.TokenGenerator;
import com.spring.social.model.Flow;
import com.spring.social.repository.FlowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.social.configuration.SocialProperties;
import com.spring.social.dao.AppUserDAO;
import com.spring.social.dao.UserConnectionDAO;
import com.spring.social.entity.AppRole;
import com.spring.social.entity.AppUser;
import com.spring.social.entity.UserConnection;
import com.spring.social.form.AppUserForm;
import com.spring.social.form.MessageForm;
import com.spring.social.security.SecurityAuto;
import com.spring.social.utils.WebUtil;
import com.spring.social.validator.AppUserValidator;
import com.spring.social.dao.InfoConnectionDAO;

import twitter4j.*;
import twitter4j.auth.AccessToken;


@Controller
@Transactional
public class MainController {

	@Autowired
	private AppUserDAO appUserDAO;

	@Autowired
	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	private UsersConnectionRepository connectionRepository;

	@Autowired
	private AppUserValidator appUserValidator;

	@Autowired
	private UserConnectionDAO userConnectionDAO;

	@Autowired
	private SocialProperties socialProperties;
	
	@Autowired
	private ConnectionRepository coR;

	@Autowired
	private InfoConnectionDAO infoConnectionDAO;

	@Autowired
	private FlowRepository flowrepository;

	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {

		// Form target
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);

		if (target.getClass() == AppUserForm.class) {
			dataBinder.setValidator(appUserValidator);
		}
		// ...
	}

	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("title", "Welcome");

		//Version hardcodée pour tests de connexion et écriture dans redis
		/*Map<String,Flow> yourTimelimeDefinition = null;

		// [Step 1] : Récupérer la timeline de l'utilisateur 'principal' connecté.
		//UserConnection uc = userConnectionDAO.findUserConnectionByUserName(username);
		System.out.println("Entrée dans le try/catch");
		try {


			System.out.println("Création du Flow");
			Flow flow = new Flow();
			flow.setId("fdfkjdf('4453245");
			flow.setUser_name("Jean-Claude");
			flow.setUser_img("https://upload.wikimedia.org/wikipedia/commons/2/27/Jean-Claude_Van_Damme_2012.jpg");
			flow.setPublished_content("J'adore l'eau...Dans 20 ans y en aura plus");
			flow.setPublishing(Calendar.getInstance().getTime());

			System.out.println("\n" + "L'id du flow vaut : " + flow.getId());
			System.out.println(flow.getUser_name()+"\n"+flow.getUser_img()+"\n"+flow.getPublished_content()+"\n"+flow.getPublishing());

			String[] medias = new String[3];
			medias[0]= "https://fr.wikipedia.org/wiki/Eau";
			medias[1]="https://www.boboco.fr/bouteilles-de-spiritueux/53-bouteille-moonea-70cl.html";
			List<String> mediaList=new ArrayList<String>();
			for(int i=0;i<medias.length; i++)
			{

				//mediaList.add(medias[i].getText());
				mediaList.add(medias[i]);
			}

			flow.setPublished_media(mediaList);

			flowrepository.save(flow);

			yourTimelimeDefinition=flowrepository.findAll();

			System.out.println(flowrepository.findAll());

			//yourTimelimeDefinition.forEach((String, Flow) -> System.out.println(String+":"+Flow.getUser_name()));


			// [Step 3] : Renvoyer la timeline à l'IHM
			//return yourTimelimeDefinition;

		}catch (Exception e){
			System.out.println(e+"\n"+"On est dans le catch");

		}

		System.out.println("Sortie du try");*/
		return "welcomePage";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(Model model, Principal principal) {

		// After user login successfully.
		String userName = principal.getName();

		System.out.println("User Name: " + userName);

		UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();

		String userInfo = WebUtil.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		return "adminPage";
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Logout");
		return "logoutSuccessfulPage";
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {

		// After user login successfully.
		AppUser logineduser2 = this.appUserDAO.findAppUserByUserName(principal.getName());

		model.addAttribute("appUser", logineduser2);

		return "userInfoPage";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			model.addAttribute("name", principal.getName());
		}

		return "403Page";
	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String login(Model model) {

		// model.addAttribute("host", "http://localhost:8081/login");
		return "loginPage";
	}

	// User login with social networking,
	// but does not allow the app to view basic information
	// application will redirect to page / signin.
	@RequestMapping(value = { "/signin" }, method = RequestMethod.GET)
	public String signInPage(Model model) {
		return "redirect:/login";
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.GET)
	public String signupPage(WebRequest request, Model model) {

		ProviderSignInUtils providerSignInUtils //
				= new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);

		// Retrieve social networking information.
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		// Twitter twitter= connection.getApi() lorsqu'on voudra envoyer des tweet
		// partie 2

		//
		AppUserForm myForm = null;
		//
		if (connection != null) {
			myForm = new AppUserForm(connection);

			System.out.println("provider = " + myForm.getSignInProvider());
		} else {
			myForm = new AppUserForm();
		}

		model.addAttribute("myForm", myForm);
		return "signupPage";
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
	public String signupSave(WebRequest request, Model model,
			@ModelAttribute("myForm") @Validated AppUserForm appUserForm, BindingResult result,
			final RedirectAttributes redirectAttributes) {

		// Validation error.
		if (result.hasErrors()) {
			return "signupPage";
		}

		List<String> roleNames = new ArrayList<String>();
		roleNames.add(AppRole.ROLE_USER);

		AppUser registered = null;

		//creation compte
		try {
			registered = appUserDAO.registerNewUserAccount(appUserForm, roleNames);

		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("errorMessage", "Error " + ex.getMessage());
			return "signupPage";
		}

		// Lorsque la requête POST de login contient un signInProvider (on se connecte
		// via Goodle ou autre),
		// on créé un enregistrement dans la table UserConnection
		if (appUserForm.getSignInProvider() != null) {

			ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator,
					connectionRepository);

			// (Spring Social API):
			// If user login by social networking.
			// This method saves social networking information to the UserConnection table.

			providerSignInUtils.doPostSignUp(registered.getUserName(), request);

		}

		// After registration is complete, automatic login.
		SecurityAuto.logInUser(registered, roleNames);
		
		java.util.Date date = new Date();
		System.out.println("LA DATEUH !!!! " + date);

		return "redirect:/userInfo";
	}

	@RequestMapping(value = { "/sendMessage" }, method = RequestMethod.GET)
	public String sendMessage(Model model) {
		model.addAttribute("messageForm", new MessageForm());
		return "sendMessage";
	}

	@RequestMapping(value = { "/sendMessage" }, method = RequestMethod.POST)
	public void sendPostMessage(WebRequest request, Model model,
			@ModelAttribute("messageForm") @Validated MessageForm messageForm, Principal principal) {

		UserConnection uc = userConnectionDAO.findUserConnectionByUserName(principal.getName());

		if (uc.getProviderId().equals("facebook")) {
			Connection<Facebook> connection = coR.findPrimaryConnection(Facebook.class);
			//Facebook facebook = connection.getApi();
			
			FacebookTemplate ft = new FacebookTemplate(uc.getAccessToken());
			//ft.feedOperations().updateStatus(messageForm.getMessage());
			
			connection.getApi().feedOperations().getFeed().forEach(post -> {
				System.out.println(post.getMessage());
				System.out.println(post.getUpdatedTime());
				System.out.println(post.getName());
				System.out.println();
			});
			
			ft.feedOperations().getFeed().forEach(post -> {
				System.out.println(post.getMessage());
				System.out.println(post.getUpdatedTime());
				System.out.println(post.getName());
				System.out.println();
			});
			

			//facebook.feedOperations().updateStatus(messageForm.getMessage());

		} else if (uc.getProviderId().equals("twitter")) {
			try {
				Twitter twitter = new TwitterFactory().getInstance();

				twitter.setOAuthConsumer(socialProperties.getTwitterConsumerKey(),
						socialProperties.getTwitterConsumerSecret());
				AccessToken accessToken = new AccessToken(uc.getAccessToken(), uc.getSecret());

				twitter.setOAuthAccessToken(accessToken);

				// get timleline
				// ResponseList<Status> timeline = twitter.getHomeTimeline() ;

				// post tweet

				// with img if needed
				/*
				 * File file = new File("/images/Done.jpg");
				 * 
				 * StatusUpdate status = new StatusUpdate(statusMessage); status.setMedia(file);
				 * // set the image to be uploaded here. twitter.updateStatus(status);
				 */

				twitter.updateStatus(messageForm.getMessage());

				// send DM
				// twitter.sendDirectMessage("@_doolmen", "Hello !");

			} catch (TwitterException te) {
				te.printStackTrace();
			}
		} else {

		}
	}

	/*@RequestMapping(value="/updateFlow", method=RequestMethod.POST)
	public Map<String,Flow> updateFlow(@RequestBody String  username) {
		Flowrepository flowrepository = null;
		Map<String,Flow> yourTimelimeDefinition = null;

		// [Step 1] : Récupérer la timeline de l'utilisateur 'principal' connecté.
		UserConnection uc = userConnectionDAO.findUserConnectionByUserName(username);
		try {
			Twitter twitter = new TwitterFactory().getInstance();


			twitter.setOAuthConsumer(socialProperties.getTwitterConsumerKey(),
					socialProperties.getTwitterConsumerSecret());
			AccessToken accessToken = new AccessToken(uc.getAccessToken(), uc.getSecret());

			twitter.setOAuthAccessToken(accessToken);


			ResponseList<Status> timeline = twitter.getHomeTimeline();



			if (timeline == null) { //si pas internet pour récupérer la timeline
				//On récupère votre objet List<Flow> en BDD si jamais il existe.
				//yourTimelimeDefinition = .....

				if(flowrepository != null) yourTimelimeDefinition=flowrepository.findAll();

			} else { //On récupère ce dont vous avez besoin de l'objet "timeline
				// [Step 2] : Ajouter cette timeline en BDD.

				timeline.forEach(tweet -> {
					Flow flow = new Flow();
					flow.setUser_name(tweet.getUser().getName());
					flow.setUser_img(tweet.getUser().get400x400ProfileImageURL());
					flow.setPublished_content(tweet.getText());
					flow.setPublishing(tweet.getCreatedAt());

					MediaEntity[] medias = tweet.getMediaEntities();
					List<String> mediaList=null;
					for(int i=0;i<medias.length; i++)
					{

						mediaList.add(medias[i].getText());
						mediaList.add(medias[i].getMediaURL());
					}
					flow.setPublished_media(mediaList);

					flowrepository.save(flow);

				});

				yourTimelimeDefinition=flowrepository.findAll();

			}


			// [Step 3] : Renvoyer la timeline à l'IHM
			//return yourTimelimeDefinition;

		}catch (TwitterException te) {
			te.printStackTrace();
		}

		return yourTimelimeDefinition;

	}*/



	@RequestMapping(value = { "/TestUpdate" }, method = RequestMethod.GET)
	public String TestUpdate(Model model) {
		model.addAttribute("title", "Welcome");

		//FlowRepository flowrepository;
		Map<String,Flow> yourTimelimeDefinition = null;

		// [Step 1] : Récupérer la timeline de l'utilisateur 'principal' connecté.
		//UserConnection uc = userConnectionDAO.findUserConnectionByUserName(username);
		System.out.println("Entrée dans le try/catch");
		try {


			System.out.println("Création du Flow");
			Flow Testflow = new Flow();
			Testflow.setId(TokenGenerator.generateNewToken());
			Testflow.setUser_name("Jean-Claude");
			Testflow.setUser_img("https://upload.wikimedia.org/wikipedia/commons/2/27/Jean-Claude_Van_Damme_2012.jpg");
			Testflow.setPublished_content("J'adore l'eau...Dans 20 ans y en aura plus");
			Testflow.setPublishing(Calendar.getInstance().getTime());

			System.out.println("\n" + "L'id du flow vaut : " + Testflow.getId());
			System.out.println(Testflow.getUser_name()+"\n"+Testflow.getUser_img()+"\n"+Testflow.getPublished_content()+"\n"+Testflow.getPublishing());

			String[] medias = new String[3];
			medias[0]= "https://fr.wikipedia.org/wiki/Eau";
			medias[1]="https://www.boboco.fr/bouteilles-de-spiritueux/53-bouteille-moonea-70cl.html";
			List<String> mediaList=new ArrayList<String>();
			for(int i=0;i<medias.length; i++)
			{

				//mediaList.add(medias[i].getText());
				mediaList.add(medias[i]);
			}

			Testflow.setPublished_media(mediaList);

			flowrepository.save(Testflow);

			System.out.println("Création du Flow2");
			Flow Testflow2 = new Flow();
			Testflow2.setId(TokenGenerator.generateNewToken());
			Testflow2.setUser_name("Jules Winnfield");
			Testflow2.setUser_img("https://vignette.wikia.nocookie.net/quentin-tarantino/images/0/00/99409e.jpg/revision/latest/scale-to-width-down/310?cb=20180122000605");
			Testflow2.setPublished_content("And I will strike down upon thee with great vegance and furious anger those who attempt to poison and destroy my brothers. And you will know my name is the lord when I lay my vengance upon thee");
			Testflow2.setPublishing(Calendar.getInstance().getTime());

			System.out.println("\n" + "L'id du flow vaut : " + Testflow2.getId());
			System.out.println(Testflow2.getUser_name()+"\n"+Testflow2.getUser_img()+"\n"+Testflow2.getPublished_content()+"\n"+Testflow2.getPublishing());

			medias[0]= "https://upload.wikimedia.org/wikipedia/commons/thumb/1/13/M1911a1.jpg/420px-M1911a1.jpg";
			medias[1]="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Holy_bible_book.jpg/1200px-Holy_bible_book.jpg";
			medias[2]="https://d2h1pu99sxkfvn.cloudfront.net/b0/8509017/511983201_k0xNjo3Lur/P0.jpg";

			for(int i=0;i<medias.length; i++)
			{

				//mediaList.add(medias[i].getText());
				mediaList.add(medias[i]);
			}

			Testflow.setPublished_media(mediaList);

			flowrepository.save(Testflow2);

			yourTimelimeDefinition=flowrepository.findAll();

			System.out.println("\n"+"Ecriture du repo dans la map OK, génération de l'affichage..."+"\n");
			System.out.println(flowrepository.findAll());

			//yourTimelimeDefinition.forEach((String, Flow) -> System.out.println(String+":"+Flow.getUser_name()));


			// [Step 3] : Renvoyer la timeline à l'IHM
			//return yourTimelimeDefinition;

		}catch (Exception e){
			System.out.println(e+"\n"+"On est dans le catch");

		}

		System.out.println("Sortie du try");
		return "welcomePage";
	}

	
}
