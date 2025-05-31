package main.java.was.framework.container;

import main.java.layeredarchitecture.controller.BoardController;
import main.java.layeredarchitecture.repository.BoardRepository;
import main.java.layeredarchitecture.repository.MemoryBoardRepository;
import main.java.layeredarchitecture.service.BoardService;
import main.java.layeredarchitecture.service.BoardServiceImpl;
import main.java.was.framework.handler.adapter.HandlerAdapter;
import main.java.was.framework.handler.mapping.HandlerMapping;
import main.java.was.framework.annotation.Bean;
import main.java.was.framework.annotation.Configuration;
import main.java.was.framework.view.ViewResolver;

@Configuration
public class Config {

    @Bean
    public HandlerMapping handlerMapping(BeanContainer beanContainer) {
        return new HandlerMapping(beanContainer);
    }

    @Bean
    public HandlerAdapter handlerAdapter() {
        return new HandlerAdapter();
    }

    @Bean
    public ViewResolver viewResolver() {
        return new ViewResolver();
    }

    @Bean
    public BoardController boardController(BoardService boardService) {
        return new BoardController(boardService);
    }

    @Bean
    public BoardService boardService(BoardRepository boardRepository) {
        return new BoardServiceImpl(boardRepository);
    }

    @Bean
    public BoardRepository boardRepository() {
        return new MemoryBoardRepository();
    }


}
