package kibow.games.casinohills.sicbo.components;

import java.util.List;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

public class AnimationComponent extends AbItemComponent {
	Scene scene;
	List<TextComponent> texts;

	public AnimationComponent(int id, int width, int height, int animatedWidth,
			int animatedHeight, int animatedColum, int animatedRow,
			String background, float positionX, float positionY,
			BaseGameActivity activity, ItemType itemType, Scene scene,
			List<TextComponent> texts) {
		super(id, width, height, background, positionX, positionY, activity,
				itemType);
		this.animatedWidth = animatedWidth;
		this.animatedHeight = animatedHeight;
		this.animatedColum = animatedColum;
		this.animatedRow = animatedRow;
		this.scene = scene;
		this.texts = texts;
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), getiWidth(), getiHeight(),
				TextureOptions.NEAREST);
		TiledTextureRegion mDiceTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mBitmapTextureAtlas, activity,
						getStrBackgroud(), animatedColum, animatedRow);

		try {
			mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}

		animatedSprite = new AnimatedSprite(getPositionX(), getPositionY(),
				mDiceTextureRegion, activity.getEngine()
						.getVertexBufferObjectManager());
	}

	public int animatedColum;

	public int animatedRow;

	public AnimatedSprite animatedSprite;

	public int animatedWidth;

	public int animatedHeight;
}
