package com.login;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.persistence.*;


@WebServlet("/querylogin1")
public class ServletLIJPA extends HttpServlet {
private static final long serialVersionUID = 1L;
private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("CredentialDB");

  protected void doPost(HttpServletRequest request,HttpServletResponse response)  throws ServletException, IOException {
  
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;

    try {
    
        String email = request.getParameter("email");
        String password = request.getParameter("psw");    
    
        // Get a transaction
        transaction = manager.getTransaction();
        // Begin the transaction
        transaction.begin();
        
        String stringQuery = "SELECT count(c) FROM Credential c where c.email= '"+email+"' and c.password= '"+password+"'";
        Query query = manager.createQuery(stringQuery);
        int size = ((Number) query.getSingleResult()).intValue();      
        
        transaction.commit();
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Log In Response</title></head>");
        out.println("<body>");
        
        if(size> 0) {
        	out.println("<h3>Thank you for logging in.</h3>");
        	response.setHeader("Refresh", "5; URL=index.html");

        } else {
        	out.println("<h3>Wrong Password or no such user exists. Click <a href=\"signup.html\">Here</a> to sign up or click <a href=\"login.html\">Here</a> to try again.</h3>");

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
