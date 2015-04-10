package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.primefaces.model.chart.PieChartModel;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Banque;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.BanqueEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.TransactionEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.util.Etat;

@ManagedBean
@SessionScoped
public class StatistiqueCtr {

	
	private PieChartModel pieModel1 = new PieChartModel();
    private PieChartModel pieModel2 = new PieChartModel();
    private List<Banque> banques = new ArrayList<Banque>();
    private Date start;
    private Date end ;
    
    
    @EJB
    BanqueEJBLocal banqueEJBlocal ;
    
    @EJB
    TransactionEJBLocal transactionEJBLocal ;
    
    
         
    private void createPieModel() {
        pieModel1 = new PieChartModel();
        pieModel2 = new PieChartModel();
        
        banques = banqueEJBlocal.findAll();
        List<Etat> etats = new ArrayList<Etat>();
        Calendar calendar = Calendar.getInstance();
		calendar.setTime(end);
		calendar.add(calendar.HOUR_OF_DAY, +23);
		calendar.add(calendar.MINUTE, 59);
		calendar.add(calendar.SECOND, 59);
		end = calendar.getTime();
		float total_montant ;
        for (Banque banque : banques) {
        	total_montant = 0 ;
        	etats = transactionEJBLocal.calculEtat(start, end, banque.getId());
        	for (Etat etat : etats) {
    			total_montant += etat.getSomme();
    		}
        	System.err.println(banque.getNom()+ "   ="+ total_montant);
        	pieModel1.set(banque.getNom(), total_montant);
            pieModel2.set(banque.getNom(), total_montant);
                     
		}
             
        pieModel1.setTitle("Statistiques périodique des banques");
        pieModel1.setLegendPosition("w");
        pieModel2.setTitle("Statistiques périodique des banques en %");
        pieModel2.setLegendPosition("e");
        pieModel2.setFill(false);
        pieModel2.setShowDataLabels(true);
        pieModel2.setDiameter(150);
    
    }
     
       
    public void doStatistiquesBancaire(){
    	createPieModel();
    	}
    
	public List<Banque> getBanques() {
		return banques;
	}


	public void setBanques(List<Banque> banques) {
		this.banques = banques;
	}


	public Date getStart() {
		return start;
	}


	public void setStart(Date start) {
		this.start = start;
	}


	public Date getEnd() {
		return end;
	}


	public void setEnd(Date end) {
		this.end = end;
	}


	public PieChartModel getPieModel1() {
		return pieModel1;
	}
	public void setPieModel1(PieChartModel pieModel1) {
		this.pieModel1 = pieModel1;
	}
	public PieChartModel getPieModel2() {
		return pieModel2;
	}
	public void setPieModel2(PieChartModel pieModel2) {
		this.pieModel2 = pieModel2;
	}
 
	
	
}
