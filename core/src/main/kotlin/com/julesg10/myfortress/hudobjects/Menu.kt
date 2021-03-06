package com.julesg10.myfortress.hudobjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.julesg10.myfortress.GameScreen

class Menu(position: Vector2,font: BitmapFont?) : HudObj(position) {
    private val btnList: MutableList<Pair<Button, String>> = mutableListOf();
    var menuState: MenuState = MenuState.MENU_DEFAULT;

    init {
        this.font = font;
    }

    enum class MenuState {
        START_GAME,
        MENU_DEFAULT,
        MENU_PAUSE
    }

    fun loadTextures() {
        val btnTextures = TextureRegion.split(Texture(Gdx.files.internal("btn.png")), 100, 50);
        val btnTexturesList: MutableList<TextureRegion> = mutableListOf();


        for (i in 0 until btnTextures.size) {
            for (j in 0 until btnTextures[i].size) {
                btnTexturesList.add(btnTextures[i][j])
            }
        }
        btnTexturesList.removeLast();
        val activeBtnTextures = mutableListOf<TextureRegion>()
        activeBtnTextures.addAll(btnTexturesList)

        val btnStartSize = Button.getSize("Start", this.font);
        this.btnList.add(
            Pair(
                Button(
                    Button.positionManagement(GameScreen.percent_to_worldValue(Vector2(50f,5f)),btnStartSize,1,2),
                    btnStartSize,
                    "Start",
                    "GO",
                    btnTexturesList,
                    activeBtnTextures.subList(1, btnTexturesList.size).toList(),
                    font
                ),
                "start_btn"
            )
        );
    }

    override fun render(hudBatch: Batch) {
        if (this.menuState == MenuState.MENU_DEFAULT) {

            val layout = GlyphLayout(font, "My Fortress");
            HUDText(
                this.font,
                hudBatch,
                position = Vector2((GameScreen.world_size().x - layout.width)/2, GameScreen.world_size().y*0.8f),
                width = 0f,
                str = arrayOf("My Fortress")
            )


            for (pBtn in this.btnList) {
                pBtn.first.render(hudBatch);
            }
        }
    }

    fun updateState(delta: Float, camera: Camera): MenuState {
        if (this.menuState == MenuState.MENU_DEFAULT) {

            val camPosition = Vector2(camera.position.x, camera.position.y)

            for (pBtn in this.btnList) {
                if (pBtn.first.isclick(delta, camPosition) && pBtn.first.eventClick) {
                    pBtn.first.eventClick = false;
                    when (pBtn.second) {
                        "start_btn" -> {
                            //println(pBtn.first.clickCount())
                            this.menuState = MenuState.START_GAME;
                        }
                    }
                }
            }
        }

        return this.menuState;
    }
}
