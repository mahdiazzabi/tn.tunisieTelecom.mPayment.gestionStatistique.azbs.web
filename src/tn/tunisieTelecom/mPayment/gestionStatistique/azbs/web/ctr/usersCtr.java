package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Admin;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Employees;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.EmployeesMpayement;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.User;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.DepartementEJBLocal;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.UserEJBLocal;

@ManagedBean
@SessionScoped
public class usersCtr {
	private List<User> users = new ArrayList<User>();
	private User user = new User();
	private Admin admin = new Admin();
	private Employees employees = new Employees();
	private EmployeesMpayement employeesMpayement = new EmployeesMpayement();
	private User selectedUser = new User();
	private int idDep;

	@EJB
	UserEJBLocal userEjbLocal;
	@EJB
	DepartementEJBLocal departementEJBLocal;

	public List<User> findAll() {
		return users = userEjbLocal.findAll();
	}

	public String addEmployee() {
		employees.setDepartement(departementEJBLocal.findById(idDep));
		try {
			userEjbLocal.add(employees);

		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_FATAL,
									"Attention : Violation d'une contrainte d'integrité : Login deja utilisé ",
									"L'utilisateur selectionné ne peut pas être supprimé : Violation d'une contrainte d'integrité "));
			selectedUser = new User();
			employees = new Employees();
			idDep = 1;

			return "";
		}
		employees = new Employees();
		selectedUser = new User();
		idDep = 1;
		return "/admin/users.jsf?faces-redirect=true";
	}

	public String addAdmin() {
		admin.setDepartement(departementEJBLocal.findById(idDep));
		System.err
				.println("ok setdep" + "  " + admin.getDepartement().getNom());
		try {
			userEjbLocal.add(admin);
		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_FATAL,
									"Attention : Violation d'une contrainte d'integrité : Login deja utilisé ",
									"L'utilisateur selectionné ne peut pas être supprimé : Violation d'une contrainte d'integrité "));
			selectedUser = new User();
			admin = new Admin();
			idDep = 1 ;
			return "";
		}
		admin = new Admin();
		idDep = 1;
		selectedUser = new User();
		return "/admin/users.jsf?faces-redirect=true";
	}

	public String addemployeMP() {
		employeesMpayement.setDepartement(departementEJBLocal.findById(idDep));
		try {

			userEjbLocal.add(employeesMpayement);
		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_FATAL,
									"Attention : Violation d'une contrainte d'integrité : Login deja utilisé ",
									"L'utilisateur selectionné ne peut pas être supprimé : Violation d'une contrainte d'integrité "));
			selectedUser = new User();
			employeesMpayement = new EmployeesMpayement();
			idDep =1;
			return "";

		}
		employeesMpayement = new EmployeesMpayement();
		selectedUser = new User();

		return "/admin/users.jsf?faces-redirect=true";
	}

	public String update() {
		try {
			userEjbLocal.update(selectedUser);

		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_FATAL,
									"Attention : L'utilisateur selectionné ne peut pas être modifié : Violation d'une contrainte d'integrité : Login deja utilisé ",
									"L'utilisateur selectionné ne peut pas être supprimé : Violation d'une contrainte d'integrité "));
			return "";
		}

		selectedUser = new User();
		return "/admin/users.jsf?faces-redirect=true";
	}

	public String delete() {
		try {
			if (selectedUser.getId() == 0) {
				FacesContext
				.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(
								FacesMessage.SEVERITY_WARN,
								"Attention : Vous devez selectionner un utilisateur. ",
								"Vous devez selectionner un utilisateur. "));
		
				return "" ;	
			}

			userEjbLocal.delete(selectedUser);

		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_FATAL,
									"Attention : L'utilisateur selectionné ne peut pas être supprimé : Violation d'une contrainte d'integrité ",
									"L'utilisateur selectionné ne peut pas être supprimé : Violation d'une contrainte d'integrité "));
			return "";
		}
		selectedUser = new User();
		
		return "/admin/users.jsf?faces-redirect=true";
	}
	public String BackIndex() {
		return "/admin/gestionBase?faces-redirect=true";
	}
	public String pageAddNewUser() {
		return "/admin/addUser.jsf?faces-redirect=true";
	}

	public String pageUsers() {
		selectedUser = new User();
		return "/admin/users.jsf?faces-redirect=true";
	}

	public String pageAddNewAdmin() {
		return "/admin/addAdmin.jsf?faces-redirect=true";
	}

	public String pageAddNewEmplyeesMP() {
		return "/admin/addemployeesMP.jsf?faces-redirect=true";
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Employees getEmployees() {
		return employees;
	}

	public User getSelectedUser() {
		try {
			return selectedUser;
		} catch (Exception e) {
			selectedUser = new User();
			return selectedUser;
		}

	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public void setEmployees(Employees employees) {
		this.employees = employees;
	}

	public int getIdDep() {
		return idDep;
	}

	public void setIdDep(int idDep) {
		this.idDep = idDep;
	}

	public EmployeesMpayement getEmployeesMpayement() {
		return employeesMpayement;
	}

	public void setEmployeesMpayement(EmployeesMpayement employeesMpayement) {
		this.employeesMpayement = employeesMpayement;
	}

}
