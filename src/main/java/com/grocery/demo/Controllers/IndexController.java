package com.grocery.demo.Controllers;

import com.grocery.demo.Model.ConfirmationToken;
import com.grocery.demo.Model.User;
import com.grocery.demo.Repository.ConfirmationTokenRepository;
import com.grocery.demo.Repository.UserRepository;
import com.grocery.demo.Service.EmailSenderService;
import com.grocery.demo.Service.UserService;
import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Controller
public class IndexController {

  @Autowired
   private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

  @Autowired
    EmailSenderService emailSenderService;


    private static final String ACCOUNT_SID = "ACd176f3083e1333d14939fe6f27d90a99";
    private static final String AUTH_ID = "4e3327cb4efbe3ecd8e893d809533763";

    @GetMapping(value="/register")
    public String registrationForm( Model model){

        model.addAttribute("user", new User());
//        User user = new User();
//        user.setDob(new Date());
        return "register";
    }

    public void sendSMS(User user) {
        try {

            Twilio.init(ACCOUNT_SID, AUTH_ID);

            Message.creator(new PhoneNumber(user.getPhoneNumber()), new PhoneNumber("+12029151841"),
                    "Dear "+user.getFname()+" "+user.getLname()+" thank you for registering with FredSystems."
            ).create();

        }
        catch (TwilioException e) {
            System.out.println("An error occured from twillio."+e.getMessage());
        }
    }

    @PostMapping(value="/registerUser")
    public ModelAndView registerUser(@Valid User user, BindingResult bindingResult, ModelAndView modelAndView){

        User checkUser = userService.findByEmail(user.getEmail());

        LocalDate today = LocalDate.now();

        Date birthDate = user.getDob();

        //Current system time
        ZoneId defaultZoneId = ZoneId.systemDefault();
        //converting user input to LocalDate
        Instant instant = birthDate.toInstant();
        LocalDate convDate = instant.atZone(defaultZoneId).toLocalDate();

        System.out.println("Let me first see what is the output" +
                "of convDate "+ convDate);

        long diffInYears = ChronoUnit.YEARS.between(convDate, today);

        int minAge = 18;

        if (diffInYears<minAge){
            System.out.print("This particular user has not attained the minimum voting age: "+diffInYears+" ");
        }else {
            System.out.println("The rest of the program execution continues...");
            System.out.println("Show me the age difference: "+diffInYears+" ");
        }


        if (bindingResult.hasErrors()){
            modelAndView.setViewName("register");

        }

        else if (checkUser!=null){

            modelAndView.addObject("message", "The email already exists");
            modelAndView.setViewName("register");
            bindingResult.reject("email");

        }

        else{

            //set a user to disabled by default before activation thru email.
            user.setEnabled(false);


            //save a user in the database.
            userService.save(user);

            //generating the confirmation token

            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(confirmationToken);

            //sending the email message

            SimpleMailMessage simpleMailMessage =   new SimpleMailMessage();
            simpleMailMessage.setTo(user.getEmail());
            simpleMailMessage.setSubject("Complete your registrations");
            simpleMailMessage.setFrom("devkibuchi2018@gmail.com");
            simpleMailMessage.setText("To activate your account, please click here : "
            +"http://localhost:8080/activate-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(simpleMailMessage);
            modelAndView.addObject("Email ", user.getEmail());

            //send also an sms message.
            sendSMS(user);

            modelAndView.setViewName("successfulRegistration");

        }

        return modelAndView;
//
    }


    @RequestMapping(value = "/activate-account", method = {RequestMethod.GET, RequestMethod.POST})
    public  ModelAndView activateUserAccount(ModelAndView modelAndView, @RequestParam("token")
    String confirmationToken){

        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token!=null){
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());

            user.setEnabled(true);
            userService.save(user);
            modelAndView.setViewName("welcome");
        }

        else{

                modelAndView.addObject("message","The link is invalid");
                modelAndView.setViewName("register");
        }
        return  modelAndView;

    }

    // Display the form
    @RequestMapping(value="/forgot-password", method=RequestMethod.GET)
    public ModelAndView displayResetPassword(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("forgotPassword");
        return modelAndView;
    }

    // Receive the address and send an email
    @RequestMapping(value="/forgot-password", method=RequestMethod.POST)
    public ModelAndView forgotUserPassword(ModelAndView modelAndView, User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            // Create token
            ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);

            // Save it
            confirmationTokenRepository.save(confirmationToken);

            // Create the email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(existingUser.getEmail());
            mailMessage.setSubject("Complete Password Reset!");
            mailMessage.setFrom("fredkibuchi64@gmail.com");
            mailMessage.setText("To complete the password reset process, please click here: "
                    + "http://localhost:8082/confirm-reset?token="+confirmationToken.getConfirmationToken());

            // Send the email
            emailSenderService.sendEmail(mailMessage);

            modelAndView.addObject("message", "Request to reset password received. Check your inbox for the reset link.");
            modelAndView.setViewName("successForgotPassword");

        } else {
            modelAndView.addObject("message", "This email address does not exist!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    // Endpoint to confirm the token
    @RequestMapping(value="/confirm-reset", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView validateResetToken(ModelAndView modelAndView, @RequestParam("token")String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            User user = userService.findByEmail(token.getUser().getEmail());
            user.setEnabled(true);
            userService.save(user);
            modelAndView.addObject("user", user);
            modelAndView.addObject("emailId", user.getEmail());
            modelAndView.setViewName("resetPassword");
        } else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    // Endpoint to update a user's password
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ModelAndView resetUserPassword(ModelAndView modelAndView, User user) {
        if (user.getEmail() != null) {
            // Use email to find user
            User tokenUser = userService.findByEmail(user.getEmail());
            //new BCryptPasswordEncoder().encode(user.getPword())
            tokenUser.setPword(user.getPword());
            userService.save(tokenUser);
            modelAndView.addObject("message", "Password successfully reset. You can now log in with the new credentials.");
            modelAndView.setViewName("successResetPassword");
        } else {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }


    @GetMapping("/login")
    public String login(Model model, String error, String logout){
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
            return "login";
        }


        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }


}
