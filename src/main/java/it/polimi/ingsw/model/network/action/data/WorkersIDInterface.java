package it.polimi.ingsw.model.network.action.data;

import java.util.List;

/**
 * WorkersIDInterface class represent response to getWorkers request
 */
public class WorkersIDInterface {
    private String action;
    private List<Integer> data;

    public WorkersIDInterface(String action, List<Integer> data){
        this.action = action;
        this.data = data;
    }
}