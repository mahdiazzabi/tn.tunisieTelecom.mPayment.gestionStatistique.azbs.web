package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.filtre;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.filefilter.TrueFileFilter;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Admin;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Employees;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.User;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr.UserCtr;

/**
 * Servlet Filter implementation class UserAfterLogin
 */
// @WebFilter(asyncSupported = true, urlPatterns = { "/admin/*.jsf" })
@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.jsf" })
public class UserAfterLogin implements Filter {

	/**
	 * 
	 */
	/**
	 * Default constructor.
	 */
		public UserAfterLogin() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession ses = req.getSession(false);
			String reqURI = req.getRequestURI();
			if (reqURI.indexOf("/index0.jsf") >= 0
					|| (ses != null && ses.getAttribute("currentUser") != null)
					|| reqURI.contains("javax.faces.resource"))
			{	chain.doFilter(request, response);
			}else {
				res.sendRedirect(req.getContextPath() + "/index0.jsf");
			
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

	}

}
