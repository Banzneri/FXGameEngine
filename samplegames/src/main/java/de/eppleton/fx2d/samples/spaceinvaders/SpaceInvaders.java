/**
 * This file is part of FXGameEngine 
 * A Game Engine written in JavaFX
 * Copyright (C) 2012 Anton Epple <info@eppleton.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://opensource.org/licenses/GPL-2.0.
 * 
 * For alternative licensing or use in closed source projects contact Anton Epple 
 * <info@eppleton.de>
 */package de.eppleton.fx2d.samples.spaceinvaders;

import de.eppleton.fx2d.beans.DoubleProperty;
import de.eppleton.fx2d.tileengine.action.TileSetAnimation;
import de.eppleton.fx2d.collision.*;
import de.eppleton.fx2d.*;
import de.eppleton.fx2d.Level;
import de.eppleton.fx2d.action.*;
import de.eppleton.fx2d.event.KeyCode;
import de.eppleton.fx2d.tileengine.*;
import java.util.Collection;
import java.util.logging.*;
import javax.xml.bind.JAXBException;
import net.java.html.canvas.GraphicsContext;
import net.java.html.canvas.Style;
import net.java.html.sound.AudioClip;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

public class SpaceInvaders extends Level {
    
    Points TEN = new Points(10);
    Points TWENTY = new Points(30);
    Points THIRTY = new Points(40);
    DoubleProperty invaderXVelocity = new DoubleProperty(0.3);
    AudioClip shootSound = AudioClip.create(SpaceInvaders.class.getResource("/assets/sound/shoot.wav").toString());
    AudioClip invaderKilledSound = AudioClip.create(SpaceInvaders.class.getResource("/assets/sound/invaderkilled.wav").toString());
    int score = 0;
    String message;
    int[][] enemies = new int[][]{
        {30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30},
        {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20},
        {20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20},
        {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
        {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10}
    };

    public SpaceInvaders(GraphicsContext graphicsContext, double playfieldWidth, double playfieldHeight, double viewPortWidth, double viewPortHeight) {
        super(graphicsContext, playfieldWidth, playfieldHeight, viewPortWidth, viewPortHeight);
    }
    
    
    
    @Override
    protected void initGame() {
        final Level canvas = this;
        try {
            TileSet invaders = TileMapReader.readSet("/assets/graphics/invaders1.tsx");
            TileSet playerTiles = TileMapReader.readSet("/assets/graphics/player.tsx");
            final TileSetAnimation animation30 = new TileSetAnimation(invaders, new int[]{0, 1}, 2);
            final TileSetAnimation animation10 = new TileSetAnimation(invaders, new int[]{4, 5}, 2);
            final TileSetAnimation animation20 = new TileSetAnimation(invaders, new int[]{2, 3}, 2);
            final TileSetAnimation playerAnimation = new TileSetAnimation(playerTiles, new int[]{0}, 100_000);
            for (int i = 0; i < enemies.length; i++) {
                int[] is = enemies[i];
                for (int j = 0; j < is.length; j++) {
                    Points points = is[j] == 30 ? THIRTY : is[j] == 20 ? TWENTY : TEN;
                    Sprite sprite = new Sprite(canvas, "" + ((j * 11) + i), 50 + (40 * j), 140 + (40 * i), 30, 20);
                    sprite.setUserObject(points);
                    canvas.addSprite(sprite);
                    sprite.setAnimation(is[j] == 30 ? animation30 : is[j] == 20 ? animation20 : animation10);
                    sprite.setVelocityXProperty(invaderXVelocity);
                }
            }
            Sprite player = new Sprite(canvas, playerAnimation, "Player", 350, 620, 40, 30);
            canvas.addSprite(player);
            player.setAnimation(playerAnimation);
            player.addAction(KeyCode.LEFT, ActionFactory.createMoveAction(playerAnimation, "left", -4, 0, 0, 0));
            player.addAction(KeyCode.RIGHT, ActionFactory.createMoveAction(playerAnimation, "right", 4, 0, 0, 0));
            player.addAction(KeyCode.UP, new ShootAction(playerAnimation, "fire", new BulletProvider(), new HitHandler(), shootSound));
        } catch (JAXBException ex) {
            Logger.getLogger(SpaceInvaders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        canvas.addLayer(new Background());
        canvas.addBehaviour(new MoveInvadersBehavior());

        canvas.addLayer(new SpriteLayer());
        canvas.start();
    }
    
  
   
    private class Points {
        
        int points;
        
        public Points(int points) {
            this.points = points;
        }
        
        public int getPoints() {
            return points;
        }
    }
    
    static class BulletProvider implements SpriteProvider {
        
        @Override
        public Sprite getSprite(Level parent, double x, double y) {
            final Sprite bullet = new Sprite(parent, "bullet", x, y + 10, 10, 20);
            parent.addSprite(bullet);
            return bullet;
        }
    }
    
    class HitHandler implements CollisionHandler {
        
        @Override
        public void handleCollision(Collision collision) {
            Points points = (Points) collision.getSpriteTwo().getUserObject();
            if (points != null) {
                score += points.getPoints();
                invaderKilledSound.play();
                collision.getSpriteOne().remove();
                collision.getSpriteTwo().remove();
            }
        }
    }
    
    class MoveInvadersBehavior extends Behavior {
        
        @Override
        public boolean perform(Level canvas, long nanos) {
            Collection<Sprite> sprites = canvas.getSprites();
            boolean stop = false;
            boolean win = true;
            for (Sprite sprite1 : sprites) {
                if (sprite1.getUserObject() != null && sprite1.getUserObject() instanceof Points) {
                    win = false;
                    if (sprite1.getX() > 650 || sprite1.getX() < 50) {
                        invaderXVelocity.set(-invaderXVelocity.doubleValue() * (stop ? 0 : 1.3));
                        if (sprite1.getY() >= 600) {
                            message = "Game Over!";
                            stop = true;
                            
                        }
                        for (Sprite sprite2 : sprites) {
                            if (sprite2.getUserObject() != null && sprite1.getUserObject() instanceof Points) {
                                sprite2.setY(sprite2.getY() + (stop ? 0 : 20));
                            }
                        }
                        break;
                    }
                }
            }
            if (win) {
                message = "You win!";
                canvas.stop();
            }
            return true;
        }
    }
    
    class Background extends Layer {        
        
        @Override
        public void draw(GraphicsContext graphicsContext, double x, double y, double width, double height) {
            graphicsContext.setFillStyle(new Style.Color("#000000"));
            graphicsContext.fillRect(0, 0, width, height);
            graphicsContext.setFillStyle(new Style.Color("#ffffff"));
            graphicsContext.setFont("20 'OCR A Std'");
            graphicsContext.fillText("SCORE<1>    HI-SCORE    SCORE<2>", 30, 30);
            graphicsContext.fillText("" + score + "            9990   ", 30, 60);
            graphicsContext.fillText(message, 300, 400);
            graphicsContext.fillText("" + 3 + "                   CREDIT " + 1, 30, 680);
            graphicsContext.setFillStyle(new Style.Color("#00ff00"));
            graphicsContext.fillRect(30, 650, 640, 4);
        }
    }
}
