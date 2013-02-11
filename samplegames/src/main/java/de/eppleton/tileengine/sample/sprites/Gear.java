/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.eppleton.tileengine.sample.sprites;

import de.eppleton.fx2d.GameCanvas;
import de.eppleton.fx2d.Sprite;
import de.eppleton.fx2d.action.Behavior;
import de.eppleton.fx2d.tileengine.TileSet;
import java.util.Properties;
import java.util.logging.Logger;
import org.openide.util.Lookup;

/**
 *
 * @author antonepple
 */
public class Gear extends Sprite {

    private TileSet tileset;
    private Properties properties;
    private int layer;

    public Gear(TileSet tileset, Properties properties, GameCanvas parent, String name, double x, double y, int width, int height, int layer) {
        super(parent, name, x, y, width, height, Lookup.EMPTY);
        this.tileset = tileset;
        this.properties = properties;
        this.layer = layer;
        addBehaviour(new Behavior() {
            @Override
            public boolean perform(Sprite sprite, GameCanvas playingField) {
                if (sprite.getCollisionBox().intersects(playingField.getSprite("hero").getCollisionBox())) {
                    boolean taken = ((Hero) playingField.getSprite("hero")).offer(Gear.this);
                    if (taken) {
                        playingField.removeSprite(sprite);
                    }
                }
                return true;
            }
        });
    }

    public TileSet getTileset() {
        return tileset;
    }

    public String getProperty(String property) {
        return properties.getProperty(property);
    }

    public int getLayer() {
        return layer;
    }
    private static final Logger LOG = Logger.getLogger(Gear.class.getName());
}
