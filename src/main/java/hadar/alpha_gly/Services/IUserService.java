package hadar.alpha_gly.Services;

import hadar.alpha_gly.Entities.ChatRoom;
import hadar.alpha_gly.Entities.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IUserService {
    User addUser(User u); // Add User
    String updateUser(User u); // Update User
    Boolean deleteUser(String Email); // Delete User

    Boolean ActivateUser(String Email); // Activate User

    Boolean isVerifiedAccount(String Email); // isActivated User

    Boolean VerifyUserToken(String Email,Long token); // Verify User Token
    //USER forgot password
    String ForgotPassword(String Email,Boolean EmailorPhone,String phonenumber); // Forgot Password
    String VerifyForgotPasswordToken(String Email,String PrevPass,String NewPass,Long token); // Verify Forgot Password Token

    //void AntiBot();

    void Authenticate(String Email);

    Long TokenGenerator(int ends);

    String UserVerificationReturnEmail(HttpServletRequest request);

    String SendSMS(String to, String body);

     void AffectToChatRoom(String email, ChatRoom r);
     boolean AddUserToChatRoom(String email, Long r, String invitedUserEmail);
     Boolean VerifyUserInChatRoom(String email, Long r);
     public void SendAndReceive(String Sender,Long IdMsg);

    User GetUserInfo(String Email);

    //get all users
    List<User> getAllUsers();

    //userExists
    boolean userExists(String email);

    boolean EditPassword(String email, String password, String newPassword);
    boolean SetUserOnline(String email);
}
