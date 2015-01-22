/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
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
 * DISCLAIMED. IN NO EVENT SHALL DataFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.messages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used to define message receivers. A method that is marked with this annotation will be called
 * whenever the {@link io.datafx.messages.MessageBus} sends a message for the given adress ({@link #value()} of the annotation).
 * The methods needs a parameter of the message content type, otherwise a {@link java.lang.RuntimeException} will thrown
 * in the {@link io.datafx.messages.MessageBus} (this don't stop the transmission to other receivers of the message).
 * In addition to an message a {@link java.util.function.Consumer} field can be annotated with {@link io.datafx.messages.OnMessage}.
 * The consumer will be called whenever the message bus transmits a new message.
 * Normally the method / consumer will be called on the JavaFX Application Thread. If the message should be received on
 * a background thread the {@link io.datafx.core.concurrent.Async} annotation should be used in combination with
 * the {@link io.datafx.messages.OnMessage} annotation. In this case the message bus will call the method / consumer
 * on a background thread.
 *
 * The annotation will automatically work in all view controls that are managed by the DataFX {@link io.datafx.controller.flow.Flow} API
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface OnMessage {

    String value() default "";
}
