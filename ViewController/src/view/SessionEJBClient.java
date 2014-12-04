package view;

import java.util.Hashtable;

import java.util.LinkedList;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import model.SessionEJB;

public class SessionEJBClient {
    public static void main(String[] args) {
        try {
            final Context context = getInitialContext();
            SessionEJB sessionEJB = (SessionEJB) context.lookup("ClassLoaderTest-Model-SessionEJB#model.SessionEJB");
            
            System.out.println("********************************************");
            System.out.println("Classloaders");
            System.out.println("********************************************");
            LinkedList<String> cls = sessionEJB.getClassLoaders();
            for(String cpEntry : cls) {
                System.out.println(cpEntry);
            }
            
            ///
            System.out.println("********************************************");
            System.out.println("Classloaders' finder");
            System.out.println("********************************************");
            cls = sessionEJB.getClassLoaders();
            for(String cpEntry : cls) {
                System.out.println("---------------------------------------------");
                System.out.println(cls);
                System.out.println("---------------------------------------------");
                LinkedList finders = sessionEJB.getClassLoaderFinder(cpEntry);
                for(Object fndEntry : finders) {
                    System.out.println(fndEntry);
                }
            }

            ///
            System.out.println("********************************************");
            System.out.println("Classloaders' classpath");
            System.out.println("********************************************");
            cls = sessionEJB.getClassLoaders();
            for(String cpEntry : cls) {
                System.out.println("---------------------------------------------");
                System.out.println(cls);
                System.out.println("---------------------------------------------");
                LinkedList paths = sessionEJB.getClassPath(cpEntry);
                for(Object pthEntry : paths) {
                    System.out.println(pthEntry);
                }
            }
            
            
            ///
            System.out.println("********************************************");
            System.out.println("Classloaders' method");
            System.out.println("********************************************");
            cls = sessionEJB.getClassLoaders();
            for(String cpEntry : cls) {
                System.out.println("---------------------------------------------");
                System.out.println(cls);
                System.out.println("---------------------------------------------");
                LinkedList paths = sessionEJB.getClassMethods(cpEntry);
                for(Object pthEntry : paths) {
                    System.out.println(pthEntry);
                }
            }
            
            
        } catch (CommunicationException ex) {
            System.out.println(ex.getClass().getName());
            System.out.println(ex.getRootCause().getLocalizedMessage());
            System.out.println("\n*** A CommunicationException was raised.  This typically\n*** occurs when the target WebLogic server is not running.\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Context getInitialContext() throws NamingException {
        Hashtable env = new Hashtable();
        // WebLogic Server 10.x/12.x connection details
        env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        env.put(Context.PROVIDER_URL, "t3://10.37.129.3:7001");
        return new InitialContext(env);
    }
}
