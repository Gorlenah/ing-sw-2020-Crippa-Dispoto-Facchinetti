package it.polimi.ingsw.model.cards.effects.remove;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.parser.DeckReader;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RemoveBlockTest {
    final Player p1 = new Player("PlayerAres", LocalDate.now(), Color.BLUE);
    final Player p2 = new Player("PlayerDummy", LocalDate.now(), Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w2 = new Worker(p1);
    final Worker w3 = new Worker(p2);
    final DeckReader reader = new DeckReader();

    @Test
    void removeBlock() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Ares"));
        p2.setPlayerCard(d.getDivinityCard("Atlas"));
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        List<Worker> workersPlayer1 = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        workersPlayer1.add(w1);
        workersPlayer1.add(w2);
        p1.setPlayerWorkers(workersPlayer1);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        w2.setWorkerPosition(3,3);
        w3.setWorkerPosition(4,3);
        //Add blocks
        Battlefield.getBattlefieldInstance().getCell(3,2).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(3,2).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(2,4).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(2,4).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(2,4).getTower().addBlock(Block.LEVEL_3);
        Battlefield.getBattlefieldInstance().getCell(4,3).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(4,3).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_3);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.DOME);
        Battlefield.getBattlefieldInstance().getCell(4,2).getTower().addBlock(Block.DOME);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - PlayerAres
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Check GlobalEffect...
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),0,1);
        //5. CheckLocalWin...
        //6. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //7. Build()
        t.buildBlock(m.getSelectedWorker(),0,0);
        //8. CheckGlobalWin...
        //Special Effect Ares
        //9. Change Worker
        m.setSelectedWorker(t.changeWorkerPlayer(m.getSelectedWorker()));
        //10. Generate Building Matrix
        m.getSelectedWorker().setWorkerView(t.generateRemoveMatrix(m.getSelectedWorker()));
        //11. RemoveBlock()
        t.removeBlock(m.getSelectedWorker(),2,4);
        //12. PassTurn


        //ASSERT : We expect two builds, but the second with a different WorkerView (no perimeterCells)
        assertEquals(2, battlefield.getCell(3, 2).getTower().getHeight());
        assertEquals(4, battlefield.getCell(4, 4).getTower().getHeight());
        assertEquals(1, battlefield.getCell(0, 0).getTower().getHeight());
        assertEquals(2, battlefield.getCell(4, 3).getTower().getHeight());
        assertEquals(2, battlefield.getCell(2, 4).getTower().getHeight());
        assertEquals(1, battlefield.getCell(4, 2).getTower().getHeight());
        assertEquals(Block.DOME, battlefield.getCell(4, 2).getTower().getLastBlock());
        //control workerView
        Cell[][] workerView = w2.getWorkerView();
        assertNull(workerView[2][2]);
        assertNull(workerView[2][3]);
        assertNotNull(workerView[2][4]);
        assertNotNull(workerView[3][2]);
        assertNull(workerView[3][3]);
        assertNull(workerView[3][4]);
        assertNull(workerView[4][2]);
        assertNull(workerView[4][3]);
        assertNull(workerView[4][4]);
        //illegal remove
        assertThrows(RuntimeException.class, ()->t.removeBlock(m.getSelectedWorker(),4,4));
        //Clean battlefield for next tests
        battlefield.cleanField();
    }
}