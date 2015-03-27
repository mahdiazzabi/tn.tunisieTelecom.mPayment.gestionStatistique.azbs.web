package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Banque;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.BanqueEJBLocal;

@ManagedBean
@SessionScoped
public class BanqueCtr {
	
	
	private List<Banque> banques = new ArrayList<Banque>();
	private Banque banque;
	private Banque selectedBanque = new Banque();
	
	@EJB
	BanqueEJBLocal banqueEJBLocal;
	
	public List<Banque> listeBanque(){
		return banques = banqueEJBLocal.findAll();
	}
	
	public void update (){
		banqueEJBLocal.update(selectedBanque);
	}
	public void supprimer () {
		banqueEJBLocal.delete(selectedBanque);
	}
	
	public void add (){
		banqueEJBLocal.add(banque);
		banque = new Banque();
	}
	public List<Banque> getBanques() {
		return banques;
	}

	
	public void setBanques(List<Banque> banques) {
		this.banques = banques;
	}

	public Banque getBanque() {
		return banque;
	}

	public void setBanque(Banque banque) {
		this.banque = banque;
	}

	public Banque getSelectedBanque() {
		return selectedBanque;
	}

	public void setSelectedBanque(Banque selectedBanque) {
		this.selectedBanque = selectedBanque;
	}

}
