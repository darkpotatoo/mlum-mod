package me.darkpotatoo.mlumm.client.misc;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import java.util.LinkedList;
import java.util.Queue;

public class TickScheduler {
    private static final Queue<Runnable> tasks = new LinkedList<>();

    public static void schedule(Runnable task) {
        tasks.add(task);
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (!tasks.isEmpty()) {
                tasks.poll().run();
            }
        });
    }
}
