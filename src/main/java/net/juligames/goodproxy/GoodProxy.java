package net.juligames.goodproxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class GoodProxy {


    private static final @NotNull Logger logger = LogManager.getLogger(GoodProxy.class);
    private static final @NotNull Logger rootLogger = LogManager.getRootLogger();

    public static void main(String @NotNull [] args) {
        SpringApplication.run(GoodProxy.class, args);

        rootLogger.info("""
                \n                         _____                                                                         _____\s
                                        ( ___ )-----------------------------------------------------------------------( ___ )
                                         |   |                                                                         |   |\s
                                         |   |       _____                    _ ______                                 |   |\s
                                         |   |      |  __ \\                  | || ___ \\                                |   |\s
                                         |   |      | |  \\/  ___    ___    __| || |_/ / _ __   ___  __  __ _   _       |   |\s
                                         |   |      | | __  / _ \\  / _ \\  / _` ||  __/ | '__| / _ \\ \\ \\/ /| | | |      |   |\s
                                         |   |      | |_\\ \\| (_) || (_) || (_| || |    | |   | (_) | >  < | |_| |      |   |\s
                                         |   |       \\____/ \\___/  \\___/  \\__,_|\\_|    |_|    \\___/ /_/\\_\\ \\__, |      |   |\s
                                         |   |                                                              __/ |      |   |\s
                                         |   |                                                             |___/       |   |\s
                                         |___|         By Ture Bentzin <ture.bentzin@alumni.fh-aachen.de>              |___|\s
                                        (_____)-----------------------------------------------------------------------(_____)
                """);
        logger.info("This proxy will make your accounting experience accountable again!");
    }

}
