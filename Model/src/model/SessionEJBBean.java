package model;

import java.lang.reflect.Method;

import java.net.URL;

import java.util.LinkedList;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

@Stateless(name = "SessionEJB", mappedName = "ClassLoaderTest-Model-SessionEJB")
public class SessionEJBBean implements SessionEJB, SessionEJBLocal {
    @Resource
    SessionContext sessionContext;

    public SessionEJBBean() {
    }


    public LinkedList<String> getClassLoaders() {
        LinkedList<String> result = new LinkedList<String>();

        ClassLoader cl = this.getClass().getClassLoader();
        while (cl != null) {
            String classStr = cl.toString();
            System.out.println(classStr);
            result.add(classStr);
            cl = cl.getParent();
        }

        return result;
    };
    
    public LinkedList<String> getContextClassLoaders() {
        LinkedList<String> result = new LinkedList<String>();

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        while (cl != null) {
            String classStr = cl.toString();
            System.out.println(classStr);
            result.add(classStr);
            cl = cl.getParent();
        }

        return result;
    };

    public LinkedList<String> getClassLoaderFinder(String _classloader) {
        LinkedList<String> result = new LinkedList<String>();

        ClassLoader cl = this.getClass().getClassLoader();

        while (cl != null) {
            System.out.println(cl.toString() + " vs. " + _classloader);
            if (cl.toString().equals(_classloader)) {
                if (cl.getClass().getName().contains("ChangeAwareClassLoader") ||
                    cl.getClass().getName().contains("FilteringClassLoader") ||
                    cl.getClass().getName().contains("GenericClassLoader")) {
                    Method method;
                    try {
                        method = cl.getClass().getMethod("getFinderClassPath");
                        String classpath[] = ((String) method.invoke(cl)).split(":");

                        for (String cpEntry : classpath) {
                            result.add(cpEntry);
                        }
                    } catch (Throwable e) {
                        result.add("(error)");
                    }
                }
            }
            cl = cl.getParent();
        }
        return result;
    }

    public LinkedList<String> getClassPath(String _classloader) {
        LinkedList<String> result = new LinkedList<String>();

        ClassLoader cl = this.getClass().getClassLoader();

        while (cl != null) {
            System.out.println(cl.toString() + " vs. " + _classloader);
            if (cl.toString().equals(_classloader)) {
                if (cl.getClass().getName().contains("ChangeAwareClassLoader") ||
                    cl.getClass().getName().contains("FilteringClassLoader") ||
                    cl.getClass().getName().contains("GenericClassLoader")) {
                    Method method;
                    try {
                        method = cl.getClass().getMethod("getClassPath");
                        String classpath[] = ((String) method.invoke(cl)).split(":");

                        for (String cpEntry : classpath) {
                            result.add(cpEntry);
                        }
                    } catch (Throwable e) {
                        result.add("(error)");
                    }
                }
            }
            cl = cl.getParent();
        }
        return result;
    }

    public LinkedList<String> getClassMethods(String _classloader) {
        LinkedList<String> result = new LinkedList<String>();

        ClassLoader cl = this.getClass().getClassLoader();

        while (cl != null) {
            System.out.println(cl.toString() + " vs. " + _classloader);
            if (cl.toString().equals(_classloader)) {
                if (cl.getClass().getName().contains("ChangeAwareClassLoader") ||
                    cl.getClass().getName().contains("FilteringClassLoader") ||
                    cl.getClass().getName().contains("GenericClassLoader")) {

                    Method[] methods = cl.getClass().getMethods();
                    for (Method method : methods) {
                        result.add(method.toGenericString());
                    }

                }
            }
            cl = cl.getParent();
        }
        return result;
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
    
    public String getResponse(String _param) {
        String result;

        result = "Echo:" + _param;

        return result;
    }
}
