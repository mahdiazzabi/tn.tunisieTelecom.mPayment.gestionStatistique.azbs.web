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
	private Produit selectedProduit = new Produit();
	private Produit produit = new Produit();

	int idSousCat;

	@EJB
	ProduitEJBLocal produitEJBLocal;
	@EJB
	SousCategotiesEJBLocal sousCategotiesEJBLocal;

	public List<Produit> doFindall() {
		return produits = produitEJBLocal.findall();
	}

	public void add() {

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

	public void update() {
		for (Produit produit : produits) {
			if (produit.getRef() == this.produit.getRef()) {
				System.err.println("duplicate Ref for produit");

				return;
			}
		}
		produitEJBLocal.update(selectedProduit);
	}

	public void delete() {
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

}
