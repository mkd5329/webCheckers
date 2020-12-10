package com.webcheckers.model;

/**
 * This Piece class represents a piece on a checker board, which is either red or white and is either a "single" or
 * "king"
 *
 * @author <a href='mailto:daw1882@rit.edu'>Dade Wood</a>
 * @author <a href='mailto:zah1276@rit.edu'>Zeb Hollinger</a>
 */
public class Piece {

    //enum to determine the "rank" of the piece
    public enum Type{
        SINGLE, KING
    }

    //enum to determine the color of the piece
    public enum Color {
        RED, WHITE
    }

    private Type type;
    private Color color;

    /**
     * Creates a new Piece object and initializes its type and color
     * @param type what kind of piece this is, SINGLE or KING
     * @param color what team this piece is on, RED or WHITE
     */
    public Piece(Type type, Color color){
        this.type = type;
        this.color = color;
    }

    /**
     * Access the type of this piece
     * @return type
     */
    public Type getType(){
        return type;
    }

    /**
     * Access the color of this piece
     * @return color
     */
    public Color getColor(){ return color; }



    /**Gets the opposite color that is assigned to the piece for
     * use when constructing the different views in game.ftl
     *
     * @return the opposite color that is assigned on the board
     */
    public Color otherColor(){
        if (color == Color.RED){
            return Color.WHITE;
        }
        return Color.RED;
    }

    /**
     * Changes the piece from single to king
     */
    public void kingMe(){
        this.type = Type.KING;
    }
}
