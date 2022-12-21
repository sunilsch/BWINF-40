public class Game {
    //___Create Variables___//
    Player[] player = new Player[2];
    int[][] cubes;
    int numberOfTokens = 4;
    boolean[] noSolution = new boolean[2];
    public Game(int[][] cubes){
        //___Create 2 Objects of player___//
        player[0] = new Player();
        player[1] = new Player();
        //___Save cube___//
        this.cubes = cubes;
    }
    public boolean move(int n, int p){
        if(!noSolution[p]) {
            //___Check Start-Field___//
            boolean fieldIsFree = player[p].isFieldEmpty(1);
            //___Move token away from start-field
            if (!fieldIsFree) {
                for (int i = 0; i < numberOfTokens; i++) {
                    if (player[p].getPosition(i) == 1) {
                        boolean answer = killAndSetPosition(1 + n, i, p, n);
                        if (answer) {
                            return n == 6;
                        } else {
                            break;
                        }
                    }
                }
            }
            //___Move token onto start-field___//
            if (n == 6 && fieldIsFree) {
                for (int i = 0; i < numberOfTokens; i++) {
                    int position = player[p].getPosition(i);
                    if (position == 0) {
                        if (killAndSetPosition(1, i, p, 1)) {
                            return true;
                        }
                    }
                }
            }
            //___"normal" movement___//
            int highestPosition = 0;
            int highestPositionIndex = 0;
            //___Find highest position___//
            for (int i = 0; i < numberOfTokens; i++) {
                int position = player[p].getPosition(i);
                if (position > highestPosition && position <= 44 - player[p].getFinishedTokens()) {
                    highestPosition = position;
                    highestPositionIndex = i;
                }
            }
            if (highestPosition != 0) {
                checkNoSolution(highestPosition, p);
                if (killAndSetPosition(highestPosition + n, highestPositionIndex, p, n)) {
                    return n == 6;
                }
            }
        }
        return false;
    }
    private boolean killAndSetPosition(int position, int positionIndex, int p, int n){
        //___Check if token can be moved___//
        if(player[p].setPosition(positionIndex,n)){
            //___Check if token can be killed__//
            if(position <= 40){
                int killPosition;
                if(position <= 20){
                    killPosition = position + 20;
                } else{
                    killPosition = position - 20;
                }
                player[p^1].kill(killPosition);
            }
            return true;
        }
        return false;
    }

    private void checkNoSolution(int highestPosition, int p){
        //___Check if there is no possible move___//
        if(highestPosition+cubes[p][0] > 44-player[p].getFinishedTokens()){
            noSolution[p] = true;
        }
    }

    public boolean isFinished(){
        //___Check if one player is finished___//
        return player[0].getFinishedTokens() == 4 || player[1].getFinishedTokens() == 4 || (noSolution[0] && noSolution[1]);
    }
    public int whoHasWon() {
        //___Check which player has finished___//
        if (player[0].getFinishedTokens() == 4) {
            return 0;
        } else if(player[1].getFinishedTokens() == 4){
            return 1;
        } else{
            return 2;
        }
    }
}