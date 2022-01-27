package io.github.xmchxup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author huayang (sunhuayangak47@gmail.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Save {
    private boolean gameContinued;

    private Position playerPosition;

    private List<Position> enemyPositions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Position {
        private int x, y;
        private Direction direction;
    }
}
