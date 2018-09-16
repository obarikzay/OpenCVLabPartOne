package application;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FXController {
	@FXML
	private Button start_btn;
	@FXML
	private ImageView currentFrame;

	private VideoCapture capture = new VideoCapture();

	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;

	@FXML
	protected void startVideo(ActionEvent event) {
		if (!capture.isOpened()) {
			capture.open("S:\\CLASS\\CS\\285\\sample_videos\\sample1.mp4");
		}

		Runnable frameGrabber = new Runnable() {

			@Override
			public void run() {
				Mat frame = new Mat();

				capture.read(frame);

				Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
				MatOfByte buffer = new MatOfByte();
				Imgcodecs.imencode(".bmp", frame, buffer);

				Image imageToShow = new Image(new ByteArrayInputStream(buffer.toArray()));
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						currentFrame.setImage(imageToShow);
					}
				});
			}

		};
		this.timer = Executors.newSingleThreadScheduledExecutor();
		this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
	}

}
