package com.login;

// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import javax.persistence.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@WebServlet("/querysignup1")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class ServletSUJPA extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("CredentialDB");

	  protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
	  
	    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
	    EntityTransaction transaction = null;
	    
	    response.setContentType("text/html");
	      // Get a output writer to write the response message into the network socket
	    PrintWriter out = response.getWriter();
	      
	      // Print an HTML page as the output of the query
	    out.println("<html>");
	    out.println("<head><title>Sign Up Response</title></head>");
	    out.println("<body>");

	    try {
	    //get parameter values and start a transaction
	        String email = request.getParameter("email");
	        String password = request.getParameter("psw"); 
	        String password_repeat = request.getParameter("psw-repeat");
	        
	        if (!password.equals(password_repeat)){
	        	out.println("<p>Passwords must match.</p>");
	         	response.setHeader("Refresh", "5; URL=signup.html");

	        }
	        else {
	    
		        transaction = manager.getTransaction();
		        transaction.begin();
		        
		        //create a query
		        String stringQuery = "SELECT count(c) FROM Credential c where c.email= '"+email+"'";
		        Query query = manager.createQuery(stringQuery);
		        int size = ((Number) query.getSingleResult()).intValue();      
		        
		        transaction.commit();
		        
		        if(size> 0) {
		        	out.println("<p>This email is already been used.</p>");
		         	response.setHeader("Refresh", "5; URL=signup.html");
		        } else {
		          transaction = manager.getTransaction();
		          transaction.begin();        
		           
		          // Create a new credential object and save it
		          Credential user= new Credential();
		          user.setPassword(password);
		          user.setEmail(email);
	
		          manager.persist(user);
		          transaction.commit();
		          out.println("<p> Signed up succefully. Please log in.</p>");
		          response.setHeader("Refresh", "5; URL=login.html");
	
		        }
	        }
	        out.println("</body></html>");
	        out.close();
	    } catch (Exception ex) {
	        // If there are any exceptions, roll back the changes
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        // Print the Exception
	        ex.printStackTrace();
	    } finally {
	        // Close the EntityManager
	        manager.close();
	    }
	  }
	}
