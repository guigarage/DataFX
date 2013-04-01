package org.datafx.reader;

import java.io.InputStream;
import org.datafx.reader.util.InputStreamConverter;

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

    public void setInputStream(InputStream is) {
        this.is = is;
    }

    public T getData() {
        if (isSingle()) {
            T answer = converter.convert(is);
            return answer;
        } else {
            T answer = converter.next(is);
            return answer;
        }
    }

    public boolean hasMoreData() {
        System.out.println("HMD called, conv = "+converter+", is = "+is);
        return converter.hasMoreData(is);
    }

    /**
     * @param converter the converter to set
     */
    public void setConverter(InputStreamConverter<T> converter) {
        this.converter = converter;
    }
}
