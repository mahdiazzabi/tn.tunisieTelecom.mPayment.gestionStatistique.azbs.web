package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Categories;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Departement;
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

	public String BackIndex() {
		return "/admin/gestionBase?faces-redirect=true";
	}

	public List<SousCategories> findAll() {
		return sousCategories = sousCategotiesEJBLocal.findAll();
	}

	public String update() {
		try {

			if (selectedSousCategories.getId() == 0) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_WARN,
										"Attention : Vous devez selectionner une sous catégorie",
										"Vous devez selectionner une sous catégorie "));

				return "";
			}

			sousCategotiesEJBLocal.update(selectedSousCategories);
			return "/admin/sousCategories?faces-redirect=true";

		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_FATAL,
									"Attention : La sous catégorie selectionnée ne peut pas être modifiée : Violation d'une contrainte d'integrité ",
									"La sous catégorie selectionnée ne peut pas être supprimée : Violation d'une contrainte d'integrité "));
			return "";
		}

	}

	public String delete() {
		try {

			if (selectedSousCategories.getId() == 0) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_WARN,
										"Attention : Vous devez selectionner une sous catégorie",
										"Vous devez selectionner une sous catégorie "));

				return "";
			}

			sousCategotiesEJBLocal.remove(selectedSousCategories);
			return "/admin/sousCategories?faces-redirect=true";

		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_FATAL,
									"Attention : La sous catégorie selectionnée ne peut pas être supprimée : Violation d'une contrainte d'integrité ",
									"La sous catégorie selectionnée ne peut pas être supprimée : Violation d'une contrainte d'integrité "));
			return "";
		}

	}

	public String add() {
		sousCategorie.setCategories(categoriesLocal.findById(idCat));
		sousCategotiesEJBLocal.add(sousCategorie);
		sousCategorie = new SousCategories();
		return "/admin/sousCategories.jsf?faces-redirect=true";
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
