import {useHistory} from "react-router-dom";

import { backendHost } from './Config';
import stompClient from './StompClient';
import { callStomp } from './StompClient';


const GameList = ({ games }) => {
    const history = useHistory();
    const join = (id) => {
        fetch(backendHost + '/games/join/' + id, {
            method: 'POST',
            headers: {"Content-Type": "application/json"},
            body: localStorage.getItem("playerId")
        }).then(() => {
            callStomp(() => stompClient.send("/mancala/join/" + id, {}));
            callStomp(() => stompClient.send("/mancala/updateGameList", {}));
            history.push('/games/' + id);
        })
    }

    return (
        <div>
            <h2>Game List:</h2>
            {games.map(game => (
                <div className="game-preview" key={game.id}>
                    <button onClick={() => join(game.id)}>Join</button>
                    <h2>{ game.name }</h2>
                </div>
            ))}
        </div>
    );
}

export default GameList;