package main.java.layeredarchitecture.repository;

import main.java.layeredarchitecture.domain.Board;

import java.util.Map;

public interface BoardRepository {
    public Map<Long, Board> getAllBoards();
    public Board getBoard(Long id);
    public Board createBoard(Board board);
    public void deleteBoard(Long id);
    public void deletAll();
}
