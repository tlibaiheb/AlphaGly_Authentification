package hadar.alpha_gly.Controllers;

import hadar.alpha_gly.Entities.User;
import hadar.alpha_gly.Services.IUserService;
import hadar.alpha_gly.payload.request.NewPasswordRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user/")
public class UserController {
    private IUserService iUserService;
    @PostMapping("addUser")
    public ResponseEntity<User> addUser(@RequestBody User User) {
            iUserService.addUser(User);
        return new ResponseEntity<User>(HttpStatus.CREATED);
    }

    @PutMapping("/updateUser")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity updateUser(HttpServletRequest request,@RequestBody User u) {
        String email= iUserService.UserVerificationReturnEmail(request);
        if (email.equals("Token Doesn't Match Authenfied User"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("TOKEN MISMATCH");

        if (u.getEmail().equals(email))
        {
            iUserService.updateUser(u);
        return ResponseEntity.status(HttpStatus.OK).body("Updated");
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("TOKEN MISMATCH");
    }



    @DeleteMapping("/deleteUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteUser(HttpServletRequest request ,@RequestParam String emailAdress) {
        String email= iUserService.UserVerificationReturnEmail(request);
        if (email.equals("Token Doesn't Match Authenfied User"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("TOKEN MISMATCH");
        if (iUserService.deleteUser(emailAdress))
            return ResponseEntity.status(HttpStatus.OK).body("Disabled !");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found !");
    }
    @GetMapping("/activateUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity ActivateUser(HttpServletRequest request, @RequestParam String emailAdress) {
        String email= iUserService.UserVerificationReturnEmail(request);
        if (email.equals("Token Doesn't Match Authenfied User"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("TOKEN MISMATCH");
        if (iUserService.ActivateUser(emailAdress))
            return ResponseEntity.status(HttpStatus.OK).body("Activated !");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found !");
    }
    //implementing the method RetievePasswordInfo //DISABLED
//    @GetMapping("/RetievePasswordInfo")
//    @PreAuthorize("hasRole('USER') or hasRole('PROVIDER') or hasRole('ADMIN') or hasRole('DELIVERY')")
//    public ResponseEntity RetievePasswordInfo(HttpServletRequest request) {
//        String email= iUserService.UserVerificationReturnEmail(request);
//        if (email.equals("Token Doesn't Match Authenfied User"))
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token Doesn't Match Authenfied User");
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("Expiration_Date", ipwdService.RetievePasswordInfo(email));
//        return ResponseEntity.status(HttpStatus.OK).body(map);
//    }
    //Implementing VerifyUserToken

    @GetMapping("/confirm-account")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity VerifyUserToken(HttpServletRequest request, @RequestParam Long token) {
        String email= iUserService.UserVerificationReturnEmail(request);
        if (email.equals("Token Doesn't Match Authenfied User"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("TOKEN MISMATCH");
        if (iUserService.VerifyUserToken(email, token))
            return ResponseEntity.status(HttpStatus.OK).body("Activated !");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went Wrong !");
        //Wrong TOKEN SENT
    }

    //Implementing Authenticate
    @GetMapping("/Authenticate")
    public void Authenticate(@RequestParam String email) {
        iUserService.Authenticate(email);

    }
    //Forgot Password //DISABLED
    @GetMapping("/recovery/ForgotPassword")
    public ResponseEntity ForgotPassword(@RequestParam String email,@RequestParam Boolean Phone,@RequestParam String PhoneNum) {

       if ( iUserService.ForgotPassword(email,Phone, PhoneNum).equals("User Not Found"))
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
       else
           return ResponseEntity.status(HttpStatus.OK).body("Token Sent Via Mobile or Email !");
    }
    //Verify Forgot Password Token //DISABLED
    @PostMapping("/recovery/VerifyForgotPasswordToken")
    public ResponseEntity VerifyForgotPasswordToken(@RequestParam String email, @RequestBody NewPasswordRequest newPasswordRequest, @RequestParam Long token) {
        //return "gg works2";
        if ( iUserService.VerifyForgotPasswordToken(email, newPasswordRequest.getPassword(), newPasswordRequest.getNewPassword(), token).equals("User Not Found"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        return ResponseEntity.status(HttpStatus.OK).body("Password Has been reset !");
    }
// SendAndReceive

    //Get User By Email
    @GetMapping("/GetUserInfoByEmail")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity GetUserByEmail(HttpServletRequest request,@RequestParam String email) {
        if (iUserService.userExists(email))
            // return iUserService.GetUserInfo(email);
        {
            User u = iUserService.GetUserInfo(email);
            u.setPassword(null);
            return ResponseEntity.status(HttpStatus.OK).body(u);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    //Get all users
    @GetMapping("/GetAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> GetAllUsers() {
            //VUNERABLE
        return iUserService.getAllUsers();
    }
//ADMIN POWER
    @PutMapping("/admin/updateUser")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUser(@RequestBody User u) {
            return iUserService.updateUser(u);

    }
    //userExists
    @GetMapping("/userExists")
    public ResponseEntity userExists(@RequestParam String email) {

       if (iUserService.userExists(email))
       {
           return ResponseEntity.status(HttpStatus.FOUND).body(null);
       }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/recovery/userRecovery")
    public ResponseEntity userRecovery(@RequestParam String email) {


        if (iUserService.userExists(email))
        {
            User u =iUserService.GetUserInfo(email);
            HashMap<String, String> map = new HashMap<>();
            map.put("email", u.getEmail());
            map.put("phone", u.getPhoneNumber1().toString());

            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
//implement GET isVerifiedAccount


    @GetMapping("/check")
    public ResponseEntity isVerifiedAccount(@RequestParam String email) {


        if (iUserService.userExists(email))
        {
            Boolean result =iUserService.isVerifiedAccount(email);
            HashMap<String, Boolean> map = new HashMap<>();
            map.put("isVerified", result);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PutMapping("/edit/password")
    public ResponseEntity updateUser(HttpServletRequest request, @RequestBody NewPasswordRequest newPasswordRequest) {
        String email= iUserService.UserVerificationReturnEmail(request);
        if (email.equals("Token Doesn't Match Authenfied User"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        if (iUserService.EditPassword(email, newPasswordRequest.getPassword(), newPasswordRequest.getNewPassword()))
            return ResponseEntity.status(HttpStatus.OK).body("Password Updated !");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went Wrong !");


    }
}
