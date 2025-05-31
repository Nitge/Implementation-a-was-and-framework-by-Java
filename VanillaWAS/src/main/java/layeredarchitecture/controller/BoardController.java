package main.java.layeredarchitecture.controller;

import main.java.layeredarchitecture.domain.Board;
import main.java.layeredarchitecture.service.BoardService;
import main.java.was.framework.annotation.Mapping;
import main.java.was.framework.annotation.Param;
import main.java.was.framework.handler.adapter.Model;
import main.java.was.http.header.HttpMethod;

import java.util.List;


public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;

    }

    @Mapping(value = "/board", method = HttpMethod.POST)
    public String writeBoard(@Param("writer") String writer,
                             @Param("content") String content) {
        Board board = new Board();
        board.setWriter(writer);
        board.setContent(content);
        boardService.createBoard(board);
        return "redirect:/board";
    }

    @Mapping(value = "/board", method = HttpMethod.GET)
    public String readAllBoard(Model model) {
        List<Board> allBoards = boardService.getAllBoards();
        model.addAttribute("boards", allBoards);
        return "board";
    }

    @Mapping(value = "/board/delete", method = HttpMethod.GET)
    public String deleteBoard(@Param("id") String id) {
        boardService.deleteBoard(Long.parseLong(id));
        return "redirect:/board";
    }

}
