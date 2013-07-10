package kibow.games.casinohills.sicbo.screen;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

public abstract class MyScene {
	
	public MyScene(Engine engine, Camera camera, BaseGameActivity activity)
	{
		this.engine = engine;
		this.camera = camera;
		this.activity = activity;
		this.scene = new Scene();
		
	}
	
	//Inherite methods
	
	public abstract void loadResource();
	public abstract void loadScene();
	public abstract void unLoadScene();
	public abstract void disableAllTouch();
	public abstract void enableAllTouch();
	
	//Field
	
	private Engine engine;
	private Camera camera;
	private Scene scene; 
	private BaseGameActivity activity;
	
	//Getter and Setter
	
	public BaseGameActivity getActivity() {
		return activity;
	}

	public void setActivity(BaseGameActivity activity) {
		this.activity = activity;
	}

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
		engine.setScene(scene);
	}

	
}
