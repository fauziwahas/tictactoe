# Tic Tac Toe

## How to play:
1. The game is a 2-player-only game, one can run the server while playing as the first player, and the other can join as the second player from another device.
2. Note that the two devices should be on the same LAN.
3. Copy the IP of the computer acting as the server, paste it to the brokerURL field of the StompJs.Client in app.js line 11; do not change the protocol, the port, nor the resource path.
4. Run the server using the "gradle clean build bootRun" or "java --jar"
5. Open the website from browser: "localhost:8080" from the server, or "&lt;server-ip&gt;:8080" from another device.
6. Set the board size using the size input field.
7. Click the create button; the one creating the game is assigned player ID 1.
8. The second player can join from another device by clicking the join button; the joining player will be assigned player ID 2.
9. The first turn goes to player ID 1, the second goes to player ID 2, the third goes back to player ID 1, and so on.
10. Upon completion, an alert will pop up indicating either a win to one of the player, or a draw.
11. To end the game, simply disconnect both player to free the resources taken for the underlying websocket.
12. All messages should appear inside the console on the bottom of the page.