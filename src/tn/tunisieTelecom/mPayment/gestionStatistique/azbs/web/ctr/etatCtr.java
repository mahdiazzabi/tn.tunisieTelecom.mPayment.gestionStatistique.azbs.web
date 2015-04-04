package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.sun.org.apache.xerces.internal.impl.dv.xs.DayDV;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Banque;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Categories;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.SousCategories;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Transaction;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.BanqueEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.CategoriesLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.SousCategotiesEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.TransactionEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.services.SousCategotiesEJB;

@ManagedBean(name = "etat")
@SessionScoped
public class etatCtr {

	@EJB
	CategoriesLocal categoriesLocal;
	@EJB
	SousCategotiesEJBLocal sousCategotiesEJBLocal ;
	@EJB
	TransactionEJBLocal transactionEJBLocal ;
	@EJB
	BanqueEJBLocal banqueEJBLocal ;
	private static final long serialVersionUID = 1L;
	
	private static List<Ligne> ligneList = new ArrayList<Ligne>();
	
	private Date date_debut ;
	private Date date_fin ;
	int idBanque ;
	
	
	public void doEtat (){
		Banque banque =banqueEJBLocal.findById(idBanque);
		ligneList = new ArrayList<etatCtr.Ligne>();
		
		
		List<Categories> categories = new ArrayList<Categories>();
		categories = categoriesLocal.findAll();
		
		for (Categories categories2 : categories) {
			//calcul count and sum
			//insertion ligne ds datatable
			ligneList.add(new Ligne("TEST" + categories2.getLibelle(), 1 , 0.1));
			for (SousCategories sousCategories2 : categories2.getSousCategories()) {
				//calcul count and sum
				//insertion ligne ds datatable
				ligneList.add(new Ligne(sousCategories2.getLibelle(), 1 , 0.1));
				
			}
		
		}
		
		for (Transaction transaction : banque.getTransactions()) {
				
			if (transaction.getDate().after(date_debut) && transaction.getDate().before(date_fin) )
			{
				System.out.println(transaction.getDate());
				
			}
			
		}
		
	}
	
	
	public Date getDate_debut() {
		return date_debut;
	}


	public void setDate_debut(Date date_debut) {
		this.date_debut = date_debut;
	}


	public Date getDate_fin() {
		return date_fin;
	}


	public void setDate_fin(Date date_fin) {
		this.date_fin = date_fin;
	}


	public int getIdBanque() {
		return idBanque;
	}


	public void setIdBanque(int idBanque) {
		this.idBanque = idBanque;
	}


	public static List<Ligne> getLigneList() {
		return ligneList;
	}

	public static void setLigneList(List<Ligne> ligneList) {
		etatCtr.ligneList = ligneList;
	}

	public List<Ligne> getLignes() {
		return ligneList;

	}

	public static class Ligne{
		 
		String libelle;
		int nombre;
		Double montant;
		
		public Ligne(String libelle, int nombre, Double montant) {
			super();
			this.libelle = libelle;
			this.nombre = nombre;
			this.montant = montant;
		}
		
		public String getLibelle() {
			return libelle;
		}
		public void setLibelle(String libelle) {
			this.libelle = libelle;
		}
		public int getNombre() {
			return nombre;
		}
		public void setNombre(int nombre) {
			this.nombre = nombre;
		}
		public Double getMontant() {
			return montant;
		}
		public void setMontant(Double montant) {
			this.montant = montant;
		}

		
		
		}
 
	}

