package com.webcheckers.model;

/**
 * @author <a href='mailto:nda7419@rit.edu'>Nicholas Antiochos</a>
 */
public class AITurn {

    /**
     * Mimics the player's PostValidateMove and PostSubmitTurn sequence
     * @param game - Game containing an AI second player
     */
    public static void makeTurn(CheckersGame game){
        Move move = TurnUtil.hasValidMoves(game.getBoard(), Piece.Color.WHITE);
        if(move == null){
            game.endGame(game.getWhitePlayer());
            return;
        }
        Piece piece = game.getBoard().getPieceAtSpace(move.getStartRow(), move.getStartCell());
        TurnUtil.isMoveValid(piece.getType(), move.getStart(), move.getEnd(), game);
        game.addMove(move);
        int[] diffs = TurnUtil.findDifferences(move.getStart(), move.getEnd());
        if(TurnUtil.isMoveJump(diffs[0], diffs[1])){
            while(true){
                move = TurnUtil.hasValidJump(game.getBoard(), piece,
                        move.getEndRow(), move.getEndCell(), game.getPiecesJumped() );
                if(move != null){
                    TurnUtil.isMoveValid(piece.getType(), move.getStart(), move.getEnd(), game);
                    game.addMove(move);
                }else{
                    break;
                }
            }
        }
        game.submitTurn();
    }
}
