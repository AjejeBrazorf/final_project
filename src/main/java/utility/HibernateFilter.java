package utility;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateFilter implements Filter {
	
	private SessionFactory sf = HibernateUtil.getSessionFactory();

	
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {		
		if (request instanceof HttpServletRequest) {
			Session s=sf.getCurrentSession();
			Transaction tx=null;
			try {
				tx=s.beginTransaction();
				//System.out.println("Prima chain");
				chain.doFilter(request, response);
				//System.out.println("Dopo chain");
				tx.commit();
				//System.out.println("Commit");
			} catch (Throwable ex) {
				if (tx!=null) tx.rollback();
				throw new ServletException(ex);
			} finally {
				if (s!=null && s.isOpen()) s.close(); 
				s=null;
			}
		}
	}


	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
