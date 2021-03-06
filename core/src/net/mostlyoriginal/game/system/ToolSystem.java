package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.component.ui.Clickable;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.dilemma.CardScriptSystem;
import net.mostlyoriginal.game.system.dilemma.CardSystem;
import net.mostlyoriginal.game.system.planet.PlanetSimulationSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class ToolSystem extends FluidIteratingSystem {
    public static final int DEGREE_SPACING = 20;
    public static final int MALL_SIZE = 3;
    public static final int SMALL_SIZE = 1;
    public static final int MEDIUM_SIZE = 3;
    public static final int LARGE_SIZE = 11;
    private TagManager tagManager;
    private GameScreenAssetSystem gameScreenAssetSystem;
    private PlanetSimulationSystem planetSimulationSystem;
    private Vector2 v = new Vector2();
    public boolean gameEnded = false;
    private CardSystem cardSystem;
    private DrawingSystem drawingSystem;
    private CardScriptSystem cardScriptSystem;
    private String active;
    private boolean pressed = false;
    private float cooldown;

    public ToolSystem() {
        super(Aspect.all(Tool.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
        int y1 = 14;
        int pos = 48 - y1;
        createTool("button_doze", pos += 32, y1);
        createTool("button_water", pos += 32, y1);
        createTool("button_magma", pos += 32, y1);
        createTool("button_dolphin", pos += 32, y1);
        createTool("button_person", pos += 32, y1);
        createTool("button_stone", pos += 32, y1);
        createTool("button_alien", pos += 32, y1);
        createTool("button_rocket", pos += 32, y1);
        createTool("button_explode", pos += 32, y1);
        createTool("button_building", pos += 32, y1);
        createTool("button_organic", pos += 32, y1);
        createTool("button_clouds", pos += 32, y1);

        pos = ((G.SCREEN_WIDTH / 2)) / G.CAMERA_ZOOM - 96 / 2 - 40;
        int y = 48 + 4;
        createTool("button_small", pos += 32, y);
        createTool("button_medium", pos += 32, y);
        createTool("button_large", pos += 32, y);
        createTool("button_reset", pos += 110, y);

    }

    @Override
    protected void begin() {
        super.begin();
        if (cooldown > 0) {
            cooldown -= world.delta;
        }
    }

    private void activateAchievement(String achievementId) {
        E e = E.E(tagManager.getEntityId(achievementId));
        if (!e.hasAchievement()) {
            e
                    .tint(1f, 1f, 1f, 1f)
                    .clickable()
                    .bounds(0, 0, 32, 32)
                    .achievementId(achievementId);
            gameScreenAssetSystem.playSfx("LD_troop_amazing");
            cardSystem.spawnCard(achievementId.toUpperCase());
        }
    }

    private void createTool(String id, int x, int y) {
        if (!tagManager.isRegistered(id)) {
            E.E()
                    .toolId(id)
                    .pos(10 + x, y)
                    .bounds(0, 0, "button_reset".equals(id) ? 64 : 32, 32)
                    .anim(id)
                    .animSpeed(0)
                    .clickable()
                    .renderLayer(G.LAYER_CARDS)
                    .tag(id)
                    .renderLayer(G.LAYER_ACHIEVEMENTS);
        }
    }

    boolean down = true;

    @Override
    protected void process(E e) {
        if (e.hasClickable()) {
            boolean hovering = e.clickableState() == Clickable.ClickState.HOVER;
            e.scale(hovering ? 1.0f : 1f);
            boolean isActive = e.toolId().equals(active);
            if ("button_small".equals(e.toolId()) && drawingSystem.getSize() == SMALL_SIZE) isActive = true;
            if ("button_medium".equals(e.toolId()) && drawingSystem.getSize() == MEDIUM_SIZE) isActive = true;
            if ("button_large".equals(e.toolId()) && drawingSystem.getSize() == LARGE_SIZE) isActive = true;
            e.animAge(isActive ? 1.1f : (hovering ? 0.6f : 0f));
            if (e.clickableState() == Clickable.ClickState.CLICKED) {
                if (!"button_small".equals(e.toolId())
                        && !"button_medium".equals(e.toolId())
                        && !"button_large".equals(e.toolId())) {
                    active = e.toolId();
                }
                PlanetCell.CellType type = null;
                if ("button_magma".equals(e.toolId())) type = PlanetCell.CellType.LAVA;
                else if ("button_water".equals(e.toolId())) type = PlanetCell.CellType.WATER;
                else if ("button_doze".equals(e.toolId())) type = PlanetCell.CellType.AIR;
                else if ("button_dolphin".equals(e.toolId())) {
                    drawingSystem.startDrawing(EntityType.DOLPHIN);
                    //cardScriptSystem.randomizeDolphin();
                } else if ("button_person".equals(e.toolId())) {
                    drawingSystem.startDrawing(EntityType.DUDE);
                    //cardScriptSystem.spawnDudes();;
                } else if ("button_stone".equals(e.toolId())) {
                    type = PlanetCell.CellType.STATIC;
                } else if ("button_small".equals(e.toolId())) {
                    drawingSystem.setSize(SMALL_SIZE);
                } else if ("button_medium".equals(e.toolId())) {
                    drawingSystem.setSize(MEDIUM_SIZE);
                } else if ("button_large".equals(e.toolId())) {
                    drawingSystem.setSize(LARGE_SIZE);
                } else if ("button_alien".equals(e.toolId())) {
                    drawingSystem.startDrawing(EntityType.ALIEN);
                    //cardScriptSystem.spawnAccelerators();
                } else if ("button_rocket".equals(e.toolId())) {
                    drawingSystem.startDrawing(EntityType.ICBM);
                    //cardScriptSystem.spawnIcbms();
                } else if ("button_explode".equals(e.toolId())) {
                    cardScriptSystem.triggerExplosives();
                } else if ("button_building".equals(e.toolId())) {
                    drawingSystem.startDrawing(EntityType.SKYSCRAPER);
                    //cardScriptSystem.spawnSkyscrapers();
                } else if ("button_organic".equals(e.toolId())) {
                    type = PlanetCell.CellType.ORGANIC_SPORE;
                } else if ("button_clouds".equals(e.toolId())) {
                    type = PlanetCell.CellType.CLOUD;
                } else if ("button_reset".equals(e.toolId())) {
                    if (cooldown <= 0) {
                        cooldown = 1;
                        cardScriptSystem.restartGame();
                        e.clickableState(Clickable.ClickState.NONE);
                    }
                }
                ;

                if (type != null) {
                    drawingSystem.startDrawing(type);
                }
            }
        }
    }
}
