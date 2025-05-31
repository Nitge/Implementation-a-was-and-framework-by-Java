package main.java.layeredarchitecture.service;

import main.java.layeredarchitecture.domain.Board;
import main.java.layeredarchitecture.repository.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    @Override
    public List<Board> getAllBoards() {
        Map<Long, Board> boards = boardRepository.getAllBoards();
        return new ArrayList<>(boards.values());
    }

    @Override
    public Board getBoard(Long id) {
        return boardRepository.getBoard(id);
    }

    @Override
    public Board createBoard(Board board) {
        return boardRepository.createBoard(board);
    }

    @Override
    public void deleteBoard(Long id) {
        boardRepository.deleteBoard(id);
    }
}
