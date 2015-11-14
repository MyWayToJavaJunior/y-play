package yplay.core.player;

import yplay.core.config.GlobalConfig;

public class ExternalVideoPlayer implements VideoPlayer {
    private final static String DEFAULT_COMMAND = "vlc [$s]";
    private final static String DEFAULT_WILDCARD = "[$s]";

    private String command;
    private String wildcard;

    public ExternalVideoPlayer() {
        command = GlobalConfig.getInstance().getValue("player", "command", DEFAULT_COMMAND);
        wildcard = GlobalConfig.getInstance().getValue("player", "wildcard", DEFAULT_WILDCARD);
    }

    @Override
    public void play(String url) {
        String cmd = command.replace(wildcard, url);
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(cmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
