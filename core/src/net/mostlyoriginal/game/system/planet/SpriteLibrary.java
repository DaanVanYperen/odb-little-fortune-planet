package net.mostlyoriginal.game.system.planet;

import net.mostlyoriginal.game.component.SpriteData;

/**
 * Repository for all cards.
 */
public class SpriteLibrary {
    public net.mostlyoriginal.game.component.SpriteData[] sprites;

    public SpriteLibrary() {
    }

    /**
     * Return dilemma, or <code>null</code> if empty.
     */
    public SpriteData getById(String id) {
        for (SpriteData sprite : sprites) {
            if (sprite.id != null && sprite.id.equals(id)) return sprite;
        }
        return null;
    }
}
