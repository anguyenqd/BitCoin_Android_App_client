package kibow.games.casinohills.sicbo.screen;

import java.util.ArrayList;

import kibow.games.casinohills.sicbo.Acitivty.HelpActivity;
import kibow.games.casinohills.sicbo.Acitivty.SplashScreenAcivity;
import kibow.games.casinohills.sicbo.Acitivty.WebviewHelpPage;
import kibow.games.casinohills.sicbo.components.AbItemComponent.ItemType;
import kibow.games.casinohills.sicbo.components.BetComponent;
import kibow.games.casinohills.sicbo.components.CharacterComponent;
import kibow.games.casinohills.sicbo.components.CharacterComponent.CharacterAction;
import kibow.games.casinohills.sicbo.components.CoinComponent;
import kibow.games.casinohills.sicbo.components.GameComponent;
import kibow.games.casinohills.sicbo.components.PatternComponent;
import kibow.games.casinohills.sicbo.components.UserComponent;
import kibow.networkmanagement.network.AsyncNetworkHandler;
import kibow.networkmanagement.network.ConnectionHandler;
import kibow.networkmanagement.network.IOnNetworkHandle;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class GameEntity implements IOnNetworkHandle {
	// Implement single ton
	private static GameEntity INSTANCE = null;

	protected GameEntity() {

	}

	public static GameEntity getInstance() {
		if (INSTANCE == null)
			INSTANCE = new GameEntity();
		return INSTANCE;
	}

	public final String runableTextContent = "Maximum bet is 100 Z - You can click back button to open menu - You can Shake phone to start game";

	// Final static fields
	public final static int CAMERA_WIDTH = 800;
	public final static int CAMERA_HEIGHT = 480;

	public static String GAME_ID = "29498045139779584";

	public final static String SIGNIN_TASK = "login";
	public final static String SIGNUP_TASK = "signup";
	public final static String SIGNOUT_TASK = "logout";
	public final static String CHANGE_PASSWORD_TASK = "changepassword";
	public final static String FORGOT_PASSWORD_TASK = "resetpassword";

	public final static String STARTGAME_TASK = "bet";

	public final static String VIEW_HISTORY = "view_bet_history";
	public final static String VIEW_HISTORY_NEXT = "view_bet_history_next";
	public static final double REMAIN_FIXED = 10000;
	public final static int miniCoiWidth = 33;
	public final static int miniCoinHeight = 31;
	public SceneManager sceneManager;
	public boolean isResultDisplay = false;
	public boolean isAnimationRunning = false;
	// Scene fields
	public Scene splashScene;
	public Scene GameScene;
	public Scene helpScene;
	public Scene historyScene;
	public Scene animatedSene;

	public int currentScreen;
	public EngineOptions engineOptions;
	public GameComponent currentGame;

	// Connection field object
	public ConnectionHandler connectionHandler;
	public int currentCoint = CoinComponent.COINTID_1;
	public UserComponent userComponent;
	public double betAmountRemain = REMAIN_FIXED;
	public boolean isMusicEnable = true;
	public boolean isMenuDisplay = false;
	public boolean isBackPress = false;

	// Enum
	public enum GameAction {
		BETING, REBET, RESET
	}

	public GameAction gameAction = GameAction.BETING;

	// Pattern ID
	public enum PatternType {
		Big("big"), Small("small"), Triple1("triples-1"), Triple2("triples-2"), Triple3(
				"triples-3"), Triple4("triples-4"), Triple5("triples-5"), Triple6(
				"triples-6"), Double1("doubles-1"), Double2("doubles-2"), Double3(
				"doubles-3"), Double4("doubles-4"), Double5("doubles-5"), Double6(
				"doubles-6"), AllTriple("triples-any"), ThreeDice4("total-4"), ThreeDice17(
				"total-17"), ThreeDice6("total-6"), ThreeDice15("total-15"), ThreeDice5(
				"total-5"), ThreeDice16("total-16"), ThreeDice7("total-7"), ThreeDice14(
				"total-14"), ThreeDice8("total-8"), ThreeDice13("total-13"), ThreeDice9(
				"total-9"), ThreeDice12("total-12"), SingleDice1("single-1"), SingleDice2(
				"single-2"), SingleDice3("single-3"), SingleDice4("single-4"), SingleDice5(
				"single-5"), SingleDice6("single-6"), ThreeDice10("total-10"), ThreeDice11(
				"total-11");

		private final String value;

		private PatternType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static PatternType getPattern(String v_name) {
			PatternType[] arr = PatternType.values();
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].value.equals(v_name))
					return arr[i];
			}

			return null;
		}
	}

	// public ShakeEventListener mSensorListener;

	/**
	 * User acction
	 * 
	 * */

	/**
	 * Enable and disable music This variable will be reference in MSComponent
	 * when handle music or sound
	 */
	public void enableMusic() {
		if (!isMusicEnable) {
			isMusicEnable = true;
			sceneManager.gameScene.backgroundMusic.resume();
		} else {
			sceneManager.gameScene.backgroundMusic.pause();
			isMusicEnable = false;
		}

	}

	/**
	 * Bet function, when user click on a pattern, the method will be call
	 * called from click action in pattern
	 * 
	 * @param X
	 * @param Y
	 * @param pattern
	 */
	public void bet(float X, float Y, PatternComponent pattern) {
		if (userComponent.balance.balance - currentCoint < 0) {
			if (!isMusicEnable)
				displayConfirmDialog("You do not enough money", 170, 200);
			else
				playVoiceCharacter(CharacterAction.NO_MORE_BET);
		} else if (betAmountRemain - currentCoint < 0) {
			if (!isMusicEnable)
				displayConfirmDialog("You can not bet over 100 zenny", 170, 200);
			else
				playVoiceCharacter(CharacterAction.NO_MORE_BET);
		} else {
			if (gameAction.equals(GameEntity.GameAction.RESET)) {
				// GameEntity.sceneManager.gameScene.coinList.clear();
				clearAllCoinList();
			}
			Y -= 5 * pattern.coinList.size();
			CoinComponent coin = new CoinComponent(currentCoint,
					GameEntity.miniCoiWidth, GameEntity.miniCoinHeight, "", X,
					Y, sceneManager.activity, currentCoint, pattern,
					ItemType.NORMAL_ITEM);
			pattern.scene.registerTouchArea(coin.sprite);
			pattern.scene.attachChild(coin.sprite);
			for (int i = 0; i < sceneManager.gameScene.textList.size(); i++) {
				if (sceneManager.gameScene.textList.get(i).getiID() == 1) {
					sceneManager.gameScene.textList.get(i).updateBalance(
							UserComponent.UserAction.DECREASE_BALANCE,
							currentCoint);
				} else if (sceneManager.gameScene.textList.get(i).getiID() == 3) {
					sceneManager.gameScene.textList.get(i).decreaseBetRemain(
							currentCoint);
				}
			}
			pattern.coinList.add(coin);
			gameAction = GameEntity.GameAction.BETING;
			if (sceneManager.gameScene.betSound != null)
				sceneManager.gameScene.betSound.play();
		}
	}

	/**
	 * Clear all coin list
	 */
	private void clearAllCoinList() {
		int listSize = sceneManager.gameScene.patternList.size();
		for (int i = 0; i < listSize; i++) {
			if (listSize > 0) {
				sceneManager.gameScene.patternList.get(i).coinList.clear();
			}
		}

	}

	public void buttonPlaySoudEffect() {
		sceneManager.gameScene.buttonPlaySound();
	}

	/**
	 * This method called when user click "clear" or "reset" button Called from
	 * Button component class - click action
	 */
	public void clearBet() {

		if (gameAction.equals(GameAction.REBET)
				|| gameAction.equals(GameAction.BETING)) {
			double amoutUpdate = 0;
			int patternListSize = sceneManager.gameScene.patternList.size();
			for (int j = 0; j < patternListSize; j++) {
				int coinListSize = sceneManager.gameScene.patternList.get(j).coinList
						.size();
				for (int i = 0; i < coinListSize; i++) {
					sceneManager.gameScene.patternList.get(j).coinList.get(i)
							.removeCoin();
					amoutUpdate += sceneManager.gameScene.patternList.get(j).coinList
							.get(i).getCoinID();
					sceneManager.gameScene.getScene().unregisterTouchArea(
							sceneManager.gameScene.patternList.get(j).coinList
									.get(i).sprite);
				}
			}
			int textListSize = sceneManager.gameScene.textList.size();
			for (int i = 0; i < textListSize; i++) {
				if (sceneManager.gameScene.textList.get(i).getiID() == 1) {
					sceneManager.gameScene.textList.get(i).updateBalance(
							UserComponent.UserAction.INCREASE_BALANCE,
							amoutUpdate);
				} else if (sceneManager.gameScene.textList.get(i).getiID() == 3) {
					sceneManager.gameScene.textList.get(i).updateBetRemain(
							REMAIN_FIXED);
				}
			}
			gameAction = GameEntity.GameAction.RESET;
		}
	}

	private double checkBalanceRebet() {
		double amoutUpdate = 0;
		int patternListSize = sceneManager.gameScene.patternList.size();
		for (int j = 0; j < patternListSize; j++) {
			int coinListSize = sceneManager.gameScene.patternList.get(j).coinList
					.size();
			for (int i = 0; i < coinListSize; i++) {
				amoutUpdate += sceneManager.gameScene.patternList.get(j).coinList
						.get(i).getCoinID();
			}
		}

		return amoutUpdate;
	}

	/**
	 * This method called when user click "rebet" button Called from Button
	 * component class - click action
	 */
	public void rebet() {
		if (gameAction.equals(GameEntity.GameAction.RESET)) {
			double amoutUpdate = checkBalanceRebet();
			if (userComponent.balance.balance - amoutUpdate < 0) {
				if (!isMusicEnable)
					displayConfirmDialog("You do not enough money", 170, 200);
				else
					playVoiceCharacter(CharacterAction.NO_MORE_BET);
			} else {
				int patternListSize = sceneManager.gameScene.patternList.size();
				for (int j = 0; j < patternListSize; j++) {
					int coinListSize = sceneManager.gameScene.patternList
							.get(j).coinList.size();
					for (int i = 0; i < coinListSize; i++) {
						sceneManager.gameScene.patternList.get(j).coinList.get(
								i).reBuildCoin();

						sceneManager.gameScene.getScene()
								.registerTouchArea(
										sceneManager.gameScene.patternList
												.get(j).coinList.get(i).sprite);
					}
				}
				int textListSize = sceneManager.gameScene.textList.size();
				for (int i = 0; i < textListSize; i++) {
					if (sceneManager.gameScene.textList.get(i).getiID() == 1) {
						sceneManager.gameScene.textList.get(i).updateBalance(
								UserComponent.UserAction.DECREASE_BALANCE,
								amoutUpdate);
					} else if (sceneManager.gameScene.textList.get(i).getiID() == 3) {
						sceneManager.gameScene.textList.get(i)
								.decreaseBetRemain(amoutUpdate);
					}
				}
				gameAction = GameEntity.GameAction.REBET;
			}

		}
	}

	/**
	 * This method will be call after user click next game button Called from
	 * button action click
	 */
	public void updateAfterBet() {
		// clearAllBet();
		int patternListSize = sceneManager.gameScene.patternList.size();
		for (int j = 0; j < patternListSize; j++) {
			int coinListSize = sceneManager.gameScene.patternList.get(j).coinList
					.size();
			for (int i = 0; i < coinListSize; i++) {

				sceneManager.gameScene.patternList.get(j).coinList.get(i)
						.removeCoin();
				sceneManager.gameScene.getScene().unregisterTouchArea(
						sceneManager.gameScene.patternList.get(j).coinList
								.get(i).sprite);
			}
		}

		int textListSize = sceneManager.gameScene.textList.size();
		for (int i = 0; i < textListSize; i++) {
			if (sceneManager.gameScene.textList.get(i).getiID() == 1) {
				sceneManager.gameScene.textList.get(i).updateBalance(
						UserComponent.UserAction.UPDATE_BALANCE,
						userComponent.getBalance());
			} else if (sceneManager.gameScene.textList.get(i).getiID() == 3) {
				sceneManager.gameScene.textList.get(i).updateBetRemain(
						GameEntity.REMAIN_FIXED);
			}
		}

		sceneManager.gameScene.betList.clear();
		gameAction = GameEntity.GameAction.RESET;
	}

	public static int random(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	private void playVoiceCharacter(CharacterComponent.CharacterAction action) {
		int charIndex = random(1, 2);
		CharacterComponent charSay = null;
		if (charIndex == 1) {
			charSay = sceneManager.gameScene.characterBoy;
		} else {
			charSay = sceneManager.gameScene.characterGirl;
		}

		switch (action) {
		case PLEASE_PLAY_BET:
			charSay.say(6);
			break;
		case NO_MORE_BET:
			charSay.say(0);
			break;
		default:
			break;
		}
	}

	/**
	 * When user click start game or shake phone, this method will be call
	 * Called from Button action click and Shake phone method on game scene
	 */
	public void startGame() {
		boolean isBet = false;
		// mSensorListener.stopRegisterShake();
		int patternListSize = sceneManager.gameScene.patternList.size();
		for (int i = 0; i < patternListSize; i++) {
			if (sceneManager.gameScene.patternList.get(i).coinList.size() > 0
					&& !gameAction.equals(GameEntity.GameAction.RESET)) {
				int coinListSize = sceneManager.gameScene.patternList.get(i).coinList
						.size();
				for (int j = 0; j < coinListSize; j++) {
					sceneManager.gameScene.betList.add(new BetComponent(
							sceneManager.gameScene.patternList.get(i).coinList
									.get(j).pattern.patternType.getValue(),
							sceneManager.gameScene.patternList.get(i).coinList
									.get(j).getCoinID()));
				}
				isBet = true;
			}
		}

		if (!isBet) {
			if (!isMusicEnable)
				displayConfirmDialog("You must bet before start game", 170, 200);
			else
				playVoiceCharacter(CharacterAction.PLEASE_PLAY_BET);
		} else {
			sortBetList();
			networkHandler = new AsyncNetworkHandler();
			sceneManager.gameScene.disableAllTouch();
			int paramsSize = sceneManager.gameScene.betList.size();

			ArrayList<String> paramsName = new ArrayList<String>();
			ArrayList<Object> paramsValue = new ArrayList<Object>();

			for (int i = 0; i < paramsSize; i++) {
				paramsName.add("bet[]");
				paramsName.add("point[]");
			}

			for (int i = 0; i < paramsSize; i++) {
				paramsValue
						.add(sceneManager.gameScene.betList.get(i).betPatternID);
				paramsValue
						.add(sceneManager.gameScene.betList.get(i).betAmount);
			}

			paramsName.add("token");
			paramsName.add("game_id");

			paramsValue.add(userComponent.token);
			paramsValue.add(GAME_ID);

			Object[] params = { connectionHandler,
					sceneManager.gameScene.getActivity(),
					GameEntity.STARTGAME_TASK, paramsName, paramsValue, this,
					true };
			sceneManager.gameScene.disableAllTouch();
			// mSensorListener.stopRegisterShake();
			networkHandler.execute(params);
		}

	}

	private int sortBetList() {
		// TODO Auto-generated method stub
		for (int i = 0; i < sceneManager.gameScene.betList.size(); i++) {
			for (int j = 0; j < sceneManager.gameScene.betList.size(); j++) {
				if (sceneManager.gameScene.betList.get(i).betPatternID == sceneManager.gameScene.betList
						.get(j).betPatternID && i != j) {
					sceneManager.gameScene.betList.get(i).betAmount += sceneManager.gameScene.betList
							.get(j).betAmount;
					sceneManager.gameScene.betList.remove(j);
					j--;
				}
			}
		}
		return sceneManager.gameScene.betList.size();
	}

	/**
	 * This method will be call when user click view history button Called from
	 * Button Action click - button component class
	 */
	public void viewHistory() {
		// Go to Webview
		Intent intent = new Intent(sceneManager.activity, WebviewHelpPage.class);
		intent.putExtra("idButton", 6);
		sceneManager.activity.startActivity(intent);
	}

	/**
	 * Go to help page activity
	 */
	public void viewHelp() {
		Intent intent1 = new Intent(sceneManager.gameScene.getActivity(),
				HelpActivity.class);

		sceneManager.gameScene.getActivity().startActivity(intent1);
	}

	private boolean isLogout = false;

	/**
	 * This method will be call when user click exit button Called from Button
	 * Action click - button component class
	 */
	public void exitGame() {
		isLogout = false;
		sceneManager.gameScene.unLoadScene();
		networkHandler = new AsyncNetworkHandler();
		Object[] params = { connectionHandler,
				sceneManager.gameScene.getActivity(), GameEntity.SIGNOUT_TASK,
				null, null, this, false };
		networkHandler.execute(params);
		betAmountRemain = GameEntity.REMAIN_FIXED;
	}

	AsyncNetworkHandler networkHandler;

	public void logout() {
		isLogout = true;
		clearBet();
		sceneManager.gameScene.unLoadScene();
		networkHandler = new AsyncNetworkHandler();
		Object[] params = { connectionHandler,
				sceneManager.gameScene.getActivity(), GameEntity.SIGNOUT_TASK,
				null, null, this, false };
		networkHandler.execute(params);

	}

	/**
	 * This method will be call when user timeout Called from GameEntity
	 * checkTimeout method
	 */
	public void exitGameTimeOut() {
		betAmountRemain = GameEntity.REMAIN_FIXED;
		sceneManager.gameScene.unLoadScene();
		sceneManager.gameScene.getActivity().finish();
		sceneManager = null;
	}

	/**
	 * @author Admin this class is the portal to sent and receive request
	 *         response with server
	 */

	@Override
	public void onNetwokrHandle(JSONObject result,
			ConnectionHandler connectionHandler, Activity activity)
			throws JSONException {
		// TODO Auto-generated method stub
		if (connectionHandler.getTaskID().equals("res_bet")) {
			// move to animation scene
			if (result.getBoolean("success")) {
				onReceiveStartGame(result);
			} else {
				Log.d("Bet error", "Something wrong???");
			}

		} else if (connectionHandler.getTaskID().equals("res_log_out")) {
			onReceiveSignout();
		} else if (connectionHandler.getTaskID().equals("res_session_expire")) {
			onSessionExpire();
		}

	}

	/**
	 * Method called when session expire
	 */
	public void onSessionExpire() {
		Toast.makeText(sceneManager.activity, "Your session is expired",
				Toast.LENGTH_LONG).show();
		isLogout = true;
		onReceiveSignout();
	}

	/**
	 * @param result
	 * @throws JSONException
	 *             This method called when receive response game result from
	 *             Server
	 * 
	 *             {"response":{"task_title":"res_bet","data":{"success":true,
	 *             "result"
	 *             :{"dice":[6,3,6],"win":["big"],"point":{"win":2,"current"
	 *             :11200}}},"status":true}}
	 */
	public void onReceiveStartGame(JSONObject result) throws JSONException {
		JSONObject game = result.getJSONObject("result");

		boolean isWin = false;
		if (!game.getString("win").equals("[]")) {
			isWin = true;
		}

		JSONObject point = game.getJSONObject("point");

		int totalBetAmount = 0;
		for (int i = 0; i < sceneManager.gameScene.betList.size(); i++) {
			totalBetAmount += sceneManager.gameScene.betList.get(i).betAmount;
		}

		currentGame.setGame(isWin, game.getString("dice"),
				point.getDouble("current"), totalBetAmount,
				point.getDouble("win"), game.getString("win"));
		userComponent.balance.balance = currentGame.newBalance;
		// sceneManager.setScene(SceneType.ANIMATION);
		sceneManager.gameScene.playAnimationComponent.playAnimation();
	}

	public void onReceiveSignout() {
		if (!isLogout) {
			sceneManager.activity.finish();
			sceneManager = null;
			System.exit(1);
		} else {
			Intent intent = new Intent(sceneManager.activity,
					SplashScreenAcivity.class);
			sceneManager.activity.startActivity(intent);
			sceneManager.activity.finish();
			sceneManager = null;
		}
	}

	// Dialog display
	// Error display
	public void displayYesNoDialog(String errorContent, int posX, int posY) {
		sceneManager.gameScene.yesnoDialog.displayDialog(
				sceneManager.gameScene, errorContent, posX, posY);
	}

	public void displayConfirmDialog(String errorContent, int posX, int posY) {
		sceneManager.gameScene.confirmDialog.displayDialog(
				sceneManager.gameScene, errorContent, posX, posY);
	}

	public void displayConfirmErrorDialog(String errorContent, int posX,
			int posY) {
		sceneManager.gameScene.confirmErrorDialog.displayDialog(
				sceneManager.gameScene, errorContent, posX, posY);
	}

	// test particle
	public void createFireWork(final float posX, final float posY,
			final int width, final int height, final Color color, int mNumPart,
			int mTimePart) {

		PointParticleEmitter particleEmitter = new PointParticleEmitter(posX,
				posY);

		IEntityFactory<Rectangle> recFact = new IEntityFactory<Rectangle>() {

			@Override
			public Rectangle create(float pX, float pY) {
				Rectangle rect = new Rectangle(posX, posY, 10, 10,
						sceneManager.activity.getVertexBufferObjectManager());
				rect.setColor(color);
				return rect;
			}

		};
		final ParticleSystem<Rectangle> particleSystem = new ParticleSystem<Rectangle>(
				recFact, particleEmitter, 500, 500, mNumPart);

		particleSystem
				.addParticleInitializer(new VelocityParticleInitializer<Rectangle>(
						-50, 50, -50, 50));
		/*
		 * particleSystem .addParticleInitializer(new
		 * ColorParticleInitializer<Rectangle>( color));
		 */
		particleSystem
				.addParticleModifier(new AlphaParticleModifier<Rectangle>(0,
						0.6f * mTimePart, 1, 0));
		particleSystem
				.addParticleModifier(new RotationParticleModifier<Rectangle>(0,
						mTimePart, 0, 360));

		sceneManager.gameScene.getScene().attachChild(particleSystem);
		sceneManager.gameScene.getScene().registerUpdateHandler(
				new TimerHandler(mTimePart, new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						particleSystem.detachSelf();
						sceneManager.gameScene.getScene().sortChildren();
						sceneManager.gameScene.getScene()
								.unregisterUpdateHandler(pTimerHandler);
					}
				}));

	}

	private PatternComponent currentHoverPattern = null;

	public void specifyDragOnItem(float X, float Y,
			TouchEvent pSceneTouchEvent, boolean isMove) {
		if (currentHoverPattern != null)
			currentHoverPattern.sprite.setAlpha(1f);
		for (int i = 0; i < sceneManager.gameScene.patternList.size(); i++) {
			if (sceneManager.gameScene.patternList.get(i).getiItemType() == ItemType.TOUCHABLE_ITEM) {
				if ((sceneManager.gameScene.patternList.get(i).getPositionX() < X && X < (GameEntity
						.getInstance().sceneManager.gameScene.patternList
						.get(i).getPositionX() + sceneManager.gameScene.patternList
						.get(i).getiWidth()))
						&& (sceneManager.gameScene.patternList.get(i)
								.getPositionY() < Y && Y < (GameEntity
								.getInstance().sceneManager.gameScene.patternList
								.get(i).getPositionY() + GameEntity
								.getInstance().sceneManager.gameScene.patternList
								.get(i).getiHeight()))) {

					if (isMove) {
						currentHoverPattern = sceneManager.gameScene.patternList
								.get(i);
						currentHoverPattern.sprite.setAlpha(0.5f);

					} else {
						sceneManager.gameScene.patternList.get(i).bet();
					}
				}
			}
		}
	}

	public void moveCoinComponent(float X, float Y, boolean isFinished) {
		if (isFinished) {
			sceneManager.gameScene.coinArrow.sprite.setPosition(-CAMERA_WIDTH,
					-CAMERA_HEIGHT);
			sceneManager.gameScene.coinBallon.sprite.setPosition(-CAMERA_WIDTH,
					-CAMERA_HEIGHT);
			currentHoverPattern = null;
		} else {
			
			float balloonY = 0 ;
			if(Y - 50 > 0)
				balloonY = Y - 50;
			
			sceneManager.gameScene.coinArrow.sprite.setPosition(X, Y + 30);
			sceneManager.gameScene.coinBallon.sprite
					.setPosition(X - 70, balloonY);
			if (currentHoverPattern != null){
				sceneManager.gameScene.coinBallonText.text
						.setText(currentHoverPattern.ballonDisplayName);
				float textX = 97 - sceneManager.gameScene.coinBallonText.text.getWidth()/2;
				sceneManager.gameScene.coinBallonText.text.setPosition(textX, 10);
			}
			else
				sceneManager.gameScene.coinBallonText.text
				.setText("");
		}
	}

	@Override
	public void onNetworkError() {
		// TODO Auto-generated method stub

	}

}
