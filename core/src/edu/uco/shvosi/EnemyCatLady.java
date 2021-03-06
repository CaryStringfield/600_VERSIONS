package edu.uco.shvosi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public class EnemyCatLady extends Antagonist {
    
    private Animation catLadyWalk; 
    private Animation catLadyAttack; 
    private boolean flip = false;
    private float elapsedTime;
    private TextureRegion temp;
    private int bernardX; 
    private int bernardY; 
    private String XorY; 
    private int xdis; 
    private int ydis; 
    private int damage = 6; 
    private boolean active = false;
    private boolean moved = false;
    private DamageEntity melee; 
    private DamageEntity self; 
    private int turns= 0;
    private boolean isCat = false;
    
    public EnemyCatLady(int cX, int cY) {
        super(Constants.EnemyType.CATLADY, TextureLoader.CATLADYTEXTURE, cX, cY);
        catLadyWalk = TextureLoader.catLadyWalk;
        catLadyAttack = TextureLoader.catLadyAttack;
        this.range = 2;
        this.name = "CatLady";
        super.xpValue = 30;
        this.health = 60; 
        this.maxHealth = this.health; 
        this.damage = damage; 
        melee = new DamageEntity(0,0,this.damage); 
        //self = new DamageEntity(0,0, this.damage); 
    }

    @Override
    public void attackAction() {
        //Do Attack Stuffs?
        
        melee.setCX(bernardX);
        melee.setCY(bernardY);
        //self.setCX(this.cX);
        //self.setCY(this.cY);
        melee.setDead(false);
        //self.setDead(false);
        Map.miscEntityList.add(melee);
        //Map.miscEntityList.add(self);
        
        this.addAction(this.finishTurn());
    }
    
        @Override
    public void draw(Batch batch, float alpha) {
        super.draw(batch, alpha);
    
                
            elapsedTime += Gdx.graphics.getDeltaTime();
            if(range == 1 && Math.abs(xdis) <=range && Math.abs(ydis) <=range)
            {
                isCat = true;
            }
            if(isCat)
            {
                if(xdis >=0)
                {
                    flip = true;
                }
                else
                {
                    flip = false;
                }
                
                if (flip) {
                    temp = this.catLadyAttack.getKeyFrame(elapsedTime);
                    temp.flip(true, false);
                    batch.draw(temp, this.getX(),getY(), Constants.TILEDIMENSION, Constants.TILEDIMENSION);
                    temp.flip(true, false);
                } else {
                    batch.draw(catLadyAttack.getKeyFrame(elapsedTime), this.getX(), this.getY(), Constants.TILEDIMENSION , Constants.TILEDIMENSION);
                }
                if (catLadyAttack.isAnimationFinished(elapsedTime)) {
                    moving = false;
                    elapsedTime = 0f;
                }
            }  
            
            else{          
                if(xdis >=0)
                {
                    flip = true;
                }
                else
                {
                    flip = false;
                }
                
                if (flip) {
                    temp = catLadyWalk.getKeyFrame(elapsedTime);
                    temp.flip(true, false);
                    batch.draw(temp, this.getX(),getY(), Constants.TILEDIMENSION, Constants.TILEDIMENSION);
                    temp.flip(true, false);
                } else {
                    batch.draw(catLadyWalk.getKeyFrame(elapsedTime), this.getX(), this.getY(), Constants.TILEDIMENSION , Constants.TILEDIMENSION);
                }
                if (catLadyWalk.isAnimationFinished(elapsedTime)) {
                    moving = false;
                    elapsedTime = 0f;
                }
            }       
    }

    
    @Override
    public void calculateTurn(Constants.MapGridCode[][] mapGrid, Constants.EntityGridCode[][] entityGrid, List<Entity> entityList) {
        //Random movement
        int tries = 0;
        Constants.Direction d = Constants.Direction.NONE;

            for(int i = 0; i < entityList.size(); i++)//get bernards location
            {
                if(entityList.get(i).getGridCode() == Constants.EntityGridCode.PLAYER){
                    bernardX = entityList.get(i).getCX();
                    bernardY = entityList.get(i).getCY();
                    break;
                }
            }
          
            xdis = this.getCX() - bernardX;//get dis between me and bernard on x axis
            ydis = this.getCY() - bernardY;//get dis between me and bernard on y axis
            
            //if bernard is less than 5 spaces away I become active
            if(Math.abs(xdis) < 5 && Math.abs(ydis) < 5)
            {
                active = true;
            }
        
            if (active)//active charater can move and attack
            {
                turns ++;
                if(!isCat && health < maxHealth)
                {
                    turns = 10;
                }
                if(turns > 9)
                {
                    range = 1;
                    if(health < maxHealth)
                    {
                        if(health > 50)
                        {
                            health = maxHealth;
                        }
                        else
                        {
                            health +=20;
                        }
                        turns = 0;
                    }//end tutn > 9
                       
                }
                int distanceDown = 0;
                int distanceUp = 0;
                int distanceRight = 0;
                int distanceLeft = 0;
                
                if(Math.abs(xdis)> range || Math.abs(ydis)> range)//moves to attack position
                {
                    if(Math.abs(xdis) > Math.abs(ydis))
                    {
                        XorY="X";
                    }
                    else
                    {
                        XorY="Y";
                    }
                    
                if("X".equals(XorY) && xdis > range)//need to go left
                {
                for(distanceDown=0; distanceDown < 5; distanceDown++)//get shortest distance around verticle obstacle
                {
                    if(mapGrid[this.cX-1][this.cY-distanceDown]== Constants.MapGridCode.FLOOR)
                    {
                        break;
                    }
                }
                for (distanceUp = 0; distanceUp < 5; distanceUp++ )
                {
                        if(mapGrid[this.cX-1][this.cY+distanceUp]== Constants.MapGridCode.FLOOR)
                        {
                            break;
                        }
                }
                if(this.canMove(Constants.Direction.LEFT,mapGrid,entityGrid))
                {
                    d = Constants.Direction.LEFT;//try to go left
                }
                else if (this.canMove(Constants.Direction.UP_LEFT,mapGrid,entityGrid))
                {
                    d = Constants.Direction.UP_LEFT;//if something in the way try to go around
                }
                else if (this.canMove(Constants.Direction.DOWN_LEFT,mapGrid,entityGrid))
                {
                    d = Constants.Direction.DOWN_LEFT;//try to go around
                }
                else if(distanceDown>=distanceUp)
                {
                    if (this.canMove(Constants.Direction.UP,mapGrid,entityGrid))
                    {       
                        d = Constants.Direction.UP;
                    }
                }
                else if (distanceDown<distanceUp)
                {
                    if (this.canMove(Constants.Direction.DOWN,mapGrid,entityGrid))
                    {       
                        d = Constants.Direction.DOWN;
                    }
                }
            }//end try to go left
                    
            if("X".equals(XorY) && xdis < range)//need to go right
            {
                for(distanceDown=0; distanceDown < 5; distanceDown++)//get shortest distance around verticle obstacle
                {
                    if(mapGrid[this.cX+1][this.cY-distanceDown]== Constants.MapGridCode.FLOOR)
                    {
                        break;
                    }
                }
                for (distanceUp = 0; distanceUp < 5; distanceUp++ )
                {
                        if(mapGrid[this.cX+1][this.cY+distanceUp]== Constants.MapGridCode.FLOOR)
                        {
                            break;
                        }
                }
                if(this.canMove(Constants.Direction.RIGHT,mapGrid,entityGrid))
                {
                    d = Constants.Direction.RIGHT;//try right
                }
                else if (this.canMove(Constants.Direction.UP_RIGHT,mapGrid,entityGrid))
                {
                    d = Constants.Direction.UP_RIGHT;//go around
                }
                else if (this.canMove(Constants.Direction.DOWN_RIGHT,mapGrid,entityGrid))
                {
                    d = Constants.Direction.DOWN_RIGHT;//go around
                }
                else if(distanceDown>=distanceUp)
                {
                    if (this.canMove(Constants.Direction.UP,mapGrid,entityGrid))
                    {       
                         d = Constants.Direction.UP;
                    }
                }
                else if (distanceDown<distanceUp)
                {
                    if (this.canMove(Constants.Direction.DOWN,mapGrid,entityGrid))
                    {       
                        d = Constants.Direction.DOWN;
                    }
                }
                     
                }//end go right
                if("Y".equals(XorY) && ydis > range)//need to go down
                 {
                    for(distanceRight=0; distanceRight < 5; distanceRight++)//get shortest distance around verticle obstacle
                    {
                        if(mapGrid[this.cX+distanceRight][this.cY-1]== Constants.MapGridCode.FLOOR)
                        {
                            break;
                        }
                    }
                    for (distanceLeft = 0; distanceLeft < 5; distanceLeft++ )
                    {
                        if(mapGrid[this.cX-distanceLeft][this.cY-1]== Constants.MapGridCode.FLOOR)
                        {
                            break;
                        }
                    }
                    if(this.canMove(Constants.Direction.DOWN,mapGrid,entityGrid))
                    {
                        d = Constants.Direction.DOWN;//try down
                    }
                    else if (this.canMove(Constants.Direction.DOWN_LEFT,mapGrid,entityGrid))
                    {
                        d = Constants.Direction.DOWN_LEFT;//go around
                    }
                    else if (this.canMove(Constants.Direction.DOWN_RIGHT,mapGrid,entityGrid))
                    {
                        d = Constants.Direction.DOWN_RIGHT;//go aroud
                    }
                    else if(distanceLeft>=distanceRight)
                        {
                            if (this.canMove(Constants.Direction.RIGHT,mapGrid,entityGrid))
                            {       
                                d = Constants.Direction.RIGHT;
                            }
                        }
                    else if (distanceLeft<distanceRight)
                        {
                            if (this.canMove(Constants.Direction.LEFT,mapGrid,entityGrid))
                            {       
                                d = Constants.Direction.LEFT;
                            }
                        }
                        
                }//end down
                if("Y".equals(XorY) && ydis < range)//need to go up
                {
                    for(distanceRight=0; distanceRight < 5; distanceRight++)//get shortest distance around verticle obstacle
                    {
                        if(mapGrid[this.cX+distanceRight][this.cY+1]== Constants.MapGridCode.FLOOR)
                        {
                            break;
                        }
                    }
                    for (distanceLeft = 0; distanceLeft < 5; distanceLeft++ )
                    {
                        if(mapGrid[this.cX-distanceLeft][this.cY+1]== Constants.MapGridCode.FLOOR)
                        {
                            break;
                        }
                    }                    
                    if(this.canMove(Constants.Direction.UP,mapGrid,entityGrid))
                    {
                        d = Constants.Direction.UP;//try up
                    }
                    else if (this.canMove(Constants.Direction.UP_LEFT,mapGrid,entityGrid))
                    {
                        d = Constants.Direction.UP_LEFT;//go around
                    }
                    else if (this.canMove(Constants.Direction.UP_RIGHT,mapGrid,entityGrid))
                    {
                        d = Constants.Direction.UP_RIGHT;//go around
                    }
                    else if(distanceLeft>=distanceRight)
                        {
                            if (this.canMove(Constants.Direction.RIGHT,mapGrid,entityGrid))
                            {       
                                d = Constants.Direction.RIGHT;
                            }
                        }
                    else if (distanceLeft<distanceRight)
                        {
                            if (this.canMove(Constants.Direction.LEFT,mapGrid,entityGrid))
                            {       
                                d = Constants.Direction.LEFT;
                            }
                        }
                       
        }//end up
            this.setTurnAction(Constants.TurnAction.MOVE);
            }//end move to one spot away
               if(range == 1 && Math.abs(xdis) <=range && Math.abs(ydis) <=range)
                {
                   this.setTurnAction(Constants.TurnAction.ATTACK);

                }                            
                 
            }//end if active
    }//end function
    
    @Override
    public void collision(Entity entity){
        
    }

}




















