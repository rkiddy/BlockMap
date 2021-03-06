package de.piegames.blockmap.gui.decoration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import org.controlsfx.control.RangeSlider;

import de.piegames.blockmap.gui.WorldRendererCanvas;
import de.piegames.blockmap.renderer.RenderSettings;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SettingsOverlay extends AnchorPane implements Initializable {

	@FXML
	private Label					minHeight, maxHeight;
	@FXML
	private RangeSlider				heightSlider;
	@FXML
	private ToggleButton			showButton;
	@FXML
	private VBox					rightMenu;

	protected WorldRendererCanvas	panel;
	protected RenderSettings		settings;

	public SettingsOverlay(WorldRendererCanvas panel, RenderSettings settings) {
		this.panel = Objects.requireNonNull(panel);
		this.settings = Objects.requireNonNull(settings);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsOverlay.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		minHeight.textProperty().bind(Bindings.format("Min: %.0f", heightSlider.lowValueProperty()));
		maxHeight.textProperty().bind(Bindings.format("Max: %.0f", heightSlider.highValueProperty()));

		ChangeListener<? super Boolean> heightListener = (e, oldVal, newVal) -> {
			if (oldVal && !newVal) {
				if (e == heightSlider.lowValueChangingProperty())
					settings.minY = (int) Math.round(heightSlider.lowValueProperty().getValue().doubleValue());
				else if (e == heightSlider.highValueChangingProperty())
					settings.maxY = (int) Math.round(heightSlider.highValueProperty().getValue().doubleValue());
				panel.invalidateTextures();
				panel.repaint();
			}
		};
		heightSlider.lowValueChangingProperty().addListener(heightListener);
		heightSlider.highValueChangingProperty().addListener(heightListener);

		showButton.setOnAction(e -> {
			if (showButton.isSelected()) {
				// showButton.setText("Hide settings");
				TranslateTransition transition = new TranslateTransition(Duration.millis(500), rightMenu);
				transition.setFromX(rightMenu.getTranslateX());
				transition.setToX(0);
				transition.play();
			} else {
				// showButton.setText("Show settings");
				TranslateTransition transition = new TranslateTransition(Duration.millis(500), rightMenu);
				transition.setFromX(rightMenu.getTranslateX());
				transition.setToX(rightMenu.getWidth() - 15);
				transition.play();
			}
		});
	}
}