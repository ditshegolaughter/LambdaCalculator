/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on Mar 26, 2005
 *
 * Author Ben Yu
 * ZBS
 */
package jfun.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This utility class includes miscellenous utility functions.
 * <p>
 * Zephyr Business Solutions Corp.
 *
 * @author Ben Yu
 *
 */
public class Misc {
  /**
   * An empty array.
   */
  public static final Object[] array0 = new Object[0];
  /**
   * Get the array type of a Class object.
   * getArrayType(int.class) returns int[].class.
   * @param ctype the component type.
   * @return the array type.
   */
  public static Class getArrayType(Class ctype){
    return Array.newInstance(ctype, 0).getClass();
  }

  /**
   * Get the human readable type name of a class.
   * for array type such as int[], it returns "int[]"
   * @param c the class object.
   * @return the array type string.
   */
  public static String getTypeName(Class c){
    if(c==null) return ""+c;
    if(c.isArray()){
      return getTypeName(c.getComponentType())+"[]";
    }
    else return c.getName();
  }

  /**
   * If the type is a primitive type, get the corresponding wrapper type.
   * @param t the type.
   * @return the wrapper type if t is primitive, t is returned otherwise.
   */
  public static Class getWrapperType(Class t) {
    final Object rt = primitives.get(t);
    return (rt == null) ? t : (Class) rt;
  }
  /**
   * Determines whether t2 is a wrapper type of t1.
   * @param t1 the first type.
   * @param t2 the second type.
   * @return true if t1 is primitive type and t2 is the wrapper type.
   */
  public static boolean isPrimitiveOf(Class t1, Class t2) {
    if (t1.isPrimitive()) {
      return t2.equals(primitives.get(t1));
    } else
      return false;
  }
  private static final HashMap primitives = new HashMap();
  static {
    primitives.put(int.class, Integer.class);
    primitives.put(long.class, Long.class);
    primitives.put(short.class, Short.class);
    primitives.put(byte.class, Byte.class);
    primitives.put(char.class, Character.class);
    primitives.put(boolean.class, Boolean.class);
    primitives.put(double.class, Double.class);
    primitives.put(float.class, Float.class);
  }
  /**
   * Reads all the constants value and put them in a map.
   * @param c the class.
   * @return the result map.
   */
  public static Map readConstants(Class c){
    final HashMap map = new HashMap();
    if(!Modifier.isPublic(c.getModifiers())) return map;
    final Field[] flds = c.getFields();
    for(int i=0; i<flds.length; i++){
      final Field fld = flds[i];
      final int mod = fld.getModifiers();
      if(Modifier.isStatic(mod) && Modifier.isFinal(mod)){
        try{
          map.put(fld.getName(), fld.get(null));
        }
        catch(IllegalAccessException e){
          throw new IllegalStateException(e.getMessage());
        }
      }
    }
    return map;
  }

  /**
   * Get the hashcode for an array of objects using value semantics.
   * @param arr the array.
   * @return the hashcode.
   */
  public static int getArrayHashcode(Object[] arr){
    int r = 0;
    for(int i=0; i<arr.length; i++){
      r = r*31+hashcode(arr[i]);
    }
    return r;
  }

  /**
   * Get the hashcode for an object. 0 is returned if obj is null.
   * @param obj the object.
   * @return the hashcode.
   */
  public static int hashcode(Object obj){
    return obj==null?0:obj.hashCode();
  }
  /**
   * Compares two objects. if o1==null, (o2==null) is returned.
   * @param o1 the first object.
   * @param o2 the second object.
   * @return true if equal.
   */
  public static boolean equals(Object o1, Object o2){
    return o1==null?o2==null:o1.equals(o2);
  }
  /**
   * Get the File object for the real path designated by
   * root/path when path is relative, 
   * or path when it is absolute or starts with a '/'
   * @param root the root.
   * @param path the path.
   * @return the real File.
   */
  public static File getAbsolutePath(final File root, 
      File path)throws IOException{
    if(root==null)
      return path;
    if(path.isAbsolute()){
      return path;
    }
    else{
      final String pathname = path.getPath();
      if(path==null || pathname.length()==0)
        return root;
      final char c = pathname.charAt(0);
      if(c == File.separatorChar)
        return path;
      return new File(root.getAbsolutePath()+File.separatorChar+
      (c==File.separatorChar?pathname.substring(1):pathname))
      .getCanonicalFile();
    }
  }

  /**
   * To read a property file from a class loader
   * into a Properties object.
   * @param loader the ClassLoader used to load resource.
   * @param resource the resource name.
   * @return the Properties object.
   * @throws IOException when loading fails.
   */
  public static Properties loadResourceProperties(ClassLoader loader, String resource)
  throws IOException{
    final Properties props = new Properties();
    final InputStream in = loader.getResourceAsStream(resource);
    if(in==null){
      throw new FileNotFoundException(resource);
    }
    try{
      props.load(in);
    }
    finally{
      try{
        in.close();
      }
      catch(Exception e){}
    }
    return props;
  }
  /**
   * To load properties from a properties file.
   * @param file the properties file.
   * @return the Properties object.
   * @throws IOException when io error happens.
   */
  public static Properties loadPropertiesFile(File file)
  throws IOException{
    final Properties props = new Properties();
    final InputStream in = new FileInputStream(file);
    try{
      props.load(in);
    }
    finally{
      try{
        in.close();
      }
      catch(Exception e){}
    }
    return props;
  }
  
}
