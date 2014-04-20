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
package org.datafx.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotation for a FXML controller. This annotation will define the FXML-file
 * this controller is made for. You don't need to use this annotation because
 * the API is designed using a convention over configuration approach: If you
 * have a fxml file "master.fxml" you can create a MasterController.java
 * controller in the same package and the controller API will use this
 * controller for the fxml. To create a controller/view pair you need to use the
 * {@link #ViewFactory}.
 * </p>
 * <p>
 * An example of a simple controller:
 * </p>
 * <pre>
 * <code>
 * {@literal @}FXMLController("Details.fxml")
 * public class DetailViewController {
 *
 * 	{@literal @}FXML
 *  private TextField myTextfield;
 *   
 *	{@literal @}FXML
 *  private Button backButton;
 *   
 *  {@literal @}PostConstruct
 *  public void init() {
 *  	myTextfield.setText("Hello!");
 *  }
 * }
 * </code>
 * </pre>
 * </p>
 * 
 * @author Hendrik Ebbers
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FXMLController {
	
	/**
	 * Defines the name of the fxml files that should be used with the annotated controller.
	 * @return name of the fxml file
	 */
	String value();

    String title() default "";

    String iconPath() default "";
}
