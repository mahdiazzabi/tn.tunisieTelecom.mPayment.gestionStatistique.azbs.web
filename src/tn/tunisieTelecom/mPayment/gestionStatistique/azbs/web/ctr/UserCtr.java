package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Admin;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Employees;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.User;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.local.services.UserEJBLocal;

@ManagedBean
@SessionScoped
public class UserCtr {
	private String login;
	private String mdp;
	private User user;
	private boolean loggedIn = false;
	@EJB
	UserEJBLocal userEJBLocal;

	public String doLogin() {
		String redirecTo = "";
		if ((user = userEJBLocal.doLogin(login, mdp)) == null) {
			// redirecTo = "index.jsf?faces-redirect=true";
		} else {
			loggedIn = true;
			if (user instanceof Admin) {
				
				redirecTo = "/admin/index.jsf?faces-redirect=true";
			} else if ((user instanceof Employees)) {
				redirecTo = "/employees/index.jsf?faces-redirect=true";

			} else
				redirecTo = "/employeesMpayement/index.jsf?faces-redirect=true";

		}
		return redirecTo;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

}
