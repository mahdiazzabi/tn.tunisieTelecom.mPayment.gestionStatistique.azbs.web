package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

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
	private String ancienMdp;
	private String nouveauMdp;
	private String confirmMdp;

	@EJB
	UserEJBLocal userEJBLocal;

	public String doLogin() {
		String redirecTo = "";
		if ((user = userEJBLocal.doLogin(login, mdp)) == null) {
			// redirecTo = "index.jsf?faces-redirect=true";
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur ",
							"Login / password incorrect."));

		} else {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);

			if (user instanceof Admin) {
				session.setAttribute("currentUser", user);
				redirecTo = "/admin/index.jsf?faces-redirect=true";
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Successful",
						"Your message: " + user.getLogin()));

			} else if ((user instanceof Employees)) {
				session.setAttribute("currentUser", user);

				redirecTo = "/employees/index.jsf?faces-redirect=true";
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Successful",
						"Your message: " + user.getLogin()));

			} else {
				session.setAttribute("currentUser", user);
				redirecTo = "/employeesMpayement/transaction.jsf?faces-redirect=true";

			}

		}

		return redirecTo;
	}

	public String doLogout() {
		((HttpSession) FacesContext.getCurrentInstance().getExternalContext()

		.getSession(true)).invalidate();
		return "/index0.jsf?faces-redirect=true";
	}

	public void update_mdp() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		user = (User) session.getAttribute("currentUser");

		if (user.getPassword().equals(ancienMdp)) {
			if (nouveauMdp.equals(confirmMdp)) {
				user.setPassword(nouveauMdp);
				userEJBLocal.update(user);
				ancienMdp = "";
				nouveauMdp = "";
				confirmMdp = "";
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage("Successful", "Mot de passe modifié avec succés"));
				
			}else{
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur ",
								"Confirmation mot de passe incorrect."));
				ancienMdp = "";
				nouveauMdp = "";
				confirmMdp = "";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur ",
							"Ancien mot de passe incorrect."));
			ancienMdp = "";
			nouveauMdp = "";
			confirmMdp = "";

		}

	}
	public String BackHome() {
		return "/admin/index?faces-redirect=true";
	}
	public String getAncienMdp() {
		return ancienMdp;
	}

	public void setAncienMdp(String ancienMdp) {
		this.ancienMdp = ancienMdp;
	}

	public String getNouveauMdp() {
		return nouveauMdp;
	}

	public void setNouveauMdp(String nouveauMdp) {
		this.nouveauMdp = nouveauMdp;
	}

	public String getConfirmMdp() {
		return confirmMdp;
	}

	public void setConfirmMdp(String confirmMdp) {
		this.confirmMdp = confirmMdp;
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
