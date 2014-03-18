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
package org.datafx.concurrent;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;

/**
 * This class wrappes the complete state of a task
 *
 * @author Hendrik Ebbers
 */
public class TaskState {

	private DoubleProperty progress;
	
	private DoubleProperty maxProgress;
	
	private ObservableStringValue title;
	
	private ObservableStringValue message;

    /**
     * Returns a property that defines the maximum value that the progress value can reach
     * @return property that defines the maximum value that the progress value can reach
     */
	public DoubleProperty maxProgress() {
		if(maxProgress == null) {
			maxProgress = new SimpleDoubleProperty();
		}
		return maxProgress;
	}

    /**
     * Sets the max progress value. The value defines the maximum value that the progress value can reach
     * @param maxProgress  the max progress value
     */
	public void setMaxProgress(double maxProgress) {
		maxProgress().set(maxProgress);
	}

    /**
     *  Returns the max progress value. The value defines the maximum value that the progress value can reach
     * @return the max progress value
     */
	public double getMaxProgress() {
		return maxProgress == null ? 0d : maxProgress.get();
	}

    /**
     *  Returns a property that defines the current progress of the task. The internal value will be between 0 and maxProgress
     * @return a property that defines the current progress
     */
	public DoubleProperty progress() {
		if(progress == null) {
			progress = new SimpleDoubleProperty();
		}
		return progress;
	}

    /**
     * Returns the current progress of the task. This will be between 0 and maxProgress
     * @return the current progress
     */
	public double getProgress() {
		return progress == null ? 0d : progress.get();
	}

    /**
     *  Sets the current progress of the task.
     * @param progress the current progress
     */
	public void setProgress(double progress) {
		progress().set(progress);
	}

    /**
     * Returns a property that defines the title of the task.
     * @return a property that defines the title
     */
	public ObservableStringValue title() {
		if(title == null) {
			title = new SimpleStringProperty();
		}
		return title;
	}

    //TODO: Message property and getter / setter for title
}
