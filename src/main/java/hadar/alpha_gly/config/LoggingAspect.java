package hadar.alpha_gly.config;


import hadar.alpha_gly.Services.IUserService;
import hadar.alpha_gly.Services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    IUserService userService;
    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);
    @Autowired
    public LoggingAspect(IUserService userService) {
        this.userService = userService;
    }
    @Before("execution(* hadar.alpha_gly.Controllers.UserController.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("Entering method {} with args {}", methodName, args);
      //  String email= userService.UserVerificationReturnEmail((HttpServletRequest) args[0]);
    //    System.out.println("el email : "+email);
      //         userService.SetUserOnline(email);
        }

}
