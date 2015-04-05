package tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.filtre;

import java.io.IOException;

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

import org.apache.commons.io.filefilter.TrueFileFilter;

import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.ejb.entity.Admin;
import tn.tunisieTelecom.mPayment.gestionStatistique.azbs.web.ctr.UserCtr;

/**
 * Servlet Filter implementation class UserAfterLogin
 */
//@WebFilter(asyncSupported = true, urlPatterns = { "/admin/*" })
public class UserAfterLogin implements Filter {

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

//		boolean letGo = true;
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse resp = (HttpServletResponse) response;
//		UserCtr userCtr = (UserCtr) req.getSession().getAttribute("UserCtr");
//		if (userCtr.isLoggedIn()) {
//			letGo = true;
//		}
//		if (letGo) {
//			chain.doFilter(request, response);
//		} else {
//			resp.sendRedirect(req.getContextPath() + "/index.jsf");
//		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
