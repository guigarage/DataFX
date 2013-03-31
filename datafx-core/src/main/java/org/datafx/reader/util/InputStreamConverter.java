/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.datafx.reader.util;

import java.io.InputStream;

/**
 *
 * @author johan
 */
public abstract class InputStreamConverter<T> implements Converter<T, InputStream> {
   
  //  protected InputStream is;
    
//    public void setInputStream (InputStream is) {
//        this.is = is;
//    }
    
    public abstract boolean hasMoreData(InputStream is) ;
    
}
