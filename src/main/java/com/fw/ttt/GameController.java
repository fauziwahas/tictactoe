package com.fw.ttt;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class GameController {

  private GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @GetMapping("")
  public String index() {
    return "index.html";
  }

  @MessageMapping("/tictactoe")
  @SendTo("/topic/client")
  public Message processMessage(Message message) {
    return gameService.processMessage(message.getContent());
  }

}
