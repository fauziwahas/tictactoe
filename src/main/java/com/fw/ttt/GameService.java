package com.fw.ttt;

import org.springframework.stereotype.Service;

@Service
public class GameService {

  private int size = 0;
  private int[][] board;
  private int playerTurn = 0;
  private int moveCounter = 0;
  private boolean createdFlag = false;
  private boolean startedFlag = false;
  private boolean finishedFlag = false;

  public Message processMessage(String content) {
    String response = "NONE";
    if(content.contains("create")) {
      if(!createdFlag && !startedFlag) {
        createGame(content);
        response = "game created";
      } else {
        response = "game not created or it already exists";
      }
    } else if(content.contains("join")) {
      if(createdFlag && !startedFlag) {
        startGame();
        response = "game started with size " + size;
      } else if(!createdFlag) {
        response = "game not exists";
      }
    } else if(content.contains("disconnect")) {
      endGame();
      response = "player disconnected";
    } else {
      if(checkIfMoveIsValid(content) && !finishedFlag) {
        response = content;
        playerTurn = playerTurn == 1 ? 2 : 1;
        moveCounter++;
        int winner = checkWinner(content);
        if(winner == 1 || winner == 2) {
          response = content + "\n" + "player " + winner + " won!";
          finishedFlag = true;
        } else if(moveCounter == size * size) {
          response = content + "\n" + "it is a draw";
          finishedFlag = true;
        }
      }
    }
    return new Message(response);
  }

  private void createGame(String content) {
    size = Integer.parseInt(content.split(" ")[1]);
    createdFlag = true;
  }

  private void startGame() {
    board = new int[size][size];
    playerTurn = 1;
    startedFlag = true;
  }

  private boolean checkIfMoveIsValid(String content) {
    boolean emptyFlag = true;
    int index = Integer.parseInt(content.split(" ")[content.split(" ").length - 1].substring(1));
    boolean turnFlag = Integer.parseInt(content.split(" ")[1]) == playerTurn;
    if(board[index / size][index % size] != 0) {
      emptyFlag = false;
    } else if(turnFlag) {
      board[index / size][index % size] = Integer.parseInt(content.split(" ")[1]);
    }
    return createdFlag && startedFlag && emptyFlag && turnFlag;
  }

  private void endGame() {
    size = 0;
    moveCounter = 0;
    board = null;
    createdFlag = false;
    startedFlag = false;
    finishedFlag = false;
  }

  /**
   *
   */
  private int checkWinner(String content) {
    int index = Integer.parseInt(content.split(" ")[content.split(" ").length - 1].substring(1));
    int x = index / size;
    int y = index % size;
    int p = Integer.parseInt(content.split(" ")[1]);
    /* check row */
    for(int i = 0; i < size; i++) {
      if(board[x][i] != p) {
        break;
      }
      if(i == size - 1) {
        return p;
      }
    }
    /* check column */
    for(int i = 0; i < size; i++) {
      if(board[i][y] != p) {
        break;
      }
      if(i == size - 1) {
        return p;
      }
    }
    /* check diagonal */
    if(x == y) {
      for(int i = 0; i < size; i++) {
        if(board[i][i] != p) {
          break;
        }
        if(i == size - 1) {
          return p;
        }
      }
    }
    /* check anti-diagonal */
    for(int i = 0; i < size; i++) {
      if(board[i][size - 1 - i] != p) {
        break;
      }
      if(i == size - 1) {
        return p;
      }
    }
    /* check draw */
    return 0;
  }

}
