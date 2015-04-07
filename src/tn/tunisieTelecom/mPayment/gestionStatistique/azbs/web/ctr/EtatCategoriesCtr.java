package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Banque;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.BanqueEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.TransactionEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.util.Etat;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.util.EtatSousCategorie;

@ManagedBean
@SessionScoped
public class EtatCategoriesCtr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Etat> etats = new ArrayList<Etat>();
	private List<EtatSousCategorie> etatSousCategories = new ArrayList<EtatSousCategorie>();
	private Date start;
	private Date end;
	private int id_banque;
	private int total_nbr;
	private float total_montant;
	private Banque banque;

	@EJB
	TransactionEJBLocal transactionEJBLocal;

	@EJB
	BanqueEJBLocal banqueEJBLocal;

	public void doEtatCategorie() {
		total_montant = 0;
		total_nbr = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(end);
		calendar.add(calendar.HOUR_OF_DAY, +23);
		calendar.add(calendar.MINUTE, 59);
		calendar.add(calendar.SECOND, 59);
		end = calendar.getTime();
		System.err.println(start + "   " + end);
		etats = transactionEJBLocal.calculEtat(start, end, id_banque);
		for (Etat etat : etats) {
			total_nbr += etat.getNbr();
			total_montant += etat.getSomme();
		}
		etatSousCategories = transactionEJBLocal.calculeEtatSousCategorie(
				start, end, id_banque);
		banque = banqueEJBLocal.findById(id_banque);
	}

	public Banque getBanque() {
		return banque;
	}

	public void setBanque(Banque banque) {
		this.banque = banque;
	}

	public int getTotal_nbr() {
		return total_nbr;
	}

	public void setTotal_nbr(int total_nbr) {
		this.total_nbr = total_nbr;
	}

	public float getTotal_montant() {
		return total_montant;
	}

	public void setTotal_montant(float total_montant) {
		this.total_montant = total_montant;
	}

	public List<Etat> getEtats() {
		return etats;
	}

	public void setEtats(List<Etat> etats) {
		this.etats = etats;
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

	public int getId_banque() {
		return id_banque;
	}

	public void setId_banque(int id_banque) {
		this.id_banque = id_banque;
	}

	public List<EtatSousCategorie> getEtatSousCategories() {
		return etatSousCategories;
	}

	public void setEtatSousCategories(List<EtatSousCategorie> etatSousCategories) {
		this.etatSousCategories = etatSousCategories;
	}

}
