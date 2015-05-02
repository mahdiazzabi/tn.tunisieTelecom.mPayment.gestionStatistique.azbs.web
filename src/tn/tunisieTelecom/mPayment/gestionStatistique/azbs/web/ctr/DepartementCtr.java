package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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

	public void add() {
		departementEJBLocal.add(departement);
		departement = new Departement();
	}

	public void update() {
		departementEJBLocal.update(selectedDepartement);
	}

	public void delete() {
		departementEJBLocal.delete(selectedDepartement);
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

}
