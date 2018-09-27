var app = require('express')();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
// socket io 가져오기 

//연결 이벤트 시에 콜백으로 socket이 매개변수로 들어가며, 안쪽에서 이벤트에 따른 분기처리를 한다. 
io.on('connection', function(socket) {
  console.log("user connect");

  socket.on('lightOn', function(){
    console.log("light on");
    io.emit('lightOn', "light");
  });

  socket.on('lightOff', function(){
    console.log("light off");
    io.emit('lightOff', "light");
  });

});

//서버를 시작한다. 
server.listen(7000, function(){
  console.log("server on 7000");
});