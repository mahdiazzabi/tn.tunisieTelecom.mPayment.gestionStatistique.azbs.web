package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Categories;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.SousCategories;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.CategoriesLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.SousCategotiesEJBLocal;

@ManagedBean
@SessionScoped
public class SousCategoriesCtr {
	private List<SousCategories> sousCategories = new ArrayList<SousCategories>();
	private SousCategories selectedSousCategories = new SousCategories();
	private SousCategories sousCategorie = new SousCategories();
	int idCat;
	@EJB
	SousCategotiesEJBLocal sousCategotiesEJBLocal;

	@EJB
	CategoriesLocal categoriesLocal;

	public List<SousCategories> findAll() {
		return sousCategories = sousCategotiesEJBLocal.findAll();
	}

	public void update() {
		sousCategotiesEJBLocal.update(selectedSousCategories);
	}

	public void delete() {
		sousCategotiesEJBLocal.remove(selectedSousCategories);
	}

	public void add() {
		sousCategorie.setCategories(categoriesLocal.findById(idCat));
		sousCategotiesEJBLocal.add(sousCategorie);
		sousCategorie = new SousCategories();
	}

	public SousCategories getSousCategorie() {
		return sousCategorie;
	}

	public void setSousCategorie(SousCategories sousCategorie) {
		this.sousCategorie = sousCategorie;
	}

	public SousCategories getSelectedSousCategories() {
		return selectedSousCategories;
	}

	public void setSelectedSousCategories(SousCategories selectedSousCategories) {
		this.selectedSousCategories = selectedSousCategories;
	}

	public List<SousCategories> getSousCategories() {
		return sousCategories;
	}

	public void setSousCategories(List<SousCategories> sousCategories) {
		this.sousCategories = sousCategories;
	}

	public int getIdCat() {
		return idCat;
	}

	public void setIdCat(int idCat) {
		this.idCat = idCat;
	}

}
