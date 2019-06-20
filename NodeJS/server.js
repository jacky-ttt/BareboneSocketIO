const express = require('express'),
  http = require('http'),
  app = express(),
  server = http.createServer(app),
  io = require('socket.io').listen(server);
app.get('/', (req, res) => {
  res.sendFile(__dirname + '/index.html');
});
io.on('connection', (socket) => {

  console.log('a user connected')

  socket.on('join', function (userNickname) {
    console.log(userNickname + " : has joined the chat ");
  });


  socket.on('message_from_client', (senderNickname, messageContent) => {
    //log the message in console 
    console.log(senderNickname + " : " + messageContent)

    //create a message object 
    let message = { "message": messageContent, "senderNickname": senderNickname }
    // send the message to the client side  
    io.emit('message_from_server', message);

  });

  socket.on('disconnect', function () {
    console.log('a user disconnected')
  });

});

server.listen(3000, () => {

  console.log('Node app is running on port 3000');

});
