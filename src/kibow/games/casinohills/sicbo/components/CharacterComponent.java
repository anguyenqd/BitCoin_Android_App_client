package kibow.games.casinohills.sicbo.components;

import java.util.ArrayList;

import kibow.games.casinohills.sicbo.components.MSComponent.MStype;
import kibow.games.casinohills.sicbo.screen.GameEntity;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.media.MediaPlayer;
import android.text.method.MovementMethod;

public class CharacterComponent extends AbItemComponent {

	public static final int CHAR_STATUS_NORMAL = 0;
	public static final int CHAR_STATUS_WIN1 = 1;
	public static final int CHAR_STATUS_WIN2 = 2;
	public static final int CHAR_STATUS_LOSE1 = 3;
	public static final int CHAR_STATUS_LOSE2 = 4;
	public static final int CHAR_STATUS_LOSE3 = 5;
	public static final int CHAR_STATUS_NORMAL2 = 6;

	public ArrayList<MSComponent> soundList;

	public enum CharacterAction {
		NO_MORE_BET, PLEASE_PLAY_BET
	}

	private int currentIndex;

	public CharacterComponent(int id, int width, int height, int colum,
			int row, String background, float positionX, float positionY,
			BaseGameActivity activity, ItemType itemType) {
		super(id, width, height, background, positionX, positionY, activity,
				itemType);
		// TODO Auto-generated constructor stub
		BitmapTextureAtlas atlastBig = new BitmapTextureAtlas(
				activity.getTextureManager(), width, height,
				TextureOptions.BILINEAR);

		ITiledTextureRegion tiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(atlastBig, activity, background, 0, 0,
						colum, row);

		soundList = new ArrayList<MSComponent>();

		loadCharacterSound(loadVoiceNames());

		tiledSprite = new TiledSprite(positionX, positionY, tiledTextureRegion,
				activity.getVertexBufferObjectManager());

		tiledSprite.setCurrentTileIndex(CHAR_STATUS_NORMAL);
		atlastBig.load();
	}

	public void changeSprite(int index) {
		tiledSprite.setCurrentTileIndex(index);
	}

	private void loadCharacterSound(String[] voiceFileNames) {
		for (int i = 0; i < voiceFileNames.length; i++) {
			soundList.add(new MSComponent(i, voiceFileNames[i], MStype.MUSIC,
					getActivity().getEngine(), getActivity(), false));
		}
	}

	private String[] loadVoiceNames() {

		String[] voiceNames = new String[7];

		switch (getiItemType()) {
		case CHARACTER_BOY:
			voiceNames[0] = "boy_no_more_bets_normal.mp3";
			voiceNames[1] = "boy_yeah_that_s_a_nice_hit_happy.mp3";
			voiceNames[2] = "boy_you_got_it_cheer.mp3";
			voiceNames[3] = "boy_oops_try_again_dude_sad.mp3";
			voiceNames[4] = "boy_oh_no_this_is_not_good_stunned.mp3";
			voiceNames[5] = "boy_i_think_you_need_more_training_angry.mp3";
			voiceNames[6] = "boy_place_your_bets_please_normal.mp3";
			break;
		case CHARACTER_GIRL:
			voiceNames[0] = "girl_no_more_bets_normal.mp3";
			voiceNames[1] = "girl_hooray_you_made_it_happy.mp3";
			voiceNames[2] = "girl_you_re_so_lucky_cheer.mp3";
			voiceNames[3] = "girl_sorry_you_can_try_again_sad.mp3";
			voiceNames[4] = "girl_unbelivevable_stunned.mp3";
			voiceNames[5] = "girl_hey_you_can_do_more_than_that_angry.mp3";
			voiceNames[6] = "girl_place_your_bets_please_normal.mp3";

			break;
		default:
			break;
		}

		return voiceNames;
	}

	public MSComponent getPlayVoice(int index) {
		currentIndex = index;
		return soundList.get(index);
	}

	public void say(int index) {
		soundList.get(index).play();
	}

	public TiledSprite tiledSprite;

	public static void playSequence(MSComponent music1, final MSComponent music2) {
		if (GameEntity.getInstance().isMusicEnable) {
			music1.music.seekTo(0);
			music1.play();
			music1.music
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							music2.music.seekTo(0);
							music2.play();

						}
					});
		}
	}

	public void stopSay() {
		if (soundList.get(currentIndex).music.isPlaying())
			soundList.get(currentIndex).pause();
	}
	
	public void slideDisplay(int pFromX, int pToX)
	{
		MoveXModifier moveModifier = new MoveXModifier(1, pFromX, pToX);
		 
		tiledSprite.registerEntityModifier(moveModifier);
	}

}
