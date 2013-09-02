package org.datafx.reader;

import java.io.InputStream;

import org.datafx.reader.converter.InputStreamConverter;

/**
 *
 * Abstract class for functionality common across datareaders that rely on
 * InputStreams. Common functionality includes the handling of a
 * Converting, translating the inputstream data into Object(s)
 * @author johan
 */
public abstract class InputStreamDataReader<T> extends AbstractDataReader<T> {

    private InputStream is;
    private InputStreamConverter<T> converter;
    
    public InputStreamDataReader () {}
    
    public InputStreamDataReader(final InputStreamConverter<T> converter) {
        this.converter = converter;
    }
    
    public InputStreamDataReader(final InputStream is, final InputStreamConverter<T> converter) {
        this.converter = converter;
        setInputStream(is);
    }

    public void setConverter (InputStreamConverter<T> converter) {
        this.converter = converter;
    }
    
    public InputStreamConverter<T> getConverter() {
        return this.converter;
    }
    
    public void setInputStream(InputStream is) {
        this.is = is;
        if (converter != null) {
            converter.initialize(is);
        }
    }

    @Override public T get() {
        return converter.get();
    }

    @Override public boolean next() {
        return converter.next();
    }
}
