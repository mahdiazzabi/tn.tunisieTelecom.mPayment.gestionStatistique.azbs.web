package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.util.ArrayList;
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

@ManagedBean
@SessionScoped
public class StatistiqueCtr {

	
	private PieChartModel pieModel1;
    private PieChartModel pieModel2;
    private List<Banque> banques = new ArrayList<Banque>();
    private Date start;
    private Date end ;
    
    
    @EJB
    BanqueEJBLocal banqueEJBlocal ;
    
    @EJB
    TransactionEJBLocal transactionEJBLocal ;
    
    @PostConstruct
    public void init() {
        createPieModels();
    }
    
    
    private void createPieModels() {
        createPieModel1();
        createPieModel2();
    }
 
    private void createPieModel1() {
        pieModel1 = new PieChartModel();
        banques = banqueEJBlocal.findAll();
        int i = 50 ;
        for (Banque banque : banques) {
        	

            pieModel1.set(banque.getNom(), i+ 50);
            
		}
             
        pieModel1.setTitle("Simple Pie");
        pieModel1.setLegendPosition("w");
    }
     
    private void createPieModel2() {
        pieModel2 = new PieChartModel();
         
        pieModel2.set("Brand 1", 540);
        pieModel2.set("Brand 2", 325);
        pieModel2.set("Brand 3", 702);
        pieModel2.set("Brand 4", 421);
        pieModel2.setTitle("Custom Pie");
        pieModel2.setLegendPosition("e");
        pieModel2.setFill(false);
        pieModel2.setShowDataLabels(true);
        pieModel2.setDiameter(150);
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
