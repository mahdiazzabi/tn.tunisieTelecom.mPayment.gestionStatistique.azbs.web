package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.io.File;
import java.io.IOException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.BanqueEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.TransactionEJBLocal;


@ManagedBean
@SessionScoped
public class transactionCtr {
	
	private File file;
	int idBanque ;
	
	
	@EJB
	TransactionEJBLocal transactionEJBLocal;
	
	@EJB
	BanqueEJBLocal banqueEJBLocal;
	
	void traitement_Fichier_STB_BH_BNA(){
		
	}
	void traitement_Fichier_ATB(){
		
	}
	void traitement_Fichier_SMT(){
	
	}

	public void traiterFichier() throws IOException {
		System.out.println("OKKKKKKKK");
		
		switch(idBanque){
			case 1 : case 2 :  case 3 : traitement_Fichier_STB_BH_BNA();break;
			case 4 : traitement_Fichier_ATB();break;
			case 5 : traitement_Fichier_SMT();break;
			default:;
		}
		
		}
		
	
	public int getIdBanque() {
		return idBanque;
	}

	public void setIdBanque(int idBanque) {
		this.idBanque = idBanque;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	

	
}
