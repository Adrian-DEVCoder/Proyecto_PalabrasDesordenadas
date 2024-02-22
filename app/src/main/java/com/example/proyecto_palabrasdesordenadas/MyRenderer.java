package com.example.proyecto_palabrasdesordenadas;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.Renderer;

public class MyRenderer extends Renderer {
    private Object3D mObjectGroup;
    private Texture mCurrentTexture; // Añade esta línea

    public MyRenderer(Context context){
        super(context);
    }

    @Override
    protected void initScene(){
        DirectionalLight mLight=new DirectionalLight(0, -1, -1);
        mLight.setPower(2);
        getCurrentScene().addLight(mLight);
    }

    @Override
    public void onRender(long elapsedRealTime,double deltaTime){
        super.onRender(elapsedRealTime, deltaTime);
        if (mObjectGroup != null) {
            mObjectGroup.rotate(Vector3.Y,(float)Math.toRadians(50));
        }
    }

    public void loadModel(int modelId, int textureId) throws ParsingException, ATexture.TextureException {
        if (mObjectGroup != null) {
            getCurrentScene().removeChild(mObjectGroup);
        }
        if (mCurrentTexture != null) {
            getTextureManager().removeTexture(mCurrentTexture);
        }
        LoaderOBJ loader = new LoaderOBJ(mContext.getResources(), mTextureManager, modelId);
        loader.parse();
        mObjectGroup = loader.getParsedObject();
        Material material = new Material();
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.enableLighting(true);
        mCurrentTexture = new Texture("myTexture", textureId); // Asegúrate de usar el ID correcto de la textura
        material.addTexture(mCurrentTexture);
        mObjectGroup.setMaterial(material);
        getCurrentScene().addChild(mObjectGroup);
        getCurrentCamera().setPosition(new Vector3(0,  4.5,  10));
        getCurrentCamera().setLookAt(mObjectGroup.getPosition());
    }


    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
