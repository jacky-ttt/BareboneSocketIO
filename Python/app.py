import asyncio

from aiohttp import web

import socketio

sio = socketio.AsyncServer(async_mode='aiohttp')
app = web.Application()
sio.attach(app)


async def index(request):
    with open('app.html') as f:
        return web.Response(text=f.read(), content_type='text/html')


@sio.event
async def join(sid, message):
    print(message + " : has joined the chat")


@sio.event
async def message_from_client(sid, nick_name, message):
    print(nick_name + " : " + message)
    await sio.emit('message_from_server', {"senderNickname": nick_name, "message": message})


@sio.event
async def connect(sid, environ):
    print("a user connected")


@sio.event
def disconnect(sid):
    print('a user disconnected')


app.router.add_static('/static', 'static')
app.router.add_get('/', index)

if __name__ == '__main__':
    web.run_app(app, port=3000)
