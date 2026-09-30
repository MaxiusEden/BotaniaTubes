package org.filip.botaniatubes;

import net.minecraft.Util;
import vazkii.botania.api.block.PetalApothecary.State;

public class FluidLevelAnimation {

    private static final float LEVELS_PER_SECOND = 2F;
    private static final long SNAP_AFTER_MILLIS = 1000;

    private State state = State.EMPTY;
    private float level;
    private long lastUpdate = -1;

    public void update(State targetState, float target) {
        long now = Util.getMillis();
        long elapsed = now - lastUpdate;
        lastUpdate = now;

        if (elapsed > SNAP_AFTER_MILLIS) {
            level = target;
        } else {
            float step = elapsed / 1000F * LEVELS_PER_SECOND;
            level = level < target ? Math.min(target, level + step) : Math.max(target, level - step);
        }

        if (targetState != State.EMPTY) {
            state = targetState;
        } else if (level <= 0F) {
            state = State.EMPTY;
        }
    }

    public State state() {
        return state;
    }

    public float level() {
        return level;
    }
}
