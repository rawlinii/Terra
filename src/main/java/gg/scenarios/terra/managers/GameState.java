package gg.scenarios.terra.managers;

import lombok.Getter;

@Getter
public enum GameState {
    LOBBY,
    SCATTERING,
    BIDDING,
    CHOOSING,
    STARTED;
}