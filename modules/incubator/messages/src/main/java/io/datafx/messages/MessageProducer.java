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
 * Annotation to define a message producer. A message producer can be a method with a return value or a
 * {@link java.util.function.Supplier} field. The {@link io.datafx.messages.MessageProducer} annotation
 * should be used in combination with the {@link io.datafx.messages.MessageTrigger} annotation to trigger
 * the transmission of the supplied message. Once the transmission is triggered the annotated method / supplier
 * will be called and the return value will be send. Normally the message will be send on the JavaFX Application
 * Thread. If the message should be send on a background thread the {@link io.datafx.core.concurrent.Async} annotation
 * should be used in combination with the {@link io.datafx.messages.MessageProducer} annotation. In this case
 * the producer (annotated method or supplier) will be called on a background thread. All messages will be send
 * by the {@link io.datafx.messages.MessageBus}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface MessageProducer {

    /**
     * Defines the adress to that the message should be send by the {@link io.datafx.messages.MessageBus}
     * @return the adress
     */
    String value() default "";

    /**
     * Defines the producer id. This is used to bind a {@link io.datafx.messages.MessageProducer} to a
     * {@link io.datafx.messages.MessageTrigger} in the same controller. Both annotations must define the
     * same id. If there is only one producer and trigger in a class a specific id is not requiered because
     * the default value "" can be used in this case.
     * @return the adress
     */
    String id() default "";

}
