package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private boolean highlightLegalMoves;
    private BoardDirection boardDirection;
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);

    private final Color lightTileColor = Color.decode("#ffdd99");
    private final Color darkTileColor = Color.decode("#734d26");
    private final static String defaultPieceImagesPath = "art/fancy3/";

    public Table(){
        this.chessBoard = Board.createStandardBoard();
        gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.boardPanel = new BoardPanel();
        boardDirection = BoardDirection.NORMAL;
        highlightLegalMoves = false;
        this.gameFrame.add(boardPanel, BorderLayout.CENTER);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN file");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        fileMenu.add(openPGN);
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();

        final JCheckBoxMenuItem legalMovesHighlightCheckBox = new JCheckBoxMenuItem("Highlight Legal Moves", false);
        legalMovesHighlightCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMovesHighlightCheckBox.isSelected();
            }
        });
        preferencesMenu.add(legalMovesHighlightCheckBox);

        return preferencesMenu;
    }
    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0 ; i < BoardUtils.NUM_TILES; i++){
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    public static class MoveLog {
        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }
        public List<Move> getMoves() {
            return this.moves;
        }
        public void addMove(final Move move) {
            this.moves.add(move);
        }
        public int size() {
            return this.moves.size();
        }
        public void clear() {
            this.moves.clear();
        }
        public Move remove(final int index) {
            return this.moves.remove(index);
        }
        public boolean remove(final Move move) {
            return this.moves.remove(move);
        }
    }

    private class TilePanel extends JPanel {
        private final int tileId;
        TilePanel(final BoardPanel boardPanel, final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            /*
            * if the right mouse button is clicked, the chosen Tile/Piece will be reset to null.
            * if it's the left mouse button:
            * the first click will save the chosen tile/piece in the variables (sourceTile/ humanMovedPiece)
            * the second click will initialize a move to the second chosen tile which will be saved in (destinationTile).
            */
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(isRightMouseButton(e)){
                        System.out.println("reset");
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                    else if(isLeftMouseButton(e)){
                        if(sourceTile == null) { //first click
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null){ //if the user chooses an empty tile
                                sourceTile = null;
                            }
                            System.out.println(humanMovedPiece+ " selected");
                        }
                        else { //second click
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) { //if the chosen move is legal
                                chessBoard = transition.getTransitionBoard(); // update the board
                            }
                            //reset the buffers
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
            validate();
        }

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if (board.getTile(this.tileId).isOccupied()){
                try {
                    final BufferedImage image = ImageIO.read(new File(
                            defaultPieceImagesPath + board.getTile(this.tileId).getPiece().getPieceAllegiance().toString().substring(0,1) +
                                      board.getTile(this.tileId).getPiece().toString() + ".png"
                    ));
                    this.add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColor() {
            if (BoardUtils.INSTANCE.EIGHTH_RANK.get(this.tileId) ||
                BoardUtils.INSTANCE.SIXTH_RANK.get(this.tileId) ||
                BoardUtils.INSTANCE.FOURTH_RANK.get(this.tileId) ||
                BoardUtils.INSTANCE.SECOND_RANK.get(this.tileId)){
                    setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            }
            if (BoardUtils.INSTANCE.SEVENTH_RANK.get(this.tileId) ||
                BoardUtils.INSTANCE.FIFTH_RANK.get(this.tileId) ||
                BoardUtils.INSTANCE.THIRD_RANK.get(this.tileId) ||
                BoardUtils.INSTANCE.FIRST_RANK.get(this.tileId)){
                    setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }
        private void highlightLegals (final Board board) {
            if (highlightLegalMoves) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        private Collection<Move> pieceLegalMoves (final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAllegiance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }
    }
}
