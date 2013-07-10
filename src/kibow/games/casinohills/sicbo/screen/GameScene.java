package kibow.games.casinohills.sicbo.screen;

import java.util.ArrayList;

import kibow.games.casinohills.sicbo.components.AbItemComponent;
import kibow.games.casinohills.sicbo.components.BetComponent;
import kibow.games.casinohills.sicbo.components.ButtonComponent;
import kibow.games.casinohills.sicbo.components.CharacterComponent;
import kibow.games.casinohills.sicbo.components.CoinComponent;
import kibow.games.casinohills.sicbo.components.DialogComponent;
import kibow.games.casinohills.sicbo.components.DragComponent;
import kibow.games.casinohills.sicbo.components.IOnItemClick;
import kibow.games.casinohills.sicbo.components.ItemComponent;
import kibow.games.casinohills.sicbo.components.MSComponent;
import kibow.games.casinohills.sicbo.components.MyMenuScene;
import kibow.games.casinohills.sicbo.components.ParticleSystemComponent;
import kibow.games.casinohills.sicbo.components.PatternComponent;
import kibow.games.casinohills.sicbo.components.PlayAnimationComponent;
import kibow.games.casinohills.sicbo.components.TextComponent;
import kibow.games.casinohills.sicbo.components.AbItemComponent.ItemType;
import kibow.games.casinohills.sicbo.components.MSComponent.MStype;
import kibow.games.casinohills.sicbo.components.ShakeEventListener.OnShakeListener;
import kibow.games.casinohills.sicbo.screen.GameEntity.GameAction;

import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import android.graphics.Typeface;

