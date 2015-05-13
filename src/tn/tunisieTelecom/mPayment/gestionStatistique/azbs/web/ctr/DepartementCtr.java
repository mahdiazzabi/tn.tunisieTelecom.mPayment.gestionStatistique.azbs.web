package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Departement;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.DepartementEJBLocal;

@ManagedBean
@SessionScoped
public class DepartementCtr {
	private List<Departement> departements = new ArrayList<Departement>();
	private Departement departement = new Departement();
	private Departement selectedDepartement = new Departement();

	@EJB
	DepartementEJBLocal departementEJBLocal;

	public List<Departement> findAll() {
		return departements = departementEJBLocal.findAll();
	}

	public String add() {
		departementEJBLocal.add(departement);
		departement = new Departement();
		return "/admin/departement?faces-redirect=true";
	}

	public String update() {
		try {

			if (selectedDepartement.getId() == 0) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_WARN,
										"Attention : Vous devez selectionner un département",
										"Vous devez selectionner un département "));

				return "";
			}

			departementEJBLocal.update(selectedDepartement);
			selectedDepartement = new Departement();
			return "/admin/departement?faces-redirect=true";

		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_FATAL,
									"Attention : Le département selectionné ne peut pas être supprimé : Violation d'une contrainte d'integrité ",
									"Le département selectionné ne peut pas être supprimé : Violation d'une contrainte d'integrité "));
			return "";
		}

	}

	public String delete() {
		try {
			if (selectedDepartement.getId() == 0) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_WARN,
										"Attention : Vous devez selectionner un département",
										"Vous devez selectionner un département "));

				return "";
			}

			departementEJBLocal.delete(selectedDepartement);
			selectedDepartement = new Departement();
			return "/admin/departement?faces-redirect=true";

		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_FATAL,
									"Attention : Le département selectionné ne peut pas être supprimé : Violation d'une contrainte d'integrité ",
									"Le département selectionné ne peut pas être supprimé : Violation d'une contrainte d'integrité "));
			return "";
		}

	}

	public List<Departement> getDepartements() {
		return departements;
	}

	public void setDepartements(List<Departement> departements) {
		this.departements = departements;
	}

	public Departement getDepartement() {
		return departement;
	}

	public void setDepartement(Departement departement) {
		this.departement = departement;
	}

	public Departement getSelectedDepartement() {
		return selectedDepartement;
	}

	public void setSelectedDepartement(Departement selectedDepartement) {
		this.selectedDepartement = selectedDepartement;
	}

	public String BackIndex() {
		return "/admin/gestionBase?faces-redirect=true";
	}
}
