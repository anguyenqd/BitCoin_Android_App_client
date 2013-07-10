package kibow.games.casinohills.sicbo.Acitivty;

import kibow.games.casinohills.sicbo.components.UserComponent;
import kibow.games.casinohills.sicbo.screen.GameEntity;
import kibow.games.casinohills.sicbo.screen.SceneManager;
import kibow.games.casinohills.sicbo.screen.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;

import android.os.Bundle;
import android.view.KeyEvent;

/**
 * @author Matim Development
 * @version 1.0.0 <br>
 * <br>
 *          https://sites.google.com/site/matimdevelopment/
 */
public class SicBoGameActivity extends BaseGameActivity {

	private Camera camera;
	public UserComponent userComponent;

	// shake phone object

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// createProgressDialog();
		// GameEntity.getInstance().mSensorListener = new
		// ShakeEventListener(this);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub

		camera = new Camera(0, 0, GameEntity.CAMERA_WIDTH,
				GameEntity.CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				camera);
		// engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;
	}

	@Override
	protected void onResume() {
		super.onResume();

		// GameEntity.getInstance().mSensorListener.registerShake();
		if (GameEntity.getInstance().sceneManager != null) {
			if (!GameEntity.getInstance().sceneManager.gameScene.backgroundMusic.music
					.isReleased()) {
				GameEntity.getInstance().sceneManager.gameScene.backgroundMusic
						.resume();
			} else {
				/*
				 * MusicFactory.setAssetBasePath("mfx/");
				 * GameEntity.getInstance(
				 * ).sceneManager.gameScene.backgroundMusic = new MSComponent(
				 * 1, "themesong.mp3", MStype.MUSIC, getEngine(), this, true);
				 * GameEntity
				 * .getInstance().sceneManager.gameScene.backgroundMusic
				 * .play();
				 */
			}
		}
	}

	@Override
	protected void onPause() {
		super.onStop();
		// GameEntity.getInstance().mSensorListener.stopRegisterShake();
		if (GameEntity.getInstance().sceneManager != null)
			if (!GameEntity.getInstance().sceneManager.gameScene.backgroundMusic.music
					.isReleased())
				GameEntity.getInstance().sceneManager.gameScene.backgroundMusic
						.pause();

	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		// createProgressDialog();
		// TODO Auto-generated method stub
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		GameEntity.getInstance().sceneManager = new SceneManager(this, mEngine,
				camera);
		GameEntity.getInstance().sceneManager.loadScene(SceneType.LOADING);
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		// TODO Auto-generated method stub
		// GameEntity.getInstance().sceneManager.setScene(SceneType.LOADING);
		GameEntity.getInstance().sceneManager
				.setCurrentScene(SceneType.LOADING);
		pOnCreateSceneCallback
				.onCreateSceneFinished(GameEntity.getInstance().sceneManager.loadingScene
						.getScene());
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * GameEntity.getInstance().userComponent.actionTime = System
		 * .currentTimeMillis();
		 */

		GameEntity.getInstance().sceneManager.asyncLoadNextScene();
		// GameEntity.getInstance().checkUserTimeout();
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (GameEntity.getInstance().sceneManager.getCurrentScene() == SceneType.GAME) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				onBackPressed();
			} else if (keyCode == KeyEvent.KEYCODE_MENU) {
				if (!GameEntity.getInstance().isAnimationRunning
						&& !GameEntity.getInstance().isBackPress) {
					GameEntity.getInstance().sceneManager.gameScene.menuScene
							.displayMenu();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		if (GameEntity.getInstance().sceneManager.getCurrentScene() == SceneType.GAME) {
			if (!GameEntity.getInstance().isMenuDisplay
					&& !GameEntity.getInstance().isAnimationRunning
					&& !GameEntity.getInstance().sceneManager.gameScene.playAnimationComponent.showBackgroundResult) {
				GameEntity.getInstance().sceneManager.gameScene
						.onBackButtonPress(true);
			}
		}
	}
}
