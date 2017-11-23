package entites;
 
import java.util.Date;
 
 
//@XmlRootElement       //only needed if we also want to generate XML
public class TicketCaptchaReturn {
 
    private String numTicket;      
    private String captcha;    
 
 
    public TicketCaptchaReturn(String numTicket, String captcha) {
        this.numTicket = numTicket;
        this.captcha = captcha;
    }
    public TicketCaptchaReturn() {        
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