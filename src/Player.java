public class Player {
    public int playerId;
    public int wins = 0;
    public boolean playing = true;

    Player(int playerId){
        this.playerId = playerId;
    }

    public void won(){
        wins++;
    }

    public int getPlayerId() {
        return playerId;
    }

}
