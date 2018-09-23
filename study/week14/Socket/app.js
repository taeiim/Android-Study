var app = require('express')();
var server = require('http').createServer(app);
// http server를 socket.io server로 upgrade한다
var io = require('socket.io')(server);
io.on('connection', function(socket) {
    console.log('connection');
    socket.on('Connect',function(data){
        socket.data = data;
    });
    socket.on('call',function(){
        console.log(socket.data);
        io.emit('data_call',socket.data);
    });
    socket.on('Disconnect',()=>{
        console.log('disconnect');
    });
});
  
server.listen(3000, function() {
    console.log('Socket IO server listening on port 3000');
});