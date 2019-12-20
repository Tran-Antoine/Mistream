package net.akami.mistream.core;

import net.akami.mistream.gamedata.BoostDataProvider;
import net.akami.mistream.gamedata.CarInfoProvider;
import net.akami.mistream.gamedata.DataProvider;
import net.akami.mistream.gamedata.GameState;
import net.akami.mistream.play.OutputSequence;
import net.akami.mistream.play.QueueHandler;
import net.akami.mistream.play.list.DiagonalKickoff;
import rlbot.flat.GameTickPacket;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MistreamQueueHandler extends QueueHandler {

    private MistreamDisplay display;

    public MistreamQueueHandler(MistreamDisplay display) {
        this.display = display;
    }

    @Override
    public void update(GameTickPacket packet) {
        super.update(packet);
        updateDisplay();
    }

    private void updateDisplay() {
        display.setText(super.currentSequence);
    }

    @Override
    protected List<Function<QueueHandler, OutputSequence>> loadPlaySuppliers() {
        // for now, Mistream is only able to do diagonal kickoffs. The rest of the time, it's afk
        // we all started somewhere didn't we ??
        return Arrays.asList(
                DiagonalKickoff::new
        );
    }

    @Override
    protected List<DataProvider> loadProviders() {
        return Arrays.asList(
                new BoostDataProvider(),
                new CarInfoProvider(),
                new GameState()
        );
    }
}
