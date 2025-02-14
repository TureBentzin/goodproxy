package net.juligames.goodproxy;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.logging.Logger;

@SpringBootApplication
public class GoodProxy {

    public static void main(String @NotNull [] args) {
        SpringApplication.run(GoodProxy.class, args);
    }

    @Bean
    public @NotNull String welcome(@NotNull ApplicationContext context) {
        Logger log = Logger.getLogger(GoodProxy.class.getName());
        log.info("\n" +
                "\n" +
                " _____                                                                         _____ \n" +
                "( ___ )-----------------------------------------------------------------------( ___ )\n" +
                " |   |                                                                         |   | \n" +
                " |   |       _____                    _ ______                                 |   | \n" +
                " |   |      |  __ \\                  | || ___ \\                                |   | \n" +
                " |   |      | |  \\/  ___    ___    __| || |_/ / _ __   ___  __  __ _   _       |   | \n" +
                " |   |      | | __  / _ \\  / _ \\  / _` ||  __/ | '__| / _ \\ \\ \\/ /| | | |      |   | \n" +
                " |   |      | |_\\ \\| (_) || (_) || (_| || |    | |   | (_) | >  < | |_| |      |   | \n" +
                " |   |       \\____/ \\___/  \\___/  \\__,_|\\_|    |_|    \\___/ /_/\\_\\ \\__, |      |   | \n" +
                " |   |                                                              __/ |      |   | \n" +
                " |   |                                                             |___/       |   | \n" +
                " |___|                                                                         |___| \n" +
                "(_____)-----------------------------------------------------------------------(_____)\n" +
                "\n");
        return "Hello, world!";

    }

}
