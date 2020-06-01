package gg.scenarios.terra.database;

import gg.scenarios.terra.Terra;
import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

@Getter
public class Redis {

    private RedissonClient client;

    private Terra terra;


    public Redis (Terra terra) {
        this.terra = terra;

        Config config = new Config();
        config.useSingleServer().setAddress("157.245.227.156:6379");
        config.useSingleServer().setPassword("Mondosbeach1");

        this.client = Redisson.create(config);
    }
}
