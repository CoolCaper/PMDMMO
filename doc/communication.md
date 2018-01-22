
# CLIENT / SERVER communication protocol
Last updated october 11th (2017)

# 1 - General informations

Communication between the game server and the clients is based around the Websocket protocol.
The websocket RFC is available here:
https://tools.ietf.org/html/rfc6455

The server is divided in two parts, each having their own TCP socket endpoint: a chat and a game information endpoint.
It is the client's job to connect to both to send and be able to recieve the appropriate information.
The endpoints are available at the following adresses:
<br/>[server ip or linked dns]:8080/PMDMMOServer/chat
<br/>[server ip or linked dns]:8080/PMDMMOServer/game

# 2 - Required payload structure

The data sent to the server MUST be in a valid JSON structure, and encoded as text in the websocket container.
The server will ignore all binary payloads recieved (This is subject to change, refer to the latest documentation folder on github).

The server will not send a direct response to a client upon recieving a payload, but can be expected to send a receipt back for most informations.

The Json contained any payload must contain a String field called "action", wich is used to describe the content of the payload.
The other informations are payload dependant.

# 3 - List of client -> server payloads

■ MESSAGE (This is a payload destined to the chat endpoint)
<br/>{"action":"message",
<br/>"tag":"DEV", // (Optionnal) The tag of the user sending the message
<br/>"sender":"Darkxell", // The name of the sender
<br/>"message":"Message content", // the content of the message
<br/>"tagcolor":"#333333", // (Optionnal) The color of the sender's tag, in valid hexadecimal string
<br/>"messagecolor":"#333333", // (Optionnal) The color of the message, in valid hexadecimal string
<br/>"sendercolor":"#333333" // (Optionnal) The color of the sender, in valid hexadecimal string
<br/>}
This is the payload you should send to the server when you want to add a message to the chat.

■ SESSIONINFO
<br/>{"action":"sessioninfo",
<br/>"name":"Darkxell", // The name of the player
<br/>}
This payload must be sent by any client before they send any other game related payload. It contains the basic login informations used by the server to autentificate the user.

■ FREEZONEPOSITION
<br/>{"action":"freezoneposition",
<br/>"name":"Darkxell", // The name of the player
<br/>"freezoneid":"base", // The string id of the player's current freezone
<br/>"posfx":"42.1337", // The x position of the player as a Double
<br/>"posfy":"42.1337", // The y position of the player as a Double
<br/>"currentpokemon":"1", // The id of the pokemon as a String
<br/>}
This payload should be sent by a client to the server to notify it about the player's position in freezones. The server will send to the client the position of nearby other players in response.

# 4 - List of server -> client payloads

■ MESSAGE (This is a payload from to the chat endpoint)
<br/>{"action":"message",
<br/>"tag":"DEV", // (Optionnal) The tag of the user sending the message
<br/>"sender":"Darkxell", // The name of the sender
<br/>"message":"Message content", // the content of the message
<br/>"tagcolor":"#333333", // (Optionnal) The color of the sender's tag, in valid hexadecimal string
<br/>"messagecolor":"#333333", // (Optionnal) The color of the message, in valid hexadecimal string
<br/>"sendercolor":"#333333" // (Optionnal) The color of the sender, in valid hexadecimal string
<br/>}

■ FREEZONEPOSITION
<br/>{"action":"freezoneposition",
<br/>"name":"Darkxell", // The name of the player
<br/>"posfx":"42.1337", // The x position of the player as a Double
<br/>"posfy":"42.1337", // The y position of the player as a Double
<br/>"currentpokemon":"1", // The id of the pokemon as a String
<br/>}
This payload is sent by the server to any client who sent a valid freezoneposition payload to the server.