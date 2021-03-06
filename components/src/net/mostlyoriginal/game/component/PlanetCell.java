package net.mostlyoriginal.game.component;

/**
 * @author Daan van Yperen
 */
public class PlanetCell {
    public static final int SPACE_AIR_STARTS_AT_DEPTH = 20;
    public int x;
    public int y;
    public int color = 0;
    public int down = -1;
    public float height = 0;
    public CellType type = CellType.STATIC;


    public CellType nextType = null;
    public int nextColor = -1;

    public static int[][] directions = {
            {-1, -1},
            {0, -1},
            {1, -1},
            {1, 0},
            {1, 1},
            {0, 1},
            {-1, 1},
            {-1, 0}
    };

    public int sleep;

    public int up() {
        return (down + 4) % 8;
    }

    public int left() {
        return (down + 6) % 8;
    }

    public int right() {
        return (down + 2) % 8;
    }

    public int upL() {
        return (down + 3) % 8;
    }

    public int downL() {
        return (down + 7) % 8;
    }

    public int downR() {
        return (down + 1) % 8;
    }

    public int upR() {
        return (down + 5) % 8;
    }

    public void activateNextType() {
        if (nextType != null) {
            type = nextType;
            nextType = null;
            sleep=0;
        }
        if (nextColor != -1) {
            color = nextColor;
            nextColor = -1;
        }
    }

    public float depth() {
        return ((G.SIMULATION_WIDTH / 2) - height);
    }

    public enum CellType {
        STATIC(null, false),
        LAVA(5f, true),
        WATER(1f, true),
        AIR(0f, true),
        ICE(null, false),
        STEAM(-0.5f, true),
        LAVA_CRUST(5f, true),
        CLOUD(-0.7f, false),
        FIRE(-0.7f, false),
        NOTHING(-100f,false),
        ORGANIC(null, false), ORGANIC_SPORE(-0.7f, false);

        public final Float density;
        private boolean flows;

        CellType(Float density, boolean flows) {
            this.density = density;
            this.flows = flows;
        }

        public boolean flows() {
            return flows;
        }

        public boolean isLighter(CellType type) {
            return type.density != null && density != null && type.density < density;
        }
    }

    public boolean isSpaceAir() {
        return depth() < SPACE_AIR_STARTS_AT_DEPTH && type == PlanetCell.CellType.AIR;
    }
}
