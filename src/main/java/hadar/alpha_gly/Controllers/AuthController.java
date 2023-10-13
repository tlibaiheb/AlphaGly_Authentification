package hadar.alpha_gly.Controllers;

import hadar.alpha_gly.Entities.User;
import hadar.alpha_gly.Repositories.UserRepository;
import hadar.alpha_gly.Security.jwt.JwtUtils;
import hadar.alpha_gly.Security.services.UserDetailsImpl;
import hadar.alpha_gly.Services.IEmailService;
import hadar.alpha_gly.Services.IUserService;
import hadar.alpha_gly.payload.mailing.EmailDetails;
import hadar.alpha_gly.payload.request.LoginRequest;
import hadar.alpha_gly.payload.response.JwtResponse;
import hadar.alpha_gly.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;


import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  AuthenticationManager authenticationManager;

 private IEmailService emailService;
  UserRepository userRepository;

  private IUserService iUserService;

  PasswordEncoder encoder;

  private WebApplicationContext appContext;
  JwtUtils jwtUtils;


  private PasswordEncoder passwordEncoder;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, String Provider) {
      Authentication authentication=null;
      if (Provider.equals("local")) {
     authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
}
else if(Provider.equals("facebook")) {
          System.out.println("Facebook Login");
          authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),"FacebookPassword"));

}
      SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(), 
                         roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody User User) {


    if (userRepository.existsByEmail(User.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }
    User u=iUserService.addUser(User);
    //Create EmailDetails
    EmailDetails emailDetails = new EmailDetails();
    emailDetails.setRecipient(User.getEmail());
    emailDetails.setSubject("Account Confirmation");
    emailDetails.setMsgBody("Hello "+User.getLastName()+" ,\n\n" +
            " welcome to our platform , your account has been created successfully .\n\n" +
            " please confirm your account by clicking on the following link : http://localhost:8083/user/confirm-account?token="+User.getToken()+"\n\n" +
            " or Enter Code Manually : "+User.getToken()+"\n\nBest Regards,\n" );

    String status
            = emailService.sendSimpleMail(emailDetails);



    return ResponseEntity.ok(new MessageResponse("User registered successfully !"+" // "+status));
  }
}
