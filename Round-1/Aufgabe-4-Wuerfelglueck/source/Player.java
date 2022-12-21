public class Player {
    //___Create Variables___//
    private final int numberOfTokens = 4;
    private final int[] positions = new int[numberOfTokens];
    private int finishedTokens = 0;
    public Player(){
        //___Set default values___//
        for(int i = 0; i < numberOfTokens; i++){
            positions[i] = 0;
        }
    }
    public int getPosition(int index){
        return positions[index];
    }
    public boolean setPosition(int index,int n){
        //___reade old position___//
        int oldPosition = positions[index];
        //___calculate new Position___//
        int newPosition = oldPosition+n;
        //___Check if move is possible___//
        if(isFieldEmpty(newPosition)){
            if(newPosition <= 44){
                positions[index] = newPosition;
                if(newPosition == 44- finishedTokens){
                    finishedTokens +=1;
                }
                return true;
            }
        }
        return false;
    }
    public boolean isFieldEmpty(int field){
        //___Check if field is empty___//
        for(int i = 0; i < numberOfTokens; i++){
            if(positions[i] == field){
                return false;
            }
        }
        return true;
    }
    public void kill(int field){
        //___Kill token at field___//
        for(int i = 0; i < numberOfTokens; i++){
            if(positions[i] == field){
                positions[i] = 0;
                return;
            }
        }
    }
    public int getFinishedTokens() {
        return finishedTokens;
    }
}