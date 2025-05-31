package main.java.layeredarchitecture.repository;

import main.java.layeredarchitecture.domain.Board;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryBoardRepository implements BoardRepository {

    private final Map<Long, Board> boards = new ConcurrentHashMap<>();
    private Long idSequence = 0L;

    @Override
    public Map<Long, Board> getAllBoards() {
        return this.boards;
    }

    @Override
    public Board getBoard(Long id) {
        return boards.get(id);
    }

    @Override
    public synchronized Board createBoard(Board board) {
        board.setId(idSequence++);
        board.setCreatedAt(LocalDateTime.now());
        boards.put(board.getId(), board);
        return board;
    }

    @Override
    public void deleteBoard(Long id) {
        boards.remove(id);
    }

    @Override
    public void deletAll() {
        boards.clear();
    }

}
