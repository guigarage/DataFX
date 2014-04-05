/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
import javafx.beans.value.WritableListValue;
import javafx.beans.value.WritableValue;
import javafx.collections.ObservableList;
import org.datafx.provider.ObjectDataProvider;

/**
 *
 * @author johan
 */
public class SSERestSource<T> extends RestSource<T> implements ServerSentEventReader {

    private BufferedReader br;
    private T answer;
    private Map<String, Field> observableFields = new HashMap<>();
    private Map<String, Field> observableListFields = new HashMap<>();

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
                                    try {
                                        obs.setValue(val.getValue());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (Exception ex) {
                            Logger.getLogger(SSERestSource.class.getName()).log(Level.SEVERE, null, ex);
                            ex.printStackTrace();
                        }
                        return null;
                    }
                });



            } catch (IllegalArgumentException ex) {
                Logger.getLogger(SSERestSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (final Field field : observableListFields.values()) {
            try {
                AccessController.doPrivileged(new PrivilegedAction() {
                    @Override
                    public Object run() {
                        try {
                            final ObservableList val = (ObservableList) field.get(newValues);
                            final ObservableList obs = (ObservableList) field.get(answer);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        obs.setAll(val);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (Exception ex) {
                            Logger.getLogger(SSERestSource.class.getName()).log(Level.SEVERE, null, ex);
                            ex.printStackTrace();
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
            if (ObservableValue.class.isAssignableFrom(clazz)) {
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

              if (ObservableList.class.isAssignableFrom(clazz)) {
                try {
                    ObservableList observableList = AccessController.doPrivileged(new PrivilegedAction<ObservableList>() {
                        public ObservableList run() {
                            try {
                                field.setAccessible(true);
                                Object f = field.get(target);
                                ObservableList answer = (ObservableList) f;
                                return answer;
                            } catch (IllegalArgumentException ex) {
                                Logger.getLogger(ObjectDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(ObjectDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return null;
                        }
                    });
                    if (observableList != null) {
                        observableListFields.put(field.getName(), field);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            
            
        }
    }
}
