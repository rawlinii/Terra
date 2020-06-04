package gg.scenarios.terra.managers;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Game {

    private String host;
    private int count;
    private String matchPost;
    private String gameType;
    private String time;
    private String ip;
    private List<String> scenarios;
}