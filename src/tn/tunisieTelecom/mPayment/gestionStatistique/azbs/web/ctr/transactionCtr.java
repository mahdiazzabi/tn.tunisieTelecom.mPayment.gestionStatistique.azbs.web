package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.BanqueEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.TransactionEJBLocal;


@ManagedBean
@SessionScoped
public class transactionCtr {
	
	
	int idBanque ;
	
	
	@EJB
	TransactionEJBLocal transactionEJBLocal;
	
	@EJB
	BanqueEJBLocal banqueEJBLocal;

	public int getIdBanque() {
		return idBanque;
	}

	public void setIdBanque(int idBanque) {
		this.idBanque = idBanque;
	}
	

	
}
