/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers All
 * rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of DataFX, the website
 * javafxdata.org, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written
 * permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.controller;

import io.datafx.controller.util.NullNode;
import javafx.scene.Node;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for a MVC controller. This annotation will define the view (FXML file or java class)
 * this controller is made for.
 * <p>
 * You don't <i>need</i> to use this annotation—the API is designed
 * using a convention over configuration approach: If you
 * have an fxml file "master.fxml" you can create a MasterController.java
 * controller in the same package and the controller API will use this
 * controller for the fxml. To create a controller/view pair you need to use the
 * {@link ViewFactory}.
 * <p>
 * An example of a simple controller:
 * </p>
 * <pre>
 * <code>
 * {@literal @}FXMLController("Details.fxml")
 * public class DetailViewController {
 *  *
 *    {@literal @}FXML
 *  private TextField myTextfield;
 *   
 *  *	{@literal @}FXML
 *  private Button backButton;
 *   
 *  {@literal @}PostConstruct
 *  public void init() {
 *  	myTextfield.setText("Hello!");
 *  }
 * }
 * </code>
 * </pre>
 *
 * @author Hendrik Ebbers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ViewController {

    /**
     * Defines the name of the fxml files that should be used with the annotated
     * controller.
     *
     * @return name of the fxml file
     */
    String value() default "";

    /**
     * Defines the title of the view. DataFX will use the title if the view is shown in a {@link javafx.scene.control.Tab} for example.
     * @return the title of the view
     */
    String title() default "";

    /**
     * Defines the icon path for the view. The path defines the default icon of the view.
     * DataFX will use the icon if the view is shown in a {@link javafx.scene.control.Tab} for example.
     * @return the icon of the view
     */
    String iconPath() default "";

    /**
     * Defines a Java class that extends Node. If this is defined and != {@link io.datafx.controller.util.NullNode}
     * a new instance of the class will be created and used as the view. Therefore the class needs a default constructor.
     * In later version maybe additional constructors (for ressource bundle support, etc.) will be supported.
     * In the view class the {@link io.datafx.controller.ViewNode} annotation can be used.
     * @return
     */
    Class<? extends Node> root() default NullNode.class;
}

