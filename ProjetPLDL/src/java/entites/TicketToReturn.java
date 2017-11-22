/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites;

/**
 *
 * @author jodel
 */
public class TicketToReturn {
    int noTicket;
        String cle;

        public TicketToReturn(int noTicket, String cle) {
            this.noTicket = noTicket;
            this.cle = cle;
        }

        public TicketToReturn() {
        }

        public int getNoTicket() {
            return noTicket;
        }

        public void setNoTicket(int noTicket) {
            this.noTicket = noTicket;
        }

        public String getCle() {
            return cle;
        }

        public void setCle(String cle) {
            this.cle = cle;
        }    
}
