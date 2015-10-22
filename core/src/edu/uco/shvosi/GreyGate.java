package edu.uco.shvosi;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;


public class GreyGate extends Antagonist {
    
    private TextureRegion temp;

    public GreyGate(int cX, int cY, Constants.Direction direction) {
        super(Constants.EnemyType.GREYGATE, TextureLoader.GREYGATELTEXTURE, cX, cY);
        if (direction == Constants.Direction.RIGHT){
            textureRegion.setTexture(TextureLoader.GREYGATERTEXTURE);
        }
        this.maxHealth = 100000;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        super.draw(batch, alpha);
    }
    
    @Override
    public void calculateTurn(Constants.MapGridCode[][] mapGrid, Constants.EntityGridCode[][] entityGrid, List<Entity> entityList) {
        
    }
    
    @Override
    public void collision(Entity entity) {
        if (entity instanceof Protagonist) {
            Protagonist bernard = (Protagonist) entity;
            Integer xCoordinate = bernard.getCX();
            Integer yCoordinate = bernard.getCY() + 1;

            if (xCoordinate == this.getCX() && yCoordinate == this.getCY() && bernard.getGreyKey()) {
                bernard.setHeldItem(0);
                GameScreen.invent.setImage(TextureLoader.INVENTORYTEXTURE);
                this.takeDamage(100000);

            }
        }
    }
    
}