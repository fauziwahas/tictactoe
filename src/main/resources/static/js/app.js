var playerId = '';

window.onload = function() {
  var textarea = document.getElementById('gameConsole');
  setInterval(function() {
      textarea.scrollTop = textarea.scrollHeight;
  }, 100);
}

const stompClient = new StompJs.Client({
  brokerURL: 'ws://192.168.0.8:8080/ttt'
});

stompClient.onConnect = (frame) => {
  console.log('Connected: ' + frame);
  stompClient.subscribe('/topic/client', (message) => {
    processMessage(JSON.parse(message.body).content);
  });
};

stompClient.onWebSocketError = (error) => {
  console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
  console.error('Broker reported error: ' + frame.headers['message']);
  console.error('Additional details: ' + frame.body);
};

$(function () {
  $("form").on('submit', (e) => e.preventDefault());
  $("#create").click(() => create());
  $("#join").click(() => join());
  $("#disconnect").click(() => disconnect());
});

function create() {
  size = $("#sizeInput").val();
  if(typeof size === "string" && size.length === 0) {
    $("#gameConsole").append('invalid size\n');
    $("#sizeInput").val('');
  } else {
    playerId = '1';
    $("#playerId").text(playerId);
    stompClient.activate();
    setTimeout(function() {
      sendMessage('create ' + size);
    }, 100);
    setDisableButtonAndInput(true);
  }
}

function join() {
  playerId = '2';
  $("#playerId").text(playerId);
  stompClient.activate();
  setTimeout(function() {
    sendMessage('join');
  }, 100);
  setDisableButtonAndInput(true);
}

function disconnect() {
  $("#gameConsole").append("disconnect\n");
  sendMessage("disconnect");
  stompClient.deactivate();
  $("#playerId").text('');
  setDisableButtonAndInput(false);
}

function sendMessage(content) {
  stompClient.publish({
    destination: "/app/tictactoe",
    body: JSON.stringify({'content': content})
  });
}

function chooseSquare(squareId) {
  sendMessage('playerId ' + playerId + ' squareId ' + squareId);
}

function drawSquare(playerId, squareId) {
  $("#" + squareId).attr("src", "/img/player" + playerId + ".png");
}

function initBoard(size) {
  var index = 0;
  var table = '<tbody>';
  for(let i = 0; i < size; i++) {
    table += '<tr>';
    for(let j = 0; j < size; j++) {
      table += '<td><input id="s' + index + '" type="image" src="/img/square.png" onclick=chooseSquare("s' + index + '") /></td>';
      index++;
    }
    table += '</tr>';
  }
  table += '</tbody>';
  $("#board").append(table);
}

function setDisableButtonAndInput(flag) {
  $("#sizeInput").attr("disabled", flag);
  $("#create").attr("disabled", flag);
  $("#join").attr("disabled", flag);
}

function processMessage(content) {
  if(content.includes("playerId") && content.includes("squareId")) {
    var playerId = content.split(' ')[1];
    var squareId = content.split(' ')[3];
    drawSquare(playerId, squareId);
  } else if(content.includes("game started")) {
    initBoard(content.split(" ")[content.split(" ").length - 1]);
  }
  if(content.includes("won") || content.includes("draw")) {
    alert(content.split("\n")[1]);
    var content1 = content.split('\n')[0];
    var playerId = content1.split(' ')[1];
    var squareId = content1.split(' ')[3];
    drawSquare(playerId, squareId);
  }
  if(content.includes("disconnect")) {
    setDisableButtonAndInput(false);
  }
  if(!content.includes("NONE")) {
    $("#gameConsole").append(content + '\n');
  }  
}
