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
public class UsersCtr {
	private List<User> users = new ArrayList<User>();
	private User user = new User();
	private Admin admin = new Admin();
	private Employees employees = new Employees();
	private EmployeesMpayement employeesMpayement = new EmployeesMpayement();
	private User selectedUser = new User() ;
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
		userEjbLocal.add(employees);
		employees = new Employees();
		return "/admin/users.jsf?faces-redirect=true";
	}

	public String addAdmin() {
		admin.setDepartement(departementEJBLocal.findById(idDep));
		System.err.println("ok setdep"+ "  "+ admin.getDepartement().getNom());
		
		userEjbLocal.add(admin);
		admin = new Admin();
		return "/admin/users.jsf?faces-redirect=true";
	}
	public String addemployeMP() {
		employeesMpayement.setDepartement(departementEJBLocal.findById(idDep));
		userEjbLocal.add(employeesMpayement);
		employeesMpayement=new EmployeesMpayement();
		return "/admin/users.jsf?faces-redirect=true";
	}
	public String update(){
		
		userEjbLocal.update(selectedUser);
				
		selectedUser = new User();
		return "/admin/users.jsf?faces-redirect=true";
	}
	public String delete(){
		userEjbLocal.delete(selectedUser);
		selectedUser = new User();
		return "/admin/users.jsf?faces-redirect=true";
	}

	public String pageAddNewUser() {
		return "/admin/addUser.jsf?faces-redirect=true";
	}

	public String pageUsers() {
		return "/admin/users.jsf?faces-redirect=true";
	}
	
	public String pageAddNewAdmin(){
		return "/admin/addAdmin.jsf?faces-redirect=true";
	}

	public String pageAddNewEmplyeesMP(){
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
		return selectedUser;
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
