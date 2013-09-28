package org.datafx.reader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import org.datafx.provider.ObjectDataProvider;

/**
 *
 * @author johan
 */
public class SSERestSource<T> extends RestSource<T> implements ServerSentEventReader {

    private BufferedReader br;
    private T answer;
    private Map<String, Field> observableFields = new HashMap<>();

    @Override
    public T get() {
        if (!requestMade) {
            try {
                InputStream is = createInputStream();
                this.br = new BufferedReader(new InputStreamReader(is));
                answer = processLine(br);
                checkObservableFields(answer);
                return answer;
            } catch (IOException ex) {
                Logger.getLogger(SSERestSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private T processLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        while (line.isEmpty()) {
            line = br.readLine().trim();
        }
        if (line.startsWith("data:")) {
            String data = line.substring(5);
            getConverter().initialize(new ByteArrayInputStream(data.getBytes()));
            T updated = getConverter().get();
            return updated;
        }
        return null;
    }

    @Override
    public void keepReading() {
        try {
            T updated = processLine(br);
            while (updated != null) {
                updated = processLine(br);
                updateFields(updated);
            }
            T obj = getConverter().get();
        } catch (IOException ex) {
            Logger.getLogger(SSERestSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateFields(final T newValues) {
        for (final Field field : observableFields.values()) {
            try {
                AccessController.doPrivileged(new PrivilegedAction() {
                    @Override
                    public Object run() {
                        try {
                            final ObservableValue val = (ObservableValue) field.get(newValues);
                            final WritableValue obs = (WritableValue) field.get(answer);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    obs.setValue(val.getValue());
                                }
                            });

                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(SSERestSource.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(SSERestSource.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return null;
                    }
                });



            } catch (IllegalArgumentException ex) {
                Logger.getLogger(SSERestSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void checkObservableFields(final T target) {
        Class c = target.getClass();
        Field[] fields = c.getDeclaredFields();
        for (final Field field : fields) {
            Class clazz = field.getType();
            if (Observable.class.isAssignableFrom(clazz)) {
                try {
                    Observable observable = AccessController.doPrivileged(new PrivilegedAction<Observable>() {
                        public Observable run() {
                            try {

                                field.setAccessible(true);
                                Object f = field.get(target);
                                Observable answer = (Observable) f;
                                return answer;
                            } catch (IllegalArgumentException ex) {
                                Logger.getLogger(ObjectDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(ObjectDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return null;
                        }
                    });
                    if (observable != null) {
                        observableFields.put(field.getName(), field);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