public class GameScene extends MyScene implements OnShakeListener,
		IOnSceneTouchListener, IOnItemClick {

	public ItemComponent background;
	public DialogComponent confirmDialog;
	public DialogComponent confirmErrorDialog;
	public DialogComponent yesnoDialog;
	public PlayAnimationComponent playAnimationComponent;

	// item list
	public ArrayList<ItemComponent> itemList;
	public ArrayList<ButtonComponent> buttonList;
	public ArrayList<PatternComponent> patternList;
	public ArrayList<DragComponent> dragList;
	public ArrayList<TextComponent> textList;
	public ArrayList<BetComponent> betList;
	public ArrayList<ParticleSystemComponent> fireworkList;

	// Music and Sound
	public MSComponent backgroundMusic;
	public MSComponent betSound;
	public MSComponent releaseBetSound;
	public MSComponent winSound;
	public MSComponent loseSound;

	// DialogComponent loadingDialog;
	// Menu
	public MyMenuScene menuScene;

	// public TextComponent runableText;
	public VertexBufferObjectManager getVertextBuffer;

	// Character
	public CharacterComponent characterBoy;
	public CharacterComponent characterGirl;

	// HUD
	public ItemComponent hudItem;

	// Coin Arow
	public ItemComponent coinArrow;

	// Coin Ballon info
	public ItemComponent coinBallon;
	public TextComponent coinBallonText;

	public GameScene(Engine engine, Camera camera, BaseGameActivity activity) {
		super(engine, camera, activity);
		// TODO Auto-generated constructor stub
		playAnimationComponent = new PlayAnimationComponent(this);
		itemList = new ArrayList<ItemComponent>();
		buttonList = new ArrayList<ButtonComponent>();
		patternList = new ArrayList<PatternComponent>();
		dragList = new ArrayList<DragComponent>();
		textList = new ArrayList<TextComponent>();
		betList = new ArrayList<BetComponent>();
		fireworkList = new ArrayList<ParticleSystemComponent>();
		getVertextBuffer = activity.getVertexBufferObjectManager();
		// GameEntity.getInstance().mSensorListener.setOnShakeListener(this);

	}

	// public MoveModifier move;
	// public LoopEntityModifier loopEntityModifier;

	private void loadCharacter() {
		characterBoy = new CharacterComponent(1, 1757, 399, 6, 1,
				"character_boy.png", -100, 100, getActivity(),
				ItemType.CHARACTER_BOY);
		characterGirl = new CharacterComponent(1, 1752, 399, 6, 1,
				"character_girl.png", 600, 100, getActivity(),
				ItemType.CHARACTER_GIRL);
	}

	private void loadDialog() {
		Font mFont = FontFactory.create(getEngine().getFontManager(),
				getEngine().getTextureManager(), 512, 512,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32,
				Color.WHITE_ABGR_PACKED_INT);
		mFont.load();
		// 0 0
		confirmDialog = new DialogComponent(1, 800, 480,
				"dialogbackground.png", -800, -480, getActivity(),
				ItemType.CONFIRM_DIALOG, 118, 38, "btnconfirm.png",
				GameEntity.CAMERA_WIDTH / 2 - 118 / 2, 300, mFont);

		confirmErrorDialog = new DialogComponent(1, 800, 480,
				"dialogbackground.png", -800, -480, getActivity(),
				ItemType.CONFIRM_ERROR, 118, 38, "btnconfirm.png",
				GameEntity.CAMERA_WIDTH / 2 - 118 / 2, 300, mFont);
		// 0 0
		yesnoDialog = new DialogComponent(2, 800, 480, "dialogbackground.png",
				-800, -480, getActivity(), ItemType.YESNO_DIALOG, 97, 38, 97,
				38, "btnyes.png", "btnno.png", "", 250, 350, 380, 350, mFont);
	}

	private void loadText() {
		Font mChangableFontSmall = FontFactory.create(getEngine()
				.getFontManager(), getEngine().getTextureManager(), 512, 512,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20,
				Color.BLACK_ABGR_PACKED_INT);
		Font mChangableFontBig = FontFactory.create(getEngine()
				.getFontManager(), getEngine().getTextureManager(), 512, 512,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32,
				Color.BLACK_ABGR_PACKED_INT);
		mChangableFontSmall.load();
		mChangableFontBig.load();
		textList.add(new TextComponent(1, 512, 512,
				GameEntity.getInstance().userComponent.getBalance() / 100 + "",
				250, 440, getActivity(), ItemType.TEXT, 1f, Color.WHITE,
				mChangableFontBig));
		textList.add(new TextComponent(3, 100, 23,
				GameEntity.getInstance().betAmountRemain / 100 + "", 507, 453,
				getActivity(), ItemType.TEXT, 1f, Color.WHITE,
				mChangableFontSmall));

		loadDialog();
	}

	private void loadMenuScene() {
		menuScene = new MyMenuScene(1, 800, 480, "dialogbackground.png", 0, 0,
				getActivity(), ItemType.NORMAL_ITEM);
		menuScene.addItem(new ButtonComponent(1, 200, 36, 1, 1, "resume.jpg",
				300, 50, getActivity(), ItemType.MENU_RESUME, menuScene));
		menuScene.addItem(new ButtonComponent(1, 200, 36, 1, 1, "help.jpg",
				300, 120, getActivity(), ItemType.MENU_HELP, menuScene));
		menuScene.addItem(new ButtonComponent(1, 200, 36, 1, 1, "logout.jpg",
				300, 190, getActivity(), ItemType.MENU_LOGOUT, menuScene));
		menuScene.addItem(new ButtonComponent(1, 200, 36, 1, 1, "exit.jpg",
				300, 260, getActivity(), ItemType.MENU_EXIT, menuScene));

	}

	private void loadMusicAndSound() {
		SoundFactory.setAssetBasePath("mfx/");
		MusicFactory.setAssetBasePath("mfx/");
		backgroundMusic = new MSComponent(1, "sicbo_bg_music.mp3",
				MStype.MUSIC, getEngine(), getActivity(), true);
		betSound = new MSComponent(2, "betcoin.wav", MStype.SOUND, getEngine(),
				getActivity());
		releaseBetSound = new MSComponent(3, "pickcoin.mp3", MStype.SOUND,
				getEngine(), getActivity());

	}

	private void loadResourceItemList() {
		// background
		background = new ItemComponent(1, 800, 480, "bg.png", 0, 0,
				getActivity(), ItemType.NORMAL_ITEM);

		// Double 1 : 11
		itemList.add(new ItemComponent(1, 365, 15, "doublesign.png", 220, 77,
				getActivity(), ItemType.NORMAL_ITEM));

		// Single 1 : 1 - 1 : 2 - 1 : 3
		itemList.add(new ItemComponent(1, 365, 15, "numbersign.png", 220, 239,
				getActivity(), ItemType.NORMAL_ITEM));

		// triple 1 : 150 - 1 : 24 - 1 : 150
		itemList.add(new ItemComponent(1, 610, 15, "triplesign.png", 97, 363,
				getActivity(), ItemType.NORMAL_ITEM));

		// balance and remain
		itemList.add(new ItemComponent(1, 610, 38, "balancesign.png", 97, 379,
				getActivity(), ItemType.NORMAL_ITEM));

		// HUD
		hudItem = (new ItemComponent(1, 800, 117, "hud.png", 0, 363,
				getActivity(), ItemType.NORMAL_ITEM));

	}

	private void loadResourcePatternList() {
		// big small
		// Pattern
		patternList.add(new PatternComponent(2, 122, 81, "big.png", 97, 11,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Big, "Big - 1 : 1"));

		patternList.add(new PatternComponent(3, 121, 81, "small.png", 586, 11,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Small, "Small - 1 : 1"));

		// Double
		patternList.add(new PatternComponent(1, 60, 66, "double1.png", 220, 11,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Double1, "Double One - 1 : 8"));
		patternList.add(new PatternComponent(1, 60, 66, "double2.png", 281, 11,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Double2, "Double Two - 1 : 8"));
		patternList.add(new PatternComponent(1, 60, 66, "double3.png", 342, 11,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Double3, "Double Three - 1 : 8"));
		patternList.add(new PatternComponent(1, 60, 66, "double4.png", 403, 11,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Double4, "Double Four - 1 : 8"));
		patternList.add(new PatternComponent(1, 60, 66, "double5.png", 464, 11,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Double5, "Double Five - 1 : 8"));
		patternList.add(new PatternComponent(1, 60, 66, "double6.png", 525, 11,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Double6, "Double Six - 1 : 8"));

		// Tree dice total
		patternList.add(new PatternComponent(7, 60, 80, "total4.png", 97, 93,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice4, "Total Four - 1 : 50"));

		patternList.add(new PatternComponent(7, 60, 80, "total5.png", 159, 93,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice5, "Total Five - 1 : 18"));

		patternList.add(new PatternComponent(7, 60, 80, "total6.png", 220, 93,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice6, "Total Six - 1 : 14"));

		patternList.add(new PatternComponent(7, 60, 80, "total7.png", 281, 93,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice7, "Total Seven - 1 : 12"));

		patternList.add(new PatternComponent(7, 60, 80, "total8.png", 342, 93,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice8, "Total Eight - 1 : 8"));

		patternList.add(new PatternComponent(7, 60, 80, "total9.png", 403, 93,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice9, "Total Nine - 1 : 6"));

		patternList.add(new PatternComponent(7, 60, 80, "total10.png", 464, 93,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice10, "Total Ten - 1 : 6"));

		patternList.add(new PatternComponent(7, 60, 80, "total11.png", 525, 93,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice11, "Total Eleven - 1 : 6"));

		patternList.add(new PatternComponent(7, 60, 80, "total12.png", 586, 93,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice12, "Total Twelve - 1 : 6"));

		patternList.add(new PatternComponent(7, 60, 80, "total13.png", 647, 93,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice13, "Total Thirteen - 1 : 8"));

		patternList.add(new PatternComponent(7, 60, 80, "total14.png", 97, 174,
				getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice14, "Total Fourteen - 1 : 12"));

		patternList.add(new PatternComponent(7, 60, 80, "total15.png", 159,
				174, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice15, "Total Fifteen - 1 : 14"));

		patternList.add(new PatternComponent(7, 60, 80, "total16.png", 586,
				174, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice16, "Total Sixteen - 1 : 18"));

		patternList.add(new PatternComponent(7, 60, 80, "total17.png", 647,
				174, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.ThreeDice17, "Total Seventeen - 1 : 50"));

		// Single dice
		patternList.add(new PatternComponent(7, 60, 65, "number1.png", 220,
				174, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.SingleDice1, "One - 1 : 1"));
		patternList.add(new PatternComponent(7, 60, 65, "number2.png", 281,
				174, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.SingleDice2, "Two - 1 : 1"));
		patternList.add(new PatternComponent(7, 60, 65, "number3.png", 342,
				174, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.SingleDice3, "Three - 1 : 2"));
		patternList.add(new PatternComponent(7, 60, 65, "number4.png", 403,
				174, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.SingleDice4, "Four - 1 : 2"));
		patternList.add(new PatternComponent(7, 60, 65, "number5.png", 464,
				174, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.SingleDice5, "Five - 1 : 3"));
		patternList.add(new PatternComponent(7, 60, 65, "number6.png", 525,
				174, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.SingleDice6, "Six - 1 : 3"));

		// triple
		patternList.add(new PatternComponent(1, 60, 108, "triple1.png", 97,
				255, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Triple1, "Triple One - 1 : 150"));
		patternList.add(new PatternComponent(1, 60, 108, "triple2.png", 159,
				255, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Triple2, "Triple Two - 1 : 150"));
		patternList.add(new PatternComponent(1, 60, 108, "triple3.png", 220,
				255, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Triple3, "Triple Three - 1 : 150"));

		// All triple
		patternList.add(new PatternComponent(1, 243, 108, "alltriple.png", 281,
				255, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.AllTriple, "Any Triple - 1 : 24"));

		// triple
		patternList.add(new PatternComponent(1, 60, 108, "triple4.png", 525,
				255, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Triple4, "Triple Four - 1 : 150"));
		patternList.add(new PatternComponent(1, 60, 108, "triple5.png", 586,
				255, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Triple5, "Triple Five - 1 : 150"));
		patternList.add(new PatternComponent(1, 60, 108, "triple6.png", 647,
				255, getActivity(), ItemType.TOUCHABLE_ITEM, getScene(),
				GameEntity.PatternType.Triple6, "Triple Six - 1 : 150"));

	}

	private void loadResourceDragList() {
		dragList.add(new DragComponent(1, 33, 31, "icon_coin_1.png", 5, 440,
				getActivity(), ItemType.GRAG_ITEM, getScene(),
				CoinComponent.COINTID_1));
		dragList.add(new DragComponent(2, 33, 31, "icon_coin_5.png", 42, 440,
				getActivity(), ItemType.GRAG_ITEM, getScene(),
				CoinComponent.COINTID_5));
		dragList.add(new DragComponent(3, 33, 31, "icon_coin_10.png", 79, 440,
				getActivity(), ItemType.GRAG_ITEM, getScene(),
				CoinComponent.COINTID_10));
		dragList.add(new DragComponent(4, 33, 31, "icon_coin_25.png", 116, 440,
				getActivity(), ItemType.GRAG_ITEM, getScene(),
				CoinComponent.COINTID_25));
		dragList.add(new DragComponent(5, 33, 31, "icon_coin_50.png", 153, 440,
				getActivity(), ItemType.GRAG_ITEM, getScene(),
				CoinComponent.COINTID_50));
		dragList.add(new DragComponent(6, 33, 31, "icon_coin_100.png", 190,
				440, getActivity(), ItemType.GRAG_ITEM, getScene(),
				CoinComponent.COINTID_100));

		coinArrow = new ItemComponent(1, 32, 29, "arrow_coin_bg.png",
				-GameEntity.CAMERA_WIDTH, -GameEntity.CAMERA_HEIGHT,
				getActivity(), ItemType.NORMAL_ITEM);

		coinBallon = new ItemComponent(2, 194, 45, "balloon_bg.png",
				-GameEntity.CAMERA_WIDTH, -GameEntity.CAMERA_HEIGHT,
				getActivity(), ItemType.NORMAL_ITEM);

		Font ballonFont = FontFactory.create(getEngine().getFontManager(),
				getEngine().getTextureManager(), 512, 512,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20,
				Color.WHITE_ABGR_PACKED_INT);
		ballonFont.load();
		coinBallonText = new TextComponent(1, 0, 0, "Big 1 : 1", 0, 10,
				getActivity(), ItemType.TEXT, 1f, Color.WHITE, ballonFont);
	}

	private void loadResourceButtonList() {
		// Button - Bottom

		buttonList.add(new ButtonComponent(59, 134, 63, 1, 1, "btn_start.png",
				663, 370, getActivity(), ItemType.BUTTON_ROLL, this));
		buttonList.add(new ButtonComponent(59, 68, 30, 1, 1, "btn_rebet.png",
				583, 445, getActivity(), ItemType.BUTTON_REBET, this));
		buttonList.add(new ButtonComponent(59, 68, 30, 1, 1, "btn_reset.png",
				653, 445, getActivity(), ItemType.BUTTON_CLEAR, this));
		buttonList.add(new ButtonComponent(59, 67, 30, 1, 1, "btn_exit.png",
				724, 445, getActivity(), ItemType.BUTTON_EXIT, this));
		buttonList.add(new ButtonComponent(59, 93, 34, 1, 1, "btn_history.png",
				5, 395, getActivity(), ItemType.BUTTON_HISTORY, this));

		// Button - top
		buttonList.add(new ButtonComponent(59, 160, 40, 4, 1, "btnsound.png",
				750, 3, getActivity(), ItemType.BUTTON_SOUND, this));
		buttonList.add(new ButtonComponent(59, 50, 50, 1, 1, "menubtn.png",
				745, 320, getActivity(), ItemType.BUTTON_MENU, this));

		// Load HUD

	}

	public void loadFireworkResource() {
		fireworkList.add(new ParticleSystemComponent(1, 32, 32,
				"particle_point.png", -800, -480, getActivity(),
				ItemType.NORMAL_ITEM, Color.RED, 20, 2, getScene()));
	}

	@Override
	public void loadResource() {
		// TODO Auto-generated method stub
		getScene().registerUpdateHandler(new FPSLogger());
		// getScene().setBackground(new Background(1f, 1f, 1f));
		GameEntity.getInstance().sceneManager.loadingScene.bar.updateBar(0.2f);
		getScene().setBackgroundEnabled(false);
		getScene().setOnAreaTouchTraversalFrontToBack();

		loadResourceItemList();
		GameEntity.getInstance().sceneManager.loadingScene.percentText
				.updateText("30%");
		GameEntity.getInstance().sceneManager.loadingScene.bar.updateBar(0.3f);
		loadResourceDragList();
		GameEntity.getInstance().sceneManager.loadingScene.percentText
				.updateText("33%");
		GameEntity.getInstance().sceneManager.loadingScene.bar.updateBar(0.33f);
		loadResourcePatternList();
		GameEntity.getInstance().sceneManager.loadingScene.percentText
				.updateText("35%");
		GameEntity.getInstance().sceneManager.loadingScene.bar.updateBar(0.35f);
		loadResourceButtonList();
		GameEntity.getInstance().sceneManager.loadingScene.percentText
				.updateText("40%");
		GameEntity.getInstance().sceneManager.loadingScene.bar.updateBar(0.4f);
		loadText();
		GameEntity.getInstance().sceneManager.loadingScene.percentText
				.updateText("45%");
		GameEntity.getInstance().sceneManager.loadingScene.bar.updateBar(0.45f);
		loadMusicAndSound();
		GameEntity.getInstance().sceneManager.loadingScene.percentText
				.updateText("60%");
		GameEntity.getInstance().sceneManager.loadingScene.bar.updateBar(0.6f);
		playAnimationComponent.loadResource();
		GameEntity.getInstance().sceneManager.loadingScene.percentText
				.updateText("70%");
		GameEntity.getInstance().sceneManager.loadingScene.bar.updateBar(0.7f);
		loadMenuScene();
		GameEntity.getInstance().sceneManager.loadingScene.percentText
				.updateText("75%");
		GameEntity.getInstance().sceneManager.loadingScene.bar.updateBar(0.75f);
		// loadRunableText();
		loadCharacter();
	}

	@Override
	public void loadScene() {
		// TODO Auto-generated method stub
		// load background
		GameEntity.getInstance().sceneManager.loadingScene.percentText
				.updateText("80%");
		GameEntity.getInstance().sceneManager.loadingScene.bar.updateBar(0.8f);
		getScene().setOnSceneTouchListener(this);

		getScene().attachChild(background.sprite);

		// add character before pattern
		getScene().attachChild(characterBoy.tiledSprite);
		getScene().attachChild(characterGirl.tiledSprite);

		// load Game pattern
		for (int i = 0; i < patternList.size(); i++) {
			// itemList.get(i).setScale(1.5f);
			getScene().registerTouchArea(patternList.get(i).sprite);
			getScene().attachChild(patternList.get(i).sprite);
		}
		GameEntity.getInstance().sceneManager.loadingScene.percentText
				.updateText("90%");
		GameEntity.getInstance().sceneManager.loadingScene.bar.updateBar(0.9f);
		// load Game item
		for (int i = 0; i < itemList.size(); i++) {
			getScene().attachChild(itemList.get(i).sprite);
		}
		getScene().attachChild(hudItem.sprite);
		// load game coin drag
		for (int i = 0; i < dragList.size(); i++) {
			getScene().registerTouchArea(dragList.get(i).sprite);
			getScene().attachChild(dragList.get(i).sprite);
			getScene().attachChild(dragList.get(i).tempDrag);
		}

		// Add coin arrow
		getScene().attachChild(coinArrow.sprite);
		// add coin balloon
		getScene().attachChild(coinBallon.sprite);
		coinBallon.sprite.attachChild(coinBallonText.text);

		// load button
		for (int i = 0; i < buttonList.size(); i++) {
			// itemList.get(i).setScale(1.5f);
			getScene().registerTouchArea(buttonList.get(i).tiledSprite);
			getScene().attachChild(buttonList.get(i).tiledSprite);
		}

		// LoadText

		for (int i = 0; i < textList.size(); i++) {
			getScene().attachChild(textList.get(i).text);
		}

		// load animation
		playAnimationComponent.loadAniamtionScene();
		getScene().attachChild(playAnimationComponent.diceEntity);
		getScene().attachChild(playAnimationComponent.textEntity);

		// getScene().attachChild(playAnimationComponent.background.sprite);
		// load Dialog
		getScene().attachChild(confirmDialog.sprite);
		getScene().attachChild(confirmErrorDialog.sprite);
		getScene().attachChild(yesnoDialog.sprite);

		getScene().setTouchAreaBindingOnActionMoveEnabled(true);
		getScene().setTouchAreaBindingOnActionDownEnabled(true);

		// add partical
		// getScene().attachChild(particleSystem);
		// menuScene.attachMenu(getScene());

		getScene().attachChild(menuScene.sprite);

		menuScene.registerTouch(getScene());
		// getScene().attachChild(runableText.text);
		GameEntity.getInstance().sceneManager.loadingScene.percentText
				.updateText("95%");
		GameEntity.getInstance().sceneManager.loadingScene.bar.updateBar(0.95f);
	}

	@Override
	public void disableAllTouch() {
		for (int i = 0; i < patternList.size(); i++) {
			getScene().unregisterTouchArea(patternList.get(i).sprite);
			for (int j = 0; j < patternList.get(i).coinList.size(); j++) {
				getScene().unregisterTouchArea(
						patternList.get(i).coinList.get(j).sprite);
			}
		}
		for (int i = 0; i < dragList.size(); i++) {
			getScene().unregisterTouchArea(dragList.get(i).sprite);
		}
		for (int i = 0; i < buttonList.size(); i++) {
			getScene().unregisterTouchArea(buttonList.get(i).tiledSprite);
		}

	}

	@Override
	public void enableAllTouch() {

		for (int i = 0; i < patternList.size(); i++) {
			getScene().registerTouchArea(patternList.get(i).sprite);
			if (GameEntity.getInstance().gameAction != GameAction.RESET) {
				for (int j = 0; j < patternList.get(i).coinList.size(); j++) {
					getScene().registerTouchArea(
							patternList.get(i).coinList.get(j).sprite);
				}
			}

		}

		for (int i = 0; i < dragList.size(); i++) {
			getScene().registerTouchArea(dragList.get(i).sprite);
		}
		for (int i = 0; i < buttonList.size(); i++) {
			getScene().registerTouchArea(buttonList.get(i).tiledSprite);
		}
	}

	@Override
	public void unLoadScene() {
		// TODO Auto-generated method stub
		if (!backgroundMusic.music.isReleased())
			backgroundMusic.music.release();
		getScene().detachChildren();
	}

	int shakeCount = 0;

	@Override
	public void onShake() {
		// TODO Auto-generated method stub
		if (shakeCount >= 3) {
			GameEntity.getInstance().startGame();
			shakeCount = 0;
		}
		shakeCount++;
	}

	public void buttonPlaySound() {
		if (releaseBetSound != null)
			releaseBetSound.play();
	}

	public void onBackButtonPress(boolean isDisplay) {
		if (isDisplay) {
			GameEntity.getInstance().displayYesNoDialog("Do you want to exit?",
					200, 300);
			// GameEntity.getInstance().mSensorListener.stopRegisterShake();
			GameEntity.getInstance().isBackPress = true;
		} else {
			// GameEntity.getInstance().mSensorListener.registerShake();
			GameEntity.getInstance().isBackPress = false;
		}
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		if (!GameEntity.getInstance().isAnimationRunning) {
			if (pSceneTouchEvent.isActionUp()) {
				if (GameEntity.getInstance().sceneManager.gameScene.playAnimationComponent.showBackgroundResult == true) {
					GameEntity.getInstance().sceneManager.gameScene
							.buttonPlaySound();
					GameEntity.getInstance().updateAfterBet();
					GameEntity.getInstance().sceneManager.gameScene.playAnimationComponent
							.stopAnimation();
					GameEntity.getInstance().sceneManager.gameScene.playAnimationComponent
							.removeRectWin();
					GameEntity.getInstance().sceneManager.gameScene.playAnimationComponent
							.unregisterModifier();
					GameEntity.getInstance().sceneManager.gameScene.playAnimationComponent
							.resetEntityPosition();
					GameEntity.getInstance().sceneManager.gameScene.playAnimationComponent.showBackgroundResult = false;
					this.enableAllTouch();
				}
			}
		}

		return true;

	}

	@Override
	public void onClick(AbItemComponent component) {
		// TODO Auto-generated method stub
		ButtonComponent button = (ButtonComponent) component;
		buttonPlaySound();
		switch (button.getiItemType()) {
		case BUTTON_ROLL:
			GameEntity.getInstance().startGame();
			break;
		case BUTTON_CLEAR:
			GameEntity.getInstance().clearBet();
			break;
		case BUTTON_REBET:
			GameEntity.getInstance().rebet();
			break;
		case BUTTON_HISTORY:
			GameEntity.getInstance().viewHistory();
			break;
		case BUTTON_MENU:
			menuScene.displayMenu();
			break;
		/*
		 * case BUTTON_NEXT: GameEntity.getInstance().updateAfterBet();
		 * playAnimationComponent .stopAnimation(); enableAllTouch(); break;
		 */
		case BUTTON_SOUND:
			GameEntity.getInstance().enableMusic();
			if (GameEntity.getInstance().isMusicEnable)
				button.tiledSprite.setCurrentTileIndex(0);
			else
				button.tiledSprite.setCurrentTileIndex(2);
			break;
		case BUTTON_EXIT:
			yesnoDialog.displayDialog(
					GameEntity.getInstance().sceneManager.gameScene,
					"Do you want to exit?", 200, 300);
			break;
		default:
			break;
		}
	}

}
