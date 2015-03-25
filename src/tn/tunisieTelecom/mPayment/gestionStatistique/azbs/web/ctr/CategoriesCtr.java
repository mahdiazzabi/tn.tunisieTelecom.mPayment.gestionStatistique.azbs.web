package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Categories;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.CategoriesLocal;

@ManagedBean
@SessionScoped
public class CategoriesCtr {
	private List<Categories> categories = new ArrayList<Categories>();
	private Categories setectedCategories;
	private Categories categorie = new Categories();
	@EJB
	CategoriesLocal categoriesLocal;

	public List<Categories> listCategories() {
		return categories = categoriesLocal.findAll();
	}

	public void deleteCat() {
		categoriesLocal.delete(setectedCategories);
	}

	public void updateCat() {
		categoriesLocal.update(setectedCategories);
	}

	public void add() {
		categoriesLocal.add(categorie);
		categorie = new Categories();
	}

	public Categories getCategorie() {
		return categorie;
	}

	public void setCategorie(Categories categorie) {
		this.categorie = categorie;
	}

	public Categories getSetectedCategories() {
		return setectedCategories;
	}

	public void setSetectedCategories(Categories setectedCategories) {
		this.setectedCategories = setectedCategories;
	}

	public List<Categories> getCategories() {
		return categories;
	}

	public void setCategories(List<Categories> categories) {
		this.categories = categories;
	}

}
