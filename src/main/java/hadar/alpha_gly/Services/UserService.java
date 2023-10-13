package hadar.alpha_gly.Services;

import hadar.alpha_gly.Entities.*;
import hadar.alpha_gly.Repositories.*;
import hadar.alpha_gly.Security.jwt.JwtUtils;
import hadar.alpha_gly.payload.mailing.EmailDetails;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
@Service
@AllArgsConstructor
public class UserService implements IUserService{
    JwtUtils jwtUtils;
    //private TwilioService twilioService;
    UserRepository userRepository;
    UserStatusRepository userStatusRepository;
    MessageRepository messagesRepository;
    private ChatRoomRepository chatRoomRepository;

    private IEmailService emailService;

    RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User addUser(User u) {
        UserStatus userStatus = new UserStatus();
        userStatus.setLastUpdate(new Date());
        userStatus.setUserStatus(userStatusEnum.Online);

        u.setToken(TokenGenerator(7));  //Token
        u.setUserStatus(userStatus); //User Status
        u.setJoined(new Date());  //Joined Date
        u.setEnabled(true); // Activated Account
        System.out.println(u);
        Set<Role> strRoles = u.getRoles(); // Role
        System.out.println(strRoles);
        u.setPassword(passwordEncoder.encode(u.getPassword())); //Password
        Set<Role> roles = new HashSet<>();
        //All The Other Data should be submited from the FrontEnd
        if (strRoles == null) {
            Role providerrRole = roleRepository.findByName(RolesTypes.ROLE_PATIENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(providerrRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.getName().toString()) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName(RolesTypes.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "ROLE_TESTER":
                        Role testRole = roleRepository.findByName(RolesTypes.ROLE_TESTER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(testRole);

                        break;

                    case "ROLE_DOCTOR":
                        Role docRole = roleRepository.findByName(RolesTypes.ROLE_DOCTOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(docRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(RolesTypes.ROLE_PATIENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        u.setRoles(roles);
        userStatusRepository.save(userStatus);
        userRepository.save(u);
        System.out.println("User Created");
        return u;
    }

    @Override
    public String updateUser(User u) {
        System.out.println(u.getPhoneNumber2() + " - " + u.getPhoneNumber1());
        System.out.println(u.getHeight());
        User u2=userRepository.findByEmail(u.getEmail()).get();
        u2.setFirstName(u.getFirstName());
        u2.setGender(u.getGender());
        u2.setLastName(u.getLastName());
        u2.setWeight(u.getWeight());
        u2.setHeight(u.getHeight());
        u2.setPhoneNumber1(u.getPhoneNumber1());
        u2.setPhoneNumber2(u.getPhoneNumber2());
        u2.setBloodType(u.getBloodType());
        System.out.println(u2.getEmail());

        userRepository.save(u2);
        return null;
    }

//    @Override
//    public String updateUser(User u) {
//        System.out.println(u);
//        User u2=userRepository.findByEmailEquals(u.getEmail());
//        //---
//        //ADD REM
//        Set<Role> roles = new HashSet<>(u.getRoles());
//        Set<Role> strRoles = new HashSet<>();
//        roles.forEach(role -> {
//            //   System.out.println(role.getId().toString()+" "+role.getName().toString());
//            switch (role.getName().toString()) {
//                case "ROLE_ADMIN":
//                    Role adminRole = roleRepository.findByName(RolesTypes.ROLE_ADMIN)
//                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                    strRoles.add(adminRole);
//
//                    break;
//                case "ROLE_TESTER":
//                    Role modRole = roleRepository.findByName(RolesTypes.ROLE_TESTER)
//                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                    strRoles.add(modRole);
//
//                    break;
//
//                case "ROLE_DOCTOR":
//                    Role providerRole = roleRepository.findByName(RolesTypes.ROLE_DOCTOR)
//                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                    strRoles.add(providerRole);
//                    break;
//                default:
//                    Role userRole = roleRepository.findByName(RolesTypes.ROLE_PATIENT)
//                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                    strRoles.add(userRole);
//            }
//        });
//        //u2.setRoles(strRoles);
//        //ADD REMM
//        u2.setId(u2.getId());
////        //set the rest of the fields
//        u2.setFirstName(u.getFirstName());
//        u2.setLastName(u.getLastName());
//        u2.setAdress(u.getAdress());
//        u2.setBirthDate(u.getBirthDate());
//        u2.setCountry(u.getCountry());
//
//        u2.setPhoneNumber(u.getPhoneNumber());
//        u2.setRoles(u.getRoles());
//        u2.setHashedPWD(u2.getHashedPWD());
//        u2.setEnabled(u.isEnabled());
//        u2.setImg_URL(u2.getImg_URL());
//        u2.setToken(u2.getToken());
//        u2.setFees(u2.getFees());
//        u2.setShoppingCart(u2.getShoppingCart());
//        u2.setRoles(strRoles);
//        userRepository.save(u2);
//
//
//
//        return ("User Updated");
//    }

    @Override
    public Boolean deleteUser(String Email) {
        User u= userRepository.findByEmailEquals(Email);
        if (u == null)
            return false;
        u.setEnabled(false);
        userRepository.save(u);
        return  true;
    }


    @Override
    public void AffectToChatRoom(String email, ChatRoom r) {
        User u = userRepository.findByEmail(email).get();
        List<ChatRoom> chatRoomList = new ArrayList<>(u.getChatRooms());
        chatRoomList.add(r);
        u.setChatRooms(chatRoomList);
        userRepository.save(u);
        System.out.println("User Affected to ChatRoom");
    }

    @Override
    public boolean AddUserToChatRoom(String email, Long r,String invitedEmail) {
//to be edited later query required
        User u = userRepository.findByEmail(email).get();
        User InvitedE=userRepository.findByEmail(invitedEmail).orElseThrow(() -> new RuntimeException("Error: User is not found."));
        ChatRoom chatRoom = chatRoomRepository.findById(r).orElse(null);
        if (chatRoom == null) {
            return false;
        }
        //but first check if user can invite (make sure he is in the chatRoom)
        Long Nbr=chatRoomRepository.VerifyUserCanInviteToChatRoom(u.getId(),r);
        Long Nbr2=chatRoomRepository.VerifyUserCanInviteToChatRoom(InvitedE.getId(),r);
        System.out.println("Nbr : "+Nbr);
        if (Nbr>=1 && Nbr2==0)
        {
            //user can be added after checking the invitor and the invited isn't already in the chatRoom
            List<ChatRoom> chatRoomList = new ArrayList<>(InvitedE.getChatRooms());
            chatRoomList.add(chatRoom);
            InvitedE.setChatRooms(chatRoomList);
            userRepository.save(InvitedE);
            return true;
        }
        else
            System.out.println("User Can't Invite or Already IN "+Nbr + " "+Nbr2);
        return false;
    }

    @Override
    public Boolean VerifyUserInChatRoom(String email, Long r) {
        User u = userRepository.findByEmail(email).get();
        System.out.println("email : "+u.getEmail());
        ChatRoom chatRoom = chatRoomRepository.findById(r).get();
        System.out.println("chatRoom : "+chatRoom.getNameChat());
        if (u.getChatRooms().indexOf(chatRoom) != -1) {
            //  return "User Can Send Message";
            return true;
        }
        else
            return false;
        // return "ERROR  : User Not in ChatRoom";
    }


    public void SendAndReceive(String Sender,Long IdMsg) {
        //fixed
        User u = userRepository.findByEmail(Sender).get();
        Message m = messagesRepository.findById(IdMsg).get();
        List<Message> oldSendings = new ArrayList<>(u.getSentList());
        List<Message> oldReceived = new ArrayList<>(u.getReceivedList());
        oldSendings.add(m);
        u.setSentList(oldSendings);
        userRepository.save(u);
        //---BROADCASTING---//
        List<Long> Receivers = chatRoomRepository.ListUserRelatedtoChatRoom(m.getChatRoom().getIdChatRoom());
        System.out.printf("Receivers : "+Receivers.size());
        for(Long receiver : Receivers) {
            if (receiver == u.getId()) {
                continue;
            }
            User u2 = userRepository.findById(receiver).get();
            System.out.println("Receiver : "+u2.getEmail());
            List<Message> oldReceived2 = new ArrayList<>(u2.getReceivedList());
            oldReceived2.add(m);
            u2.setReceivedList(oldReceived2);
            userRepository.save(u2);
            System.out.println("Message Sent From : "+u.getEmail()+" To : "+u2.getEmail());
        }


        System.out.println("Message Sent");
    }

    //Activate User Account ADMIN ONLY
    @Override
    public Boolean ActivateUser(String Email) {
        User u= userRepository.findByEmailEquals(Email);
        if (u == null)
            return false;
        u.setEnabled(true);
        userRepository.save(u);
        return true;
    }

    @Override
    public Boolean isVerifiedAccount(String Email) {
        User u= userRepository.findByEmailEquals(Email);
        if (u == null || u.getToken().toString().length()==7)
            return false;
        return true;
    }

    public Boolean VerifyUserToken(String Email,Long token){
        User u= userRepository.findByEmailEquals(Email);
        if (u == null)
            return false;
        System.out.println("User Token : "+u.getToken());
        if(u.getToken().equals(token)){
            u.setToken(0L);
            userRepository.save(u);
            return true;
        }
        return  false;
    }


    @Override
    public String ForgotPassword(String Email,Boolean Phone,String phonenumber) {
        User u= userRepository.findByEmailEquals(Email);
        if (u == null || u.isEnabled()==false)
            return "User Not Found";
        u.setToken(TokenGenerator(6));
        userRepository.save(u);
        String body = "Hello " + u.getFirstName() + " " + u.getLastName() + " ,\n\n" +
                "Your OTP Code is : " + u.getToken() + "\n\n" +
                "Regards,\n" +
                "Team E-Commerce";
        if (Phone == false) {
            //SENDS OTP TO USER EMAIL
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(u.getEmail());
            emailDetails.setSubject("Forgot Password");
            emailDetails.setMsgBody(body);

            String status
                    = emailService.sendSimpleMail(emailDetails);

            //END
        }
        else
        {
            // Send an SMS message
          //  twilioService.sendSMS("+216"+phonenumber, body);

        }
        return "OTP Sent";
    }

    @Override
    public String VerifyForgotPasswordToken(String Email,String PrevPass,String NewPass ,Long token) {
        User u= userRepository.findByEmailEquals(Email);
        if (u == null)
            return "User Not Found";
        if(u.getToken().equals(token) && PrevPass.equals(NewPass)){
            u.setToken(0L);
            u.setPassword(passwordEncoder.encode(NewPass));
            userRepository.save(u);
            //GRANT PERMISSION TO CHANGE PASSWORD --> Redirect to Change Password Page
            //SENDS OTP TO USER EMAIL
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(u.getEmail());
            emailDetails.setSubject("Password Changed");
            emailDetails.setMsgBody("Hello "+u.getFirstName()+" "+u.getLastName()+" ,\n\n" +
                    "Your Password has been Changed Successfully\n\n" +
                    "Regards,\n" +
                    "Team E-Commerce");
            return "User Password Changed"  ;
        }
        return  "Passwords Doesn't Match or Wrong Token";
    }

    //@Scheduled(cron = "0 * * * * 7")
    //@Scheduled(cron = "*/10 0 0 * * *")
    @Scheduled(fixedRate = 60000)
    public void CheckifOnline()
    {
        System.out.println("Checking if Online");
        Date datenow = new Date();
        userRepository.findByEnabledTrue().forEach(u->{
            long diff = datenow.getTime() - u.getUserStatus().getLastUpdate().getTime();
                //diff more than 2 minutes
            if ( diff / (60 * 1000) > 2)
            {
            //updating user info
                u.getUserStatus().setUserStatus(userStatusEnum.Away);
                userRepository.save(u);
                System.out.println("User : "+u.getEmail()+" is now Away");
            }
            else if ( diff / (60 * 1000) > 30)
            {
                //updating user info
                u.getUserStatus().setUserStatus(userStatusEnum.Offline);
                userRepository.save(u);
                System.out.println("User : "+u.getEmail()+" is now Offline");
            }
        });
    }
    //set user status to online
    @Override
    public boolean SetUserOnline(String Email) {
        User u= userRepository.findByEmailEquals(Email);
        System.out.println("User : "+u.getEmail()+" is now Online");
        if (u == null)
            return false;
        u.getUserStatus().setUserStatus(userStatusEnum.Online);
        u.getUserStatus().setLastUpdate(new Date());
        userRepository.save(u);
    //    System.out.println("User : "+u.getEmail()+" is now Online");
        return true;
    }

//    @Scheduled(cron = "0 * * * * 7")
    public void AntiBot() {
        System.out.println("AntiBot check");
        Date datenow = new Date();

        userRepository.findAll().forEach(u->{
            if (u.getToken().toString().length()==7 && u.isEnabled()==true)
            {
                System.out.println("User Account Disabled : "+u.getEmail()+" |" );
                long diff = datenow.getTime() - u.getJoined().getTime();
                if( ((diff / (24 * 60 * 60 * 1000) <= 7))){
                    u.setEnabled(false);
                    userRepository.save(u);
                }
            }
        });

    }

//    @Override
//    public int NumberOfSubs() {
//        return userRepository.NumberOfSubs();
//    }

    @Override
    public void Authenticate(String Email) {
        // System.out.println("FETCHING USER");
        Optional<User> user = userRepository.findByEmail(Email);
        if (user.isPresent())
        {
              System.out.println(user.get().getEmail() + " | " + user.get().getPassword());
        }
        else
        {
            System.out.println("User Not Found");
        }

    }

    @Override
    public Long TokenGenerator(int ends) {
        String token=new Random().nextLong()+"";
        token=token.substring(0,ends);
        //7 CHARACTERS FOR SIGN
        //6 FOR RECO
        return Math.abs(Long.parseLong(token));

    }

    @Override
    public String UserVerificationReturnEmail(HttpServletRequest request) {
        String headerAuth= request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return jwtUtils.getUserNameFromJwtToken(headerAuth.substring(7, headerAuth.length()));
        }
        return "Token Doesn't Match Authenfied User";
    }

    @Override
    public String SendSMS(String to, String body) {

        System.out.println("SMS SENT TO : "+to);
        // Send an SMS message
        //twilioService.sendSMS("+216"+to, body);
        return "SMS MESSAGE SENT";
    }
    public User GetUserInfo(String Email) {
        return userRepository.findByEmailEquals(Email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    //user already exists
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean EditPassword(String email, String password, String newPassword) {
        User u= userRepository.findByEmailEquals(email);
        if (u == null)
            return false;
        if(passwordEncoder.matches(password,u.getPassword())){
            u.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(u);
            return true;
        }
        return false;
    }

}
