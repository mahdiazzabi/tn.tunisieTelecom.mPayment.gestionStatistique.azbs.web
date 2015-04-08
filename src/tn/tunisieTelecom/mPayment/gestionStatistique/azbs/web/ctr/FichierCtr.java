package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Fichier;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.FichierEJBLocal;

@ManagedBean
@SessionScoped
public class FichierCtr {

	
	@EJB
	FichierEJBLocal fichierEJBLocal;

	public List<Fichier> listeFichier(){
		return fichierEJBLocal.findAll();
	}
	

}
