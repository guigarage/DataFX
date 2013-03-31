
package org.datafx.reader;

/**
 *
 * @author johan
 */
public abstract class AbstractDataReader<T> implements DataReader<T> {

    private boolean single;
    
    /**
     * @param isList the isList to set
     */
    public void setSingle(boolean v) {
        this.single = v;
    }
    
    public boolean isSingle() {
        return this.single;
    }
}
