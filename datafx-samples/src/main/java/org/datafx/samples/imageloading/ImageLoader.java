package org.datafx.samples.imageloading;

import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.datafx.concurrent.ConcurrentUtils;
import org.datafx.concurrent.ObservableExecutor;

import java.util.concurrent.ExecutionException;

public class ImageLoader {

    private ObservableExecutor executor;

    private Image defaultImage;

    public ImageLoader() {
        this(ObservableExecutor.getDefaultInstance(), null);
    }

    public ImageLoader(ObservableExecutor executor, Image defaultImage) {
        this.executor = executor;
        this.defaultImage = defaultImage;
    }

    public Worker<Void> updateImageView(ImageView view, String url) {
        view.setImage(defaultImage);

        return executor.submit(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateTitle("Loading Image");
                updateMessage("Loading Image by URL: " + url);
                Image image = new Image(url, false);
                try {
                    ConcurrentUtils.runAndWait(() -> view.setImage(image));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public void debug() {
        executor.currentServicesProperty().addListener((ListChangeListener)(e) -> System.out.println(executor.currentServicesProperty().size() + " threads are loading images."));
    }

}
