package view;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.LinkedList;

import javax.naming.Context;

import javax.naming.NamingException;

import javax.naming.ServiceUnavailableException;

import javax.servlet.*;
import javax.servlet.http.*;

import model.SessionEJB;


public class analyzeEJB extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    SessionEJB sessionEJB;
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            final Context context = SessionEJBClient.getInitialContext();
             sessionEJB = (SessionEJB) context.lookup("ClassLoaderTest-Model-SessionEJB#model.SessionEJB");
        } catch (NamingException e) {
            throw new ServletException(e);
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>analyzeEJB</title></head>");
        out.println("<body>");
        out.println("<a href='analyzeWEB'>analyzeWEB</a>");
        out.println("<a href='analyzeEJB'>analyzeEJB</a>");
        
        out.println("<h1>Loaded resource</h1>");
        analyzeResource(out, "/file1.txt");
        analyzeResource(out, "/fileA.txt");
        analyzeResource(out, "/fileZ.txt");
        analyzeResource(out, "/WebLogic_CMP_RDBMS.xml");
        analyzeResource(out, "/commonj/work/Work.class");
        analyzeResource(out, "/weblogic/Server.class");
        analyzeResource(out, "/java/lang/String");
        
        out.println("<h1>Loaded class</h1>");
        analyzeClass(out, "model.SessionEJBBean");
        analyzeClass(out, "commonj.work.Work");
        analyzeClass(out, "weblogic.Server");
        analyzeClass(out, "java.lang.String");
        
        
        out.println("<h1>Class loaders</h1>");
        LinkedList<String> cls = sessionEJB.getClassLoaders();
        out.println("<p>");
        out.println("</br>");
        for(String cpEntry : cls) {
            out.println("Cl: " + cpEntry + "</br>");
        }
        out.println("</p>");
        
        out.println("<h1>Context class loaders</h1>");
        LinkedList<String> ccls = sessionEJB.getClassLoaders();
        out.println("<p>");
        out.println("</br>");
        for(String cpEntry : ccls) {
            out.println("Cl: " + cpEntry + "</br>");
        }
        out.println("</p>");
        
        out.println("<h1>Classloader finder</h1>");
        for(String cpEntry : cls) {
            out.println("<h2>" + "Cl: " + cpEntry + "</h2>");
            LinkedList finders = sessionEJB.getClassLoaderFinder(cpEntry);
            for(Object fndEntry : finders) {
                out.println(fndEntry + "</br>");
            }
        }
        out.println("</p>");
        
        out.println("<h1>classpath</h1>");
        for(String cpEntry : cls) {
            out.println("<h2>" + "Cl: " + cpEntry + "</h2>");
            LinkedList finders = sessionEJB.getClassPath(cpEntry);
            for(Object fndEntry : finders) {
                out.println(fndEntry + "</br>");
            }
        }
        out.println("</p>");       
 
        out.println("<h1>Methods</h1>");
        for(String cpEntry : cls) {
            out.println("<h2>" + "Cl: " + cpEntry + "</h2>");
            LinkedList finders = sessionEJB.getClassMethods(cpEntry);
            for(Object fndEntry : finders) {
                out.println(fndEntry + "</br>");
            }
        }
        out.println("</p>");   
        
        
        out.println("<h1>References</h1>");
        out.println("<p>Class loader analysis idea taken from:<a href='http://salzotech.blogspot.ch/2013/08/inspecting-weblogic-classloaders.html'>http://salzotech.blogspot.ch/2013/08/inspecting-weblogic-classloaders.html</a></p>");
        out.println("</body></html>");
        out.close();
    }
    
    private void analyzeClass(PrintWriter out, String _clazzName) {
        out.println("<b>" + _clazzName + "</b>");
        out.println("Cl: " + sessionEJB.getClass(_clazzName) + "</br>");
    }
    
    private void analyzeResource(PrintWriter out, String _resName) {
        out.println("<b>" + _resName + "</b>");
        out.println("Res: " + sessionEJB.getResource(_resName) + "</br>");
    }
    
    
}
