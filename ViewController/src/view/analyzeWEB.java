package view;

import java.io.IOException;
import java.io.PrintWriter;

import java.lang.reflect.Method;

import java.net.URL;

import javax.servlet.*;
import javax.servlet.http.*;

public class analyzeWEB extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Class loader analysis</title></head>");
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
        ClassLoader cl = this.getClass().getClassLoader();
        out.println("<p>");
        while (cl != null) {
            out.println("Cl: " + cl + "</br>");
            cl = cl.getParent();
        }
        out.println("</p>");
        
        out.println("<h1>Context class loaders</h1>");
        //ClassLoader cl2 = this.getClass().getClassLoader();
        ClassLoader cl2 = Thread.currentThread().getContextClassLoader();
        out.println("<p>");
        while (cl2 != null) {
            out.println("Cl: " + cl2 + "</br>");
            cl2 = cl2.getParent();
        }
        out.println("</p>");
        
        out.println("<h1>Classloader finder</h1>");
        cl = this.getClass().getClassLoader();
        while (cl != null) {
            out.println("<h2>" + "Cl: " + cl + "</h2>");
            if (cl.getClass().getName().contains("ChangeAwareClassLoader") ||
                cl.getClass().getName().contains("FilteringClassLoader") ||
                cl.getClass().getName().contains("GenericClassLoader")) {
                Method method;
                try {
                    method = cl.getClass().getMethod("getFinderClassPath");
                    String classpath[] = ((String) method.invoke(cl)).split(":");
                    out.println("<p>");
                    for(String cpEntry : classpath) {
                        out.println(cpEntry+"</br>" );
                    }
                    out.println("</p>");
                    
                    
                } catch (Throwable e) {
                    out.println("<p>" + "error geting classpath");
                }
            }
            cl = cl.getParent();
        }
        
        out.println("<h1>classpath</h1>");
        cl = this.getClass().getClassLoader();
        while (cl != null) {
            out.println("<h2>" + "Cl: " + cl + "</h2>");
            if (cl.getClass().getName().contains("ChangeAwareClassLoader") ||
                cl.getClass().getName().contains("FilteringClassLoader") ||
                cl.getClass().getName().contains("GenericClassLoader")) {
                //System.out.println("Classpath:");
                Method method;
                try {
                    method = cl.getClass().getMethod("getClassPath");
                    String classpath[] = ((String) method.invoke(cl)).split(":");
                    out.println("<p>");
                    for(String cpEntry : classpath) {
                        out.println(cpEntry+"</br>" );
                    }
                    out.println("</p>");
                    
                    
                } catch (Throwable e) {
                    out.println("<p>" + "error geting classpath");
                }
            }
            cl = cl.getParent();
        }

        out.println("<h1>Methods</h1>");
        cl = this.getClass().getClassLoader();
        while (cl != null) {
            out.println("<h2>" + "Cl: " + cl + "</h2>");
            if (cl.getClass().getName().contains("ChangeAwareClassLoader") ||
                cl.getClass().getName().contains("FilteringClassLoader") ||
                cl.getClass().getName().contains("GenericClassLoader")) {
                Method[] methods = cl.getClass().getMethods();
                out.println("<p>");
                for (Method method : methods) {
                    out.println( "- " + method + "</br>");
                }
                out.println("</p>");
            }
            cl = cl.getParent();
        }
        
        out.println("<h1>References</h1>");
        out.println("<p>Class loader analysis idea taken from:<a href='http://salzotech.blogspot.ch/2013/08/inspecting-weblogic-classloaders.html'>http://salzotech.blogspot.ch/2013/08/inspecting-weblogic-classloaders.html</a></p>");
        out.println("</body></html>");
        out.close();
    }


    public String getResource(String fileName) {

        //ClassLoader tcl = Thread.currentThread().getContextClassLoader();
        URL turla = this.getClass().getResource(fileName);

        String result;
        if (turla == null)
            result = "(none)";
        else
            result = turla.toString();

        return result;
    }
    
    @SuppressWarnings("oracle.jdeveloper.java.insufficient-catch-block")
    public String getClass(String fileName) {

        //ClassLoader tcl = Thread.currentThread().getContextClassLoader();
        Class clazz = null;
        String exception = "";
        try {
            clazz = Class.forName(fileName);
        } catch (ClassNotFoundException e) {
            exception = e.toString();
        }

        String result;
        if (clazz == null) {
            result = "("+ exception +")";
        } else {
            if (clazz.getClassLoader() == null)
                result = "(bootstrap)";
            else
                result = clazz.getClassLoader().toString() ;
        }
        
        return result;
    }
    
    private void analyzeClass(PrintWriter out, String _clazzName) {
        out.println("<b>" + _clazzName + "</b>");
        out.println("Cl: " + getClass(_clazzName) + "</br>");
    }
    
    private void analyzeResource(PrintWriter out, String _resName) {
        out.println("<b>" + _resName + "</b>");
        out.println("Res: " + getResource(_resName) + "</br>");
    }
    
}
