package it.polimi.ingsw.model;

/**
 * Turn class represents a generic turn that must be specialized with a card effect
 */
public abstract class Turn {

    private final int N_VIEW_ROWS = 5;
    private final int N_VIEW_COLUMNS = 5;
    protected Match currentMatch;
    protected int movesLeft = 1;
    protected int blocksLeft = 1;
    protected int moves = 1;
    protected int blocks = 1;
    private boolean changeLevel;
    protected boolean reachedLevel3 = false;

    /**
     * Class Constructor
    *
     public Turn(Match currentMatch) {
        this.currentMatch = currentMatch;
    }
    */


    public Turn(){
        //null
    }

    /**
     *
     * @param currentMatch current Match
     */
    public void setCurrentMatch(Match currentMatch){
        this.currentMatch = currentMatch;
    }

    /**
     * Manages what happens at the end of the turn
     */
    public void passTurn(){
        currentMatch.nextPlayer();
    }
    /**
     * Get moves left in the turn
     * @return moves left
     */
    public int getMovesLeft(){
        return movesLeft;
    }

    /**
     * Get blocks left in the turn
     * @return blocks left
     */
    public int getBlocksLeft(){
        return blocksLeft;
    }
    /**
     * Abstract methods that are specialized by the sons of this class
     */
    public abstract void checkLocalCondition(Worker selectedWorker);
    public abstract void moveWorker(Worker selectedWorker, int newRow, int newCol);
    public abstract void buildBlock(Worker selectedWorker,int blockRow, int blockCol);

    public Cell[][] generateMovementMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        return battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent() && battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight());
        /*Cell[][] returnMatrix = battlefield.getWorkerView(selectedWorker, (cell)->battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight());
        returnMatrix[selectedWorker.getRowWorker()][selectedWorker.getColWorker()]=null;
        return returnMatrix;*/
    }

    public Cell[][] generateBuildingMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        /*Cell[][] returnMatrix = battlefield.getWorkerView(selectedWorker);
        returnMatrix[selectedWorker.getRowWorker()][selectedWorker.getColWorker()]=null;
        return returnMatrix;*/
        return battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent());
    }

    /**
     * Allows you to generate a RemoveMatrix without cells where there is a player or ground (Basic remove)
     * @param selectedWorker is the worker selected by the current player
     * @return return the RemoveMatrix
     */
    public Cell[][] generateRemoveMatrix(Worker selectedWorker){
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //setWorkerView without cells where there is a player or ground
        return battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                && !(cell.getTower().getHeight() == 0));
        //the user now chooses if to make the remove or not and pass the turn (controller)
    }

    /**
     * Allows you to Remove in cell(delBlockRow,delBlockCol) the Latest Block of the Tower
     * @param selectedWorker is the worker selected by the current player
     * @param delBlockRow is the x coordinate of the block to be deleted
     * @param delBlockCol is the y coordinate of the block to be deleted
     */
    public void removeBlock(Worker selectedWorker, int delBlockRow,int delBlockCol){
        //Check coordinates
        if(selectedWorker.getWorkerView()[delBlockRow][delBlockCol]==null)
            throw new RuntimeException("Unexpected Error!");
        //Delete Block
        Battlefield.getBattlefieldInstance().getCell(delBlockRow,delBlockCol).getTower().removeLatestBlock();
    }

    /**
     * Allows you to change selectedWorker with the other worker that has not been moved
     * Attention: you can't choose the next worker, is the next of the list playerWorkers (in Player)
     * Attention2: don't launch this method if you have only 1 worker (check)
     * @param selectedWorker is the worker selected by the current player
     * @return return the new Worker (different from input selectedWorker)
     */
    public Worker changeWorkerPlayer(Worker selectedWorker){
        //change selectedWorker in the other worker that has not been moved
        return selectedWorker.getOwnerWorker().getStationaryWorker(selectedWorker);
    }

    public void resetTurn(){
        this.movesLeft = this.moves;
        this.blocksLeft = this.blocks;
    }
}
