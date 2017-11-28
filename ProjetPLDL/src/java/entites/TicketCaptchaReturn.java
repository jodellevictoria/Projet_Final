package entites;
 
import java.util.Date;
 
 
//@XmlRootElement       //only needed if we also want to generate XML
public class TicketCaptchaReturn {
 
    private String numTicket;      
    private String captcha;    
    private String captchaImageFlux;
 
   
 
 
    public TicketCaptchaReturn(String numTicket, String captcha, String captchaImageFlux) {
        this.numTicket = numTicket;
        this.captcha = captcha;
        this.captchaImageFlux = captchaImageFlux;
    }
    public TicketCaptchaReturn() {        
    }
   
    public void setCaptchaImageFlux(String captchaImageFlux) {
        this.captchaImageFlux = captchaImageFlux;
    }
 
    public String getCaptchaImageFlux() {
        return captchaImageFlux;
    }
 
    public String getNumTicket() {
        return numTicket;
    }
   
    public String getCaptcha() {
        return captcha;
    }
 
    public void setNumTicket(String numTicket) {
        this.numTicket = numTicket;
    }
 
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
   
   
   
 
}