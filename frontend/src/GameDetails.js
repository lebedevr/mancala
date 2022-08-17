import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";

import { backendHost } from './Config';
import stompClient from './StompClient';
import {callStomp} from './StompClient';

const GameDetails = () => {
    const {id} = useParams();
    const [player, setPlayer] = useState(null);
    const [game, setGame] = useState(null);

    useEffect(() => {
        fetch(backendHost + '/games/' + id)
            .then(res => {
                return res.json();
            })
            .then(data => {
                if (data.firstPlayer === localStorage.getItem("playerId"))
                    setPlayer(1)
                if (data.secondPlayer === localStorage.getItem("playerId"))
                    setPlayer(2)
                setGame(data)
                callStomp(() => stompClient.subscribe('/mancala/state/' + id, function (data) {
                    setGame(JSON.parse(data.body));
                }));
            })
    }, [id])

    const move = (index) => {
        callStomp(() => stompClient.send("/mancala/move/" + id, {},
            JSON.stringify({player: player, index: index})));
    }

    return (
        <div className="game-details">
            {game && (
                <article>
                    <p>Game id : {game.id}</p>
                    <p>Game name : {game.name}</p>
                    {player == null && <p>You have no access to this game</p>}
                    {player !== null && <p>You are Player {player}</p>}
                    {player !== null && game.status === 'WAITING' && <p>Waiting for another player</p>}
                    {player !== null && game.gameEnd === false && game.status === 'STARTED' && <p>Player {game.currentPlayer} turn</p>}
                    {player !== null && game.gameEnd === true && game.winner === null && <p className="game-end">Draw!</p>}
                    {player !== null && game.gameEnd === true && game.winner !== null &&
                        <p className="game-end">Player {game.winner} win!</p>}
                    <br/>
                    {game.status === 'STARTED' && player !== null &&
                        <table>
                            <tbody>
                            <tr>
                                <td></td>
                                <td className="pit">
                                    {player === 2
                                        ? <button onClick={() => move(13)}
                                                  disabled={game.board[13] === 0 || player !== game.currentPlayer}>
                                            {game.board[13]}
                                        </button>
                                        : game.board[13]
                                    }
                                </td>
                                <td className="pit">
                                    {player === 2
                                        ? <button onClick={() => move(12)}
                                                  disabled={game.board[12] === 0 || player !== game.currentPlayer}>
                                            {game.board[12]}
                                        </button>
                                        : game.board[12]
                                    }
                                </td>
                                <td className="pit">
                                    {player === 2
                                        ? <button onClick={() => move(11)}
                                                  disabled={game.board[11] === 0 || player !== game.currentPlayer}>
                                            {game.board[11]}
                                        </button>
                                        : game.board[11]
                                    }
                                </td>
                                <td className="pit">
                                    {player === 2
                                        ? <button onClick={() => move(10)}
                                                  disabled={game.board[10] === 0 || player !== game.currentPlayer}>
                                            {game.board[10]}
                                        </button>
                                        : game.board[10]
                                    }
                                </td>
                                <td className="pit">
                                    {player === 2
                                        ? <button onClick={() => move(9)}
                                                  disabled={game.board[9] === 0 || player !== game.currentPlayer}>
                                            {game.board[9]}
                                        </button>
                                        : game.board[9]
                                    }
                                </td>
                                <td className="pit">
                                    {player === 2
                                        ? <button onClick={() => move(8)}
                                                  disabled={game.board[8] === 0 || player !== game.currentPlayer}>
                                            {game.board[8]}
                                        </button>
                                        : game.board[8]
                                    }
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td className="pit">{game.board[0]}</td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td className="pit">{game.board[7]}</td>
                            </tr>
                            <tr>
                                <td></td>
                                <td className="pit">
                                    {player === 1
                                        ? <button onClick={() => move(1)}
                                                  disabled={game.board[1] === 0 || player !== game.currentPlayer}>
                                            {game.board[1]}
                                        </button>
                                        : game.board[1]
                                    }
                                </td>
                                <td className="pit">
                                    {player === 1
                                        ? <button onClick={() => move(2)}
                                                  disabled={game.board[2] === 0 || player !== game.currentPlayer}>
                                            {game.board[2]}
                                        </button>
                                        : game.board[2]
                                    }
                                </td>
                                <td className="pit">
                                    {player === 1
                                        ? <button onClick={() => move(3)}
                                                  disabled={game.board[3] === 0 || player !== game.currentPlayer}>
                                            {game.board[3]}
                                        </button>
                                        : game.board[3]
                                    }
                                </td>
                                <td className="pit">
                                    {player === 1
                                        ? <button onClick={() => move(4)}
                                                  disabled={game.board[4] === 0 || player !== game.currentPlayer}>
                                            {game.board[4]}
                                        </button>
                                        : game.board[4]
                                    }
                                </td>
                                <td className="pit">
                                    {player === 1
                                        ? <button onClick={() => move(5)}
                                                  disabled={game.board[5] === 0 || player !== game.currentPlayer}>
                                            {game.board[5]}
                                        </button>
                                        : game.board[5]
                                    }
                                </td>
                                <td className="pit">
                                    {player === 1
                                        ? <button onClick={() => move(6)}
                                                  disabled={game.board[6] === 0 || player !== game.currentPlayer}>
                                            {game.board[6]}
                                        </button>
                                        : game.board[6]
                                    }
                                </td>
                                <td></td>
                            </tr>
                            </tbody>
                        </table>
                    }
                </article>
            )}
        </div>
    );
}

export default GameDetails;