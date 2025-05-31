package main.java.layeredarchitecture.service;

import main.java.layeredarchitecture.domain.Board;

import java.util.List;

public interface BoardService {
    public List<Board> getAllBoards();
    public Board getBoard(Long id);
    public Board createBoard(Board board);
    public void deleteBoard(Long id);
}
