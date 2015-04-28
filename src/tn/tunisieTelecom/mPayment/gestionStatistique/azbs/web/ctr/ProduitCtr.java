package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Produit;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.SousCategories;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.ProduitEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.SousCategotiesEJBLocal;

@ManagedBean
@SessionScoped
public class ProduitCtr {
	private List<Produit> produits = new ArrayList<Produit>();
	private List<SousCategories> sousCategories = new ArrayList<SousCategories>();
	private Produit selectedProduit = new Produit();
	private Produit produit = new Produit();

	int idSousCat;

	@EJB
	ProduitEJBLocal produitEJBLocal;
	@EJB
	SousCategotiesEJBLocal sousCategotiesEJBLocal;


	public List<Produit> charger_liste_produits() {

		return produits = produitEJBLocal.findall();
		
	}
	public List<SousCategories> charger_liste_sous_cat() {
		return sousCategories = sousCategotiesEJBLocal.findAll();
	}

	public void ajouter_produit() {

		produit.setSousCategories(sousCategotiesEJBLocal.findById(idSousCat));
		for (Produit produit : produits) {
			if (produit.getRef() == this.produit.getRef()) {
				System.err.println("duplicate Ref for produit");

				return;
			}
		}

		produitEJBLocal.add(produit);
		produit = new Produit();
	}

	public void modifier_produit() {
		for (Produit produit : produits) {
			if (produit.getRef() == this.produit.getRef()) {
				System.err.println("duplicate Ref for produit");

				return;
			}
		}
		produitEJBLocal.update(selectedProduit);
	}

	public void supprimer_produit() {
		produitEJBLocal.delete(selectedProduit);
	}

	public List<Produit> getProduits() {
		return produits;
	}

	public void setProduits(List<Produit> produits) {
		this.produits = produits;
	}

	public Produit getSelectedProduit() {
		return selectedProduit;
	}

	public void setSelectedProduit(Produit selectedProduit) {
		this.selectedProduit = selectedProduit;
	}

	public int getIdSousCat() {
		return idSousCat;
	}

	public void setIdSousCat(int idSousCat) {
		this.idSousCat = idSousCat;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public List<SousCategories> getSousCategories() {
		return sousCategories;
	}

	public void setSousCategories(List<SousCategories> sousCategories) {
		this.sousCategories = sousCategories;
	}

	
}
