package it.polimi.ingsw.model.cards.effects.basic;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.parser.DeckReader;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockUnderTest {

    final  Player p1 = new Player("Steve Jobs", LocalDate.now(), Color.BLUE);
    final Worker w1 = new Worker(p1);
    final DeckReader reader = new DeckReader();

    @Test
    void buildUnderYourself() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Zeus"));
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Steve Jobs
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //3. Build()
        t.buildBlock(m.getSelectedWorker(),m.getSelectedWorker().getRowWorker(),m.getSelectedWorker().getColWorker());

        //ASSERT : We expect a new block under the selected worker
        assertTrue(battlefield.getCell(m.getSelectedWorker().getRowWorker(),m.getSelectedWorker().getColWorker()).getTower().getHeight()==1);

        battlefield.cleanField();
    }

    @Test
    void blockUnderException() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Zeus"));
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Steve Jobs
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));

        //ASSERTS
        //Building outside the worker view
        Throwable expectedException = assertThrows(RuntimeException.class, () -> t.buildBlock(m.getSelectedWorker(),3,3));
        assertEquals("Unexpected Error!", expectedException.getMessage());

        battlefield.cleanField();
    }
}