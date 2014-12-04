package model;

import java.util.LinkedList;

import javax.ejb.Remote;

@Remote
public interface SessionEJB {
    public LinkedList<String> getClassLoaders();
    public LinkedList<String> getContextClassLoaders();
    public LinkedList<String> getClassLoaderFinder(String _classloader);
    public LinkedList<String> getClassPath(String _classloader);
    public LinkedList<String> getClassMethods(String _classloader);
    public String getResource(String fileName);
    public String getClass(String fileName);
    public String getResponse(String _param);
}
